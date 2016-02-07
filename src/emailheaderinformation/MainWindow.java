package emailheaderinformation;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import emailheaderinformation.model.Header;
import emailheaderinformation.parser.EmailParser;

public class MainWindow {

	private JFrame mFrame;
	private JButton mOpen;
	private JButton mStart;
	private String mInputEmail = "";

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
	 * Initialize the contents of the mFrame.
	 */
	private void initialize() {
		mFrame = new JFrame();
		mFrame.setBounds(100, 100, 450, 300);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				HeaderAnalyser headerAnalyser = new HeaderAnalyser(header);
			}
		});
		inputFrame.add(mOpen);
		inputFrame.add(mStart);
		mFrame.add(inputFrame);
	}

}
