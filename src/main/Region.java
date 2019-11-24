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
	
	private List<Person> people;

	private int quantityTotalPeople = Constants.QUANTITY_TOTAL_PEOPLE;
	private int quantityInfectedPeople = 0;
	private int quantityDeadPeople = 0;
	
	private double rateTransmissionVariable = 0;
	private double rateTransmissionGeneral = 0;
	
	private int plagueTemperatureIdeal = 0;
	private int plagueDeathPotential = 0;
	
	private int temperature;
		
	private RegionGUI regionGUI;
	private Random random = new Random();
	
	private int daysToFindCure;
	
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
			
			daysToFindCure = random.nextInt((Constants.MAX_DAYS_TO_FIND_CURE - Constants.MIN_DAYS_TO_FIND_CURE) + 1) + Constants.MIN_DAYS_TO_FIND_CURE;
			
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
				
				if (getTickCount() >= daysToFindCure) {
					// TODO: Implementar a cura
					// CURA -> diminuiria a proliferação da praga
					//		-> diminuir em todos as regions
					//		-> o region que encontrar a cura primeiro tem que dar um jeito de notificar as outras regions
					//		-> de dia em dia ou em menos, curando tantas pessoas, não vai curar todos instantaneamente, podendo ocasionar mortes no meio deste processo
				}
				
				if (climate == 'H') {
					temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
				} else if (climate == 'C') {
					temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
				}
				
				System.out.println("TEMPERATURE " + getAID().getName() + " " + temperature);
				
				
				if (plagueDeathPotential == 0 || plagueTemperatureIdeal == 0) {
					
					ACLMessage msg = receive();
					
					if(msg != null) {	
						String[] results = msg.getContent().split("-", 2);
						plagueTemperatureIdeal = Integer.parseInt(results[0]);
						plagueDeathPotential = Integer.parseInt(results[1]);
						System.out.println("POTENCIAL DE MORTE " + plagueDeathPotential);
						System.out.println("TEMPERATURA IDEAL " + plagueTemperatureIdeal);
					 
					} else {
						System.out.println("Block");
						block();
					}
				}
				
				for (Person person : people) {
					if (person.isInfected()) {
						person.incrementDaysInfected();
						if (person.getDaysInfected() == plagueDeathPotential) {
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
