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
                System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item by himself at the base vertex");
                myBuyerAgent.isReceivedAnItem = true;
            } else if (myBuyerAgent.bestOffer.price <= myBuyerAgent.money) {
                ACLMessage replyMsg = myBuyerAgent.bestOffer.message.createReply();
                replyMsg.setPerformative(ACLMessage.AGREE);
                replyMsg.setContent(Integer.toString(myBuyerAgent.bestOffer.deleiveryPoint) + " " + Integer.toString(myBuyerAgent.bestOffer.price)); // 1 аргумент - точка, где ждём доставщика, 2 - цена
                myBuyerAgent.send(replyMsg);
                System.out.println("Agent " + myBuyerAgent.getLocalName() + " is waiting the item to be delivered at the vertex " + Integer.toString(myBuyerAgent.bestOffer.deleiveryPoint)
                        + " from the agent " + myBuyerAgent.bestOffer.message.getSender().getLocalName());
                myBuyerAgent.isReceivedAnItem = true;
            }
        } else {
            if (!myBuyerAgent.isStatic && myBuyerAgent.selfPrice <= myBuyerAgent.money) {
                System.out.println("Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item by himself at the base vertex");
                myBuyerAgent.isReceivedAnItem = true;
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
            int newPrice = myBuyerAgent.money + myBuyerAgent.money / 10;
            System.out.println("Agent " + myBuyerAgent.getLocalName() + " is ready to spend on delivery " + newPrice + "$ instead of " + myBuyerAgent.money + "$");
            myBuyerAgent.money = newPrice;
            return myParent.NEGATIVE_CONDITION;
        }

    }
}
