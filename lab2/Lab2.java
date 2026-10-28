public class Lab2 {
    public static void main(String[] args) {
        System.out.println("========== NIVEL MEDIU ==========");
        NivelMediu.ruleaza();

        System.out.println();
        System.out.println("========== NIVEL AVANSAT ==========");
        NivelAvansat.ruleaza();
    }
}

class NivelMediu {

    static void ruleaza() {
        ThreadGroup main = Thread.currentThread().getThreadGroup();

        ThreadGroup g7 = new ThreadGroup(main, "G7");
        ThreadGroup g3 = new ThreadGroup(g7, "G3");
        ThreadGroup g2 = new ThreadGroup(main, "G2");

        Thread t = null;

        t = new Thread(g3, new Sarcina(), "Tha");
        t.setPriority(5);
        t.start();

        t = new Thread(g3, new Sarcina(), "Thb");
        t.setPriority(2);
        t.start();

        t = new Thread(g3, new Sarcina(), "Thc");
        t.setPriority(5);
        t.start();

        t = new Thread(g3, new Sarcina(), "Thd");
        t.setPriority(2);
        t.start();

        t = new Thread(g7, new Sarcina(), "ThA");
        t.setPriority(6);
        t.start();

        t = new Thread(g7, new Sarcina(), "ThB");
        t.setPriority(5);
        t.start();

        t = new Thread(main, new Sarcina(), "Th2");
        t.setPriority(2);
        t.start();

        t = new Thread(g2, new Sarcina(), "Th1");
        t.setPriority(6);
        t.start();

        t = new Thread(g2, new Sarcina(), "Th2");
        t.setPriority(2);
        t.start();

        t = new Thread(g2, new Sarcina(), "Th3");
        t.setPriority(2);
        t.start();

        int numThreads = main.activeCount();
        Thread[] listOfThreads = new Thread[numThreads];
        main.enumerate(listOfThreads);
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread #" + i
                    + " | nume: " + listOfThreads[i].getName()
                    + " | grupa: " + listOfThreads[i].getThreadGroup().getName()
                    + " | prioritate: " + listOfThreads[i].getPriority());
        }
        System.out.println();
        main.list();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Sarcina implements Runnable {
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class NivelAvansat {

    static void ruleaza() {
        ThreadGroup main = Thread.currentThread().getThreadGroup();
        ThreadGroup g7 = new ThreadGroup(main, "G7");
        ThreadGroup g3 = new ThreadGroup(g7, "G3");
        ThreadGroup g2 = new ThreadGroup(main, "G2");

        System.out.println("Starting all threads:");
        new Fir(g3, "Tha", 5);
        new Fir(g3, "Thb", 2);
        new Fir(g3, "Thc", 5);
        new Fir(g3, "Thd", 2);

        new Fir(g7, "ThA", 6);
        new Fir(g7, "ThB", 5);
        new Fir(main, "Th2", 2);

        new Fir(g2, "Th1", 6);
        new Fir(g2, "Th2", 2);
        new Fir(g2, "Th3", 2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All threads finished");
    }
}

class Fir extends Thread {

    public Fir(ThreadGroup grup, String nume, int prioritate) {
        super(grup, nume);
        setPriority(prioritate);
        start();
    }

    public void run() {
        System.out.println("Firul " + getName()
                + " | grupa: " + getThreadGroup().getName()
                + " | prioritate: " + getPriority());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}