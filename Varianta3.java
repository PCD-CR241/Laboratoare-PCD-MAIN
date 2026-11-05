package Varianta3;

import java.util.ArrayList;
import java.util.List;

/**
 * Laborator 2 - Varianta 3 - Nivel avansat
 *
 * Main{GN{GH{Tha(4), Thb(3), Thc(6), Thd(3)}, ThA(3)},
 *      GM{Th1(2), Th2(3), Th3(3), ThDemon}, Th1(8), Th2(3)}
 */
public class Varianta3 {

    static class Fir extends Thread {

        // prioritatea este mostenita de la firul care creeaza (main)
        public Fir(ThreadGroup group, String name) {
            super(group, name);
        }

        @Override
        public void run() {
            System.out.println("Firul: " + getName()
                    + " | grupul: " + getThreadGroup().getName()
                    + " | prioritatea: " + getPriority()
                    + " | demon: " + isDaemon());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Demon extends Thread {

        public Demon(ThreadGroup group, String name) {
            super(group, name);
            setDaemon(true);
        }

        @Override
        public void run() {
            System.out.println("Firul: " + getName()
                    + " | grupul: " + getThreadGroup().getName()
                    + " | prioritatea: " + getPriority()
                    + " | demon: " + isDaemon());

            while (true) {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static final List<Fir> toate = new ArrayList<>();

    private static Fir creeaza(ThreadGroup g, String nume) {
        Fir f = new Fir(g, nume);
        toate.add(f);
        return f;
    }

    private static void afiseazaLista(ThreadGroup g, int nivel) {

        String indentare = "";

        for (int i = 0; i < nivel; i++) {
            indentare = indentare + "    ";
        }

        String lista = indentare + g.getName() + " list:";

        Thread[] fire = new Thread[g.activeCount() + 5];

        int nf = g.enumerate(fire, false);

        for (int i = 0; i < nf; i++) {

            if (fire[i] == Thread.currentThread()) {
                continue;
            }

            lista = lista + " " + fire[i].getName()
                    + "(" + fire[i].getPriority() + ")"
                    + (fire[i].isDaemon() ? "[demon]" : "");
        }

        System.out.println(lista);

        ThreadGroup[] subgrupe =
                new ThreadGroup[g.activeGroupCount() + 5];

        int ng = g.enumerate(subgrupe, false);

        for (int i = 0; i < ng; i++) {
            afiseazaLista(subgrupe[i], nivel + 1);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread main = Thread.currentThread();
        ThreadGroup grupMain = main.getThreadGroup();

        ThreadGroup gn = new ThreadGroup(grupMain, "GN");
        ThreadGroup gh = new ThreadGroup(gn, "GH");
        ThreadGroup gm = new ThreadGroup(grupMain, "GM");

        int prioritateInitiala = main.getPriority();

        main.setPriority(2);

        main.setPriority(3);
        creeaza(gh, "Thb");
        creeaza(gh, "Thd");
        creeaza(gn, "ThA");
        creeaza(gm, "Th2");
        creeaza(gm, "Th3");
        creeaza(grupMain, "Th2");

        main.setPriority(4);
        creeaza(gh, "Tha");

        main.setPriority(6);
        creeaza(gh, "Thc");

        main.setPriority(8);
        creeaza(grupMain, "Th1");

        main.setPriority(prioritateInitiala);

        Demon demon = new Demon(gm, "ThDemon");

        System.out.println("=== Executia firelor ===");

        demon.start();

        for (Fir f : toate) {
            f.start();
        }

        Thread.sleep(200);

        System.out.println("\n=== Lista firelor pe grupuri ===");
        grupMain.list();

        System.out.println("\n=== Lista personalizata ===");
        afiseazaLista(grupMain, 0);

        for (Fir f : toate) {
            f.join();
        }

        System.out.println("\nToate firele s-au terminat.");

        System.out.println("Demonul mai este viu? "
                + demon.isAlive()
                + " | este demon? " + demon.isDaemon()
                + " (se opreste cand se termina JVM-ul)");
    }
}