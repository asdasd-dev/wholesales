package custom.DynamicBehaviours;

import custom.Agree;
import custom.BuyerAgent;
import custom.FloydWarshall;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

import static java.util.Collections.addAll;

public class dynamicAnswerToAgreeBehaviour extends OneShotBehaviour {

    private BuyerAgent myBuyerAgent;
    private ReceiverBehaviour be1;

    public dynamicAnswerToAgreeBehaviour(ReceiverBehaviour be1){
        this.be1 = be1;
    }


    @Override
    public void action() {
        myBuyerAgent = (BuyerAgent) myAgent;
        if(be1.done()) {
            try {
                ACLMessage msg = be1.getMessage();
                Agree a = new Agree(msg);
                ACLMessage replyMsg = a.message.createReply();
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int minPriceToAgent = 999999;
                int minPriceToBase = 999999;
                int minVertexToAgent = myRoute[0];
                int minVertexToBase = myRoute[0];
                for (int i = 0; i < myRoute.length; i++) {
                    int priceToAgent = myBuyerAgent.fw[myRoute[i]][a.deleiveryPoint] * myBuyerAgent.getGreed() * 2;
                    if (priceToAgent < minPriceToAgent) {
                        minPriceToAgent = priceToAgent;
                        minVertexToAgent = myRoute[i];
                    }
                    ;
                    int priceToBase = myBuyerAgent.fw[myRoute[i]][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed() * 2;
                    if (priceToBase < minPriceToBase) {
                        minPriceToBase = priceToBase;
                        minVertexToBase = myRoute[i];
                    }
                    ;
                }
                if (myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToAgent) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToAgent, a.deleiveryPoint);
                    myBuyerAgent.sumDist += myBuyerAgent.fw[minVertexToAgent][a.deleiveryPoint];
                    for (int i = 0; i < shortestWay.length; i++) {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                } else if (!myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToAgent / 2 + minPriceToBase / 2 +
                        myBuyerAgent.fw[a.deleiveryPoint][myBuyerAgent.baseWithGoods] * myBuyerAgent.getGreed()) // то есть минимальный путь: точка моего маршрута - база - точка маршрута агента, которому доставляем
                {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    myBuyerAgent.connectedWithBase = true;
                    System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                    myBuyerAgent.isReceivedAnItem = true;
                    myBuyerAgent.removeBehaviour(myBuyerAgent.myStaticBuyerBehaviour);
                    int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                    myBuyerAgent.sumDist += myBuyerAgent.fw[minVertexToBase][myBuyerAgent.baseWithGoods];
                    for (int i = 0; i < shortestWay.length; i++) {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                    shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.baseWithGoods, a.deleiveryPoint);
                    for (int i = 0; i < shortestWay.length; i++) {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, a.deleiveryPoint, minVertexToAgent);
                    myBuyerAgent.sumDist += myBuyerAgent.fw[a.deleiveryPoint][minVertexToAgent];
                    for (int i = 0; i < shortestWay.length; i++) {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                } else if (myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint)) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                } else if (!myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToBase) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    myBuyerAgent.connectedWithBase = true;
                    System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                    myBuyerAgent.isReceivedAnItem = true;
                    myBuyerAgent.removeBehaviour(myBuyerAgent.myStaticBuyerBehaviour);
                    int[] shortestWay = FloydWarshall.shortestWay(myBuyerAgent.c, minVertexToBase, myBuyerAgent.baseWithGoods);
                    myBuyerAgent.sumDist += myBuyerAgent.fw[minVertexToBase][myBuyerAgent.baseWithGoods];
                    for (int i = 0; i < shortestWay.length; i++) {
                        myBuyerAgent.routes.add(shortestWay[i]);
                    }
                    if(!myBuyerAgent.routes.contains(myBuyerAgent.baseWithGoods))
                        myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                    System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                } else {
                    replyMsg.setPerformative(ACLMessage.CANCEL);
                    System.out.println(myBuyerAgent.getLocalName() + " has declined the agree message from " + a.message.getSender().getLocalName());
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
