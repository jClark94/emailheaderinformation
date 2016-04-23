package emailheaderinformation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import emailheaderinformation.analysers.CveAnalyser;
import emailheaderinformation.analysers.ExchangeHeaderAnalyser;
import emailheaderinformation.analysers.HeaderAnalyser;
import emailheaderinformation.analysers.OxfordHeaderAnalyser;
import emailheaderinformation.model.Header;
import emailheaderinformation.parser.EmailParser;

public class MainWindow {

	private JFrame mFrame;
	private JButton mOpen;
	private JButton mStart;
	private String mInputEmail = "";
	private MainWindow mSelf = this;
	private JTable mFoundInformationTable;
	private Object[][] mInformationTable = new Object[4][4];
	private List<Object[]> mInformationList = new ArrayList<Object[]>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}
	
	/**
	 * Add new information to the table
	 */
	public synchronized void addToTable(Object[] arr) {
		DefaultTableModel model = (DefaultTableModel) mFoundInformationTable.getModel();
		model.addRow(arr);
	}

	/**
	 * Initialize the contents of the mFrame.
	 */
	private void initialize() {
		mFrame = new JFrame();
		mFrame.setBounds(100, 100, 450, 300);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel blo = new JPanel(new BorderLayout());
		JPanel analyserFrame = new JPanel();
		
		JPanel inputFrame = new JPanel();
		mOpen = new JButton();
		final JFileChooser fc = new JFileChooser();
		mOpen.setText("Open Email file");
		mOpen.setHorizontalAlignment(SwingConstants.LEFT);
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(mFrame) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists() && file.isFile() && file.canRead()) {
						Path path = Paths.get(file.toURI());
						try {
							List<String> stream = Files
									.readAllLines(path, Charset.defaultCharset());
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
			}
		});
		mStart = new JButton();
		mStart.setText("Parse");
		mStart.setHorizontalAlignment(SwingConstants.RIGHT);
		mStart.setEnabled(false);
		mStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EmailParser emailParser = new EmailParser();
				Header header = emailParser.parse(mInputEmail);
				HeaderAnalyser oha = new OxfordHeaderAnalyser(header, mSelf);
				HeaderAnalyser eha = new ExchangeHeaderAnalyser(header, mSelf);
				HeaderAnalyser vha = new CveAnalyser(header, mSelf);
				Collection<Callable<Object>> has = new ArrayList<Callable<Object>>();
				has.add(Executors.callable(oha));
				has.add(Executors.callable(eha));
				has.add(Executors.callable(vha));
		    ExecutorService executor = Executors.newFixedThreadPool(8);
		    try {
					executor.invokeAll(has);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}
		});
		inputFrame.add(mOpen);
		inputFrame.add(mStart);
		
		
		String[] columnNames = {"Class", "Type", "Present", "Details" };
		mFoundInformationTable = new  JTable(new DefaultTableModel(columnNames, 0));
		JScrollPane scrollPane = new JScrollPane(mFoundInformationTable);
		
		blo.add(inputFrame, BorderLayout.PAGE_START);
		blo.add(scrollPane, BorderLayout.CENTER);
		mFrame.add(blo);
	}

}
