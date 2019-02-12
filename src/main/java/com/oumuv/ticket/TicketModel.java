package com.oumuv.ticket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程抢票应用
 */
public class TicketModel {


    public static void main(String[] args) {
        TicketModel ticketModel = new TicketModel();
        Ticket ticket = ticketModel.new Ticket(20);
        TicketConsumer ticketConsumer = ticketModel.new TicketConsumer("售票点1", ticket);
        TicketConsumer ticketConsumer2 = ticketModel.new TicketConsumer("售票点2", ticket);
        TicketConsumer ticketConsumer3 = ticketModel.new TicketConsumer("售票点3", ticket);
//        new Thread(ticketConsumer).start();
//        new Thread(ticketConsumer2).start();
//        new Thread(ticketConsumer3).start();


        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(ticketConsumer);
        executorService.submit(ticketConsumer2);
        executorService.submit(ticketConsumer3);

    }

    /**
     * 余票
     */
    class Ticket {
        int num;//余票数量

        public Ticket(int num) {
            this.num = num;
        }

        protected synchronized boolean sellTicket(String name) throws InterruptedException {
            if (num > 0) {
                System.out.println(name + ":您成功购买到一张票，余票剩余" + (--num));
                Thread.sleep(100);
                return true;
            } else {
                System.out.println(name + ":余票不足，购买失败");
                return false;
            }
        }
    }

    /**
     * 售票端
     */
    class TicketConsumer implements Runnable {
        String name;
        Ticket ticket;

        public TicketConsumer(String name, Ticket ticket) {
            this.name = name;
            this.ticket = ticket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (!ticket.sellTicket(name))
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                synchronized (this.ticket) {
//                    if (ticket.num > 0) {
//                        System.out.println(name + ":您成功购买到一张票，余票剩余" + (--ticket.num));
//                        try {
//                            Thread.sleep(50);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        System.out.println(name + ":余票不足，购买失败");
//                        break;
//                    }
//                }
            }
        }
    }

}



