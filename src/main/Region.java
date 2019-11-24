package main;

import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

public class Region extends Agent {

	private static final long serialVersionUID = 1L;
	
	private char climate;
	
	public int side = 20;
	
	public int quantityTotalPeople = side * side;
	public int quantityInfectedPeople = 0;
	
	public double rateTransmissionVariable = 0;
	public double rateTransmissionGeneral = 0;
	
	public int temperature;
	
	public RegionGUI regionGUI;
	public Random random = new Random();
	
	public void setup() {	
		
		System.out.println("Setup Region");
		System.out.println("Hello! Region-agent "+ getAID().getName() + " is ready.");

		// Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			climate = (char) args[0];
			System.out.println("Climate is "+ climate);
			
			if (climate == 'H') {
				temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
				System.out.println("TEMPERATURE " + temperature);
			} else if (climate == 'C') {
				temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
				System.out.println("TEMPERATURA " + temperature);
			} else {
				System.out.println("No corrected climate specified");
				doDelete();
			}

			regionGUI = new RegionGUI(quantityTotalPeople);
			
			rateTransmissionGeneral = 0.1 + random.nextDouble() * 0.3;
			
			// Register the service in the yellow pages
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName( this.getAID() );
			 
			ServiceDescription sd = new ServiceDescription();
			sd.setType( "region" );
			sd.setName( "register-region" );
			dfd.addServices(sd);  
			 
			try {
			  DFService.register(this, dfd);
			}
			catch (FIPAException fe) {
			  fe.printStackTrace();
			}
			
			addBehaviour(new TemperatureTickerBehaviour(this, Constants.TIME_DAY_MS));
			 
			addBehaviour(new InfectRegionTickerBehaviour(this, Constants.TIME_DAY_MS));
			
		} else {
			// Make the agent terminate
			System.out.println("No climate specified");
			doDelete();
		}
	}
	
	private void infectar() {
//		if(quantityInfectedPeople == 0)
//			regionGUI = new RegionGUI(quantityTotalPeople);
	
		while(quantityInfectedPeople < rateTransmissionVariable || quantityInfectedPeople == 0) {
			
			this.quantityInfectedPeople++;		
			this.regionGUI.mostrarPessoaInfectada();
		}
		
		System.out.println(String.valueOf("Qtd: " + this.quantityInfectedPeople));		
	}
	
	class TemperatureTickerBehaviour extends TickerBehaviour {

		public TemperatureTickerBehaviour(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			
			if (quantityInfectedPeople == quantityTotalPeople) {
				System.out.println("PARANDO COMPORTAMENTO TemperatureTickerBehaviour (todos infectados)");
				stop();
			} else {
				if (climate == 'H') {
					temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
				} else if (climate == 'C') {
					temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
				}
				
				System.out.println("TEMPERATURE " + getAID().getName() + " " + temperature);
			}
		}
	}
	
	class InfectRegionTickerBehaviour extends TickerBehaviour {

		public InfectRegionTickerBehaviour(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			
			if (quantityInfectedPeople == quantityTotalPeople) {
				System.out.println("PARANDO COMPORTAMENTO InfectTickerBehaviour (todos infectados)");
				stop();
			} else {
//				if (climate == 'H') {
//					temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
//				} else if (climate == 'C') {
//					temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
//				}
				
//				System.out.println("TEMPERATURE " + getAID().getName() + " " + temperature);
				
				System.out.println("A taxa de transimissao atual eh: " + rateTransmissionGeneral);
				
				ACLMessage msg = receive();
				
				if(msg != null) {	
					
					if(msg.getContent().equals(new String("Infect"))) {
						System.out.println("Infect");
						infectar();
					} else {
						System.out.println("Erro:" + msg.getContent());
					}					 
				} else {
					System.out.println("Block");
					block();
				}
				
				rateTransmissionVariable += rateTransmissionGeneral * quantityInfectedPeople;
				System.out.println(rateTransmissionVariable);
				
				if(((int) rateTransmissionVariable) > quantityInfectedPeople && ((int) rateTransmissionVariable) < quantityTotalPeople) {
					infectar();
				} else if(((int) rateTransmissionVariable) >= quantityTotalPeople) {
					
					if(rateTransmissionVariable > quantityInfectedPeople) {
						rateTransmissionVariable = quantityTotalPeople;
						infectar();
					}
					// TODO: verificar (o agente não irá ser suspenso, ele tem que continuar ativo para curas e mortes)
					myAgent.doSuspend();
				}								 
			}
			
		}
	}
}
