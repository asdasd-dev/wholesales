package custom.StaticBehaviours;

import custom.DynamicBehaviours.dynamicBuyerBehaviour;
import custom.Inform;
import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class staticAnswerToInformBehaviour extends OneShotBehaviour {

    private staticBuyerBehaviour myParent;

    @Override
    public void action() {
        myParent = (staticBuyerBehaviour) getParent();
        Inform bestOffer = myParent.informOffers.getFirst();

        for (Inform a : myParent.informOffers) {
            if (a.price < bestOffer.price) {
                bestOffer = a;
            }
        }

        if (bestOffer.price <= myParent.myBuyerAgent.getMoney()) {
            ACLMessage replyMsg = bestOffer.message.createReply();
            replyMsg.setPerformative(ACLMessage.AGREE);
            replyMsg.setContent(Integer.toString(bestOffer.deleiveryPoint) + " " + Integer.toString(bestOffer.price)); // 1 аргумент - точка, где ждём доставщика, 2 - цена
            myParent.myBuyerAgent.send(replyMsg);
            System.out.println("Agent " + myParent.myBuyerAgent.getLocalName() + " is waiting the item to be delivered at the vertex " + Integer.toString(bestOffer.deleiveryPoint)
                    + " from the agent " + bestOffer.message.getSender().getLocalName());
        }
    }
}
