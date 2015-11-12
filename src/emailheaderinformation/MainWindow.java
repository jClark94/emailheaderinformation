package emailheaderinformation;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame mFrame;
	private JTextField mInputHeader;
	private JButton mStart;

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
		mInputHeader = new JTextField();
		mStart = new JButton();
		mStart.setText("Parse");
		mStart.setHorizontalAlignment(SwingConstants.RIGHT);
		mInputHeader.setHorizontalAlignment(SwingConstants.CENTER);
		inputFrame.add(mInputHeader);
		inputFrame.add(mStart);
		mFrame.add(inputFrame);
	}

}
