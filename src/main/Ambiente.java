package main;

import java.util.Random;

import jade.core.AID;
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
	public int qtd_pessoas_total = 0;
	public int clima;
	public int saneamento;
	
	public void setup() {
		
		
		 // Register the  service in the yellow pages
		 
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
		 
		 
		
		addBehaviour(new CyclicBehaviour( this ) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage msg = receive();
				
				if(msg != null) {
					
					System.out.println("== Resposta" + " <- " 
							 +  msg.getContent() + " de "
							 +  msg.getSender().getName());
					
				}
				
				block();
				
			}
		});
		 
		
		
	}
	

	
}
