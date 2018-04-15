package custom;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.leap.ArrayList;

public class BuyerAgent extends Agent {

    private int[][] adjacencyMatrix; // матрица смежности с весами
    private int[] routes; // маршрут агента

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Yo! Agent " + getAID().getName() + " is in game!");

        Object args[] = getArguments(); //первый аргумент - матрица (a11 a12 a13 a21 a22 a23 ...), второй - маршрут агента (в виде строки с номерами вершин "r1 r2 r3")

        setAdjacencyMatrix(args[0].toString()); // задаём матрицу

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setName("buyer-agent");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();

        }
    }

    private void setAdjacencyMatrix(String adjacencyMatrix){ // задаём матрицу смежности по строке
        String adjacencyMatrixArray[] = adjacencyMatrix.split(" ");
        int countOfVertices = (int) Math.sqrt(adjacencyMatrixArray.length);
        this.adjacencyMatrix = new int[countOfVertices][countOfVertices];
        for (int i = 0; i < countOfVertices * countOfVertices; i++)
        {
            this.adjacencyMatrix[i / countOfVertices][i % countOfVertices] = Integer.parseInt(adjacencyMatrixArray[i]);
        }
    }

    private void setAgentRoute(String agentRoute){ // задаём маршруты передвижений агентов
        String agentRouteArray[] = agentRoute.split(" ");
        int countOfVertices = (int) Math.sqrt(agentRouteArray.length);
        this.routes = new int[countOfVertices];
        for (int i = 0; i < countOfVertices; i++)
        {
            this.routes[i] = Integer.parseInt(agentRouteArray[i]);
        }
    }

    public int[][] getAdjacencyMatrix(){
        return this.adjacencyMatrix;
    }

    public int[] getRoutes(){
        return this.routes;
    }
}
