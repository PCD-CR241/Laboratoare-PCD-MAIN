
class Sarcina implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class Lab2Mediu {

    static Thread creeazaFir(ThreadGroup grup, String nume, int prioritate) {
        Thread t = new Thread(grup, new Sarcina(), nume);
        t.setPriority(prioritate);
        t.start();
        return t;
    }

    public static void main(String[] args) {
        ThreadGroup grupMain = Thread.currentThread().getThreadGroup();

        ThreadGroup g7 = new ThreadGroup(grupMain, "G7");
        ThreadGroup g3 = new ThreadGroup(g7, "G3");
        ThreadGroup g2 = new ThreadGroup(grupMain, "G2");

        creeazaFir(g3, "Tha", 5);
        creeazaFir(g3, "Thb", 2);
        creeazaFir(g3, "Thc", 5);
        creeazaFir(g3, "Thd", 2);

        creeazaFir(g7, "ThA", 6);
        creeazaFir(g7, "ThB", 5);

        creeazaFir(grupMain, "Th2", 2);

        creeazaFir(g2, "Th1", 6);
        creeazaFir(g2, "Th2", 2);
        creeazaFir(g2, "Th3", 2);

        Thread[] lista = new Thread[grupMain.activeCount()];
        int n = grupMain.enumerate(lista, true);

        for (int i = 0; i < n; i++) {
            if (lista[i] == Thread.currentThread()) continue;
            System.out.println("Thread #" + i
                    + " | nume: " + lista[i].getName()
                    + " | grupa: " + lista[i].getThreadGroup().getName()
                    + " | prioritate: " + lista[i].getPriority());
        }

        System.out.println();
        grupMain.list();
    }
}