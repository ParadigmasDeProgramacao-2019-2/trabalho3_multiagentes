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
	
	public DFAgentDescription[] result = null;
	public DFAgentDescription dfd = new DFAgentDescription();
	public ServiceDescription sd = new ServiceDescription();
	
	public void setup() {
		System.out.println("Setup Plague");
		System.out.println("Hello! Region-agent "+ getAID().getName() + " is ready.");
		System.out.println(getAID().getName() + " ideal temperature " + temperatureIdeal);
		
		addBehaviour(new InfectTickerBehaviour(this, Constants.TIME_PROLIFERATION_MS));

	}
	
	class InfectTickerBehaviour extends TickerBehaviour {

		public InfectTickerBehaviour(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			
			sd.setType( "region" );
			dfd.addServices(sd);
			
			try {
				result = DFService.search(myAgent, dfd);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			
//			System.out.println(result.length + " resultados");
			
			if (result.length > 0) {
				for (DFAgentDescription res : result) {						
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);		
					
					msg.setContent("Infect");
//					System.out.println("PRINTANDO O NOME " + res.getName());
					msg.addReceiver(res.getName());	
					send(msg);			
				}
			}
			
		}
	}
	
}
