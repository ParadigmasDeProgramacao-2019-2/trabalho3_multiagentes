package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class AmbienteGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	
	private Integer lado;
	private Integer taxaAtualizaçãoTela = 0;
	private ArrayList<Point> posicoes;
	
	public AmbienteGUI(Integer qtd_pessoas){
		this.setBounds(0, 0, 1000, 1000);
		this.setVisible(true);
		this.lado = (int)(Math.sqrt(qtd_pessoas));		
		this.posicoes = new ArrayList<>();
		
		this.table = new JTable(lado, lado){
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

	public void mostrarPessoaInfectada() {		
		Random r = new Random();
		int linha = r.nextInt(this.lado);
		int coluna = r.nextInt(this.lado);

		while(posicoes.contains(new Point(linha, coluna))) {
			System.out.println("Ponto já criado");
			linha = r.nextInt(this.lado);
			coluna = r.nextInt(this.lado);
		}
		
		System.out.println("Linha: " + linha + " Coluna: " + coluna);
		this.posicoes.add(new Point(linha, coluna));		
		table.setValueAt("*", linha, coluna);
		
		CellRenderer mcr = new CellRenderer();
		this.table.getColumnModel().getColumn(coluna).setCellRenderer(mcr);
		
		atualizarTela();
		
	}

	private void atualizarTela() {
		this.table.repaint();
		this.repaint();
		
		if(taxaAtualizaçãoTela == 0) {
			this.setSize(this.getWidth() + 1, this.getHeight() + 1);
			taxaAtualizaçãoTela = 1;
		} else {
			this.setSize(this.getWidth() - 1, this.getHeight() - 1);
			taxaAtualizaçãoTela = 0;
		}
			
	}
	
	

}