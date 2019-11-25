package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
	private int quantityHealedPeople = 0;
	
	private double rateTransmissionVariable = 0;
	private double rateTransmissionGeneral = 0;
	
	private int plagueTemperatureIdeal = 0;
	private int plagueDeathPotential = 0;
	
	private int temperature;
		
	private RegionGUI regionGUI;
	private Random random = new Random();
	
	private int daysToFindCure;
	private int healedPersonsADay;
	
	public void setup() {	
		
		System.out.println("Setup Region");
		System.out.println("Hello! Region-agent "+ getAID().getName() + " is ready.");

		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			climate = args[0].toString().charAt(0);
			
			if (climate == 'H') {
				temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
				System.out.println("TEMPERATURE " + temperature);
			} else if (climate == 'C') {
				temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
				System.out.println("TEMPERATURE " + temperature);
			} else {
				System.out.println("No corrected climate specified");
				doDelete();
			}
			
			daysToFindCure = random.nextInt((Constants.MAX_DAYS_TO_FIND_CURE - Constants.MIN_DAYS_TO_FIND_CURE) + 1) + Constants.MIN_DAYS_TO_FIND_CURE;
			healedPersonsADay = random.nextInt((Constants.MAX_PEOPLE_TO_HEAL - Constants.MIN_PEOPLE_TO_HEAL) + 1) + Constants.MIN_PEOPLE_TO_HEAL;
			
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
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}
	
	class DayTickerBehaviour extends TickerBehaviour {

		public DayTickerBehaviour(Agent a, long period) {
			super(a, period);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			
			if (quantityDeadPeople + quantityHealedPeople == quantityTotalPeople) {
				System.out.println("PARANDO COMPORTAMENTO DayTickerBehaviour " + myAgent.getName());

				System.out.println("\n\n**********REPORT**********");
				System.out.println("Quantity of days: " + getTickCount());
				System.out.println("Quantity of people: " + quantityTotalPeople);
				System.out.println("Quantity of infected people: " + quantityInfectedPeople);
				System.out.println("Quantity of healed people: " + quantityHealedPeople);
				System.out.println("Quantity of dead people: " + quantityDeadPeople);
				System.out.println("Quantity of days to find cure: " + daysToFindCure);
				System.out.println("Daily cure capacity: " + healedPersonsADay + "\n\n");
				
				OutputStream os = null;
				try {
					os = new FileOutputStream(new File(myAgent.getName() + ".txt"));
					
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("**********REPORT**********\n");
					stringBuilder.append("Quantity of days: " + getTickCount() + "\n");
					stringBuilder.append("Quantity of people: " + quantityTotalPeople + "\n");
					stringBuilder.append("Quantity of infected people: " + quantityInfectedPeople + "\n");
					stringBuilder.append("Quantity of healed people: " + quantityHealedPeople + "\n");
					stringBuilder.append("Quantity of dead people: " + quantityDeadPeople + "\n");
					stringBuilder.append("Quantity of days to find cure: " + daysToFindCure + "\n");
					stringBuilder.append("Daily cure capacity: " + healedPersonsADay + "\n");
						
					os.write(stringBuilder.toString().getBytes());
				
					os.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
				this.myAgent.doDelete();
				stop();
			} else {
					if (getTickCount() >= daysToFindCure) {
						cure();
					}
					
					updateTemperature();
					
					System.out.println("TEMPERATURE " + getAID().getName() + " " + temperature);
					
					if (plagueDeathPotential == 0 || plagueTemperatureIdeal == 0) {
						receiveMessagePlague();
					}
					
					updateDeath();
					
					verifyInfectByTemperature();
			}
		}

		private void verifyInfectByTemperature() {
			if (temperature >= plagueTemperatureIdeal - Constants.PLAGUE_TEMPERATURE_IDEAL_VARIATION &&  temperature <= plagueTemperatureIdeal + Constants.PLAGUE_TEMPERATURE_IDEAL_VARIATION) {
				infect();
				
				rateTransmissionVariable += rateTransmissionGeneral * quantityInfectedPeople;
				
				if(((int) rateTransmissionVariable) > quantityInfectedPeople && ((int) rateTransmissionVariable) < quantityTotalPeople) {
					infect();
				} else if(((int) rateTransmissionVariable) >= quantityTotalPeople) {
					
					if(rateTransmissionVariable > quantityInfectedPeople) {
						rateTransmissionVariable = quantityTotalPeople;
						infect();
					}
				}
			} else {
				System.out.println("Temperature in " + myAgent.getName() + " is not ideal to proliferation!");
			}
		}
		
		private void infect() {
			
			while(quantityInfectedPeople < rateTransmissionVariable || quantityInfectedPeople == 0) {
				
				Person personToInfect = people.get( random.nextInt(people.size()) );
				while (personToInfect.isInfected() || personToInfect.isDead()) {
					personToInfect = people.get( random.nextInt(people.size()) );
				}
				
				regionGUI.showInfectedPerson(personToInfect);
				quantityInfectedPeople++;
				personToInfect.setInfected(true);
			}
			
		}

		private void updateTemperature() {
			if (climate == 'H') {
				temperature = random.nextInt((Constants.MAX_HEAT_TEMPERATURE - Constants.MIN_HEAT_TEMPERATURE) + 1) + Constants.MIN_HEAT_TEMPERATURE;
			} else if (climate == 'C') {
				temperature = random.nextInt((Constants.MAX_COLD_TEMPERATURE - Constants.MIN_COLD_TEMPERATURE) + 1) + Constants.MIN_COLD_TEMPERATURE;
			}
		}

		private void receiveMessagePlague() {
			ACLMessage msg = receive();
			if(msg != null) {
				myAgent.doWake();
				String[] results = msg.getContent().split("-", 2);
				plagueTemperatureIdeal = Integer.parseInt(results[0]);
				plagueDeathPotential = Integer.parseInt(results[1]);
			} else {
				myAgent.doWait();
				block();
			}
		}

		private void updateDeath() {
			for (Person person : people) {
				if (person.isInfected()) {
					person.incrementDaysInfected();
					if (person.getDaysInfected() == plagueDeathPotential && !person.isHealed()) {
						regionGUI.showDeadPerson(person);
						person.setDead(true);
						quantityDeadPeople++;
					}
				}
			}
		}

		private void cure() {
			Collections.shuffle(people);
			int flag = 0;
			for (Person p : people) {
				if (p.isInfected() == true && p.isDead() == false && p.isHealed() == false && flag <= healedPersonsADay) {
					p.setHealed(true);
					regionGUI.showHealedPerson(p);
					flag += 1;
					quantityHealedPeople++;
				}
			}
		}
	}

}
