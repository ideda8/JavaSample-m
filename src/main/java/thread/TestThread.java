package thread;

public class TestThread {
    public static void main(String args[]){
        RunnableDemo r1 = new RunnableDemo("线程一");
        r1.start();

        RunnableDemo r2 = new RunnableDemo("线程二");
        r2.start();

        RunnableDemo r3 = new RunnableDemo("线程三");
        r3.start();
    }

}
