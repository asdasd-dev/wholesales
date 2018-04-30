package custom;

import java.lang.Object;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initialization (" + Main.class.getName() + ")");

        jade.Boot.main(new String[] {
                "-gui", "creator:custom.CreatorAgent(" + args[0].toString() + ")"
        });

    }
}
