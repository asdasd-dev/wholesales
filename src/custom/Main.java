package custom;

import java.lang.Object;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initialization (" + Main.class.getName() + ")");

        MainParameters configParameters = new MainParameters(args[0], args[1], args[2], args[3]);

        StringBuilder creatorParams = new StringBuilder();
        creatorParams
                .append(configParameters.getCountOfVertices()).append(",")
                .append(configParameters.getAdjacencyMatrix().replace(',', ' ')).append(",")
                .append(configParameters.getAgentsCount()).append(",")
                .append(configParameters.getAgentsRoutes().replace(',', ' '));

        jade.Boot.main(new String[] {
                "-gui", "creator:custom.CreatorAgent(" + creatorParams.toString() + ")"
        });

    }
}
