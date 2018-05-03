package custom.StaticBehaviours;

import custom.DynamicBehaviours.dynamicBuyerBehaviour;
import custom.Inform;
import custom.Offer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class staticReceiveInformBehaviour extends SimpleBehaviour {
    private long timeOut, wakeupTime;
    private boolean finished;

    private staticBuyerBehaviour myParent; // Экземпляр конечного автомата

    private ACLMessage msg;
    private int messagesReceived;

    MessageTemplate template;

    public ACLMessage getMessage() { return msg; }

    public staticReceiveInformBehaviour(Agent a, int millis) {
        super(a);
        timeOut = millis;
        finished = false;
    }

    public void onStart() {
        myParent = (staticBuyerBehaviour) getParent();
        template = new MessageTemplate((MessageTemplate.MatchExpression) aclMessage ->
                aclMessage.getPerformative() == (ACLMessage.INFORM)); // Шаблон для предложений
        wakeupTime = (timeOut<0 ? Long.MAX_VALUE
                :System.currentTimeMillis() + timeOut);
    }

    @Override
    public void action() {
        msg = myAgent.receive(template);
        if (msg != null) {
            handle( msg );
        }
        long dt = wakeupTime - System.currentTimeMillis();
        if ( dt > 0 ) {
            block(dt > timeOut / 10 ? timeOut / 10 : dt);
        }
        else
        {
            finished = true;
        }
    }

    @Override
    public boolean done() {
        return finished;
    }

    public void handle(ACLMessage m) {

        if (m == null) {
            System.out.println(myParent.myBuyerAgent.getLocalName() + " received a null message");
        } else {
            myParent.informOffers.add(new Inform(m));
            System.out.println(myParent.myBuyerAgent.getLocalName() + " received an inform message from " + m.getSender().getLocalName());
        }

    }
}
