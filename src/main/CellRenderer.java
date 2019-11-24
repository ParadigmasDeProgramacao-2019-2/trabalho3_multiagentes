package main;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	@Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    if (table.getModel().getValueAt(row, column) != null && table.getModel().getValueAt(row, column).equals(new String("*"))) {
	      component.setBackground(Color.RED);
	      component.setForeground(Color.RED);
	    } else {
	      component.setBackground(Color.WHITE);
	    }

	  return component;
	}
}