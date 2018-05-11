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
                int minPriceToBaseAndAgent = 999999;
                int minPriceToBase = 999999;
                int minVertexToAgent = myBuyerAgent.home;
                for (int j = 0; j < agentRoute.length; j++) {
                    int priceToAgent = (myBuyerAgent.fw[myBuyerAgent.home][agentRoute[j]] + myBuyerAgent.fw[agentRoute[j]][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                    if (priceToAgent < minPriceToAgent) {
                        minVertexToAgent = agentRoute[j];
                        minPriceToAgent = priceToAgent;
                    }
                    int priceToBaseAndAgent = (myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods] + myBuyerAgent.fw[myBuyerAgent.baseWithGoods][agentRoute[j]] + myBuyerAgent.fw[agentRoute[j]][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                    if (priceToBaseAndAgent < minPriceToBaseAndAgent) {
                        minPriceToBaseAndAgent = priceToBaseAndAgent;
                    };
                    int priceToBase = (myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods] + myBuyerAgent.fw[myBuyerAgent.baseWithGoods][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                    if (priceToBase < minPriceToBase) {
                        minPriceToBase = priceToBase;
                    };
                };
                replyMsg.setPerformative(ACLMessage.INFORM);
                if (myBuyerAgent.connectedWithBase && intersection.length > 0) // если наш агент проходит через базу с товарами и через точку,
                // в которой может находиться агент, которому надо доставить товар, то предлагаем доставить за 0 рублей
                {
                    replyMsg.setContent(Integer.toString(intersection[0]) + " 0 " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                } else if (myBuyerAgent.connectedWithBase && intersection.length == 0) // проезжаем через базу, но не пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(minVertexToAgent) + " " + Integer.toString(minPriceToAgent) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                } else if (!myBuyerAgent.connectedWithBase && intersection.length > 0) // не проезжаем базу и пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(intersection[0]) + " " + Integer.toString(minPriceToBaseAndAgent) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                } else if (!myBuyerAgent.connectedWithBase && intersection.length == 0) // не проезжаем базу и не пересекаемся с агентом
                {
                    replyMsg.setContent(Integer.toString(minVertexToAgent) + " " + Integer.toString(minPriceToBaseAndAgent) + " " + Integer.toString(myBuyerAgent.getIndex())); // 1 аргумент - точка, куда можем доставить, 2 - цена (0), 3 - наш индекс
                }
                myBuyerAgent.send(replyMsg);
                ((SequentialBehaviour) getParent()).reset();
            }
            catch (ReceiverBehaviour.TimedOut timedOut) {
                myBuyerAgent.doDelete();
            } catch (ReceiverBehaviour.NotYetReady notYetReady) {
                notYetReady.printStackTrace();
            }
        }
    }
}
