package custom.DynamicBehaviours;

import jade.core.behaviours.CyclicBehaviour;

public class dynamicCyclicRAProposals extends CyclicBehaviour {
    @Override
    public void action() {
        myAgent.addBehaviour(new dynamicReceiveProposalsBehaviour(myAgent, 1000));
        myAgent.addBehaviour(new dynamicAnswerToProposalsBehaviour());
    }
}
