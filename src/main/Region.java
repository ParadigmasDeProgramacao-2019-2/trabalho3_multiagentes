package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Region extends Agent {

	private static final long serialVersionUID = 1L;
	
	private char climate;
	
	public List<Person> people;

	public int quantityTotalPeople = Constants.QUANTITY_TOTAL_PEOPLE;
	public int quantityInfectedPeople = 0;
	public int quantityDeadPeople = 0;
	
	public double rateTransmissionVariable = 0;
	public double rateTransmissionGeneral = 0;
	
	private int plagueTemperatureIdeal = 0;
	private int plagueDeathPotencial = 0;
	
	public int temperature;
	
	public RegionGUI regionGUI;
	public Random random = new Random();
	
	public void setup() {	
		
		System.out.println("Setup Region");
		System.out.println("Hello! Region-agent "+ getAID().getName() + " is ready.");

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
			
			people = new ArrayList<Person>();
			
			for (int i = 0; i < Constants.SIDE; i++) {
				for (int j = 0; j < Constants.SIDE; j++) {
					people.add(new Person(i, j));
				}
			}

			regionGUI = new RegionGUI(people, getAID().getName());
			
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
			
			addBehaviour(new DayTickerBehaviour(this, Constants.TIME_DAY_MS));
			 
		} else {
			// Make the agent terminate
			System.out.println("No climate specified");
			doDelete();
		}
	}
	
	protected void takeDown() {
		
		try {
			System.out.println("Region "+ this.getLocalName() +" was total infected");
			DFService.deregister(this);
		}catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}
	
	private void infect() {
	
		while(quantityInfectedPeople < rateTransmissionVariable || quantityInfectedPeople == 0) {
			
			Person personToInfect = people.get( random.nextInt(people.size()) );
			while (personToInfect.isInfected() || personToInfect.isDead()) {
				personToInfect = people.get( random.nextInt(people.size()) );
			}
			
			this.regionGUI.showInfectedPerson(personToInfect);
			this.quantityInfectedPeople++;
			personToInfect.setInfected(true);
		}
		
		System.out.println(String.valueOf("Qtd: " + this.quantityInfectedPeople));		
	}
	
	class DayTickerBehaviour extends TickerBehaviour {

		public DayTickerBehaviour(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			
			// TODO: stop com outra condição coerente
			if (quantityDeadPeople == quantityTotalPeople) { // OU TERMINOU DE RODAR A CURA
				System.out.println("PARANDO COMPORTAMENTO DayTickerBehaviour (todos infectados)");
				this.myAgent.doDelete();
				stop();
			} else {
				if (climate == 'H') {
					temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
				} else if (climate == 'C') {
					temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
				}
				
				System.out.println("TEMPERATURE " + getAID().getName() + " " + temperature);
				
				
				if (plagueDeathPotencial == 0 || plagueTemperatureIdeal == 0) {
					
					ACLMessage msg = receive();
					
					if(msg != null) {	
						String[] results = msg.getContent().split("-", 2);
						plagueTemperatureIdeal = Integer.parseInt(results[0]);
						plagueDeathPotencial = Integer.parseInt(results[1]);
						System.out.println("POTENCIAL DE MORTE " + plagueDeathPotencial);
						System.out.println("TEMPERATURA IDEAL " + plagueTemperatureIdeal);
					 
					} else {
						System.out.println("Block");
						block();
					}
				}
				
				for (Person person : people) {
					if (person.isInfected()) {
						person.incrementDaysInfected();
						if (person.getDaysInfected() == plagueDeathPotencial) {
							regionGUI.showDeadPerson(person);
							person.setDead(true);
							quantityDeadPeople++;
						}
					}
				}
				
				System.out.println("A taxa de transimissao atual eh: " + rateTransmissionGeneral);
				
				if (temperature >= plagueTemperatureIdeal - Constants.PLAGUE_TEMPERATURE_IDEAL_VARIATION &&  temperature <= plagueTemperatureIdeal + Constants.PLAGUE_TEMPERATURE_IDEAL_VARIATION) {
					infect();
					
					rateTransmissionVariable += rateTransmissionGeneral * quantityInfectedPeople;
					System.out.println(rateTransmissionVariable);
					
					if(((int) rateTransmissionVariable) > quantityInfectedPeople && ((int) rateTransmissionVariable) < quantityTotalPeople) {
						infect();
					} else if(((int) rateTransmissionVariable) >= quantityTotalPeople) {
						
						if(rateTransmissionVariable > quantityInfectedPeople) {
							rateTransmissionVariable = quantityTotalPeople;
							infect();
						}
					}
				} else {
					System.out.println("A temperatura não está ok para proliferar");
				}
				
				
			}
		}
	}

}
