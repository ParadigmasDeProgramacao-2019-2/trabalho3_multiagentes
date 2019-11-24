package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class RegionGUI extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	
	private CellRenderer cellRenderer;
	private Integer side;
	
	public RegionGUI(List<Person> people){
		this.setTitle("RegionGUI");
		this.setBounds(0, 0, 400, 400);
		this.setVisible(true);
		this.side = Constants.SIDE;	
		this.cellRenderer = new CellRenderer();
		
		this.table = new JTable(side, side){
			
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
	        }
	    };		    
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setEnabled(false);
		table.setTableHeader(null);
		
		this.getContentPane().add( new JScrollPane( table ), BorderLayout.CENTER );
	}

	public void showInfectedPerson(Person person) {		
		
		int line = person.getLine();
		int column = person.getColumn();

		System.out.println("Linha: " + line + " Coluna: " + column);
		table.setValueAt("*", line, column);
		
		this.table.getColumnModel().getColumn(column).setCellRenderer(cellRenderer);
		
	}
	
	public void showDeadPerson(Person person) {		
		
		int line = person.getLine();
		int column = person.getColumn();
		
		System.out.println("Linha: " + line + " Coluna: " + column);
		table.setValueAt("-", line, column);
		
		this.table.getColumnModel().getColumn(column).setCellRenderer(cellRenderer);
		
	}

	@Override
	public void run() {
        this.repaint();
	}
		
}