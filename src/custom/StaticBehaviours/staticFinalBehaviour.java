package custom.StaticBehaviours;

import custom.BuyerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class staticFinalBehaviour extends OneShotBehaviour {
    private BuyerAgent myBuyerAgent;
    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if(myBuyerAgent.isStatic) {
            System.out.println("Bye from the agent " + myBuyerAgent.getLocalName());
        }
    }
}
