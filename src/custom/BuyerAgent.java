package custom;

import com.google.gson.Gson;
import custom.DynamicBehaviours.*;
import custom.StaticBehaviours.staticBuyerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BuyerAgent extends Agent {

    public int baseWithGoods; // вершина, в которую доставлены все товары
    public List<Integer> routes; // маршрут агента
    private int greed; // кф жадности (стоимость 1 ед. отклонения от траектории)
    public int[][] graph;
    public int[][] fw;
    public int[][] c;
    public int money;
    private int index;
    public boolean isStatic;
    public boolean isReceivedAnItem;
    public Data data;
    public int selfPrice;
    public List<AID> listOfDynamicAgents = new LinkedList<>();
    public LinkedList<Inform> informOffers  = new LinkedList<Inform>();
    public Inform bestOffer;
    public boolean connectedWithBase;
    public LinkedList<Offer> offerToAdd = new LinkedList<Offer>();
    public LinkedList<Agree> agreeOffers = new LinkedList<Agree>();

    public staticBuyerBehaviour myStaticBuyerBehaviour;

    @Override
    protected void setup() {
        super.setup();
        selfPrice = 99999;

        Gson g = new Gson();
        Data data = new Data();
        try {
            data = g.fromJson(new FileReader("C:\\jade\\src\\custom\\data2.json"), Data.class);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        setGraph(data.adjacencyMatrix);

        listOfDynamicAgents = new LinkedList<>();
        informOffers  = new LinkedList<Inform>();
        offerToAdd = new LinkedList<Offer>();
        agreeOffers = new LinkedList<Agree>();
        routes = new ArrayList<Integer>();
        isReceivedAnItem = false;

        Object[] args = getArguments();
        this.baseWithGoods = Integer.parseInt(args[0].toString());
        setAgentRoute(args[1].toString());
        this.index = Integer.parseInt(args[2].toString());
        this.money = Integer.parseInt(args[3].toString());
        Pair<int[][], int[][]> fwres = FloydWarshall.fw(graph);
        this.greed = data.greed[this.index];
        fw = fwres.getKey();
        c = fwres.getValue();

        if (routes.contains(baseWithGoods))
        {
            connectedWithBase = true;
        }
        else
        {
            connectedWithBase = false;
        }
        System.out.println("Yo! Agent " + getAID().getName() + " is in game!");


        if (routes.size() > 1)
        {
            for (int i = 0; i < routes.size(); i++) { // если динамический и уже находится в вершине с товарами
                if (routes.get(i) == getBaseWithGoods()) {
                    System.out.println("●" + getLocalName() + " has received the item at the vertex " + routes.get(i)); // если динамический и уже находится в вершине с товарами
                    isReceivedAnItem = true;
                    break;
                }
            }
            isStatic = false;
            if (!isReceivedAnItem) // если динамический не проезжает через вершину с товарами, то добавляем сценарий статического агента
            {
                int[] myRoute = getRoutes();
                for (int i = 0; i < myRoute.length; i++) {
                    if (fw[myRoute[i]][baseWithGoods] < selfPrice) {
                        selfPrice = fw[myRoute[i]][baseWithGoods] * getGreed() * 2;
                    }
                }
                this.myStaticBuyerBehaviour = new staticBuyerBehaviour(this);
                addBehaviour(myStaticBuyerBehaviour);
            }

            //Добавляем поведения для динамического агента

            addBehaviour(new dynamicRegisterInYPBehaviour());

            SequentialBehaviour be = new SequentialBehaviour(this);
            ReceiverBehaviour be1 = new dynamicReceiveProposalsBehaviour(this, 1000);
            OneShotBehaviour be2 = new dynamicAnswerToProposalsBehaviour(be1);
            be.addSubBehaviour(be1);
            be.addSubBehaviour(be2);

            SequentialBehaviour beA = new SequentialBehaviour(this);
            ReceiverBehaviour be1A = new dynamicReceiveAgreeBehaviour(this, 1000);
            OneShotBehaviour be2A = new dynamicAnswerToAgreeBehaviour(be1A);
            beA.addSubBehaviour(be1A);
            beA.addSubBehaviour(be2A);

            ParallelBehaviour pe = new ParallelBehaviour();
            pe.addSubBehaviour(be);
            pe.addSubBehaviour(beA);

            addBehaviour(pe);
        }
        else
        {
            if (routes.get(0) == getBaseWithGoods()){ // если статический и уже находится в вершине с товарами, то до свидания
                System.out.println("●" + getLocalName() + " has received the item at the vertex " + routes.get(0));
                doDelete();
            }
            isStatic = true;
            this.myStaticBuyerBehaviour = new staticBuyerBehaviour(this);
            addBehaviour(myStaticBuyerBehaviour);
        }
    }

    private void setAgentRoute(String agentRoute){ // задаём маршруты передвижений агентов
        String agentRouteSArray[] = agentRoute.split(" ");
        for (int i = 0; i < agentRouteSArray.length; i++)
        {
            this.routes.add(Integer.parseInt(agentRouteSArray[i]));
        }
    }

    public int[] getRoutes(){
        int[] routesIArray = new int[this.routes.size()];
        for (int i = 0; i < routesIArray.length; i++)
        {
            routesIArray[i] = routes.get(i);
        }
        return routesIArray;
    }

    public int getBaseWithGoods(){
        return this.baseWithGoods;
    }

    public int getIndex(){
        return this.index;
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
