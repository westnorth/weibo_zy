import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel类，继承了AbstractTableModel
 * 
 * @author 五斗米
 * @author westnorth进行了部分修正
 */
public class Table_Model extends AbstractTableModel {
	private static final long serialVersionUID = -7495940408592595397L;
	private Vector content = null;
	private String[] title_name = null;
	public Table_Model(Vector vectContent, String[] title) {
		content = vectContent;
		title_name = title;
	}

	public void addRow(Vector vectRow) {
		// Vector v = new Vector(4);
		// v.add(0, new Integer(content.size()));
		// v.add(1, name);
		// v.add(2, new Boolean(sex));
		// v.add(3, age);
		content.add(vectRow);
	}

	public void removeRow(int row) {
		content.remove(row);
	}

	public void removeRows(int row, int count) {
		for (int i = 0; i < count; i++) {
			if (content.size() > row) {
				content.remove(row);
			}
		}
	}

	/**
	 * 让表格中某些值可修改，但需要setValueAt(Object value, int row, int col)方法配合才能使修改生效
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// if (columnIndex == 0) {
		// return false;
		// }
		// return true;
		return false;
	}

	/**
	 * 使修改的内容生效
	 */
	public void setValueAt(Object value, int row, int col) {
		((Vector) content.get(row)).remove(col);
		((Vector) content.get(row)).add(col, value);
		this.fireTableCellUpdated(row, col);
	}

	public String getColumnName(int col) {
		return title_name[col];
	}

	public int getColumnCount() {
		return title_name.length;
	}

	public int getRowCount() {
		return content.size();
	}

	public Object getValueAt(int row, int col) {
		return ((Vector) content.get(row)).get(col);
	}

	/**
	 * 返回数据类型
	 */
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
}