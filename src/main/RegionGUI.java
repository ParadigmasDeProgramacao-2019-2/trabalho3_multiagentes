package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class RegionGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	
	private Integer side;
	private Integer rateScreenUpdate = 0;
	private ArrayList<Point> positions;
	
	public RegionGUI(List<Person> people){
		this.setTitle("RegionGUI");
		this.setBounds(0, 0, 400, 400);
		this.setResizable(false);
		this.setVisible(true);
		this.side = Constants.SIDE; // (int)(Math.sqrt(people.size()));		
		this.positions = new ArrayList<>();
		
		this.table = new JTable(side, side){
			/**
			 * 
			 */
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
//		Random random = new Random();
		
		int line = person.getLine(); // random.nextInt(this.side);
		int column = person.getColumn(); // random.nextInt(this.side);

//		while(positions.contains(new Point(line, column))) {
//			System.out.println("Ponto já criado");
//			line = random.nextInt(this.side);
//			column = random.nextInt(this.side);
//		}
		
		System.out.println("Linha: " + line + " Coluna: " + column);
		this.positions.add(new Point(line, column));		
		table.setValueAt("*", line, column);
		
		CellRenderer mcr = new CellRenderer();
		this.table.getColumnModel().getColumn(column).setCellRenderer(mcr);
		
		updateScreen();
		
	}
	
	public void showDeadPerson(Person person) {		
		
		int line = person.getLine(); // random.nextInt(this.side);
		int column = person.getColumn(); // random.nextInt(this.side);
		
		System.out.println("Linha: " + line + " Coluna: " + column);
		this.positions.add(new Point(line, column));		
		table.setValueAt("-", line, column);
		
		CellRenderer mcr = new CellRenderer();
		this.table.getColumnModel().getColumn(column).setCellRenderer(mcr);
		
		updateScreen();
		
	}

	private void updateScreen() {
		this.table.repaint();
		this.repaint();
		
		if(rateScreenUpdate == 0) {
			this.setSize(this.getWidth() + 1, this.getHeight() + 1);
			rateScreenUpdate = 1;
		} else {
			this.setSize(this.getWidth() - 1, this.getHeight() - 1);
			rateScreenUpdate = 0;
		}
			
	}
	
	

}