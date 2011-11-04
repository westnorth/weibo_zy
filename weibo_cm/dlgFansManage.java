import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableRowSorter;

import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;

import weibo4j.User;

public class dlgFansManage extends JDialog {
	private static final long serialVersionUID = 1L;
	private static String DB_CONNECTION_TEXT = "jdbc:sqlite:weibo.db";
	private JTable table;

	private String[] strTableTitle={"名称","最后一条微博","粉丝数","关注数","用户id"};//表标题
	private Vector vectTableData=new Vector();//表数据
	
	
	String[] strToken=new String[3];
	
	private JScrollPane scrollPaneContent;
	private Table_Model TableDataModel;
	private JTable tableFans;
	
	final int INITIAL_ROWHEIGHT = 33;
	
	private String[] strSelectName;
	
	private static String CONSUMKEY = "416693359";
	private static String CONSUMSECRET = "f3aedd1273689a65b2f6a82f7d77dd25";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			dlgFansManage dialog = new dlgFansManage("");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public dlgFansManage(String strName) {
        try {
			UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		setBounds(100, 100, 607, 393);
		getContentPane().setLayout(null);

		JPanel panelFansTable = new JPanel();
		panelFansTable.setBounds(12, 12, 413, 348);
		getContentPane().add(panelFansTable);
		panelFansTable.setLayout(null);
		tableFans = new JTable();
		tableFans.setBounds(12, 12, 389, 283);
		
		// 将表放入滚动面板中，这种使滚动条和控件分离的做法实在有点不优雅。
		scrollPaneContent=createTable("李四");
		scrollPaneContent.getViewport().setView(tableFans);
		panelFansTable.add(scrollPaneContent);

		scrollPaneContent
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneContent
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneContent.setBounds(12, 12, 390, 285);

		JPanel panelTableControl = new JPanel();
		panelTableControl.setBounds(0, 307, 413, 41);
		panelFansTable.add(panelTableControl);

		JButton button = new JButton("|<<");
		panelTableControl.add(button);

		JButton button_1 = new JButton("<");
		panelTableControl.add(button_1);

		JButton button_2 = new JButton(">");
		panelTableControl.add(button_2);

		JButton button_3 = new JButton(">>|");
		panelTableControl.add(button_3);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(437, 12, 156, 336);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JButton btnDeleteThem = new JButton("delete them");
		btnDeleteThem.setBounds(12, 12, 132, 25);
		panel_1.add(btnDeleteThem);

		JButton btnDeleteAll = new JButton("delete all");
		btnDeleteAll.setBounds(12, 49, 132, 25);
		panel_1.add(btnDeleteAll);

		JButton btnBlockThem = new JButton("block them");
		btnBlockThem.setBounds(12, 86, 132, 25);
		panel_1.add(btnBlockThem);

		JButton btnBlockAll = new JButton("block all");
		btnBlockAll.setBounds(12, 123, 132, 25);
		panel_1.add(btnBlockAll);

		JButton btnFollowBack = new JButton("follow back");
		btnFollowBack.setBounds(12, 160, 132, 25);
		panel_1.add(btnFollowBack);
	}
	
	public JScrollPane createTable(String strName) {

		getFolloersByName(strName);
		// Create a model of the vectTableData.
		TableDataModel = new Table_Model(vectTableData, strTableTitle);
		// Create the table
		tableFans = new JTable(TableDataModel);
		@SuppressWarnings("rawtypes")
		TableRowSorter sorter = new TableRowSorter(TableDataModel);
		tableFans.setRowSorter(sorter);
		tableFans.setAutoscrolls(true);

		tableFans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				final int nrow = tableFans.rowAtPoint(me.getPoint());
				if (nrow < 0)
					return;// 如果当前选择的行小于1,说明处于未选中状态，则返回。
				Object objSelectRow = vectTableData.get(nrow);
				strSelectName = objSelectRow.toString().replace('[', ' ')
						.replace(']', ' ').split(",");// 转换为String后，字符会用一对方括号括起来，这个先用硬编码取掉。
				if (me.getButton() == MouseEvent.BUTTON1) {// 单击鼠标左键
					if (me.getClickCount() == 2) {// 如果是双击
						strToken[0]=strSelectName[0];
						UserDetail dlgDetail=new UserDetail(strToken);
						dlgDetail.show();
					}
				}

				if (me.getButton() == MouseEvent.BUTTON3) {
					tableFans.setRowSelectionInterval(nrow, nrow);
					CreatePopMenu(tableFans, me, nrow, strSelectName[0]);
				}
			}
		});
		tableFans.setRowHeight(INITIAL_ROWHEIGHT);
		scrollPaneContent = new JScrollPane(tableFans);
		return scrollPaneContent;
	}
	
	/**
	 * 通过用户名获得该用户粉丝信息
	 * @param strName 用户名
	 * @return 如果已经将粉丝信息保存在vectTableData，返回真，否则返回假
	 */
	private boolean getFolloersByName(String strName){
//		strTableTitle={"选择","名称","最后一条微博","关注时间","粉丝数","关注数","是否认证","用户id"};
		mySQLite mysql=new mySQLite(DB_CONNECTION_TEXT);
		String[] strUserInfo=mysql.getRowDataFromDB("userInfo", "screenName", "心情慵懒");

		strToken[0]=strUserInfo[0];//user id
		strToken[1]=strUserInfo[2];//token 
		strToken[2]=strUserInfo[3];//token secret
		myWeibo thisWeibo=new myWeibo(CONSUMKEY,CONSUMSECRET);
		long[] ids=thisWeibo.getFollowersIDs(strToken, 0);
		for(long id:ids)
		{
			Vector subVect=new Vector();
			strToken[0]=String.valueOf(id);
			User curUser=thisWeibo.getUserInfo(strToken);
			if(curUser==null)
				continue;
			subVect.add(curUser.getName());
			subVect.add(curUser.getStatus().getText());
			subVect.add(curUser.getFollowersCount());
			subVect.add(curUser.getFriendsCount());
			subVect.add(curUser.getId());
			vectTableData.add(subVect);
		}
		
		return false;
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
