package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.DynamicBehaviours.dynamicBuyerBehaviour;
import custom.FloydWarshall;
import custom.Inform;
import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class staticAnswerToInformBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if (!myBuyerAgent.informOffers.isEmpty()) {
            Inform bestOffer = myBuyerAgent.informOffers.getFirst();
            for (Inform a : myBuyerAgent.informOffers) {
                if (a.price < bestOffer.price) {
                    bestOffer = a;
                }
            }

            if (!myBuyerAgent.isStatic && !myBuyerAgent.isReceivedAnItem)
            {
                int[][] fw = myBuyerAgent.fw;
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int basePoint = this.myBuyerAgent.getBaseWithGoods();
                myBuyerAgent.selfPrice = fw[myRoute[0]][basePoint] * myBuyerAgent.getGreed() * 2;
                for (int i = 0; i < myRoute.length; i++)
                {
                    if (fw[myRoute[i]][basePoint] < myBuyerAgent.selfPrice)
                    {
                        myBuyerAgent.selfPrice = fw[myRoute[i]][basePoint]  * myBuyerAgent.getGreed() * 2;
                    }
                }
            }

            if (!myBuyerAgent.isStatic && bestOffer.price > myBuyerAgent.selfPrice)
            {
                System.out.println("Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item by himself at the base vertex");
            }
            else if (bestOffer.price <= myBuyerAgent.getMoney()) {
                ACLMessage replyMsg = bestOffer.message.createReply();
                replyMsg.setPerformative(ACLMessage.AGREE);
                replyMsg.setContent(Integer.toString(bestOffer.deleiveryPoint) + " " + Integer.toString(bestOffer.price)); // 1 аргумент - точка, где ждём доставщика, 2 - цена
                myBuyerAgent.send(replyMsg);
                System.out.println("Agent " + myBuyerAgent.getLocalName() + " is waiting the item to be delivered at the vertex " + Integer.toString(bestOffer.deleiveryPoint)
                        + " from the agent " + bestOffer.message.getSender().getLocalName());
            }
        }
        else
        {
            block(1000);
        }
    }
}
