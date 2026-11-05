// Lab 2, varianta 7 - NIVEL MEDIU
// Main{ ThA(3), Th11(3), 2(3), G2{Th1(5), Th2(3), Th33(7)}, G1{G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)}} }
// Conditie: NU suprascriem metoda run() din clasa Thread.

public class NivelMediu {

    // Sarcina comuna a firelor: doar "traiesc" 3 secunde, ca sa apara la list()
    static Runnable asteapta = new Runnable() {
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // Creeaza un fir in grupul dat, ii pune prioritatea si il porneste
    static Thread creeazaFir(ThreadGroup grup, String nume, int prioritate) {
        Thread t = new Thread(grup, asteapta, nume);   // Runnable, deci run() din Thread NU e suprascris
        t.setPriority(prioritate);
        t.start();
        return t;
    }

    public static ThreadGroup construieste(Jurnal j) {

        // ===== PASUL 1: grupul sys (etalonul) =====
        ThreadGroup sys = Thread.currentThread().getThreadGroup();
        j.mesaj("(1) sys.list() - etalonul, grupul sys la pornire:");
        j.lista(sys);

        sys.setMaxPriority(Thread.MAX_PRIORITY - 1);      // maxpri: 10 -> 9
        Thread curr = Thread.currentThread();
        curr.setPriority(curr.getPriority() + 1);         // firul curent: 5 -> 6
        j.mesaj("(2) sys.list() - dupa setMaxPriority(9) si prioritatea firului curent +1:");
        j.lista(sys);


        creeazaFir(sys, "ThA", 3);
        creeazaFir(sys, "Th11", 3);
        creeazaFir(sys, "Th22", 3);
        j.mesaj("(3) sys.list() - dupa crearea firelor singure ThA, Th11, Th22:");
        j.lista(sys);


        ThreadGroup g2 = new ThreadGroup(sys, "G2");
        creeazaFir(g2, "Th1", 5);
        creeazaFir(g2, "Th2", 3);
        creeazaFir(g2, "Th33", 7);
        j.mesaj("(4) g2.list() - grupul G2:");
        j.lista(g2);


        ThreadGroup g1 = new ThreadGroup(sys, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");      // G3 e copilul lui G1
        creeazaFir(g3, "Thaa", 2);
        creeazaFir(g3, "Thbb", 3);
        creeazaFir(g3, "Thcc", 8);
        creeazaFir(g3, "Thdd", 3);
        j.mesaj("(5) g1.list() - grupul G1, cu subgrupul G3:");
        j.lista(g1);


        j.mesaj("(6) sys.list() - structura completa:");
        j.lista(sys);


        Thread[] fire = new Thread[sys.activeCount()];
        int n = sys.enumerate(fire, true);                // true = si din subgrupuri
        j.mesaj("(7) Enumerarea firelor:");
        for (int i = 0; i < n; i++) {
            j.mesaj("Fir: " + fire[i].getName()
                    + " | Grup: " + fire[i].getThreadGroup().getName()
                    + " | Prioritate: " + fire[i].getPriority());
        }
        return sys;
    }

    public static void main(String[] args) {
        construieste(new Jurnal() {
            public void mesaj(String text) { System.out.println(text); }
            public void lista(ThreadGroup grup) { grup.list(); }
        });
    }
}
