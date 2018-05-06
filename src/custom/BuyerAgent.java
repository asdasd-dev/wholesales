package custom;

import com.google.gson.Gson;
import custom.DynamicBehaviours.dynamicBuyerBehaviour;
import custom.StaticBehaviours.staticBuyerBehaviour;
import jade.core.AID;
import jade.core.Agent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class BuyerAgent extends Agent {

    private int baseWithGoods; // вершина, в которую доставлены все товары
    private int[] routes; // маршрут агента
    private int greed; // кф жадности (стоимость 1 ед. отклонения от траектории)
    public int[][] graph;
    private int myMoney;
    private int index;
    public boolean isStatic;
    public boolean isReceivedAnItem;
    public Data data;
    public int selfPrice;
    public List<AID> listOfDynamicAgents = new LinkedList<>();
    public LinkedList<Inform> informOffers  = new LinkedList<Inform>();
    public boolean connectedWithBase;
    public LinkedList<Offer> offerToAdd = new LinkedList<Offer>();
    public LinkedList<Agree> agreeOffers = new LinkedList<Agree>();

    public staticBuyerBehaviour myStaticBuyerBehaviour;
    public dynamicBuyerBehaviour myDynamicBuyerBehaviour;

    @Override
    protected void setup() {
        super.setup();
        selfPrice = 99999;

        Gson g = new Gson();
        Data data = new Data();
        try {
            data = g.fromJson(new FileReader("C:\\jade\\src\\custom\\data.json"), Data.class);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        setGraph(data.adjacencyMatrix);
        this.greed = data.greed[this.index];

        listOfDynamicAgents = new LinkedList<>();
        informOffers  = new LinkedList<Inform>();
        offerToAdd = new LinkedList<Offer>();
        agreeOffers = new LinkedList<Agree>();
        connectedWithBase = false;
        isReceivedAnItem = false;

        Object[] args = getArguments();
        this.baseWithGoods = Integer.parseInt(args[0].toString());
        setAgentRoute(args[1].toString());
        this.index = Integer.parseInt(args[2].toString());
        this.myMoney = Integer.parseInt(args[3].toString());


        System.out.println("Yo! Agent " + getAID().getName() + " is in game!");


        if (routes.length > 1)
        {
            for (int i = 0; i < routes.length; i++) { // если динамический и уже находится в вершине с товарами
                if (routes[i] == getBaseWithGoods()) {
                    System.out.println(getLocalName() + " has received the item at the vertex " + routes[i]); // если динамический и уже находится в вершине с товарами
                    isReceivedAnItem = true;
                    break;
                }
            }
            isStatic = false;
            if (!isReceivedAnItem) // если динамический не проезжает через вершину с товарами, то добавляем сценарий статического агента
            {
                this.myStaticBuyerBehaviour = new staticBuyerBehaviour(this);
                addBehaviour(myStaticBuyerBehaviour);
            }
            this.myDynamicBuyerBehaviour = new dynamicBuyerBehaviour(this);
            addBehaviour(myDynamicBuyerBehaviour);
        }
        else
        {
            if (routes[0] == getBaseWithGoods()){ // если статический и уже находится в вершине с товарами, то до свидания
                System.out.println(getLocalName() + " has received the item at the vertex " + routes[0]);
                doDelete();
            }
            isStatic = true;
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
