package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.Inform;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class staticReceiveConfirmBehaviour extends SequentialBehaviour {

    private BuyerAgent myBuyerAgent;
    private boolean isConfirmed;

    public staticReceiveConfirmBehaviour(Agent a, int millis){
        myBuyerAgent = (BuyerAgent) myAgent;
        isConfirmed = false;
        ReceiverBehaviour be1 = new ReceiverBehaviour(a, millis, MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        OneShotBehaviour be2 = new OneShotBehaviour() {
            @Override
            public void action() {
                if(be1.done()) {
                    try {
                        ACLMessage msg = be1.getMessage();
                        isConfirmed = true;
                        System.out.println(myAgent.getLocalName() + " received a confirm message from " + msg.getSender().getLocalName());
                        this.onEnd();
                    }
                    catch (ReceiverBehaviour.TimedOut timedOut) {
                        isConfirmed = false;
                    } catch (ReceiverBehaviour.NotYetReady notYetReady) {
                        notYetReady.printStackTrace();
                    }
                }
            }
        };
        addSubBehaviour(be1);
        addSubBehaviour(be2);
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
}
