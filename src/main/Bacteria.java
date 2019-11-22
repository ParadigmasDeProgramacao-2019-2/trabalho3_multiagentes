package main;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Bacteria extends Agent {

	private static final long serialVersionUID = 1L;
	
	public DFAgentDescription[] result = null;
	
	public void setup() {
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType( "ambiente" );
		dfd.addServices(sd);
		
		
		try {
			
			result = DFService.search(this, dfd);
		
		} catch (FIPAException e) {
			
			e.printStackTrace();
		}
		
		System.out.println(result.length + " resultados");
		if (result.length > 0)
			System.out.println(" " + result[0].getName());
		
		

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("Testando meu parça! ");
		msg.addReceiver(result[0].getName());
		
		send(msg);
		
		
		
	}
	
	
}
