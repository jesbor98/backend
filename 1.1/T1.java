
public class T1 extends Thread {

    @Override
    public void run() {
        try {
            while(isAlive()) { // executes while Thread is alive
                System.out.println("Tråd 1");
                Thread.sleep(1000); // 1 second between each print of "Tråd 1"
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

