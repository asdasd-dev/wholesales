package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.Inform;
import custom.Offer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.myReceiver;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class staticReceiveInformBehaviour extends myReceiver {

    private BuyerAgent myBuyerAgent;
    private long startTime;
    private int millis;

    public staticReceiveInformBehaviour(Agent a, int millis) {
        super(a, millis, MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        this.millis = millis;
    }

    public void onStart() {
        startTime = System.currentTimeMillis();
        myBuyerAgent = (BuyerAgent) myAgent;
    }

    @Override
    public boolean done() {
        return startTime + millis < System.currentTimeMillis();
    }

    public void handle(ACLMessage m) {

        if (m == null) {
            //System.out.println(myBuyerAgent.getLocalName() + " received a null message");
        } else {
            myBuyerAgent.informOffers.add(new Inform(m));
            System.out.println(myBuyerAgent.getLocalName() + " received an inform message from " + m.getSender().getLocalName());
        }

    }
}
