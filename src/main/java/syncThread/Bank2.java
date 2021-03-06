package syncThread;

public class Bank2 {
    //使用局部变量实现线程同步
    /**
     * 如果使用ThreadLocal管理变量，则每一个使用该变量的线程都获得该变量的副本，副本之间相互独立，
     * 这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
     * 现在明白了，原来每个线程运行的都是一个副本，也就是说存钱和取钱是两个账户，只是名字相同而已
     */
    private static ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    //存钱
    public void addMoney(int money) {
        count.set(count.get() + money);
        System.out.println(System.currentTimeMillis() + "存进：" + money);
    }

    //取钱
    public synchronized void subMoney(int money) {
        if (count.get() - money < 0) {
            System.out.println("余额不足");
            return;
        }
        count.set(count.get() - money);
        System.out.println(System.currentTimeMillis() + "取出：" + money);
    }

    //查询
    public void lookMoney() {
        System.out.println("账户余额：" + count.get());
    }

}
