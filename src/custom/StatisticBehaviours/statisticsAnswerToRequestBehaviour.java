package custom.StatisticBehaviours;

import custom.StatisticAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class statisticsAnswerToRequestBehaviour extends OneShotBehaviour {
    private StatisticAgent myStatisticsAgent;
    private ReceiverBehaviour be1;

    public statisticsAnswerToRequestBehaviour(ReceiverBehaviour be1){
        this.be1 = be1;
    }

    @Override
    public void action() {
        myStatisticsAgent = (StatisticAgent) myAgent;
        if(be1.done()) {
            try {
                ACLMessage msg = be1.getMessage();
                System.out.println("Statistics agent has received delta from the " + msg.getSender().getLocalName() + ": " + msg.getContent());
                myStatisticsAgent.sum += Integer.parseInt(msg.getContent());
                ((SequentialBehaviour) getParent()).reset();
            }
            catch (ReceiverBehaviour.TimedOut timedOut) {
                myStatisticsAgent.doDelete();
            } catch (ReceiverBehaviour.NotYetReady notYetReady) {
                notYetReady.printStackTrace();
            }
        }
    }
}
