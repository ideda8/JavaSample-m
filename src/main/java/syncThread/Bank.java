package syncThread;

public class Bank {
    //https://blog.csdn.net/pengzhisen123/article/details/79450844
    /**
     * 使用特殊域变量（volatile）实现线程同步
     * a.volatile关键字为域变量的访问提供了一种免锁机制
     * b.使用volatile修饰符相当于告诉虚拟机该域可能会被其他线程更新
     * c.因此每次使用该域就要重新计算，而不是使用寄存器中的值
     * d.volatile不会提供任何原子操作，它也不能用来修饰fianl类型的变量
     *  private volatile int count = 0;//账号余额
     */

    private int count = 0;//账号余额

    /**
     * 1、同步是一种高开销的操作，因此应尽量减少同步的内容。
     * 2、通常没有必要同步整个方法，使用synchronized代码块同步关键代码即可。
     * 即有synchronized关键字修饰的语句块。被该关键字修饰的语句块会自动被加上内置锁，从而实现同步
     * synchronized(object){}
     */

    //存钱
    public void addMoney(int money) {
        synchronized(this) {
            count += money;
        }
        System.out.println(System.currentTimeMillis() + "存进：" + money);
    }

    //取钱
    public synchronized void subMoney(int money) {
        synchronized(this) {
            if (count - money < 0) {
                System.out.println("余额不足");
                return;
            }
            count -= money;
        }
        System.out.println(System.currentTimeMillis() + "取出：" + money);
    }

    //查询
    public void lookMoney() {
        System.out.println("账户余额：" + count);
    }

}
