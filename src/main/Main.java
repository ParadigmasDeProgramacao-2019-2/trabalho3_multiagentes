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
		startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "FGA");
		
		Object[] cold = {(Object) 'C'};
		Object[] heat = {(Object) 'H'};
		
		addAgent(containerController, "Plague", Plague.class.getName(), null);		
		addAgent(containerController, "RegionCold1", Region.class.getName(), cold);
		addAgent(containerController, "RegionCold2", Region.class.getName(), cold);
		addAgent(containerController, "RegionHeat1", Region.class.getName(), heat);
		addAgent(containerController, "RegionHeat2", Region.class.getName(), heat);
	}
	
	public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
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
