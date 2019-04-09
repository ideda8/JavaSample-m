package threadClassDemo;

public class DisplayMessage implements Runnable{
    private String message;

    private Integer i = 0;

    public DisplayMessage(String message) {
        this.message = message;
    }

    public void run() {
        while(true) {
            System.out.println(message + " " + i++);
        }
    }
}
