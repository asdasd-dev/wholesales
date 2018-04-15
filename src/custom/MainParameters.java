package custom;

import java.util.*;

public class MainParameters {

    private String countOfVertices; // количество вершин
    private String adjacencyMatrix; // матрица смежности с весами (0 - пути нет)
    private String agentsCount; // количество агентов
    private String agentsRoutes; // маршруты передвижений агентов (первый индекс - его начальное положение и далее - цикл)

    public MainParameters() {
    }

    public MainParameters(String countOfVertices, String adjacencyMatrix, String agentsCount, String agentsRoutes) {
        this.countOfVertices = countOfVertices;
        this.adjacencyMatrix = adjacencyMatrix;
        this.agentsCount = agentsCount;
        this.agentsRoutes = agentsRoutes;
    }

    public String getCountOfVertices (){
        return this.countOfVertices;
    }

    public String getAdjacencyMatrix (){
        return this.adjacencyMatrix;
    }

    public String getAgentsCount (){ // задаём количество агентов
        return this.agentsCount;
    }

    public String getAgentsRoutes (){ // задаём маршруты передвижений агентов
        return this.agentsRoutes;
    }

    /*private void setCountOfVertices (String countOfVertices){ // задаём количество вершин
        this.countOfVertices = Integer.parseInt(countOfVertices);
    }

    private void setAdjacencyMatrix (String adjacencyMatrix){ // задаём матрицу смежности
        this.adjacencyMatrix = new int[this.countOfVertices][this.countOfVertices];
        String adjacencyMatrixArray[] = adjacencyMatrix.split(",");
        for (int i = 0; i < this.countOfVertices * this.countOfVertices; i++)
        {
            this.adjacencyMatrix[i / this.countOfVertices][i % this.countOfVertices] = Integer.parseInt(adjacencyMatrixArray[i]);
        }
    }

    private void setAgentsCount (String agentCount){ // задаём количество агентов
        this.agentsCount = Integer.parseInt(agentCount);
    }

    private void setAgentsRoutes (String agentRoutes){ // задаём маршруты передвижений агентов
        this.agentsRoutes = new ArrayList<int[]>();
        String agentRoutesArray[] = agentRoutes.split(".");
        for (int i = 0; i < this.agentsCount; i++) // цикл по количеству агентов
        {
            String currentRouteStringArray[] = agentRoutesArray[i].split(",");
            int routeLength = currentRouteStringArray.length;
            int currentRouteIntArray[] = new int[routeLength];
            for (int j = 0; j < routeLength; j++)
            {
                currentRouteIntArray[j] = Integer.parseInt(currentRouteStringArray[j]);
            }
            this.agentsRoutes.add(currentRouteIntArray); // задаём маршрут каждому агенту
        }
    }*/
}
