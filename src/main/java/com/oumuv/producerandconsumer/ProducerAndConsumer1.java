package com.oumuv.producerandconsumer;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 多线程生产者与消费者问题
 * 生产者生产，消费者立刻消费
 * 使用阻塞队列 {@link BlockingQueue}保存产品
 */
public class ProducerAndConsumer1 {


    public static void main(String[] args) {
        ProducerAndConsumer1 pac = new ProducerAndConsumer1();
        Storage storage = pac.new Storage();
        Producer p1 = pac.new Producer("生产者1", "小米手机", storage);
        Producer p2 = pac.new Producer("生产者2", "华为手机", storage);

        Consumer c1 = pac.new Consumer(storage);


        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(p1);
        executorService.submit(p2);
        executorService.submit(c1);

    }

    /**
     * 生产者
     */
    class Producer implements Runnable {
        private String name;//生产者名称
        private String pname;//生产的产品
        private Storage storage;

        public Producer(String name, String pname, Storage storage) {
            this.name = name;
            this.pname = pname;
            this.storage = storage;
        }


        @Override
        public void run() {
            try {
                for (int i = 0; i < 50; i++) {
                    Product p = new Product(pname, name, i);
                    storage.push(p);
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

        private Storage storage;

        public Consumer(Storage storage) {
            this.storage = storage;
        }


        @Override
        public void run() {
            try {
                while (true) {
                    storage.pop();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 产品信息
     */
    class Product {
        private String name;
        private String from;//来自哪个生产者
        private int id = 0;

        public Product(String name, String from, int id) {
            this.name = name;
            this.from = from;
            this.id = id;
        }


        public String getName() {
            return name;
        }


        public String getFrom() {
            return from;
        }


        public int getId() {
            return id;
        }

    }

    /**
     * 产品仓库
     */
    class Storage {

        //阻塞队列
        private BlockingQueue<Product> storage = new LinkedBlockingDeque<>();


        /**
         * 消费
         */
        protected void pop() throws InterruptedException {
            Product product = storage.take();
            System.out.println(product.getFrom() + "的" + product.getName() + "--> id:" + product.getId() + "被消费了");
        }

        /**
         * 生产
         */
        protected void push(Product product) throws InterruptedException {
            storage.put(product);
            System.out.println(product.getFrom() + ",生产了" + product.getName() + "--> id:" + product.getId());
        }

    }
}

