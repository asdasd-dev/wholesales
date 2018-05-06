package custom.DynamicBehaviours;

import custom.Agree;
import custom.BuyerAgent;
import custom.Offer;
import custom.StaticBehaviours.searchForDeliveryOffers;
import custom.StaticBehaviours.staticAnswerToInformBehaviour;
import custom.StaticBehaviours.staticReceiveInformBehaviour;
import custom.StaticBehaviours.staticSendProposalsBehaviour;
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
    private final String SEARCH_DELIVERYMAN_STATE             = "Search deliveryman";
    private final String SEND_PROPOSALS_STATE                 = "Send proposals";
    private final String WAIT_FOR_YP_REGISTER                 = "Waiting for YP registration";
    private final String RECEIVE_INFORMATION_STATE            = "Receive an inform";
    private final String ANSWER_TO_INFORM_STATE               = "Answer to inform";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public dynamicBuyerBehaviour(Agent a)
    {
        super(a);

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

        /*registerFirstState(new dynamicRegisterInYPBehaviour(), REGISTER_IN_YP_STATE);
        registerState(new dynamicReceiveProposalsBehaviour(a, 1000), RECEIVE_PROPOSALS_STATE);
        registerState(new dynamicAnswerToProposalsBehaviour(), ANSWER_TO_PROPOSALS_STATE);
        registerState(new dynamicReceiveAgreeBehaviour(a, 1000), RECEIVE_AGREE_STATE);
        registerState(new WakerBehaviour(a, 100) { }, WAIT_FOR_YP_REGISTER);
        registerState(new searchForDeliveryOffers(), SEARCH_DELIVERYMAN_STATE);
        registerState(new staticSendProposalsBehaviour(), SEND_PROPOSALS_STATE);
        registerState(new staticReceiveInformBehaviour(a, 1000), RECEIVE_INFORMATION_STATE);
        registerState(new staticAnswerToInformBehaviour(), ANSWER_TO_INFORM_STATE);
        registerLastState(new dynamicAnswerToAgreeBehaviour(), DELIVERY_ITEMS_STATE);

        registerTransition(
                REGISTER_IN_YP_STATE,
                RECEIVE_PROPOSALS_STATE,
                POSITIVE_CONDITION
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

        registerTransition(
                REGISTER_IN_YP_STATE,
                WAIT_FOR_YP_REGISTER,
                NEGATIVE_CONDITION
        );

        registerDefaultTransition(
                WAIT_FOR_YP_REGISTER,
                SEARCH_DELIVERYMAN_STATE
        );

        registerDefaultTransition(
                SEARCH_DELIVERYMAN_STATE,
                SEND_PROPOSALS_STATE
        );

        registerDefaultTransition(
                SEND_PROPOSALS_STATE,
                RECEIVE_INFORMATION_STATE
        );

        registerDefaultTransition(
                RECEIVE_INFORMATION_STATE,
                ANSWER_TO_INFORM_STATE
        );

        registerDefaultTransition(
                ANSWER_TO_INFORM_STATE,
                RECEIVE_PROPOSALS_STATE
        );*/
    }
}
