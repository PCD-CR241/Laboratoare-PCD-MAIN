/*
 * Lucrarea de laborator nr. 2
 * Tema: Gruparea firelor de executie. Modificarea prioritatii.
 *
 * Sarcina (problema pentru nivelul mediu):
 * Creati o structura, conform formulei date in tabelul 1, conform variantei.
 * Enumerati toate firele de executie din grupa principala si subgrupele ce le contine.
 * Afisati informatia despre numele firului de executie, numele grupei din care face parte
 * si prioritatea sa. Prioritatea fiecarui Thread este indicata in paranteze () pentru
 * fiecare fir de executie. Nu suprascrieti metoda run() din clasa Thread.
 *
 * Varianta 9:
 * Main{G6{ThA(3)}, Th1(4), Th2(3), G2{Th1(2), G3{Tha(2), Thb(3), Thc(4), Thd(3)}, Th2(3), Th3(3)}}
 */
public class Thread1 {

    public static void main(String[] args) {
        Runnable sarcina = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        };

        Thread curent = Thread.currentThread();
        curent.setPriority(3);
        ThreadGroup main = curent.getThreadGroup();

        Thread th1 = new Thread(sarcina, "Th1");
        th1.setPriority(4);
        th1.start();
        Thread th2 = new Thread(sarcina, "Th2");
        th2.setPriority(3);
        th2.start();

        ThreadGroup g6 = new ThreadGroup("G6");
        Thread thA = new Thread(g6, sarcina, "ThA");
        thA.setPriority(3);
        thA.start();

        ThreadGroup g2 = new ThreadGroup("G2");
        Thread g2Th1 = new Thread(g2, sarcina, "Th1");
        g2Th1.setPriority(2);
        g2Th1.start();
        Thread g2Th2 = new Thread(g2, sarcina, "Th2");
        g2Th2.setPriority(3);
        g2Th2.start();
        Thread g2Th3 = new Thread(g2, sarcina, "Th3");
        g2Th3.setPriority(3);
        g2Th3.start();

        ThreadGroup g3 = new ThreadGroup(g2, "G3");
        Thread tha = new Thread(g3, sarcina, "Tha");
        tha.setPriority(2);
        tha.start();
        Thread thb = new Thread(g3, sarcina, "Thb");
        thb.setPriority(3);
        thb.start();
        Thread thc = new Thread(g3, sarcina, "Thc");
        thc.setPriority(4);
        thc.start();
        Thread thd = new Thread(g3, sarcina, "Thd");
        thd.setPriority(3);
        thd.start();

        Thread[] lista = new Thread[main.activeCount()];
        int n = main.enumerate(lista);

        System.out.println("Firele de executie din grupa main si din subgrupele ei:");
        for (int i = 0; i < n; i++) {
            if (lista[i] != curent) {
                System.out.println("Firul: " + lista[i].getName()
                        + ", grupa: " + lista[i].getThreadGroup().getName()
                        + ", prioritatea: " + lista[i].getPriority());
            }
        }

        System.out.println();
        System.out.println("Structura grupei main:");
        main.list();
    }
}