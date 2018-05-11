package custom.StaticBehaviours;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class staticReceiveInformBehaviour extends SequentialBehaviour {

    public staticReceiveInformBehaviour(Agent a, int millis){
        ReceiverBehaviour be1 = new ReceiverBehaviour(a, millis, MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        OneShotBehaviour be2 = new staticStoreInformBehaviour(be1);
        addSubBehaviour(be1);
        addSubBehaviour(be2);
    }
}
