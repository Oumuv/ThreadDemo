package com.oumuv.producerandconsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者与消费者问题：wait、notify（notifyAll）应用
 * 一个篮子可存20颗鸡蛋，鸡蛋数量小于5时生产者开始生产鸡蛋,篮子满后生产者停止生产并进入等待状态，鸡蛋数量等于0时消费者不能消费
 */
public class ProducerAndConsumer2 {

    public static void main(String[] args) {
        ProducerAndConsumer2 pac = new ProducerAndConsumer2();

        EggKep eggKep = pac.new EggKep();
        Producer p1 = pac.new Producer(eggKep);
//        ProducerAndConsumer2.Producer p2 = pac.new Producer(eggKep);

        Consumer c1 = pac.new Consumer(eggKep);
//        ProducerAndConsumer2.Consumer c2 = pac.new Consumer(eggKep);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(p1);
//        executorService.submit(p2);
        executorService.submit(c1);
//        executorService.submit(c2);
    }


    /**
     * 生产者
     */
    class Producer implements Runnable {
        EggKep agg;

        public Producer(EggKep agg) {
            this.agg = agg;
        }

        @Override
        public void run() {
            try {
//                for (int i = 0; i < 50; i++) {
                while (true) {
                    agg.produce();
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消费者
     */
    class Consumer implements Runnable {
        EggKep agg;

        public Consumer(EggKep agg) {
            this.agg = agg;
        }

        @Override
        public void run() {
            try {
//                for (int i = 0; i < 50; i++) {
                while (true) {
                    agg.consume();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 鸡蛋篮子
     */
    class EggKep {
        int maxSize = 20;//篮子大小
        int num = 0;//鸡蛋数量
        boolean flag = true;

        public EggKep(int maxSize, int num) {
            this.maxSize = maxSize;
            this.num = num;
        }

        public EggKep() {

        }

        protected synchronized void produce() throws InterruptedException {
            while (!flag) {
                this.wait();
            }

            System.out.println("生产了一颗鸡蛋，现有鸡蛋：" + (++num) + "颗");
            if (num == maxSize) {
                flag = false;
                System.out.println("篮子满了，生产者停止工作！");
            }
            this.notifyAll();
        }

        protected synchronized void consume() throws InterruptedException {
            while (num == 0) {
                System.out.println("现有鸡蛋：" + num + "颗，消费者不可消费！");
                this.wait();
            }
            System.out.println("消费一颗鸡蛋，现有鸡蛋：" + (--num) + "颗");
            if (num <= 5 && !flag) {
                flag = true;
                System.out.println("通知生产者开始工作！");
            }
            this.notifyAll();
        }
    }
}

