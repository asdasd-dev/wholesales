package custom.DynamicBehaviours;

import custom.Agree;
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
    private final String ANSWER_TO_PROPOSALS_STATE        = "Answer to proposals";
    private final String RECEIVE_AGREE_STATE              = "Receive agree messages";
    private final String DELIVERY_ITEMS_STATE             = "Delivery items";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public LinkedList<Offer> offerToAdd;
    public LinkedList<Agree> agreeOffers;

    public dynamicBuyerBehaviour(Agent a)
    {
        super(a);
        connectedWithBase = false;
        this.myBuyerAgent = (BuyerAgent) a;
        this.offerToAdd = new LinkedList<Offer>();
        this.agreeOffers = new LinkedList<Agree>();
        for (int i = 0; i < myBuyerAgent.getRoutes().length; i++){
            if (myBuyerAgent.getRoutes()[i] == myBuyerAgent.getBaseWithGoods())
                connectedWithBase = true;
        }

        registerFirstState(new dynamicRegisterInYPBehaviour(), REGISTER_IN_YP_STATE);
        registerState(new dynamicReceiveProposalsBehaviour(a, 1000), RECEIVE_PROPOSALS_STATE);
        registerState(new dynamicAnswerToProposalsBehaviour(), ANSWER_TO_PROPOSALS_STATE);
        registerState(new dynamicReceiveAgreeBehaviour(a, 1000), RECEIVE_AGREE_STATE);
        registerLastState(new dynamicAnswerToAgreeBehaviour(), DELIVERY_ITEMS_STATE);

        registerDefaultTransition(
                REGISTER_IN_YP_STATE,
                RECEIVE_PROPOSALS_STATE
        );

        registerDefaultTransition(
                RECEIVE_PROPOSALS_STATE,
               ANSWER_TO_PROPOSALS_STATE
        );

        registerDefaultTransition(
                ANSWER_TO_PROPOSALS_STATE,
                RECEIVE_AGREE_STATE
        );

        registerDefaultTransition(
                RECEIVE_AGREE_STATE,
                DELIVERY_ITEMS_STATE
        );
    }
}
