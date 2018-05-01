package custom;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Inform {
    public int deleiveryPoint;
    public int price;
    public int index;
    public AID id;
    public ACLMessage message;

    public Inform (ACLMessage message){

        if(message.getPerformative() == ACLMessage.INFORM){
            this.id = message.getSender();
            String[] message_info = message.getContent().split(" ");

            this.index = Integer.parseInt(message_info[2]);

            this.price = Integer.parseInt(message_info[1]);
            this.deleiveryPoint = Integer.parseInt(message_info[0]);

            this.message = message;
        }
    }
}
