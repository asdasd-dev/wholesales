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
            int minPrice = 999999;
            int minVertex = myRoute[0];
            for (int i = 0; i < myRoute.length; i++)
            {
                int price = myBuyerAgent.fw[myRoute[i]][a.deleiveryPoint] * myBuyerAgent.getGreed() * 2;
                if (price < minPrice)
                {
                    minPrice = price;
                    minVertex = i;
                };
            }
            if(a.price >= minPrice)
            {
                replyMsg.setPerformative(ACLMessage.CONFIRM);
                System.out.println(myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                if (!myBuyerAgent.routes.contains(a.deleiveryPoint))
                {
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertex, a.deleiveryPoint);
                    for(int i = 0; i < shortestWay.length; i++)
                    {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    if (myBuyerAgent.routes.contains(myBuyerAgent.baseWithGoods)){
                        myBuyerAgent.connectedWithBase = true;
                    }
                }
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
