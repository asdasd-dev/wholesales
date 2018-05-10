package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.Inform;
import jade.core.Agent;
import jade.core.behaviours.myReceiver;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class staticReceiveConfirmBehaviour extends myReceiver {

    private BuyerAgent myBuyerAgent;
    private long startTime;
    private int millis;
    private boolean isConfirmed;

    public staticReceiveConfirmBehaviour(Agent a, int millis) {
        super(a, millis, MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        this.millis = millis;
    }

    public void onStart() {
        isConfirmed = false;
        startTime = System.currentTimeMillis();
        myBuyerAgent = (BuyerAgent) myAgent;
        if (myBuyerAgent.isReceivedAnItem && myBuyerAgent.connectedWithBase)
        {
            isConfirmed = true;
            onEnd();
        }
    }

    @Override
    public boolean done() {
        return startTime + millis < System.currentTimeMillis();
    }

    @Override
    public int onEnd() {
        staticBuyerBehaviour myParent = (staticBuyerBehaviour) getParent();
        if (isConfirmed)
        {
            return myParent.POSITIVE_CONDITION;
        }
        else
        {
            return myParent.NEGATIVE_CONDITION;
        }
    }

    public void handle(ACLMessage m) {

        if (m == null) {
            //System.out.println(myBuyerAgent.getLocalName() + " received a null message");
        } else {
            isConfirmed = true;
            System.out.println(myBuyerAgent.getLocalName() + " received a confirm message from " + m.getSender().getLocalName());
        }

    }
}
