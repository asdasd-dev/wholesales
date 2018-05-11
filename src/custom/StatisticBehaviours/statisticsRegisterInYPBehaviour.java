package custom.StatisticBehaviours;

import custom.BuyerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class statisticsRegisterInYPBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("dynamicStatistics");
        sd.setName("JADE-wholesale");

        dfd.addServices(sd);

        try {
            DFService.register(myAgent, dfd);
            System.out.println("Statistics agent " + myAgent.getAID().getLocalName() + " is registered in Yellow Pages service");
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}

