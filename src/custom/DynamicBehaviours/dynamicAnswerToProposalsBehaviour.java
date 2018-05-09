package custom.DynamicBehaviours;

import custom.BuyerAgent;
import custom.FloydWarshall;
import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.ReceiverBehaviour;

import java.util.Arrays;

public class dynamicAnswerToProposalsBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        for (Offer a: myBuyerAgent.offerToAdd) {
            int[] intersection = Arrays.stream(myBuyerAgent.getRoutes())
                    .distinct()
                    .filter(x -> Arrays.stream(a.routes).anyMatch(y -> y == x))
                    .toArray();
            ACLMessage replyMsg = a.message.createReply();
            replyMsg.setPerformative(ACLMessage.INFORM);
            if (myBuyerAgent.connectedWithBase && intersection.length > 0) // если наш агент проходит через базу с товарами и через точку,
                                                                    // в которой может находиться агент, которому надо доставить товар, то предлагаем доставить за 0 рублей
            {
                replyMsg.setContent(Integer.toString(intersection[0]) + " 0 " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myBuyerAgent.send(replyMsg);
                System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (myBuyerAgent.connectedWithBase && intersection.length == 0) // проезжаем через базу, но не пересекаемся с агентом
            {
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int[] agentRoute = a.routes;
                int minPoint = agentRoute[0];
                int minDist = myBuyerAgent.fw[myRoute[0]][agentRoute[0]];
                for (int i = 0; i < myRoute.length; i++)
                {
                    for(int j = 0; j < agentRoute.length; j++)
                    {
                        if (myBuyerAgent.fw[myRoute[i]][agentRoute[j]] < minDist)
                        {
                            minPoint = agentRoute[j];
                            minDist = myBuyerAgent.fw[myRoute[i]][agentRoute[j]];
                        }
                    }
                }
                replyMsg.setContent(Integer.toString(minPoint) + " " + Integer.toString(minDist * 2 * myBuyerAgent.getGreed()) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myBuyerAgent.send(replyMsg);
                System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (!myBuyerAgent.connectedWithBase && intersection.length > 0) // не проезжаем базу и пересекаемся с агентом
            {
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int basePoint = this.myBuyerAgent.getBaseWithGoods();
                int minDist = myBuyerAgent.fw[myRoute[0]][basePoint];
                for (int i = 0; i < myRoute.length; i++)
                {
                    if (myBuyerAgent.fw[myRoute[i]][basePoint] < minDist)
                    {
                        minDist = myBuyerAgent.fw[myRoute[i]][basePoint];
                    }
                }
                replyMsg.setContent(Integer.toString(intersection[0]) + " " + Integer.toString(minDist * myBuyerAgent.getGreed() * 2) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myBuyerAgent.send(replyMsg);
                System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            else if (!myBuyerAgent.connectedWithBase && intersection.length == 0) // не проезжаем базу и не пересекаемся с агентом
            {
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int[] agentRoute = a.routes;
                int basePoint = this.myBuyerAgent.getBaseWithGoods();
                int minDistToBase = myBuyerAgent.fw[myRoute[0]][basePoint];
                int minDistToAgent = myBuyerAgent.fw[myRoute[0]][agentRoute[0]];
                int minPoint = agentRoute[0];
                for (int i = 0; i < myRoute.length; i++)
                {
                    if (myBuyerAgent.fw[myRoute[i]][basePoint] < minDistToBase)
                    {
                        minDistToBase = myBuyerAgent.fw[myRoute[i]][basePoint];
                    }
                    for(int j = 0; j < agentRoute.length; j++)
                    {
                        if (myBuyerAgent.fw[myRoute[i]][agentRoute[j]] < minDistToAgent)
                        {
                            minPoint = agentRoute[j];
                            minDistToAgent = myBuyerAgent.fw[myRoute[i]][agentRoute[j]];
                        }
                    }
                }
                replyMsg.setContent(Integer.toString(minPoint) + " " + Integer.toString((minDistToAgent + minDistToBase) * 2 * myBuyerAgent.getGreed()) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myBuyerAgent.send(replyMsg);
                System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
            }
            myBuyerAgent.offerToAdd.remove(a);
        }
    }
}
