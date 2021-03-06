package callable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableThreadTest implements Callable<String> {
    public static void main(String[] args){
        CallableThreadTest ctt = new CallableThreadTest();
        FutureTask<String> ft = new FutureTask<String>(ctt);
        for(int i = 0; i<100; i++){
            System.out.println(Thread.currentThread().getName()+ " 的循环变量i的值 " + i);
            if(i==20){
                new Thread(ft, "有返回值的线程").start();
            }
        }
        try{
            System.out.println("子线程的返回值："+ft.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String call() throws Exception {
        int i = 0;
        for(; i<100; i++){
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
        return "执行完" + i;
    }
}
