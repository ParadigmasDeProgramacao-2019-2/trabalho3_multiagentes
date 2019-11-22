package main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
	
	private static AgentContainer containerController;
	private static AgentController agentController;

	public static void main(String[] args) {
		startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "UFABC");
		addAgent(containerController, "Ambiente", Ambiente.class.getName(), null);
		addAgent(containerController, "Bacteria", Bacteria.class.getName(), null);		
	}
	
	public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);

        containerController = runtime.createMainContainer(profile);
    }
	
    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) {
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
    }
}
