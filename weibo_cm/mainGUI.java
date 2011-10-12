import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import javax.swing.JCheckBox;

//import ch.randelshofer.quaqua.*;
class statusTree {
	private String strIdFather;
	private Status status;

	public statusTree(String idFather, Status myStatus) {
		strIdFather = idFather;
		status = myStatus;
	}

	public String getDadID() {
		return strIdFather;
	}

	public Status getStatus() {
		return status;
	}
}

public class mainGUI extends JFrame {

	/**
	 * 主界面
	 */
	private static final long serialVersionUID = 1L;
	String[] strToken;// 1,id 2.token 3.token secret
	private static String DB_CONNECTION_TEXT = "jdbc:sqlite:weibo.db";
	private JList listUser;// 显示帐号的列表控件
	final DefaultListModel listModelUser = new DefaultListModel();
	private JTextField txtFindcontent;
	private JTable tableStatus;
	private static String CONSUMKEY = "416693359";
	private static String CONSUMSECRET = "f3aedd1273689a65b2f6a82f7d77dd25";
	private JTextField txtUsersearch;
	private Table_Model TableDataModel;
	final int INITIAL_ROWHEIGHT = 33;
	private ArrayList<statusTree> mainStatusTree = new ArrayList<statusTree>();
	private JScrollPane scrollPaneContent;

	private String[] strSelectName;
	private Vector vectTableData = new Vector();
	String[] strTableTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
				try {
					UIManager
							.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
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
		strToken = new String[3];
		strToken = dialog.getStrArrToken();
		if (strToken[0] == null) {
			JOptionPane.showMessageDialog(null, "未获得授权，即将退出");
			System.exit(ABORT);
		}
		String strUserID = strToken[0];

		Weibo weiboInfo = new Weibo();
		weiboInfo.setToken(strToken[1], strToken[2]);
		User user;
		try {
			user = weiboInfo.showUser(strUserID);
			System.out.println("this user:" + user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		setSize(913, 713);
		getContentPane().setLayout(null);

		// 账号面板
		JPanel panelName = new JPanel();
		panelName.setBorder(new TitledBorder("微博账号名称"));
		panelName.setBounds(12, 6, 220, 619);
		getContentPane().add(panelName);
		panelName.setLayout(null);

		String[] strArrName = getDataFromDB("userInfo", "screenName");
		if (strArrName != null) {
			// DefaultListModel
			listModelUser.addElement(strArrName[0]);
			listUser = new JList(listModelUser);
			mainStatusTree = getStatus("姚晨", strToken[1], strToken[2], 1, 20);
		} else
			listUser = new JList();
		listUser.setBounds(12, 145, 189, 511);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						mainStatusTree = getStatus(o.toString(), strToken[1],
								strToken[2], 1, 100);
						useDataFillTable();
						System.out
								.println("Double-clicked on: " + o.toString());
					}
				}
			}
		};
		listUser.addMouseListener(mouseListener);

		// 将列表放入滚动面板中，这种使滚动条和控件分离的做法实在有点不优雅。
		JScrollPane scrollPaneName = new JScrollPane(listUser);
		scrollPaneName.getViewport().setView(listUser);
		panelName.add(scrollPaneName);

		scrollPaneName
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneName
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneName.setBounds(12, 175, 192, 432);// 不设置的话，就连List都显示不出来了。
		// scrollPane.setSize(192, 515);

		JLabel label_1 = new JLabel("账号");
		label_1.setBounds(12, 34, 71, 25);
		panelName.add(label_1);

		txtUsersearch = new JTextField();
		txtUsersearch.setText("");
		txtUsersearch.setBounds(82, 31, 119, 33);
		panelName.add(txtUsersearch);
		txtUsersearch.setColumns(10);

		JButton btnShowStatus = new JButton("Show status");
		btnShowStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainStatusTree = getStatus(txtUsersearch.getText(),
						strToken[1], strToken[2], 1, 100);
				useDataFillTable();
				listModelUser.addElement(txtUsersearch.getText());
				// final DefaultListModel listModel = new DefaultListModel();//
				// (DefaultListModel)listUser.getModel();
				// listModel.insertElementAt(txtUsersearch.getText(), 0);
				// listUser.repaint();
			}
		});
		btnShowStatus.setBounds(39, 71, 142, 38);
		panelName.add(btnShowStatus);

		JButton btnShowAuthUser = new JButton("显示授权帐号");
		btnShowAuthUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mySQLite userSQL = new mySQLite(DB_CONNECTION_TEXT);
				String[] strUserAuth = userSQL
						.getColumnDataFromDB("userInfo", "name");
				if (strUserAuth[0] == null) {
					JOptionPane.showMessageDialog(null,
							"There are  no Authority User");
					return;
				}
				listModelUser.removeAllElements();
				for (String strName : strUserAuth)
					listModelUser.addElement(strName);
			}
		});
		btnShowAuthUser.setBounds(62, 114, 119, 26);
		panelName.add(btnShowAuthUser);

		JCheckBox chckbxShowLocalData = new JCheckBox("Show local data");
		chckbxShowLocalData.setBounds(12, 148, 169, 23);
		panelName.add(chckbxShowLocalData);

		// 内容面板

		JPanel panelContent = new JPanel();
		panelContent.setBorder(new TitledBorder("微博内容"));
		panelContent.setBounds(236, 8, 660, 655);
		getContentPane().add(panelContent);
		panelContent.setLayout(null);

		tableStatus = new JTable();
		tableStatus.setBounds(12, 173, 636, 483);
		createTable();
		scrollPaneContent.getViewport().setView(tableStatus);
		panelContent.add(scrollPaneContent);

		scrollPaneContent
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneContent
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneContent.setBounds(10, 170, 636, 438);// 不设置的话，就连List都显示不出来了。
		panelContent.add(scrollPaneContent);

		JPanel panelSearchOption = new JPanel();
		panelSearchOption.setBorder(new TitledBorder("搜索选项"));
		panelSearchOption.setBounds(12, 25, 267, 136);
		panelContent.add(panelSearchOption);
		panelSearchOption.setLayout(null);

		JLabel label = new JLabel("搜索内容：");
		label.setBounds(8, 29, 71, 25);
		panelSearchOption.add(label);

		txtFindcontent = new JTextField();
		txtFindcontent.setBounds(81, 27, 167, 27);
		panelSearchOption.add(txtFindcontent);
		txtFindcontent.setText("");
		txtFindcontent.setColumns(10);

		ButtonGroup groupSearch = new ButtonGroup();
		JRadioButton radioSearchCurrentAccount = new JRadioButton("搜索当前帐号微博内容",
				true);
		radioSearchCurrentAccount.setBounds(8, 66, 155, 23);
		panelSearchOption.add(radioSearchCurrentAccount);

		JRadioButton radioSearchAll = new JRadioButton("搜索所有微博内容", false);
		radioSearchAll.setBounds(8, 90, 155, 23);
		panelSearchOption.add(radioSearchAll);
		groupSearch.add(radioSearchAll);
		groupSearch.add(radioSearchCurrentAccount);

		JButton btnBtnsearch = new JButton("搜索");
		btnBtnsearch.setBounds(183, 66, 65, 47);
		panelSearchOption.add(btnBtnsearch);

		JPanel panelContentTool = new JPanel();
		panelContentTool.setBorder(new TitledBorder("内容工具"));
		panelContentTool.setBounds(288, 25, 360, 136);
		panelContent.add(panelContentTool);
		panelContentTool.setLayout(null);

		JButton btnCalcfrequece = new JButton("统计词频");
		btnCalcfrequece.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// I think the good way is :get status and writer all to the
				// pure text file, do this in Funcition getStatus
				// and then use IKAnalyzer to seperate by Chinese words
				// finally ,write per word as a record into table in SQLite DB.
				mySQLite sqlSeg=new mySQLite(DB_CONNECTION_TEXT);
				sqlSeg.insertParseText("status", "status_temp");
			}
		});
		btnCalcfrequece.setBounds(12, 25, 97, 28);
		panelContentTool.add(btnCalcfrequece);

		JPanel panel = new JPanel();
		panel.setBounds(10, 616, 661, 39);
		panelContent.add(panel);

		JButton btnHead = new JButton("|<<");
		btnHead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnHead);

		JButton btnPrev = new JButton("<");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnPrev);

		JButton btnNext = new JButton(">");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnNext);

		JButton btnTail = new JButton(">>|");
		btnTail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnTail);

		initialize();
	}

	/**
	 * set vectTableData to tableStatus
	 * 
	 * @return
	 */
	private boolean useDataFillTable() {
		vectTableData.removeAllElements();
		getDataByStatus();
		if (!vectTableData.isEmpty())// 如果表格数据不为空
			TableDataModel.fireTableDataChanged();
		// tableView.getSelectionModel().setSelectionInterval(0, 0);
		else {
			JOptionPane.showMessageDialog(null, "status is empty");
			tableStatus.revalidate();
		}
		return true;
	}

	public boolean getDataByStatus() {
		int nStatusSize = mainStatusTree.size();
		PrintWriter writerStater =null;
		try {
			writerStater = new PrintWriter("status");
			for (int i = 0; i < nStatusSize; i++) {
				Vector subVect = new Vector();
				Status currStatus = mainStatusTree.get(i).getStatus();
				subVect.add(currStatus.getText());
				subVect.add(String.valueOf(currStatus.getId()));
				subVect.add(mainStatusTree.get(i).getDadID());
				vectTableData.add(subVect);
				
				writerStater.write(currStatus.getText());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
		writerStater.flush();
		writerStater.close();
		}
		return true;
	}

	/**
	 * 由数据库的表中取出一列数据
	 * 
	 * @param strTable
	 *            表名称
	 * @param strColumn
	 *            列名称
	 * @return 数组，包含了该列数据
	 */
	private String[] getDataFromDB(String strTable, String strColumn) {
		String[] strResult = null;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn;
			conn = DriverManager.getConnection(DB_CONNECTION_TEXT);
			// 建立事务机制,禁止自动提交，设置回滚点
			conn.setAutoCommit(false);

			Statement stat = conn.createStatement();
			// stat.executeUpdate(
			ResultSet rs = stat
					.executeQuery("SELECT COUNT(*) AS NumberOfUsers FROM "
							+ strTable + ";");
			String strNumber = rs.getString("NumberOfUsers").trim();
			if (strNumber == null || strNumber == "" || strNumber.equals("0"))
				return null;
			int nUser = Integer.parseInt(rs.getString("NumberOfUsers"));
			strResult = new String[nUser];
			ResultSet rs2 = stat.executeQuery("select " + strColumn + " from "
					+ strTable + ";");
			int i = 0;
			while (rs2.next()) {
				strResult[i] = rs2.getString(strColumn);
				i++;
			}
			stat.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * 获得微博内容,要注意的是如果是Retweet的消息，则需要取得retweeted_status中的内容
	 * 
	 * @param strName
	 *            欲获得内容微博主人的名字或id
	 * @param strTokenKey
	 *            访问令牌
	 * @param strTokenSecret
	 *            访问密钥
	 * @param nPage
	 *            要获得内容的页码编号
	 * @param nNumberOfStatus
	 *            每页要获得的Status的数量
	 * @return 获得内容列表
	 */
	private ArrayList<statusTree> getStatus(String strName, String strTokenKey,
			String strTokenSecret, int nPage, int nNumberOfStatus) {
		System.setProperty("weibo4j.oauth.consumerKey", CONSUMKEY);
		System.setProperty("weibo4j.oauth.consumerSecret", CONSUMSECRET);
		ArrayList<statusTree> listTree = new ArrayList<statusTree>();
		try {
			Weibo weibo = new Weibo();
			weibo.setToken(strTokenKey, strTokenSecret);
			List<Status> list = weibo.getUserTimeline(strName,
					new Paging(nPage).count(nNumberOfStatus));
			if (list.size() == 0) {
				System.out.println("there is no status");
			} else {
				for (int i = 0; i < list.size(); i++) {
					Status statusUser = list.get(i);
					statusTree myTree = new statusTree("0", statusUser);// root
																		// status
																		// the
																		// dad
																		// is
																		// zero
					String strDadId = String.valueOf(statusUser.getId());
					listTree.add(myTree);
					while (statusUser.getRetweeted_status() != null) {
						Status subStatus = statusUser.getRetweeted_status();
						statusTree subTree = new statusTree(strDadId, subStatus);
						listTree.add(subTree);
						statusUser = subStatus;
						strDadId = String.valueOf(statusUser.getId());
					}
					mySQLite myDB = new mySQLite(DB_CONNECTION_TEXT);
					myDB.insertStatus(list);
					String Text = statusUser.getText();
					System.out.println(Text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return listTree;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	}

	public JScrollPane createTable() {

		getDataByStatus();
		strTableTitle = new String[3];
		strTableTitle[0] = "内容";
		strTableTitle[1] = "Id";
		strTableTitle[2] = "dad id";

		// Create a model of the vectTableData.
		TableDataModel = new Table_Model(vectTableData, strTableTitle);
		// Create the table
		tableStatus = new JTable(TableDataModel);
		@SuppressWarnings("rawtypes")
		TableRowSorter sorter = new TableRowSorter(TableDataModel);
		tableStatus.setRowSorter(sorter);
		tableStatus.setAutoscrolls(true);

		tableStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				final int nrow = tableStatus.rowAtPoint(me.getPoint());
				if (nrow < 0)
					return;// 如果当前选择的行小于1,说明处于未选中状态，则返回。
				Object objSelectRow = vectTableData.get(nrow);
				strSelectName = objSelectRow.toString().replace('[', ' ')
						.replace(']', ' ').split(",");// 转换为String后，字符会用一对方括号括起来，这个先用硬编码取掉。
				if (me.getButton() == MouseEvent.BUTTON1) {// 单击鼠标左键
					if (me.getClickCount() == 2) {// 如果是双击
					}
				}

				if (me.getButton() == MouseEvent.BUTTON3) {
					tableStatus.setRowSelectionInterval(nrow, nrow);
					CreatePopMenu(tableStatus, me, nrow, strSelectName[0]);
				}
			}
		});
		tableStatus.setRowHeight(INITIAL_ROWHEIGHT);
		scrollPaneContent = new JScrollPane(tableStatus);
		return scrollPaneContent;
	}

	public void CreatePopMenu(JTable tableView, MouseEvent evt,
			int nSelectedLine, String strName) {
		tableView.changeSelection(nSelectedLine, 2, false, false);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem itemDetail = new JMenuItem("");
		JMenuItem itemDelCurrent = new JMenuItem("delete current weibo");
		JMenuItem itemDelAll = new JMenuItem("delete all weibo");
		JMenuItem itemExport = new JMenuItem("export weibo");
		JMenuItem itemAdd = new JMenuItem("add new weibo");

		popupMenu.add(itemDetail);
		popupMenu.add(itemAdd);
		popupMenu.addSeparator();
		popupMenu.add(itemDelCurrent);
		popupMenu.add(itemDelAll);
		popupMenu.addSeparator();
		popupMenu.add(itemExport);

		itemDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Desktop desktop = Desktop.getDesktop();
				// File fileOpen = new File(getCurrentDir()
				// + strSelectName[2].trim() + File.separator
				// + strCalcFile);
				// if (!fileOpen.isFile()) {
				// JOptionPane.showMessageDialog(null, "文件"
				// + getCurrentDir() + strSelectName[2].trim()
				// + File.separator + strCalcFile + "不存在，请先创建文件");
				// return;
				// }
				// desktop.open(fileOpen);
			}
		});
		itemDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("click detail");
				// TODO detail item
			}
		});
		itemAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("click add item");
				// TODO add item
			}
		});
		itemDelCurrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("click del item");
				// TODO del item
			}
		});
		itemDelAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("click del all");
				// TODO del all
			}
		});
		itemAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点击了添加菜单");
				// TODO add item
			}
		});
		itemExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("click export");
				// TODO export item
			}
		});

		popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	public String getCurrentDir() {
		String curdir = "";
		try {
			curdir = Thread.currentThread().getContextClassLoader()
					.getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return curdir;

	}

}
