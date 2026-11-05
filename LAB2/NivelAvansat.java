// Lab 2, varianta 7 - NIVEL AVANSAT
// Main{ ThA(3), Th11(3), Th22(3), G2{Th1(5), Th2(3), Th33(7)}, G1{G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)}} }
// Conditie: SUPRASCRIEM run() - fiecare fir isi afiseaza numele, grupul si prioritatea.

public class NivelAvansat {

    private Jurnal jurnal;

    public NivelAvansat(Jurnal jurnal) {
        this.jurnal = jurnal;
    }

    // Firul nostru: extinde Thread si isi suprascrie metoda run()
    class FirAvansat extends Thread {

        FirAvansat(ThreadGroup grup, String nume, int prioritate) {
            super(grup, nume);          // firul intra in grup chiar la creare
            setPriority(prioritate);    // prioritatea se pune INAINTE de start()
        }

        @Override
        public void run() {
            jurnal.mesaj("Sunt firul " + getName()
                    + ", din grupul " + getThreadGroup().getName()
                    + ", cu prioritatea " + getPriority());
            try {
                Thread.sleep(3000);     // ramane activ putin, ca sa apara la list()
            } catch (InterruptedException e) {
                jurnal.mesaj(getName() + " a fost intrerupt");
            }
        }
    }

    // creeaza si porneste un grup intreg de fire, dintr-un singur apel
    private void pornesteFire(ThreadGroup grup, String[] nume, int[] prioritati) {
        for (int i = 0; i < nume.length; i++) {
            new FirAvansat(grup, nume[i], prioritati[i]).start();
        }
    }

    // parcurge recursiv grupul: firele lui, apoi subgrupurile, cu indentare
    private void parcurgeGrup(ThreadGroup grup, String indent) {
        jurnal.mesaj(indent + "Grupul " + grup.getName() + " (maxpri=" + grup.getMaxPriority() + ")");

        Thread[] fire = new Thread[grup.activeCount()];
        int n = grup.enumerate(fire, false);              // false = doar firele DIRECTE ale grupului
        for (int i = 0; i < n; i++) {
            jurnal.mesaj(indent + "    fir " + fire[i].getName() + ", prioritate " + fire[i].getPriority());
        }

        ThreadGroup[] subgrupuri = new ThreadGroup[grup.activeGroupCount()];
        int m = grup.enumerate(subgrupuri, false);        // doar subgrupurile directe
        for (int i = 0; i < m; i++) {
            if (subgrupuri[i].activeCount() > 0) {        // doar grupurile care mai au fire active
                parcurgeGrup(subgrupuri[i], indent + "    ");
            }
        }
    }

    public ThreadGroup construieste() throws InterruptedException {

        // ===== PASUL 1: grupul sys (etalonul) =====
        ThreadGroup sys = Thread.currentThread().getThreadGroup();
        jurnal.mesaj("[1] sys.list() - etalonul: " + sys.getName() + ", maxpri=" + sys.getMaxPriority());
        jurnal.lista(sys);

        sys.setMaxPriority(Thread.MAX_PRIORITY - 1);
        Thread curr = Thread.currentThread();
        curr.setPriority(curr.getPriority() + 1);
        jurnal.mesaj("[2] sys.list() - dupa modificare: maxpri=" + sys.getMaxPriority()
                + ", firul " + curr.getName() + " are prioritatea " + curr.getPriority());
        jurnal.lista(sys);

        // ===== PASUL 2: firele singure =====
        jurnal.mesaj("[3] Pornesc firele singure din main:");
        pornesteFire(sys, new String[]{"ThA", "Th11", "Th22"}, new int[]{3, 3, 3});
        Thread.sleep(200);   // las firele sa se prezinte inainte de pasul urmator
        jurnal.mesaj("[3] sys.list() - dupa firele singure:");
        jurnal.lista(sys);

        // ===== PASUL 3: grupul G2 =====
        ThreadGroup g2 = new ThreadGroup(sys, "G2");
        jurnal.mesaj("[4] Pornesc grupul G2:");
        pornesteFire(g2, new String[]{"Th1", "Th2", "Th33"}, new int[]{5, 3, 7});
        Thread.sleep(200);
        jurnal.mesaj("[4] g2.list() - grupul G2:");
        jurnal.lista(g2);

        // ===== PASUL 4: G1 cu subgrupul G3 =====
        ThreadGroup g1 = new ThreadGroup(sys, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        jurnal.mesaj("[5] Pornesc grupul G3 (din G1):");
        pornesteFire(g3, new String[]{"Thaa", "Thbb", "Thcc", "Thdd"}, new int[]{2, 3, 8, 3});
        Thread.sleep(200);
        jurnal.mesaj("[5] g1.list() - grupul G1, cu subgrupul G3:");
        jurnal.lista(g1);

        // ===== PASUL 5: toata structura, parcursa recursiv =====
        jurnal.mesaj("[6] sys.list() - structura completa:");
        jurnal.lista(sys);
        jurnal.mesaj("[7] Aceeasi structura, parcursa recursiv (nume, grup, prioritate):");
        parcurgeGrup(sys, "");
        return sys;
    }

    public static void main(String[] args) throws InterruptedException {
        NivelAvansat program = new NivelAvansat(new Jurnal() {
            public void mesaj(String text) { System.out.println(text); }
            public void lista(ThreadGroup grup) { grup.list(); }
        });
        program.construieste();
    }
}
