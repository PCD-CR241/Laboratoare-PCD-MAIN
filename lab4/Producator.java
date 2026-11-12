
public class Producator extends Thread {
    private final Depozit depozit;

    public Producator(Depozit depozit, String nume) {
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int a = 2 * (int) (Math.random() * 10) + 1;
                int b = 2 * (int) (Math.random() * 10) + 1;
                depozit.put(getName(), a, b);
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
