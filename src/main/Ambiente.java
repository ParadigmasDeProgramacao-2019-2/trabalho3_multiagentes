package main;

import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class Ambiente extends Agent {

	private static final long serialVersionUID = 1L;
	
	public int qtd_pessoas_infectadas = 0;
	public int lado = 50;
	public int qtd_pessoas_total = lado * lado;
	
	public double taxa_transmissao_variante = 0;
	public int clima;
	public int saneamento;
	public AmbienteGUI ambienteGUI;
	public Random r = new Random();
	public double taxa_transmissao_geral = 0;
	
	public void setup() {		
		
		taxa_transmissao_geral = 0.1 + r.nextDouble() * 0.4;
		
		 // Register the  service in the yellow pages
		 System.out.println("Setup Amb");
		 DFAgentDescription dfd = new DFAgentDescription();
		 dfd.setName( this.getAID() );
		 
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType( "ambiente" );
		 sd.setName( "registrar-ambiente" );
		 dfd.addServices(sd);  
		 
		 try {
		   DFService.register(this, dfd);
		 }
		 catch (FIPAException fe) {
		   fe.printStackTrace();
		 } 
		 
		 
		 addBehaviour(new TickerBehaviour(this, 2000 ) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				
				System.out.println("A taxa de transimissao atual eh: " + taxa_transmissao_geral);
				
				
				ACLMessage msg = receive();
				
				 if(msg != null) {	
					 
					 if(msg.getContent().equals(new String("Infectar"))) {
						 infectar();
					 }
					 else {
						 System.out.println("Erro:" + msg.getContent());
					 }
					 
				 } else {
					 block();
				 }
				 
				 taxa_transmissao_variante += taxa_transmissao_geral * qtd_pessoas_infectadas;
				 System.out.println(taxa_transmissao_variante);
				 
				 if(((int) taxa_transmissao_variante) > qtd_pessoas_infectadas && ((int) taxa_transmissao_variante) < qtd_pessoas_total)
					 infectar();
				 else if(((int) taxa_transmissao_variante) >= qtd_pessoas_total) {
					 
					 if(taxa_transmissao_variante > qtd_pessoas_infectadas) {
						 taxa_transmissao_variante = qtd_pessoas_total;
						 infectar();
					 }
					 
					 myAgent.doSuspend();
				 }
								 
			}
		 });
	}
	
	private void infectar() {
		if(qtd_pessoas_infectadas == 0)
			ambienteGUI = new AmbienteGUI(qtd_pessoas_total);
	
		while(qtd_pessoas_infectadas < taxa_transmissao_variante || qtd_pessoas_infectadas == 0) {
			this.qtd_pessoas_infectadas++;		
			this.ambienteGUI.mostrarPessoaInfectada();
		}
		
		System.out.println(String.valueOf("Qtd: " + this.qtd_pessoas_infectadas));		
	}
}
