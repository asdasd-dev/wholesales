package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.Inform;
import custom.Offer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.WakerBehaviour;


import java.util.LinkedList;
import java.util.List;

public class staticBuyerBehaviour extends FSMBehaviour {

    public BuyerAgent myBuyerAgent;
    public List<AID> listOfDynamicAgents = new LinkedList<>();

    private final String SEARCH_DELIVERYMAN_STATE             = "Search deliveryman";
    private final String SEND_PROPOSALS_STATE                 = "Send proposals";
    private final String WAIT_FOR_YP_REGISTER                 = "Waiting for YP registration";
    private final String RECEIVE_INFORMATION_STATE            = "Receive an inform";
    private final String ANSWER_TO_INFORM_STATE               = "Answer to inform";
    private final String RECEIVE_CONFIRM_STATE                = "Receive confirm";
    private final String END_STATE                            = "Bye state";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public LinkedList<Inform> informOffers;

    public staticBuyerBehaviour(Agent a)
    {
        super(a);

        registerFirstState(new WakerBehaviour(a, 100) { }, WAIT_FOR_YP_REGISTER);
        registerState(new searchForDeliveryOffers(), SEARCH_DELIVERYMAN_STATE);
        registerState(new staticSendProposalsBehaviour(), SEND_PROPOSALS_STATE);
        registerState(new staticReceiveInformBehaviour(a, 1000), RECEIVE_INFORMATION_STATE);
        registerState(new staticAnswerToInformBehaviour(), ANSWER_TO_INFORM_STATE);
        registerState(new staticReceiveConfirmBehaviour(a, 1000), RECEIVE_CONFIRM_STATE);
        registerLastState(new staticFinalBehaviour(), END_STATE);

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

        registerTransition(
                ANSWER_TO_INFORM_STATE,
                SEARCH_DELIVERYMAN_STATE,
                NEGATIVE_CONDITION
        );

        registerTransition(
                ANSWER_TO_INFORM_STATE,
                RECEIVE_CONFIRM_STATE,
                POSITIVE_CONDITION
        );

        registerTransition(
                RECEIVE_CONFIRM_STATE,
                SEARCH_DELIVERYMAN_STATE,
                NEGATIVE_CONDITION
        );

        registerTransition(
                RECEIVE_CONFIRM_STATE,
                END_STATE,
                POSITIVE_CONDITION
        );

    }
}