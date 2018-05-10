package custom.DynamicBehaviours;

import custom.Agree;
import custom.BuyerAgent;
import custom.FloydWarshall;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import static java.util.Collections.addAll;

public class dynamicAnswerToAgreeBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        for (Agree a: myBuyerAgent.agreeOffers) {
            ACLMessage replyMsg = a.message.createReply();
            int[] myRoute = this.myBuyerAgent.getRoutes();
            int minPriceToAgent = 999999;
            int minPriceToBase = 999999;
            int minVertexToAgent = myRoute[0];
            int minVertexToBase = myRoute[0];
            for (int i = 0; i < myRoute.length; i++)
            {
                int priceToAgent = myBuyerAgent.fw[myRoute[i]][a.deleiveryPoint] * myBuyerAgent.getGreed() * 2;
                if (priceToAgent < minPriceToAgent)
                {
                    minPriceToAgent = priceToAgent;
                    minVertexToAgent = myRoute[i];
                };
                int priceToBase = myBuyerAgent.fw[myRoute[i]][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed() * 2;
                if (priceToBase < minPriceToBase)
                {
                    minPriceToBase = priceToBase;
                    minVertexToBase = myRoute[i];
                };
            }
            if(myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToAgent)
            {
                replyMsg.setPerformative(ACLMessage.CONFIRM);
                System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                myBuyerAgent.routes.add(a.deleiveryPoint);
                int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToAgent, a.deleiveryPoint);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
            }
            else if (!myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToAgent / 2 + minPriceToBase / 2 +
                    myBuyerAgent.fw[a.deleiveryPoint][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed()) // то есть минимальный путь: точка моего маршрута - база - точка маршрута агента, которому доставляем
            {
                replyMsg.setPerformative(ACLMessage.CONFIRM);
                System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                myBuyerAgent.connectedWithBase = true;
                System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                myBuyerAgent.isReceivedAnItem = true;
                int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                myBuyerAgent.routes.add(a.deleiveryPoint);
                shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.baseWithGoods, a.deleiveryPoint);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToAgent, a.deleiveryPoint);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
            }
            else if(myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint))
            {
                replyMsg.setPerformative(ACLMessage.CONFIRM);
                System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
            }
            else if(!myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToBase)
            {
                replyMsg.setPerformative(ACLMessage.CONFIRM);
                System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                myBuyerAgent.connectedWithBase = true;
                System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                myBuyerAgent.isReceivedAnItem = true;
                int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
            }
            else
            {
                replyMsg.setPerformative(ACLMessage.CANCEL);
                System.out.println(myBuyerAgent.getLocalName() + " has declined the agree message from " + a.message.getSender().getLocalName());
            }
            myBuyerAgent.send(replyMsg);
            myBuyerAgent.agreeOffers.remove(a);
        }
    }
}
