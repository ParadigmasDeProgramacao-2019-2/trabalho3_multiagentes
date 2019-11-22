package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AmbienteGUI extends JFrame{
	private JPanel panel;
	private Integer lado;
	private Integer taxaAtualização = 0;
	
	public AmbienteGUI(Integer qtd_pessoas){
		this.setBounds(50, 50, 400, 400);
		this.setVisible(true);
		
		this.lado = (int)(Math.sqrt(qtd_pessoas));
		this.panel = new JPanel(new GridLayout(lado, lado));
		this.panel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
//		for (int i =0; i<(lado*lado); i++){
//		    final JLabel label = new JLabel("Label");
//		    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//		    panel.add(label);
//		}		
		this.add(panel);
		this.panel.setVisible(true);
	}

	public void mostrarPessoaInfectada() {		
//		Random r = new Random();
//		int x = r.nextInt(this.lado + 1);		
//		
//		r = new Random();
//		int y = r.nextInt(this.lado + 1);
		
//		ImageIcon img = new ImageIcon("assets/infectada.jpg");
		this.panel.add(new JLabel("*"));
		
		atualizarTela();
	}

	private void atualizarTela() {
		this.panel.repaint();
		this.repaint();
		
		if(taxaAtualização == 0) {
			this.setSize(this.getWidth() + 1, this.getHeight() + 1);
			taxaAtualização = 1;
		} else {
			this.setSize(this.getWidth() - 1, this.getHeight() - 1);
			taxaAtualização = 0;
		}
			
	}

}
