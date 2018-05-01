package custom.StaticBehaviours;

import custom.BuyerAgent;
import custom.StaticBehaviours.staticBuyerBehaviour;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;


import java.util.LinkedList;
import java.util.List;

public class searchForDeliveryOffers extends OneShotBehaviour {
    private BuyerAgent myBuyerAgent;

    private List<AID> agentsForOffer = new LinkedList<>();

    @Override
    public void action(){

        staticBuyerBehaviour myParent = (staticBuyerBehaviour) getParent();
        myBuyerAgent = (BuyerAgent) myAgent;

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType("dynamicWholesale");
            agentDescription.addServices(serviceDescription);

            DFAgentDescription[] results = DFService.search(myAgent, agentDescription);

            // ищем подходящих агентов для доставки по маршруту их передвижения

            if (results.length > 0) {
                for (DFAgentDescription dfd : results) {
                    AID provider = dfd.getName();
                    if (!provider.equals(myAgent.getAID()) && !agentsForOffer.contains(provider)){
                        myParent.listOfDynamicAgents.add(provider);
                        agentsForOffer.add(provider);
                    }
                }
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
