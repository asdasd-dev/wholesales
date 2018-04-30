package custom;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.leap.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class BuyerAgent extends Agent {

    private int baseWithGoods; // вершина, в которую доставлены все товары
    private int[] routes; // маршрут агента
    private double greed; // кф жадности (стоимость 1 ед. отклонения от траектории)
    private int index;
    public Data data;

    public staticBuyerBehaviour myStaticBuyerBehaviour;
    public dynamicBuyerBehaviour myDynamicBuyerBehaviour;

    @Override
    protected void setup() {
        super.setup();

        Gson g = new Gson();
        Data data = new Data();

        try {
            data = g.fromJson(new FileReader("C:\\jade\\src\\custom\\data.json"), Data.class);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }

        Object[] args = getArguments();
        this.baseWithGoods = Integer.parseInt(args[0].toString());
        setAgentRoute(args[1].toString());
        this.index = Integer.parseInt(args[2].toString());

        System.out.println("Yo! Agent " + getAID().getName() + " is in game!");

        if (routes.length > 1)
        {
            this.myDynamicBuyerBehaviour = new dynamicBuyerBehaviour(this);
            addBehaviour(myDynamicBuyerBehaviour);
        }
        else
        {
            this.myStaticBuyerBehaviour = new staticBuyerBehaviour(this);
            addBehaviour(myStaticBuyerBehaviour);
        }
    }

    private void setAgentRoute(String agentRoute){ // задаём маршруты передвижений агентов
        String agentRouteArray[] = agentRoute.split(" ");
        this.routes = new int[agentRouteArray.length];
        for (int i = 0; i < agentRouteArray.length; i++)
        {
            this.routes[i] = Integer.parseInt(agentRouteArray[i]);
        }
    }

    public int[] getRoutes(){
        return this.routes;
    }

    public int getBaseWithGoods(){
        return this.baseWithGoods;
    }

    public int getIndex(){
        return this.index;
    }
}
