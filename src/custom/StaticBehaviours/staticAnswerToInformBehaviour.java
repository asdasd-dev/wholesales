package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.FloydWarshall;
import custom.Inform;
import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;
import java.util.Iterator;

public class staticAnswerToInformBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if (!myBuyerAgent.informOffers.isEmpty() && !myBuyerAgent.isReceivedAnItem) {
            if (myBuyerAgent.bestOffer == null)
                myBuyerAgent.bestOffer = myBuyerAgent.informOffers.getFirst();
            for (Inform a : myBuyerAgent.informOffers) {
                if (a.price < myBuyerAgent.bestOffer.price) {
                    myBuyerAgent.bestOffer = a;
                }
            }
        }

        if (myBuyerAgent.bestOffer != null) {
            if (!myBuyerAgent.isStatic && myBuyerAgent.bestOffer.price > myBuyerAgent.selfPrice && myBuyerAgent.selfPrice <= myBuyerAgent.money) {
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int minDistToBase = myBuyerAgent.fw[myRoute[0]][myBuyerAgent.baseWithGoods];
                int minVertexToBase = myRoute[0];
                for (int i = 0; i < myRoute.length; i++)
                {
                    int distToBase = myBuyerAgent.fw[myRoute[i]][myBuyerAgent.baseWithGoods];
                    if (distToBase < minDistToBase)
                    {
                        minDistToBase = distToBase;
                        minVertexToBase = myRoute[i];
                    };
                }
                System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item by himself at the base vertex");
                myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                myBuyerAgent.isReceivedAnItem = true;
            } else if (myBuyerAgent.bestOffer.price <= myBuyerAgent.money) {
                ACLMessage replyMsg = myBuyerAgent.bestOffer.message.createReply();
                replyMsg.setPerformative(ACLMessage.AGREE);
                replyMsg.setContent(Integer.toString(myBuyerAgent.bestOffer.deleiveryPoint) + " " + Integer.toString(myBuyerAgent.bestOffer.price)); // 1 аргумент - точка, где ждём доставщика, 2 - цена
                myBuyerAgent.send(replyMsg);
                System.out.println("Agent " + myBuyerAgent.getLocalName() + " is waiting the item to be delivered at the vertex " + Integer.toString(myBuyerAgent.bestOffer.deleiveryPoint)
                        + " from the agent " + myBuyerAgent.bestOffer.message.getSender().getLocalName());
                myBuyerAgent.isReceivedAnItem = true;
                myBuyerAgent.connectedWithBase = true;
            }
        } else {
            if (!myBuyerAgent.isStatic && myBuyerAgent.selfPrice <= myBuyerAgent.money) {
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int minDistToBase = myBuyerAgent.fw[myRoute[0]][myBuyerAgent.baseWithGoods];
                int minVertexToBase = myRoute[0];
                for (int i = 0; i < myRoute.length; i++)
                {
                    int distToBase = myBuyerAgent.fw[myRoute[i]][myBuyerAgent.baseWithGoods];
                    if (distToBase < minDistToBase)
                    {
                        minDistToBase = distToBase;
                        minVertexToBase = myRoute[i];
                    };
                }
                System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item by himself at the base vertex");
                myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                for(int i = 0; i < shortestWay.length; i++)
                {
                    myBuyerAgent.routes.add(shortestWay[i]);
                }
                System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                myBuyerAgent.isReceivedAnItem = true;
                myBuyerAgent.connectedWithBase = true;
            }
        }
    }

    @Override
    public int onEnd() {
        staticBuyerBehaviour myParent = (staticBuyerBehaviour) getParent();
        if (myBuyerAgent.isReceivedAnItem)
        {
            return myParent.POSITIVE_CONDITION;
        }
        else
        {
            int newPrice = myBuyerAgent.money + myBuyerAgent.money / 5;
            myBuyerAgent.money = newPrice;
            return myParent.NEGATIVE_CONDITION;
        }

    }
}
