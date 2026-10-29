import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class main {

    public static void main(String[] args) throws InterruptedException {
        executaNivel(false); 
        executaNivel(true);  
    }

    private static void executaNivel(boolean avansat) throws InterruptedException {
        System.out.println("\n=======================================");
        System.out.println(avansat ? "NIVELUL AVANSAT" : "NIVELUL MEDIU");
        System.out.println("Varianta 10 + un thread Th4(5)");
        System.out.println("=======================================");

        CountDownLatch pornite = new CountDownLatch(11);
        CountDownLatch terminare = new CountDownLatch(1);
        List<Thread> fire = new ArrayList<>();

        ThreadGroup main = new ThreadGroup("Main");

        Thread th2 = creeazaFir(main, "Th2", 3, avansat, pornite, terminare);
        Thread th4 = creeazaFir(main, "Th4", 5, avansat, pornite, terminare); // adaugat
        fire.add(th2);
        fire.add(th4);

        ThreadGroup g7 = new ThreadGroup(main, "G7");
        ThreadGroup g3 = new ThreadGroup(g7, "G3");

        fire.add(creeazaFir(g3, "Tha", 6, avansat, pornite, terminare));
        fire.add(creeazaFir(g3, "Thb", 3, avansat, pornite, terminare));
        fire.add(creeazaFir(g3, "Thc", 6, avansat, pornite, terminare));
        fire.add(creeazaFir(g3, "Thd", 3, avansat, pornite, terminare));

        fire.add(creeazaFir(g7, "ThA", 7, avansat, pornite, terminare));
        fire.add(creeazaFir(g7, "ThB", 6, avansat, pornite, terminare));

        ThreadGroup g2 = new ThreadGroup(main, "G2");
        fire.add(creeazaFir(g2, "Th1", 7, avansat, pornite, terminare));
        fire.add(creeazaFir(g2, "Th2", 3, avansat, pornite, terminare));
        fire.add(creeazaFir(g2, "Th3", 3, avansat, pornite, terminare));

        for (Thread fir : fire) {
            fir.start();
        }
        pornite.await();

        System.out.println("\nSTRUCTURA GRUPELOR:");
        afiseazaGrupa(main, 0);
        System.out.println("Total fire create: " + fire.size());

        terminare.countDown();
        for (Thread fir : fire) {
            fir.join();
        }
        System.out.println("Nivel finalizat.\n");
    }

    private static Thread creeazaFir(ThreadGroup grupa, String nume,
                                    int prioritate, boolean avansat,
                                    CountDownLatch pornite,
                                    CountDownLatch terminare) {
        Thread fir;

        if (avansat) {
            fir = new Avansat(grupa, nume, pornite, terminare);
        } else {
            Runnable sarcina = () -> {
                pornite.countDown();
                asteapta(terminare);
            };
            fir = new Thread(grupa, sarcina, nume);
        }

        fir.setPriority(prioritate);
        return fir;
    }

    private static void afiseazaGrupa(ThreadGroup grupa, int nivel) {
        String spatii = "    ".repeat(nivel);
        System.out.println(spatii + "Grupa: " + grupa.getName());

        Thread[] fire = new Thread[32];
        int nrFire = grupa.enumerate(fire, false);
        for (int i = 0; i < nrFire; i++) {
            Thread fir = fire[i];
            System.out.println(spatii + "  " + fir.getName()
                    + " | grupa: " + grupa.getName()
                    + " | prioritate: " + fir.getPriority());
        }

        ThreadGroup[] subgrupe = new ThreadGroup[16];
        int nrSubgrupe = grupa.enumerate(subgrupe, false);
        for (int i = 0; i < nrSubgrupe; i++) {
            afiseazaGrupa(subgrupe[i], nivel + 1);
        }
    }

    private static void asteapta(CountDownLatch semnal) {
        try {
            semnal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Avansat extends Thread {
        private final CountDownLatch pornite;
        private final CountDownLatch terminare;

        Avansat(ThreadGroup grupa, String nume,
                   CountDownLatch pornite, CountDownLatch terminare) {
            super(grupa, nume);
            this.pornite = pornite;
            this.terminare = terminare;
        }

        @Override
        public void run() {
            System.out.println("Ruleaza: " + getName()
                    + " | grupa: " + getThreadGroup().getName()
                    + " | prioritate: " + getPriority());
            pornite.countDown();
            asteapta(terminare);
        }
    }
}
