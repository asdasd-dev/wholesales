package custom.DynamicBehaviours;

import custom.BuyerAgent;
import custom.Offer;
import jade.core.Agent;
import jade.core.behaviours.ReceiverBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class dynamicReceiveProposalsBehaviour extends ReceiverBehaviour {

    private BuyerAgent myBuyerAgent;

    public dynamicReceiveProposalsBehaviour(Agent a, int millis) {
        super(a, millis, MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
    }

    public void onStart() {
        myBuyerAgent = (BuyerAgent) myAgent;
    }

    public void handle(ACLMessage m) {

        if (m == null) {
            //System.out.println(myBuyerAgent.getLocalName() + " received a null message");
        } else {
            myBuyerAgent.offerToAdd.add(new Offer(m));
            System.out.println(myBuyerAgent.getLocalName() + " received a proposal from " + m.getSender().getLocalName());
        }

    }
}
