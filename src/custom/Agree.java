package custom;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Agree {
    public int price;
    public int deleiveryPoint;
    public AID id;
    public ACLMessage message;

    public Agree (ACLMessage message){

        if(message.getPerformative() == ACLMessage.AGREE){
            this.id = message.getSender();
            String[] message_info = message.getContent().split(" ");

            this.price = Integer.parseInt(message_info[1]);

            this.deleiveryPoint = Integer.parseInt(message_info[0]);

            this.message = message;
        }
    }
}
