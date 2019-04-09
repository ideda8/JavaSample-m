package thread;

public class RunnableDemo implements Runnable {
    private Thread t;
    private String threadName;

    public RunnableDemo(String threadName) {
        this.threadName = threadName;
        System.out.println("1.创建线程：" + threadName);
    }

    public void run() {
        System.out.println("3.运行线程：" + threadName);
        try{
            for(int i=0; i<5; i++){
                System.out.println("线程：" + threadName + " i=" + (i+1));
                Thread.sleep(300);
            }
        }catch (InterruptedException e){
            System.out.println("线程错误：" + threadName);
            e.printStackTrace();
        }
        System.out.println("4.完成线程：" + threadName);
    }

    public void  start(){
        System.out.println("2.开始线程：" + threadName);
        if(t==null){
            t = new Thread(this, threadName);
            t.start();
        }

        if(t.getName().equals("线程三")){
            t.setPriority(9);
        }
        System.out.println(threadName + " getName ：" + t.getName());
        System.out.println(threadName + " getPriority ：" + t.getPriority());
        System.out.println(threadName + " getState ：" + t.getState());
        System.out.println(threadName + " currentThread ：" + t.currentThread());
        System.out.println(threadName + " isAlive ：" + t.isAlive());
    }

}
