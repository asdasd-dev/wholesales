package custom;

import jade.util.leap.ArrayList;

import java.util.Arrays;
import java.util.List;

public class FloydWarshall{

        final static int INF = 99999;

        public static int[][] floydWarshall(int graph[][], List<int[]> routes)
        {
            int size = graph[0].length;
            int dist[][] = new int[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    dist[i][j] = graph[i][j] > 0 ? graph[i][j] : INF; // если нет пути из точки в точку, то ставим бесконечность
                }
            }

            for (int[] route1: routes){ // задаём нули для каждой пары точек из пересекающихся маршрутов
                for (int[] route2: routes) {
                    if(Arrays.stream(route1)
                            .distinct()
                            .filter(x -> Arrays.stream(route2).anyMatch(y -> y == x))
                            .toArray().length > 0)
                    {
                        // здесь как-то определить, что пути конкретных агентов пересекаются в конкретных точках
                        for (int i = 0; i < route1.length; i++){
                            for (int j = 0; j < route2.length; j++){
                                dist[route1[i]][route2[j]] = 0;
                                dist[route2[j]][route1[i]] = 0;
                            }
                        }
                    };
                }
            }

            for (int k = 0; k < size; k++) // определяем минимальную величину изменения маршрута для доставки товара из одной точки в другую
            {
                for (int i = 0; i < size; i++)
                {
                    for (int j = 0; j < size; j++)
                    {
                        if (dist[i][k] + dist[k][j] < dist[i][j])
                            dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }

            return dist;
        }
}
