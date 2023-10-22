package course.ping_pong;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongLock {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static boolean flag = false;

    public static void main(String[] args) {
        Thread threadPing = new Thread(() -> {
            try {
                ping();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread threadPong = new Thread(() -> {
            try {
                pong();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadPing.start();
        threadPong.start();
    }

    public static void ping() throws InterruptedException {
        try {
            lock.lock();
            while (true) {
                while (flag) {
                    condition.await();
                    Thread.sleep(2000);
                }
                System.out.println("ping");
                flag = true;
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void pong() throws InterruptedException {
        try {
            lock.lock();
            while (true) {
                while (!flag) {
                    condition.await();
                    Thread.sleep(2000);
                }
                System.out.println("pong");
                flag = false;
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
