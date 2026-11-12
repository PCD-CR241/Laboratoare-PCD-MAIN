
public class Consumator extends Thread {
    private final Depozit depozit;

    public Consumator(Depozit depozit, String nume) {
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                depozit.get(getName());
            }
            System.out.println(getName() + " a consumat toate cele 5 obiecte si a terminat.");
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
