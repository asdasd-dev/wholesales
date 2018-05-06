package custom.DynamicBehaviours;

import custom.Agree;
import custom.BuyerAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class dynamicAnswerToAgreeBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;

    @Override
    public void action() {

        myBuyerAgent = (BuyerAgent) myAgent;

        for (Agree a: myBuyerAgent.agreeOffers) {
            System.out.println(myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
        }
    }
}
