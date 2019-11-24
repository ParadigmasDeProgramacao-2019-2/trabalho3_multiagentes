package main;

import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class Plague extends Agent {

	private static final long serialVersionUID = 1L;
	
	public Random random = new Random();
	private int temperatureIdeal = random.nextInt((Constants.MAX_PLAGUE_TEMPERATURE_IDEAL - Constants.MIN_PLAGUE_TEMPERATURE_IDEAL) + 1) + Constants.MIN_PLAGUE_TEMPERATURE_IDEAL;
	private int deathPotencial = random.nextInt((30 - 20) + 1) + 20;
	
	public void setup() {
		System.out.println("Setup Plague");
		System.out.println("Hello! Region-agent "+ getAID().getName() + " is ready.");
		System.out.println(getAID().getName() + " ideal temperature " + temperatureIdeal);
		
		// Register the service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName( this.getAID() );
		 
		ServiceDescription sd = new ServiceDescription();
		sd.setType( "plague" );
		sd.setName( "register-plague" );
		dfd.addServices(sd);  
		 
		try {
		  DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
		  fe.printStackTrace();
		}
		
		addBehaviour(new InfectTickerBehaviour(this, Constants.TIME_PROLIFERATION_MS));

	}
	
	class InfectTickerBehaviour extends TickerBehaviour {

		private static final long serialVersionUID = 1L;

		public InfectTickerBehaviour(Agent a, long period) {
			super(a, period);
		}
		
		@Override
		public void onTick() {
			
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			DFAgentDescription[] result = null;

			sd.setType( "region" );
			dfd.addServices(sd);
			
			try {
				result = DFService.search(myAgent, dfd);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			
			if (result.length > 0) {
				for (DFAgentDescription res : result) {						
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);		
					
					msg.setContent(String.valueOf(temperatureIdeal) + "-" + String.valueOf(deathPotencial));
					msg.addReceiver(res.getName());	
					send(msg);			
				}
			}
			
		}
	}
	
}
