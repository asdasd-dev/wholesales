package custom.DynamicBehaviours;

import custom.Offer;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class dynamicAnswerToProposalsBehaviour extends OneShotBehaviour {

    private dynamicBuyerBehaviour myParent;

    @Override
    public void action() {

        myParent = (dynamicBuyerBehaviour) getParent();

        for (Offer a: myParent.offerToAdd) {
            int[] intersection = Arrays.stream(myParent.myBuyerAgent.getRoutes())
                    .distinct()
                    .filter(x -> Arrays.stream(a.routes).anyMatch(y -> y == x))
                    .toArray();
            if (myParent.connectedWithBase && intersection != null) // если наш агент проходит через базу с товарами и через точку,
                                                                    // в которой может находиться агент, которому надо доставить товар, то предлагаем доставить за 0 рублей
            {
                ACLMessage replyMsg = a.message.createReply();
                replyMsg.setPerformative(ACLMessage.INFORM);
                replyMsg.setContent(Integer.toString(intersection[0]) + " 0 " + Integer.toString(myParent.myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                myParent.myBuyerAgent.send(replyMsg);
            }
        }
    }
}
