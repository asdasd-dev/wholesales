package custom.DynamicBehaviours;

import custom.BuyerAgent;
import custom.FloydWarshall;
import custom.Offer;
import examples.content.Receiver;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.ReceiverBehaviour;

import java.util.Arrays;

public class dynamicAnswerToProposalsBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;
    private ReceiverBehaviour be1;

    public dynamicAnswerToProposalsBehaviour(ReceiverBehaviour be1){
        this.be1 = be1;
    }

    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if(be1.done()) {
            try {
                ACLMessage msg = be1.getMessage();
                Offer a = new Offer(msg);
                int[] intersection = Arrays.stream(myBuyerAgent.getRoutes())
                        .distinct()
                        .filter(x -> Arrays.stream(a.routes).anyMatch(y -> y == x))
                        .toArray();
                ACLMessage replyMsg = a.message.createReply();
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int[] agentRoute = a.routes;
                int minPriceToAgent = 999999;
                int minPriceToBase = 999999;
                int minVertexToAgent = myRoute[0];
                for (int i = 0; i < myRoute.length; i++) {
                    for (int j = 0; j < agentRoute.length; j++) {
                        int priceToAgent = myBuyerAgent.fw[myRoute[i]][agentRoute[j]] * 2 * myBuyerAgent.getGreed();
                        if (priceToAgent < minPriceToAgent) {
                            minVertexToAgent = agentRoute[j];
                            minPriceToAgent = myBuyerAgent.fw[myRoute[i]][agentRoute[j]] * myBuyerAgent.getGreed() * 2;
                        }
                    }
                    int priceToBase = myBuyerAgent.fw[myRoute[i]][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed() * 2;
                    if (priceToBase < minPriceToBase) {
                        minPriceToBase = priceToBase;
                    };
                }
                replyMsg.setPerformative(ACLMessage.INFORM);
                if (myBuyerAgent.connectedWithBase && intersection.length > 0) // если наш агент проходит через базу с товарами и через точку,
                // в которой может находиться агент, которому надо доставить товар, то предлагаем доставить за 0 рублей
                {
                    replyMsg.setContent(Integer.toString(intersection[0]) + " 0 " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                    System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
                } else if (myBuyerAgent.connectedWithBase && intersection.length == 0) // проезжаем через базу, но не пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(minVertexToAgent) + " " + Integer.toString(minPriceToAgent) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                    System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
                } else if (!myBuyerAgent.connectedWithBase && intersection.length > 0) // не проезжаем базу и пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(intersection[0]) + " " + Integer.toString(minPriceToBase) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                    System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
                } else if (!myBuyerAgent.connectedWithBase && intersection.length == 0) // не проезжаем базу и не пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(minVertexToAgent) + " " + Integer.toString(minPriceToAgent / 2 + minPriceToBase / 2 +
                            myBuyerAgent.fw[minVertexToAgent][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed()) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                    System.out.println(myBuyerAgent.getLocalName() + " sent an inform to " + a.message.getSender().getLocalName());
                }
                myBuyerAgent.send(replyMsg);
                ((SequentialBehaviour) getParent()).reset();
            }
            catch (ReceiverBehaviour.TimedOut timedOut) {
                ((SequentialBehaviour) getParent()).reset();
            } catch (ReceiverBehaviour.NotYetReady notYetReady) {
                notYetReady.printStackTrace();
            }
        }
    }
}
