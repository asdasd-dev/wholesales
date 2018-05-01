package custom;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Offer {
    public int[] routes;
    public int pointToDelivery;
    public AID id;
    public ACLMessage message;

    public Offer (ACLMessage message){

        if(message.getPerformative() == ACLMessage.PROPOSE){
            this.id = message.getSender();
            String[] message_info = message.getContent().split(" ");

            this.pointToDelivery = Integer.parseInt(message_info[0]);

            String[] routesStr = message_info[1].split(",");
            routes = new int[routesStr.length];

            for (int i = 0; i < routesStr.length; i++)
            {
                routes[i] = Integer.parseInt(routesStr[i]);
            }

            this.message = message;
        }
    }
}
