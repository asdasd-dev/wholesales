package custom.DynamicBehaviours;

import custom.BuyerAgent;
import custom.Offer;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.util.leap.List;

import java.util.LinkedList;

public class dynamicBuyerBehaviour extends FSMBehaviour {

    public BuyerAgent myBuyerAgent;
    public boolean connectedWithBase;

    private final String REGISTER_IN_YP_STATE             = "Register in YP";
    private final String RECEIVE_PROPOSALS_STATE          = "Receive proposals";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public LinkedList<Offer> offerToAdd;

    public dynamicBuyerBehaviour(Agent a)
    {
        super(a);
        connectedWithBase = false;
        this.myBuyerAgent = (BuyerAgent) a;
        for (int i = 0; i < myBuyerAgent.getRoutes().length; i++){
            if (myBuyerAgent.getRoutes()[i] == myBuyerAgent.getBaseWithGoods())
                connectedWithBase = true;
        }

        registerFirstState(new dynamicRegisterInYPBehaviour(), REGISTER_IN_YP_STATE);
        registerLastState(new dynamicReceiveProposalsBehaviour(a, 30000), RECEIVE_PROPOSALS_STATE);

        registerDefaultTransition(
                REGISTER_IN_YP_STATE,
                RECEIVE_PROPOSALS_STATE
        );
    }
}
