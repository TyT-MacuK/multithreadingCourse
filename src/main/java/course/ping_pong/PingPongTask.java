package course.ping_pong;

public class PingPongTask {
    private static final Object lock = new Object();
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
        while (true) {
            synchronized (lock) {
                while (flag) {
                    lock.wait();
                    Thread.sleep(2000);
                }
                System.out.println("ping");
                flag = true;
                lock.notify();
            }
        }
    }

    public static void pong() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (!flag) {
                    lock.wait();
                    Thread.sleep(2000);
                }
                System.out.println("pong");
                flag = false;
                lock.notify();
            }
        }
    }
}
