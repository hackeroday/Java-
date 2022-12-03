package com.java.concurrent.BlockQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProducerCustomerModel {
    public static volatile boolean isCustomer = true;
    public static void main(String[] args) throws Throwable {
        BlockingQueue<Long> blockingQueue = new ArrayBlockingQueue<>(11);
        Producer producer = new Producer();
        Customer customer = new Customer();

        Thread threadP = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                blockingQueue.add(producer.produceTimeLong());
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                while (isCustomer) {
                    System.out.println("customer start");
                    Long timeLong = blockingQueue.poll(5, TimeUnit.SECONDS);
                    if (timeLong != null) {
                        customer.customerTimeLong(timeLong);
                    }
                }

                System.out.println("customer end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadP.start();
        TimeUnit.SECONDS.sleep(2);
        threadC.start();
        TimeUnit.SECONDS.sleep(5);

        isCustomer = false;
    }

    private static class Producer {
        public long produceTimeLong() {
            long time = System.currentTimeMillis();

            System.out.println(time);
            return time;
        }
    }

    private static class Customer {
        public void customerTimeLong(long timeLong) {
            Date date = new Date(timeLong);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(timeLong + " " + simpleDateFormat.format(date));
        }
    }
}


