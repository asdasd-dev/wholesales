package custom.DynamicBehaviours;

import custom.Agree;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class dynamicAnswerToAgreeBehaviour extends OneShotBehaviour {

    private dynamicBuyerBehaviour myParent;

    @Override
    public void action() {

        myParent = (dynamicBuyerBehaviour) getParent();

        for (Agree a: myParent.agreeOffers) {
            System.out.println(myParent.myBuyerAgent.getLocalName() + " is delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
        }
    }
}
