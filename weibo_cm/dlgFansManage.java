import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;


public class dlgFansManage extends JDialog {
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			dlgFansManage dialog = new dlgFansManage();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public dlgFansManage() {
		setBounds(100, 100, 607, 393);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 413, 348);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 307, 413, 41);
		panel.add(panel_2);
		
		JButton button = new JButton("|<<");
		panel_2.add(button);
		
		JButton button_1 = new JButton("<");
		panel_2.add(button_1);
		
		JButton button_2 = new JButton(">");
		panel_2.add(button_2);
		
		JButton button_3 = new JButton(">>|");
		panel_2.add(button_3);
		
		table = new JTable();
		table.setBounds(12, 12, 389, 283);
		panel.add(table);
		
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
}
