import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Criando novo agente! ");
		TestAgent agent = new TestAgent();
		System.out.println("Chamando setup do agente! ");
		agent.setup();
	}
}
	

