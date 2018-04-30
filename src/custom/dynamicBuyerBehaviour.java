package custom;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

public class dynamicBuyerBehaviour extends FSMBehaviour {

    public BuyerAgent myBuyerAgent;

    private final String REGISTER_IN_YP_STATE             = "Register in YP";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public dynamicBuyerBehaviour(Agent a)
    {
        super(a);
        this.myBuyerAgent = (BuyerAgent) a;

        registerFirstState(new dynamicRegisterInYPBehaviour(), REGISTER_IN_YP_STATE);
    }
}
