package threadClassDemo;

public class ThreadClassDemo {
    public static void main(String[] args) throws Exception{
        Runnable hello = new DisplayMessage("Hello");
        Thread thread1 = new Thread(hello);
        thread1.setDaemon(true);        //守护线程 会一直执行 打印
        thread1.setName("hello");
        System.out.println("Starting hello thread...");
        thread1.start();

        Runnable bye = new DisplayMessage("Goodbye");
        Thread thread2 = new Thread(bye);
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.setDaemon(true);
        System.out.println("Starting goodbye thread...");
        thread2.start();

//        Thread.sleep(1000);

        System.out.println("Starting thread3...");
        Thread thread3 = new GuessANumber(27);
        thread3.setName("thread3");
        thread3.start();
        try {
            thread3.join(); //让“主线程”等待“子线程”结束之后才能继续运行
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
        System.out.println("Starting thread4...");
        Thread thread4 = new GuessANumber(75);
        thread4.setName("thread4");
        thread4.start();
        System.out.println("main() is ending...");
    }
}
