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
	private int deathPotential = random.nextInt((Constants.MAX_PLAGUE_DEATH_POTENTIAL - Constants.MIN_PLAGUE_DEATH_POTENTIAL) + 1) + Constants.MIN_PLAGUE_DEATH_POTENTIAL;
	
	public void setup() {
		System.out.println("Setup Plague");
		System.out.println("Hello! Plague-Agent "+ getAID().getName() + " is ready.");
		System.out.println(getAID().getName() + " ideal temperature " + temperatureIdeal + " death potential " + deathPotential);
		
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
		
		addBehaviour(new InfectCyclicBehaviour(this));

	}
	
	class InfectCyclicBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public InfectCyclicBehaviour(Agent a) {
			super(a);
		}
		
		@Override
		public void action() {
			
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
					
					msg.setContent(String.valueOf(temperatureIdeal) + "-" + String.valueOf(deathPotential));
					msg.addReceiver(res.getName());	
					send(msg);			
				}
			} else {
				block();
			}
			
		}
	}
	
}
