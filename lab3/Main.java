
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random(10);

        for (int i = 0; i < Taran.numere.length; i++) {
            Taran.numere[i] = random.nextInt(101) + 15;
        }

        Taran.Th1 th1 = new Taran.Th1();
        Taran.Th2 th2 = new Taran.Th2();
        rihter.Th3 th3 = new rihter.Th3();
        rihter.Th4 th4 = new rihter.Th4();

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        Thread[] fire = {th1, th2, th3, th4};

        for (Thread fir : fire) {
            while (fir.getState() != Thread.State.TIMED_WAITING) {
                Thread.sleep(10);
            }
        }

        th2.interrupt();
        th2.join();

        th4.interrupt();
        th4.join();

        th1.interrupt();
        th1.join();

        th3.interrupt();
        th3.join();

        System.out.println("Toate cele 4 fire au terminat executia.");
    }
}
