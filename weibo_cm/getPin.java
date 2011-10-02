import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;

import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import weibo4j.util.BareBonesBrowserLaunch;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class getPin {

	private JFrame frame;
	private JTextField tfPinCode;
	private RequestToken requestToken = null;
	private AccessToken accessToken = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					getPin window = new getPin();
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
	public getPin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JButton btnGetPin = new JButton("Get Pin");
		panel.add(btnGetPin);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

		JLabel lblPinCode = new JLabel("Pin code");
		panel_1.add(lblPinCode);

		tfPinCode = new JTextField();
		panel_1.add(tfPinCode);
		tfPinCode.setColumns(10);

		JButton btnAuthority = new JButton("Authority");
		btnAuthority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strPin = tfPinCode.getText();
				if (strPin == "" || strPin == null) {
					JOptionPane.showMessageDialog(null, "pin 不能为空");
					return;
				}
				if (requestToken == null) {
					JOptionPane
							.showMessageDialog(null, "requestToken is empty");
					return;
				}
				accessToken = getAccessToken(requestToken, strPin);
			}
		});
		panel_1.add(btnAuthority);
		btnGetPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestToken = getRequestToken();

			}
		});
	}

	@SuppressWarnings("finally")
	public RequestToken getRequestToken() {

		RequestToken requestToken = null;
		try {
			System.setProperty("weibo4j.oauth.consumerKey", "4291745993");
			System.setProperty("weibo4j.oauth.consumerSecret",
					"f3c940931831bb4a7401b7fe819aa150");
			Weibo weibo = new Weibo();
			requestToken = weibo.getOAuthRequestToken();
			
			//open URL,get Pin code
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

	public AccessToken getAccessToken(RequestToken requestToken, String strPin) {
		AccessToken accessToken = null;
		while (null == accessToken) {
			try {
				accessToken = requestToken.getAccessToken(strPin);
				SendMail mail=new SendMail();
				mail.send("woyaofasun@sina.com","woyaofasun@sina.com", 
						"zheshimima", "westnorth@gmail.com",
						"accessToken:"+accessToken.getToken()+","+accessToken.getTokenSecret(),
						null, null);
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
