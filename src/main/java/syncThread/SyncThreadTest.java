package syncThread;

public class SyncThreadTest {
    public static void main(String args[]) {
        final Bank bank = new Bank();
//        final Bank2 bank = new Bank2();

        Thread tadd = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bank.addMoney(100);
                    bank.lookMoney();
                    System.out.println();
                }
            }
        });

        Thread tsub = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    bank.subMoney(100);
                    bank.lookMoney();
                    System.out.println();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tsub.start();
        tadd.start();
    }

}
