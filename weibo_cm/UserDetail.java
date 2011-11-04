import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.JLabel;

import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;

import weibo4j.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class UserDetail extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private Table_Model TableDataModel;
	final Vector vectTableData=new Vector();
	final Vector subVect=new Vector();
	private String strUserID = "";
	private myWeibo weiboUserDetail=null;
	private static String CONSUMKEY = "416693359";
	private static String CONSUMSECRET = "f3aedd1273689a65b2f6a82f7d77dd25";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			String[] strToken={"李开复","7ec54f90c153baf78238f114d8d08508",
					"066d1018fde349269c77622db21bd4ce"};
			UserDetail dialog = new UserDetail(strToken);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public UserDetail(final String[] strToken) {
        try {
			UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		weiboUserDetail=new myWeibo(CONSUMKEY,CONSUMSECRET);
		strUserID=strToken[0];
		
		setBounds(100, 100, 550, 455);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		table = new JTable();
		table.setBounds(12, 12, 354, 375);
		JScrollPane scrollPaneContent=createTable(strToken);
		scrollPaneContent.getViewport().setView(table);
		contentPanel.add(scrollPaneContent);

		User currUser=weiboUserDetail.getUserInfo(strToken);
		String strImagURL=currUser.getProfileImageURL().toString();
		scrollPaneContent
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneContent
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneContent.setBounds(12, 12, 355, 376);
		createTable(strToken);

		JPanel panel = new JPanel();
		panel.setBounds(378, 12, 122, 114);
		contentPanel.add(panel);

		JLabel lblLblimage = new JLabel();
		lblLblimage.setText("<html><img src='"+strImagURL+"' /><html>");
		panel.add(lblLblimage);
		
//		getContentPane().add(lblLblimage);

		JLabel label = new JLabel("粉丝数：");
		label.setBounds(384, 139, 52, 15);
		contentPanel.add(label);

		JLabel lblNumberoffollowers = new JLabel("");
		lblNumberoffollowers.setText(String.valueOf(currUser.getFollowersCount()));
		lblNumberoffollowers.setBounds(440, 139, 70, 15);
		contentPanel.add(lblNumberoffollowers);

		JLabel label_1 = new JLabel("关注：");
		label_1.setBounds(384, 166, 52, 15);
		contentPanel.add(label_1);

		JLabel lblNumberoffriends = new JLabel("");
		lblNumberoffriends.setText(String.valueOf(currUser.getFriendsCount()));
		lblNumberoffriends.setBounds(440, 166, 70, 15);
		contentPanel.add(lblNumberoffriends);

		JLabel label_2 = new JLabel("发贴数：");
		label_2.setBounds(382, 193, 52, 15);
		contentPanel.add(label_2);
		
		JLabel lblNumberofstatus = new JLabel("numberOfStatus");
		lblNumberofstatus.setText(String.valueOf(currUser.getStatusesCount()));
		lblNumberofstatus.setBounds(440, 193, 70, 15);
		
		JButton btnDelete = new JButton("delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				weiboUserDetail.DestroyGuy(strToken);
			}
		});
		btnDelete.setBounds(378, 312, 117, 25);
		contentPanel.add(btnDelete);

		JButton btnBlock = new JButton("block");
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnBlock.setBounds(378, 230, 117, 25);
		contentPanel.add(btnBlock);

		JButton btnFollowBack = new JButton("follow back");
		btnFollowBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weiboUserDetail.followerSomeGuy(strToken);
			}
		});
		btnFollowBack.setBounds(378, 271, 117, 25);
		contentPanel.add(btnFollowBack);
		

		contentPanel.add(lblNumberofstatus);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public JScrollPane createTable(String[] strToken) {

		// Create a model of the vectTableData.

		String[] strTitle={"微博"};
		ArrayList<statusTree> statusTree= weiboUserDetail.getStatus(
				strToken[0], strToken[1], strToken[2], 1, 20);
		for(statusTree subTree:statusTree)
		{
			Vector subVect=new Vector();
			subVect.add(subTree.getStatus().getText());
			vectTableData.add(subVect);
		}
		TableDataModel = new Table_Model(vectTableData, strTitle);
		// Create the table
		table = new JTable(TableDataModel);
		table.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer()); 
		@SuppressWarnings("rawtypes")
		TableRowSorter sorter = new TableRowSorter(TableDataModel);
		table.setRowSorter(sorter);
		table.setAutoscrolls(true);

		
		table.setRowHeight(25);
		JScrollPane scrollPaneContent = new JScrollPane(table);
		return scrollPaneContent;
	}
	
}

class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {   
    public TableCellTextAreaRenderer() {   
        setLineWrap(true);     
        setWrapStyleWord(true);   
    }   
  
    public Component getTableCellRendererComponent(JTable table, Object value,   
            boolean isSelected, boolean hasFocus, int row, int column) {   
        // 计算当下行的最佳高度   
        int maxPreferredHeight = 0;   
        for (int i = 0; i < table.getColumnCount(); i++) {   
            setText("" + table.getValueAt(row, i));   
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);   
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);   
        }   
  
        if (table.getRowHeight(row) != maxPreferredHeight)  // 少了这行则处理器瞎忙   
            table.setRowHeight(row, maxPreferredHeight);   
  
        setText(value == null ? "" : value.toString());   
        return this;   
    }   
}   
