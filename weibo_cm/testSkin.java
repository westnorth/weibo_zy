import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.JSlider;


public class testSkin {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testSkin window = new testSkin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testSkin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("你好");
		label.setBounds(16, 22, 67, 18);
		frame.getContentPane().add(label);
		
		textField = new JTextField();
		textField.setBounds(57, 18, 140, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnOk = new JButton("Ok");
		btnOk.setBounds(207, 18, 106, 26);
		frame.getContentPane().add(btnOk);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		chckbxNewCheckBox.setBounds(48, 67, 132, 28);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		rdbtnNewRadioButton.setBounds(185, 68, 148, 26);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(46, 129, 232, 25);
		frame.getContentPane().add(progressBar);
		
		JSlider slider = new JSlider();
		slider.setBounds(33, 177, 280, 28);
		frame.getContentPane().add(slider);
	}
}
