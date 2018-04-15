package custom;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.regex.Pattern;

public class CreatorAgent extends Agent {
    @Override
    protected void setup() {
        Object[] args = getArguments(); //[0] - кол-во вершин, [1] - матрица, [2] - кол-во агентов, [3] - маршруты агентов

        ContainerController cc = getContainerController();
        AgentController agent;
        try {
            for (int i = 0; i < Integer.parseInt(args[2].toString()); i++) {
                String nameAgent = "agent_" + i;
                agent = cc.createNewAgent(nameAgent, "custom.BuyerAgent",
                        new Object[]{args[1], (args[3].toString()).split(Pattern.quote("."))[i]}); // изначально агент получает "карту" и маршрут своего передвижения по ней
                agent.start();
            }
        }
        catch (StaleProxyException spe) {
            spe.printStackTrace();
        }
    }
}
