package custom.StatisticBehaviours;

import custom.StatisticAgent;
import jade.core.Agent;
import jade.core.behaviours.ReceiverBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class statisticsReceiveRequestBehaviour extends ReceiverBehaviour {
    private StatisticAgent myStatisticsAgent;

    public statisticsReceiveRequestBehaviour(Agent a, int millis) {
        super(a, millis, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    }

    public void onStart() {
        myStatisticsAgent = (StatisticAgent) myAgent;
    }
}
