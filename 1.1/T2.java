public class T2 implements Runnable{

    public void run() {
        try {
            for (int i = 0; i < 10; i++) { // loop 10 times according to description given
                System.out.println("Tråd 2");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

