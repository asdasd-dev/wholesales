package custom;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;

public class CreatorAgent extends Agent {
    @Override
    protected void setup() {
        Object[] args = getArguments(); //[0] - вершина с товарами


        Gson g = new Gson();
        Data data = new Data();

        try {
            data = g.fromJson(new FileReader("C:\\jade\\src\\custom\\data3.json"), Data.class);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }

        ContainerController cc = getContainerController();
        AgentController agent;
        try {
            agent = cc.createNewAgent("StatisticsAgent", "custom.StatisticAgent", new Object[]{});
            agent.start();
            for (int i = 0; i < data.numberOfAgents; i++) {
                String nameAgent = "agent_" + i;
                agent = cc.createNewAgent(nameAgent, "custom.BuyerAgent",
                        new Object[]{args[0], data.routes[i].replace(',', ' '), Integer.toString(i), Integer.toString(data.money[i])}); // изначально агент получает номер вершины с базой с товарами, маршрут своего передвижения по ней и свой индекс и количество денег
                agent.start();
            }
        }
        catch (StaleProxyException spe) {
            spe.printStackTrace();
        }
    }
}
