package custom.DynamicBehaviours;

import custom.FloydWarshall;
import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class dynamicAnswerToProposalsBehaviour extends OneShotBehaviour {

    private dynamicBuyerBehaviour myParent;

    @Override
    public void action() {

        myParent = (dynamicBuyerBehaviour) getParent();

        for (Offer a: myParent.offerToAdd) {
            int[] intersection = Arrays.stream(myParent.myBuyerAgent.getRoutes())
                    .distinct()
                    .filter(x -> Arrays.stream(a.routes).anyMatch(y -> y == x))
                    .toArray();
            ACLMessage replyMsg = a.message.createReply();
            replyMsg.setPerformative(ACLMessage.INFORM);
            if (myParent.connectedWithBase && intersection.length > 0) // если наш агент проходит через базу с товарами и через точку,
                                                                    // в которой может находиться агент, которому надо доставить товар, то предлагаем доставить за 0 рублей
            {
                replyMsg.setContent(Integer.toString(intersection[0]) + " 0 " + Integer.toString(myParent.myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myParent.myBuyerAgent.send(replyMsg);
                System.out.println(myParent.myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (myParent.connectedWithBase && intersection.length == 0) // проезжаем через базу, но не пересекаемся с агентом
            {
                int[][] fw = FloydWarshall.fw(myParent.myBuyerAgent.graph);
                int[] myRoute = this.myParent.myBuyerAgent.getRoutes();
                int[] agentRoute = a.routes;
                int minPoint = agentRoute[0];
                int minDist = fw[myRoute[0]][agentRoute[0]];
                for (int i = 0; i < myRoute.length; i++)
                {
                    for(int j = 0; j < agentRoute.length; j++)
                    {
                        if (fw[myRoute[i]][agentRoute[j]] < minDist)
                        {
                            minPoint = agentRoute[j];
                            minDist = fw[myRoute[i]][agentRoute[j]];
                        }
                    }
                }
                replyMsg.setContent(Integer.toString(minPoint) + " " + Integer.toString(minDist * 2 * myParent.myBuyerAgent.getGreed()) + " " + Integer.toString(myParent.myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myParent.myBuyerAgent.send(replyMsg);
                System.out.println(myParent.myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (!myParent.connectedWithBase && intersection.length > 0) // не проезжаем базу и пересекаемся с агентом
            {
                int[][] fw = FloydWarshall.fw(myParent.myBuyerAgent.graph);
                int[] myRoute = this.myParent.myBuyerAgent.getRoutes();
                int basePoint = this.myParent.myBuyerAgent.getBaseWithGoods();
                int minDist = fw[myRoute[0]][basePoint];
                for (int i = 0; i < myRoute.length; i++)
                {
                    if (fw[myRoute[i]][basePoint] < minDist)
                    {
                        minDist = fw[myRoute[i]][basePoint];
                    }
                }
                replyMsg.setContent(Integer.toString(intersection[0]) + " " + Integer.toString(minDist * myParent.myBuyerAgent.getGreed()) + " " + Integer.toString(myParent.myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myParent.myBuyerAgent.send(replyMsg);
                System.out.println(myParent.myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (!myParent.connectedWithBase && intersection.length == 0) // не проезжаем базу и не пересекаемся с агентом
            {
                int[][] fw = FloydWarshall.fw(myParent.myBuyerAgent.graph);
                int[] myRoute = this.myParent.myBuyerAgent.getRoutes();
                int[] agentRoute = a.routes;
                int basePoint = this.myParent.myBuyerAgent.getBaseWithGoods();
                int minDistToBase = fw[myRoute[0]][basePoint];
                int minDistToAgent = fw[myRoute[0]][agentRoute[0]];
                int minPoint = agentRoute[0];
                for (int i = 0; i < myRoute.length; i++)
                {
                    if (fw[myRoute[i]][basePoint] < minDistToBase)
                    {
                        minDistToBase = fw[myRoute[i]][basePoint];
                    }
                    for(int j = 0; j < agentRoute.length; j++)
                    {
                        if (fw[myRoute[i]][agentRoute[j]] < minDistToAgent)
                        {
                            minPoint = agentRoute[j];
                            minDistToAgent = fw[myRoute[i]][agentRoute[j]];
                        }
                    }
                }
                replyMsg.setContent(Integer.toString(minPoint) + " " + Integer.toString((minDistToAgent + minDistToBase) * 2 * myParent.myBuyerAgent.getGreed()) + " " + Integer.toString(myParent.myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myParent.myBuyerAgent.send(replyMsg);
                System.out.println(myParent.myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
        }
    }
}
