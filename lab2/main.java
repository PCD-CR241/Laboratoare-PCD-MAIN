
import java.util.concurrent.CountDownLatch;

public class main {
    static CountDownLatch pregatite = new CountDownLatch(10);
    static CountDownLatch finalizare = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup grupaMain = Thread.currentThread().getThreadGroup();
        Thread[] fire = new Thread[10];
        int i = 0;

        fire[i++] = creeaza(grupaMain, "Th2", 4);

        ThreadGroup g7 = new ThreadGroup(grupaMain, "G7");
        ThreadGroup g3 = new ThreadGroup(g7, "G3");
        ThreadGroup g2 = new ThreadGroup(grupaMain, "G2");

        fire[i++] = creeaza(g3, "Tha", 7);
        fire[i++] = creeaza(g3, "Thb", 4);
        fire[i++] = creeaza(g3, "Thc", 7);
        fire[i++] = creeaza(g3, "Thd", 4);

        fire[i++] = creeaza(g7, "ThA", 8);
        fire[i++] = creeaza(g7, "ThB", 7);

        fire[i++] = creeaza(g2, "Th1", 8);
        fire[i++] = creeaza(g2, "Th2", 4);
        fire[i++] = creeaza(g2, "Th3", 4);

        for (Thread fir : fire) {
            fir.start();
        }

        pregatite.await();

        System.out.println("NIVEL MEDIU - VARIANTA 10 (+1 LA PRIORITATI)");
        afiseaza(grupaMain, "");
        System.out.println("Total thread-uri create: " + fire.length);

        finalizare.countDown();

        for (Thread fir : fire) {
            fir.join();
        }
    }

    static Thread creeaza(ThreadGroup grupa, String nume, int prioritate) {
        Thread fir = new Thread(grupa, () -> {
            pregatite.countDown();

            try {
                finalizare.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, nume);

        fir.setPriority(prioritate);
        return fir;
    }

    static void afiseaza(ThreadGroup grupa, String spatiu) {
        System.out.println(spatiu + "Grup: " + grupa.getName());

        Thread[] fire = new Thread[Math.max(20, grupa.activeCount() * 2)];
        int nrFire = grupa.enumerate(fire, false);

        for (int i = 0; i < nrFire; i++) {
            if (fire[i] != Thread.currentThread()) {
                System.out.println(spatiu + "  " + fire[i].getName()
                        + " | grupa: " + grupa.getName()
                        + " | prioritate: " + fire[i].getPriority());
            }
        }

        ThreadGroup[] subgrupe = new ThreadGroup[
                Math.max(10, grupa.activeGroupCount() * 2)];

        int nrGrupe = grupa.enumerate(subgrupe, false);

        for (int i = 0; i < nrGrupe; i++) {
            afiseaza(subgrupe[i], spatiu + "  ");
        }
    }
}
