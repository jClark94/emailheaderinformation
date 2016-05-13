package emailheaderinformation;

import com.google.gson.Gson;
import emailheaderinformation.analysers.*;
import emailheaderinformation.model.FoundInformation;
import emailheaderinformation.model.Header;
import emailheaderinformation.parser.EmailParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class MainWindow {

  private VulnerabilityFinderManager vfm;
  private JFrame mFrame;
  private JButton mStart;
  private String mInputEmail = "";
  private MainWindow mSelf = this;
  private JTable mFoundInformationTable;
  private FoundInformation foundInformation = new FoundInformation();
  private ExecutorService mExecutorService = newFixedThreadPool(8);
  private WebServer mWebServer;

  /**
   * Create the application.
   */
  public MainWindow () {
    initialize();
    vfm = new VulnerabilityFinderManagerImpl(this);
  }

  /**
   * Initialize the contents of the mFrame.
   */
  private void initialize () {
    mFrame = new JFrame();
    mFrame.setBounds(100, 100, 450, 300);
    mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel blo = new JPanel(new BorderLayout());

    JPanel inputFrame = new JPanel();
    JButton mOpen = new JButton();
    final JFileChooser fc = new JFileChooser();
    mOpen.setText("Open Email file");
    mOpen.setHorizontalAlignment(SwingConstants.LEFT);
    mOpen.addActionListener(e -> {
      if (fc.showOpenDialog(mFrame) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        if (file.exists() && file.isFile() && file.canRead()) {
          Path path = Paths.get(file.toURI());
          try {
            List<String> stream = Files.readAllLines(path, Charset.defaultCharset());
            StringBuilder sb = new StringBuilder();
            for (String s : stream) {
              sb.append(s);
              sb.append('\n');
            }
            mInputEmail = sb.toString();
            mStart.setEnabled(true);
          } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } else {
          JOptionPane.showMessageDialog(mFrame, "Please select a valid file");
        }
      }
    });
    mStart = new JButton();
    mStart.setText("Parse");
    mStart.setHorizontalAlignment(SwingConstants.CENTER);
    mStart.setEnabled(false);
    mStart.addActionListener(e -> {
      EmailParser emailParser = new EmailParser();
      Header header = emailParser.parse(mInputEmail);
      HeaderAnalyser oha = new OxfordHeaderAnalyser(header, mSelf);
      HeaderAnalyser eha = new ExchangeHeaderAnalyser(header, mSelf);
      HeaderAnalyser ci = new ClientInferrer(header, mSelf);
      HeaderAnalyser si = new SenderInformationExtractor(header, mSelf);
      HeaderAnalyser da = new DeviceAnalyser(header, mSelf);
      Callable[] headerAnalysers = new Callable[] { oha, eha, ci, si, da };
      Collection<Callable<Object>> has = new ArrayList<>();
      for (Callable callable : headerAnalysers) {
        has.add(callable);
      }
      try {
        mExecutorService.invokeAll(has);
      } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

    });

    JButton mShowVulnerabilities = new JButton();
    mShowVulnerabilities.setText("Show Found CVEs");
    mShowVulnerabilities.setHorizontalAlignment(SwingConstants.RIGHT);
    mShowVulnerabilities.setEnabled(true);
    mShowVulnerabilities.addActionListener(actionEvent -> {
      if (vfm.noVulnerabilitiesFound()) {
        JOptionPane.showMessageDialog(mFrame, "No vulnerabilities found (yet!)");
      } else {
        try {
          File file = new File(this.getClass().getResource("/result-template.html").toURI());
          try (InputStream in = new FileInputStream(file);
               InputStreamReader isr = new InputStreamReader(in);
               BufferedReader br = new BufferedReader(isr)) {
            StringBuilder webPage = new StringBuilder();
            br.lines().forEach((String s) -> {
              Gson gson = new Gson();
              if (s.contains("${name}")) {
                webPage.append(s.replace("${name}", foundInformation.getName()));
              } else if (s.contains("var cveentries = [];")) {
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                vfm.getVulnerabilities().forEach(vd -> {
                  sb.append(gson.toJson(vd));
                  sb.append(',');
                  sb.append(System.lineSeparator());
                });
                sb.append(']');
                webPage.append(s.replace("[]", sb.toString()));
              } else if (s.contains("var factentries = [];")) {
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                foundInformation.getFacts().forEach(f -> {
                  sb.append(gson.toJson(f));
                  sb.append(',');
                  sb.append(System.lineSeparator());
                });
                sb.append(']');
                webPage.append(s.replace("[]", sb.toString()));
              } else if (s.contains("var userentries = [];")) {
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                foundInformation.getUsernameList().forEach(u -> {
                  sb.append(gson.toJson(u));
                  sb.append(',');
                  sb.append(System.lineSeparator());
                });
                sb.append(']');
                webPage.append(s.replace("[]", sb.toString()));
              } else if (s.contains("${software}")) {
                webPage.append(s.replace("${software}", foundInformation.getSoftware()));
              } else if (s.contains("var products = [];")) {
                webPage.append(s.replace("[]", gson.toJson(vfm.getKeywords())));
              } else if (s.contains("var servers = [];")) {
                webPage.append(s.replace("[]", gson.toJson(foundInformation.getDevices())));
              } else {
                webPage.append(s);
              }
              webPage.append(System.lineSeparator());
            });
            Path out = Paths.get("/tmp/webpage.html");
            Files.write(out, webPage.toString().getBytes());
            if (mWebServer != null) {
              mWebServer.stop();
            }
            mWebServer = new WebServer(mFrame, webPage.toString(), 3142);
          } catch (IOException e) {
            e.printStackTrace();
          }
        } catch (URISyntaxException e) {
          e.printStackTrace();
        }
      }});

    inputFrame.add(mOpen);
    inputFrame.add(mStart);
    inputFrame.add(mShowVulnerabilities);

    String[] columnNames = { "Class", "Type", "Present", "Details" };
    mFoundInformationTable = new JTable(new DefaultTableModel(columnNames, 0));
    JScrollPane scrollPane = new JScrollPane(mFoundInformationTable);

    blo.add(inputFrame, BorderLayout.PAGE_START);
    blo.add(scrollPane, BorderLayout.CENTER);
    mFrame.add(blo);
  }

  /**
   * Launch the application.
   */
  public static void main (String[] args) {
    EventQueue.invokeLater(() -> {
      try {
        MainWindow window = new MainWindow();
        window.mFrame.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public Future submitToExecutorService (Callable task) {
    return mExecutorService.submit(task);
  }

  /**
   * Add new information to the table
   */
  public synchronized void addToTable (Object[] arr) {
    foundInformation.addFact((String) arr[0], (String) arr[1], (String) arr[3]);
    DefaultTableModel model = (DefaultTableModel) mFoundInformationTable.getModel();
    model.addRow(arr);
  }

  public VulnerabilityFinderManager getVfm () {
    return vfm;
  }

  public FoundInformation getFoundInformation () {
    return foundInformation;
  }
}
