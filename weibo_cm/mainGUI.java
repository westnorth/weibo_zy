import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import ch.randelshofer.quaqua.*;


public class mainGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object[] objToken;//1,id 2.token 3.token secret
	private JTextField txtFindcontent;
	private JTable table;

	private static final int DEFAULT_WIDTH = 860;
	private static final int DEFAULT_HEIGHT = 717;
	private JTextField txtUsersearch;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
		         System.setProperty(
		                 "Quaqua.tabLayoutPolicy","wrap"
		              );
				try {
//					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格，jdk6 update10版本以后的才会出现
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//当前系统风格
//					UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");//Motif风格，是蓝黑
//					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//跨平台的Java风格
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//windows风格
					//UIManager.setLookAndFeel("javax.swing.plaf.windows.WindowsLookAndFeel");//windows风格
//					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");//java风格
//					UIManager.setLookAndFeel("com.apple.mrj.swing.MacLookAndFeel");//待考察，
					UIManager.setLookAndFeel(
			                  "ch.randelshofer.quaqua.QuaquaLookAndFeel");
				mainGUI window = new mainGUI();
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);
				window.setResizable(false);
				window.getContentPane().setLayout(null);
				window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mainGUI() {
		dlgGetPin dialog = new dlgGetPin(this);	
		dialog.setVisible(true);
		objToken=dialog.getObjectToken();
		if(objToken[0]==null)
		{
			JOptionPane.showMessageDialog(null, "未获得授权，即将退出");
			System.exit(ABORT);
		}
		String strConsumKey= "416693359";
		String strConsumSecret="f3aedd1273689a65b2f6a82f7d77dd25";
		long nUserID=(Long) objToken[0];
		
		Weibo weiboInfo=new Weibo();
		weiboInfo.setToken(objToken[1].toString(),objToken[2].toString());
		User user;
		try {
			user = weiboInfo.showUser(String.valueOf(nUserID));
			System.out.println("this user:"+user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		setSize(913, 717);
		getContentPane().setLayout(null);
		
		
		JPanel panelName = new JPanel();
		panelName.setBorder(new TitledBorder("微博账号名称"));
		panelName.setBounds(12, 6, 220, 670);
		getContentPane().add(panelName);
		panelName.setLayout(null);
		
		JList list = new JList();
		list.setBounds(12, 145, 189, 511);
		panelName.add(list);
		
		JLabel label_1 = new JLabel("搜索账号");
		label_1.setBounds(12, 34, 71, 25);
		panelName.add(label_1);
		
		txtUsersearch = new JTextField();
		txtUsersearch.setText("");
		txtUsersearch.setBounds(82, 31, 119, 33);
		panelName.add(txtUsersearch);
		txtUsersearch.setColumns(10);
		
		JButton btnGo = new JButton("开始搜索帐号");
		btnGo.setBounds(39, 71, 142, 26);
		panelName.add(btnGo);
		
		JButton button = new JButton("显示授权帐号");
		button.setBounds(62, 107, 119, 26);
		panelName.add(button);
		
		JPanel panelContent = new JPanel();
		panelContent.setBorder(new TitledBorder("微博内容"));
		panelContent.setBounds(236, 8, 660, 668);
		getContentPane().add(panelContent);
		panelContent.setLayout(null);
		
		table = new JTable();
		table.setBounds(12, 173, 636, 483);
		panelContent.add(table);
		
		JPanel panelSearchOption = new JPanel();
		panelSearchOption.setBorder(new TitledBorder("搜索选项"));
		panelSearchOption.setBounds(12, 25, 267, 136);
		panelContent.add(panelSearchOption);
		panelSearchOption.setLayout(null);
		
		JLabel label = new JLabel("搜索内容：");
		label.setBounds(8, 29, 82, 25);
		panelSearchOption.add(label);
		
		txtFindcontent = new JTextField();
		txtFindcontent.setBounds(78, 27, 170, 27);
		panelSearchOption.add(txtFindcontent);
		txtFindcontent.setText("");
		txtFindcontent.setColumns(10);
		
		JRadioButton radioButton = new JRadioButton("搜索当前帐号微博");
		radioButton.setBounds(8, 66, 129, 23);
		panelSearchOption.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("搜索所有微博");
		radioButton_1.setBounds(8, 90, 103, 23);
		panelSearchOption.add(radioButton_1);
		
		JButton btnBtnsearch = new JButton("搜索");
		btnBtnsearch.setBounds(172, 66, 65, 47);
		panelSearchOption.add(btnBtnsearch);
		
		JPanel panelContentTool = new JPanel();
		panelContentTool.setBorder(new TitledBorder("内容工具"));
		panelContentTool.setBounds(288, 25, 360, 136);
		panelContent.add(panelContentTool);
		panelContentTool.setLayout(null);
		
		JButton btnCalcfrequece = new JButton("统计词频");
		btnCalcfrequece.setBounds(12, 25, 97, 28);
		panelContentTool.add(btnCalcfrequece);
		


		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
//		frame.setBounds(100, 100, 450, 300);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
