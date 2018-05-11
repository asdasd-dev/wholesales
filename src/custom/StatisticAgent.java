package custom;

import custom.StatisticBehaviours.statisticsAnswerToRequestBehaviour;
import custom.StatisticBehaviours.statisticsReceiveRequestBehaviour;
import custom.StatisticBehaviours.statisticsRegisterInYPBehaviour;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;

public class StatisticAgent extends Agent {
    public int sum;
    @Override
    protected void setup() {
        super.setup();
        sum = 0;
        addBehaviour(new statisticsRegisterInYPBehaviour());

        SequentialBehaviour be = new SequentialBehaviour(this);
        ReceiverBehaviour be1 = new statisticsReceiveRequestBehaviour(this, 10000);
        OneShotBehaviour be2 = new statisticsAnswerToRequestBehaviour(be1);
        be.addSubBehaviour(be1);
        be.addSubBehaviour(be2);

        addBehaviour(be);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("Delta is " + sum);
    }
}
