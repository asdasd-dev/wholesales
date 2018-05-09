package custom.DynamicBehaviours;

import jade.core.behaviours.CyclicBehaviour;

public class dynamicCyclicRAAgree extends CyclicBehaviour {
    @Override
    public void action() {
        myAgent.addBehaviour(new dynamicReceiveAgreeBehaviour(myAgent, 1000));
        myAgent.addBehaviour(new dynamicAnswerToAgreeBehaviour());
    };
}
