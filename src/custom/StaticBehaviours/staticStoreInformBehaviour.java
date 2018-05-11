package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.Inform;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class staticStoreInformBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;
    private ReceiverBehaviour be1;

    public staticStoreInformBehaviour(ReceiverBehaviour be1){
        this.be1 = be1;
    }

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if(be1.done()) {
            try {
                ACLMessage msg = be1.getMessage();
                myBuyerAgent.informOffers.add(new Inform(msg));
                ((SequentialBehaviour) getParent()).reset();
            }
            catch (ReceiverBehaviour.TimedOut timedOut) {
                ((SequentialBehaviour) getParent()).done();
            } catch (ReceiverBehaviour.NotYetReady notYetReady) {
                notYetReady.printStackTrace();
            }
        }
    }
}
