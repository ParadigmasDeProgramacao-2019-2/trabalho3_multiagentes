package main;

import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.behaviours.*;

public class Ambiente extends Agent {

	private static final long serialVersionUID = 1L;

	
	
	public int qtd_pessoas_infectadas = 0;
	public int qtd_pessoas_total = 0;
	public int clima;
	public int saneamento;
	
	public void setup() {
		
		
//		 // Register the  service in the yellow pages
//		 
//		 DFAgentDescription dfd = new DFAgentDescription();
//		 dfd.setName( this.getAID() );
//		 
//		 ServiceDescription sd = new ServiceDescription();
//		 sd.setType( "ambiente" );
//		 sd.setName( "registrar-ambiente" );
//		 dfd.addServices(sd);  
//		 
//		 try {
//		   DFService.register(this, dfd);
//		 }
//		 catch (FIPAException fe) {
//		   fe.printStackTrace();
//		 }
		
		addBehaviour(new TickerBehaviour(this, 500) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {

				qtd_pessoas_infectadas = new Random().nextInt(5); 
				System.out.println("Numero de pessoas infectadas: " + qtd_pessoas_infectadas);
				
			}
			
		});
		 
		
		
	}
	

	
}
