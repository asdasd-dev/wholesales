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
                if(myBuyerAgent.getIndex() == 8)
                {
                    System.out.println();
                }
                ACLMessage msg = be1.getMessage();
                Agree a = new Agree(msg);
                ACLMessage replyMsg = a.message.createReply();
                int[] myRoute = this.myBuyerAgent.getRoutes();
                int minPriceToAgent = 999999;
                int minPriceToBaseAndAgent = 999999;
                int minPriceToBase = 999999;
                int minVertexToAgent = myBuyerAgent.home;
                int priceToAgent = (myBuyerAgent.fw[myBuyerAgent.home][a.deleiveryPoint] + myBuyerAgent.fw[a.deleiveryPoint][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                if (priceToAgent < minPriceToAgent) {
                    minPriceToAgent = priceToAgent;
                }
                int priceToBaseAndAgent = (myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods] + myBuyerAgent.fw[myBuyerAgent.baseWithGoods][a.deleiveryPoint] + myBuyerAgent.fw[a.deleiveryPoint][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                if (priceToBaseAndAgent < minPriceToBaseAndAgent) {
                    minPriceToBaseAndAgent = priceToBaseAndAgent;
                };
                int priceToBase = (myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods] + myBuyerAgent.fw[myBuyerAgent.baseWithGoods][myBuyerAgent.work]) * myBuyerAgent.getGreed();
                if (priceToBase < minPriceToBase) {
                    minPriceToBase = priceToBase;
                };
                if (myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToAgent) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    if(myBuyerAgent.receivedFrom == null)
                        System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    else
                        System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price +
                                " (got from the " + myBuyerAgent.receivedFrom.getLocalName() + ")");
                    int[] shortestWayToAgent = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.home, a.deleiveryPoint);
                    int[] shortestWayToWork = FloydWarshall.shortestWay(myBuyerAgent.c, a.deleiveryPoint, myBuyerAgent.work);
                    //myBuyerAgent.sumDist += myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods];
                    myBuyerAgent.routes = myBuyerAgent.routes.subList(0, myBuyerAgent.routes.indexOf(myBuyerAgent.home));
                    myBuyerAgent.routes.add(myBuyerAgent.home);
                    for (int i = 0; i < shortestWayToAgent.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToAgent[i]);
                    }
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    for (int i = 0; i < shortestWayToWork.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToWork[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.work);
                    myBuyerAgent.home = a.deleiveryPoint;
                    System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                } else if (!myBuyerAgent.connectedWithBase && !myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToBaseAndAgent) // то есть минимальный путь: точка моего маршрута - база - точка маршрута агента, которому доставляем
                {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    myBuyerAgent.connectedWithBase = true;
                    System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                    myBuyerAgent.isReceivedAnItem = true;
                    myBuyerAgent.removeBehaviour(myBuyerAgent.myStaticBuyerBehaviour);
                    int[] shortestWayToBase = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.home, myBuyerAgent.baseWithGoods);
                    int[] shortestWayToAgent = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.baseWithGoods, a.deleiveryPoint);
                    int[] shortestWayToWork = FloydWarshall.shortestWay(myBuyerAgent.c, a.deleiveryPoint, myBuyerAgent.work);
                    //myBuyerAgent.sumDist += myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods];
                    myBuyerAgent.routes = myBuyerAgent.routes.subList(0, myBuyerAgent.routes.indexOf(myBuyerAgent.home));
                    myBuyerAgent.routes.add(myBuyerAgent.home);
                    for (int i = 0; i < shortestWayToBase.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToBase[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                    for (int i = 0; i < shortestWayToAgent.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToAgent[i]);
                    }
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    for (int i = 0; i < shortestWayToWork.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToWork[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.work);
                    myBuyerAgent.home = a.deleiveryPoint;
                    System.out.println(myBuyerAgent.getLocalName() + "'s new route is " + myBuyerAgent.routes.toString());
                } else if (myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint)) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    if(myBuyerAgent.receivedFrom == null)
                        System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    else
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price +
                            " (got from the " + myBuyerAgent.receivedFrom.getLocalName() + ")");
                } else if (!myBuyerAgent.connectedWithBase && myBuyerAgent.routes.contains(a.deleiveryPoint) && a.price >= minPriceToBaseAndAgent) {
                    replyMsg.setPerformative(ACLMessage.CONFIRM);
                    System.out.println("●" + myBuyerAgent.getLocalName() + " has delivered the item to the " + a.message.getSender().getLocalName() + " at the vertex " + a.deleiveryPoint + " for $" + a.price);
                    myBuyerAgent.connectedWithBase = true;
                    System.out.println("●Dynamic agent " + myBuyerAgent.getLocalName() + " has received the item at the base vertex"); // если динамический и уже находится в вершине с товарами
                    myBuyerAgent.isReceivedAnItem = true;
                    myBuyerAgent.removeBehaviour(myBuyerAgent.myStaticBuyerBehaviour);
                    int[] shortestWayToBase = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.home, myBuyerAgent.baseWithGoods);
                    int[] shortestWayToAgent = FloydWarshall.shortestWay(myBuyerAgent.c, myBuyerAgent.baseWithGoods, a.deleiveryPoint);
                    int[] shortestWayToWork = FloydWarshall.shortestWay(myBuyerAgent.c, a.deleiveryPoint, myBuyerAgent.work);
                    //myBuyerAgent.sumDist += myBuyerAgent.fw[myBuyerAgent.home][myBuyerAgent.baseWithGoods];
                    myBuyerAgent.routes = myBuyerAgent.routes.subList(0, myBuyerAgent.routes.indexOf(myBuyerAgent.home));
                    myBuyerAgent.routes.add(myBuyerAgent.home);
                    for (int i = 0; i < shortestWayToBase.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToBase[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.baseWithGoods);
                    for (int i = 0; i < shortestWayToAgent.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToAgent[i]);
                    }
                    myBuyerAgent.routes.add(a.deleiveryPoint);
                    for (int i = 0; i < shortestWayToWork.length; i++) {
                        myBuyerAgent.routes.add(shortestWayToWork[i]);
                    }
                    myBuyerAgent.routes.add(myBuyerAgent.work);
                    myBuyerAgent.home = a.deleiveryPoint;
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
