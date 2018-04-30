package custom;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.leap.Iterator;


import java.util.LinkedList;
import java.util.List;

public class staticBuyerBehaviour extends FSMBehaviour {

    public BuyerAgent myBuyerAgent;
    public List<AID> listOfDynamicAgents = new LinkedList<>();

    private final String SEARCH_DELIVERYMAN_STATE             = "Search deliveryman";
    private final String SEND_PROPOSALS_STATE                 = "Send proposals";

    public static final int POSITIVE_CONDITION =  1;
    public static final int NEGATIVE_CONDITION =  0;
    public static final int FORCE_REJECT       = -1;

    public staticBuyerBehaviour(Agent a)
    {
        super(a);
        this.myBuyerAgent = (BuyerAgent) myAgent;

        registerFirstState(new searchForDeliveryOffers(), SEARCH_DELIVERYMAN_STATE);
        registerState(new staticSendProposalsBehaviour(), SEND_PROPOSALS_STATE);

        registerDefaultTransition(
                SEARCH_DELIVERYMAN_STATE,
                SEND_PROPOSALS_STATE
        );
    }
}