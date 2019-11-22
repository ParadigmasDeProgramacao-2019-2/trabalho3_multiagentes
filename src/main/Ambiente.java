package main;

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
	
	public double transmissao = 0;
	public int clima;
	public int saneamento;
	public AmbienteGUI ambienteGUI;
	
	public void setup() {		
		
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
		 
		 
		 addBehaviour(new TickerBehaviour(this, 2000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				 ACLMessage msg = receive();
				
				 if(msg != null) {					
					 System.out.println("== Resposta" + " <- " 
							 +  msg.getContent() + " de "
							 +  msg.getSender().getName());	
					 if(msg.getContent().equals(new String("Infectar"))) {
						 infectar();
					 }
					 else {
						 System.out.println("Erro:" + msg.getContent());
					 }
				 }
				 
				 transmissao += 0.1 * qtd_pessoas_infectadas;
				 System.out.println(transmissao);
				 
				 if(((int) transmissao) > qtd_pessoas_infectadas && ((int) transmissao) < qtd_pessoas_total)
					 infectar();
				 else if(((int) transmissao) >= qtd_pessoas_total)
		 			block();				 
			}
		 });
	}
	
	private void infectar() {
		if(qtd_pessoas_infectadas == 0)
			ambienteGUI = new AmbienteGUI(qtd_pessoas_total);
	
		this.qtd_pessoas_infectadas++;		
		this.ambienteGUI.mostrarPessoaInfectada();
		
		System.out.println(String.valueOf("Qtd: " + this.qtd_pessoas_infectadas));		
	}
}
