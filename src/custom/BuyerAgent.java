package custom;

import com.google.gson.Gson;
import custom.DynamicBehaviours.dynamicBuyerBehaviour;
import custom.StaticBehaviours.staticBuyerBehaviour;
import jade.core.Agent;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class BuyerAgent extends Agent {

    private int baseWithGoods; // вершина, в которую доставлены все товары
    private int[] routes; // маршрут агента
    private int greed; // кф жадности (стоимость 1 ед. отклонения от траектории)
    public int[][] graph;
    private int myMoney;
    private int index;
    public boolean isStatic;
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
        setGraph(data.adjacencyMatrix);
        this.index = Integer.parseInt(args[2].toString());
        this.myMoney = Integer.parseInt(args[3].toString());
        this.greed = data.greed[this.index];

        System.out.println("Yo! Agent " + getAID().getName() + " is in game!");

        if (routes.length > 1)
        {
            this.myDynamicBuyerBehaviour = new dynamicBuyerBehaviour(this);
            addBehaviour(myDynamicBuyerBehaviour);
            isStatic = false;
        }
        else
        {
            this.myStaticBuyerBehaviour = new staticBuyerBehaviour(this);
            addBehaviour(myStaticBuyerBehaviour);
            isStatic = true;
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

    public int getMoney(){
        return this.myMoney;
    }

    public int getGreed(){
        return this.greed;
    }


    private void setGraph(String[] adjMatrix){
        int length = adjMatrix.length;
        this.graph = new int [length][length];
        for (int i = 0; i < length; i++)
        {
            String[] line = adjMatrix[i].split(",");
            for (int j = 0; j < length; j++)
            {
                this.graph[i][j] = Integer.parseInt(line[j]);
            }
        }
    }
}
