import java.io.IOException;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.behaviours.*;
//import jade.wrapper.StaleProxyException;

import jade.core.Runtime; 
import jade.core.Profile; 

import jade.wrapper.*;
//import jade.wrapper.AgentController;
//import jade.wrapper.ContainerController;

/*
 * Pessoal, pe�o desculpas pelos nomes das vari�veis, falta de t�cnicas de programa��o, e falta de outras boas pr�ticas nesse testAgent
 * A inten��o foi manter apenas um compilado de solu��es para as d�vidas que surgiram em tempo de aula : )
 */
public class TestAgent extends Agent{

	private static final long serialVersionUID = 1L;

	protected void setup() {
		
		//Permite executar comportamentos em threads dedicadas
		ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory(); 
		
		System.out.println("Ol� mundo! Eu sou um agente preocupado com comportamento gen�rico!");
		System.out.println("Estou disparando um comportamento gen�rico");
		//addBehaviour(new MyGenericBehaviour(this));
		
		Behaviour b1 = new MyGenericBehaviour(this);
		
		addBehaviour(tbf.wrap(b1));

		
		
		System.out.println("Ol� mundo! Eu sou um agente preocupado com bloqueio!");
		System.out.println("Irei executar o meu comportamento com bloqueio");
		//addBehaviour(new MyBlockBehaviour(this,1000)); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b2 = new MyBlockBehaviour(this,1000);
		
		addBehaviour(tbf.wrap(b2));
		
		
		
		System.out.println("Ol� mundo! Eu sou um agente criador de DUMMYS!");
		System.out.println("Irei executar o meu comportamento de cria��o de agentes");
		//addBehaviour(new DummyAgentCreator()); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b3 = new DummyAgentCreator();
		
		addBehaviour(tbf.wrap(b3));
		
		
		System.out.println("Ol� mundo! Eu sou um agente de a��o �nica!");
		System.out.println("Irei executar o meu comportamento OneShotBehaviour");
		//addBehaviour(new MyOneShotBehaviour()); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b4 = new MyOneShotBehaviour();
		
		addBehaviour(tbf.wrap(b4));
		
		
		
		System.out.println("Ol� mundo! Eu sou um agente de a��o c�clica!");
		System.out.println("Irei executar o meu comportamento CyclicBehaviour");
		//addBehaviour(new MyCyclicBehaviour()); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b5 = new MyCyclicBehaviour(this);
		
		addBehaviour(tbf.wrap(b5));
		
		
		
		System.out.println("Ol� mundo! Eu sou um agente de a��o temporal �nica!");
		System.out.println("Irei executar o meu comportamento WakerBehaviour");
		//addBehaviour(new MyWakerBehaviour()); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b6 = new MyWakerBehaviour(this, 1000);
		
		addBehaviour(tbf.wrap(b6));
		
		
				
		System.out.println("Ol� mundo! Eu sou um agente de a��o temporal c�clica!");
		System.out.println("Irei executar o meu comportamento TickerBehaviour");
		//addBehaviour(new MyTickerBehaviour()); //Comentei, pois estou optando por trabalhar com o conceito de threads dedicadas
		
		Behaviour b7 = new MyTickerBehaviour(this, 1000);
		
		addBehaviour(tbf.wrap(b7));


	}
}

//Comportamento Gen�rico
class MyGenericBehaviour extends Behaviour{

	private static final long serialVersionUID = 1L;
	
	int i =0;

	public MyGenericBehaviour(Agent a) {
		super(a);
	}

	public void action() {
		System.out.println("Meu nome � " + myAgent.getLocalName( ) + " " + i);
		i = i + 1;
		
		// MontaMapa via matriz : )
		// Aqui, a matriz n�o � um agente. Vide classe Mapa. Ok?
		Mapa mapa = new Mapa();
		mapa.montaMapaSimples();
		
		//Se quiserem ver o mapa abaixo funcionando, 
		//aconselho - primeiramente - limpar um pouco o c�digo 
		//para visualizar as solicita��es de valores de entrada - via terminal 
		//bem como os resultados gerados.
		//Basta descomentar... : )
		//mapa.montaMapaPassandoLinhasEColunas(); 
		
	}

	public boolean done () {
		//Caso este m�todo retorne TRUE, o comportamento ser� finalizado
		return i > 3;
	}
}


//Comportamento de Bloqueio
class MyBlockBehaviour extends Behaviour{
	
	private static final long serialVersionUID = 1L;
	
	int executionNumber = 1;
	long delay;
	long initialTime = System.currentTimeMillis();

	public MyBlockBehaviour(Agent a, long delay) {
		super(a);
		this.delay = delay;
	}

	public void action() {
		block(delay); //Aqui, bloqueio...
		System.out.println("# Tempo " + (System.currentTimeMillis() - initialTime) + ": Meu nome � " + myAgent.getLocalName());
		executionNumber = executionNumber + 1;
	}

	public boolean done () {
		System.out.println (executionNumber);
		return executionNumber > 10;
	}

}

//Comportamento Criador de Agentes
class DummyAgentCreator extends Behaviour {

	private static final long serialVersionUID = 1L;

	@Override
	public void action() {

		// Recuperando a inst�ncia jade.core.Runtime
		Runtime rt = Runtime.instance(); 
		
		// Criando um profile default   
		Profile p = new ProfileImpl();     
		
		// Criando um novo container
		// main container (i.e. nesse host, porta 1099) 
		ContainerController cc = rt.createAgentContainer(p); 
		
		// Criando um novo agente, no caso, um DummyAgent 
		// Passando-o como refer�ncia...
		Object reference = new Object(); 
		Object args[] = new Object[1]; 
		args[0] = reference; 
		AgentController dummy;
		try {
			dummy = cc.createNewAgent("inProcess", "jade.tools.DummyAgent.DummyAgent", args);
			
			// Disparando o agente
			dummy.start(); 
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}
	
}


//Comportamento OneShotBehaviour
class MyOneShotBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public void action(){
		System.out.println("Executando OneShotBehaviour");
	}
}


//Comportamento CyclicBehaviour
class MyCyclicBehaviour extends CyclicBehaviour{
	
    int counter = 0;

	private static final long serialVersionUID = 1L;
	
	public MyCyclicBehaviour(Agent a) {
        super(a);
    }
 
    public void action() {
        System.out.println("Executando CyclicBehaviour" + " " + counter);
        counter++;
        
        if(counter > 2){
        	System.out.println("Fim do CyclicBehaviour");
        	this.block();
        }
    }
}



//Comportamento WakerBehaviour
class MyWakerBehaviour extends WakerBehaviour {
 
	public MyWakerBehaviour(Agent a, long timeout) {
		super(a, timeout);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	protected void onWake ( ) {
        System.out.println("Executanto WakerBehaviour");
    }
}


//Comportamento TickerBehaviour
class MyTickerBehaviour extends TickerBehaviour {

	public MyTickerBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	protected void onTick() {
        if (getTickCount() > 5) {
            stop();
        }else {
            System.out.println( "Estou realizando meu " + getTickCount() + " tick");
        }
    }
}

