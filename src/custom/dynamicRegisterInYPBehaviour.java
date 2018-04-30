package custom;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class dynamicRegisterInYPBehaviour extends OneShotBehaviour {

    private dynamicBuyerBehaviour myParent;

    @Override
    public void action() {

        String myRoutes = "";
        int[] routesArray = ((BuyerAgent) myAgent).getRoutes();

        for(int i = 0; i < routesArray.length; i++)
        {
            myRoutes += routesArray[i];
            if (i != routesArray.length - 1)
                myRoutes += ",";
        }

        myParent = (dynamicBuyerBehaviour) getParent();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("dynamicWholesale");
        sd.setName("JADE-wholesale");

        Property p = new Property();
        p.setName("routes");
        p.setValue(myRoutes); //регистрируемся в YP и передаём туда маршрут своего передвижения
        sd.addProperties(p);

        dfd.addServices(sd);

        try {
            DFService.register(myAgent, dfd);
            System.out.println("Dynamic agent " + myAgent.getAID().getLocalName() + " is registered in Yellow Pages service");

            /*//init offer pool
            myParentFSM.myCitizenAgent.offersPool = new LinkedList<>();
            myParentFSM.myCitizenAgent.best_offer = new LinkedList<>();*/

        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
