package main;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class Bacteria extends Agent {

	private static final long serialVersionUID = 1L;
	
	public DFAgentDescription[] result = null;
	public DFAgentDescription dfd = new DFAgentDescription();
	public ServiceDescription sd = new ServiceDescription();
	
	public void setup() {
		System.out.println("setup bac");		
		
		addBehaviour(new TickerBehaviour(this, 500) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				
				sd.setType( "ambiente" );
				dfd.addServices(sd);
				
				try {
					result = DFService.search(myAgent, dfd);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
				
				System.out.println(result.length + " resultados");
				
				if (result.length > 0) {
//					System.out.println(" " + result[0].getName());
					
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					
					msg.setContent("Infectar");
					msg.addReceiver(result[0].getName());		
					
					send(msg);			
				}
				
			}
		});

	}
}
