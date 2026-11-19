// Membrul 2 - Volcovschi: firele Th3 si Th4
// Metodele de sincronizare ale clasei Thread folosite aici: setPriority() si isAlive()
public class Volcovschi implements Runnable {

    private int inceput, sfarsit;
    private boolean dePeInceput;   
    private String text;           

    public Volcovschi(int inceput, int sfarsit, boolean dePeInceput, String text) {
        this.inceput = inceput;
        this.sfarsit = sfarsit;
        this.dePeInceput = dePeInceput;
        this.text = text;
    }

    @Override
    public void run() {
        Thread eu = Thread.currentThread();
        String nume = eu.getName();
        System.out.println("Porneste " + nume);

        // Sarcina: parcurgerea intervalului, cate 15 numere pe rand
        int i = dePeInceput ? inceput : sfarsit;
        int ultim = dePeInceput ? sfarsit : inceput;
        int pas = dePeInceput ? 1 : -1;
        StringBuilder linie = new StringBuilder();
        int k = 0;
        while (true) {
            linie.append(i).append(' ');
            k++;
            if (k == 15 || i == ultim) {
                System.out.println(nume + ": " + linie);
                linie.setLength(0);
                k = 0;
            }
            if (i == ultim) break;
            i += pas;
        }

        // 1) setPriority(): semnal ca parcurgerea s-a terminat (prioritatea devine 10)
        eu.setPriority(Thread.MAX_PRIORITY);

        // 2) isAlive(): firul asteapta pana termina firul dinaintea lui
        //    Th4 asteapta dupa Th2 (numele), Th3 asteapta dupa Th1 (prenumele)
        Thread asteptat = dePeInceput ? Main.th1 : Main.th2;
        while (asteptat.isAlive()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) { }
        }
        afiseazaLent(nume, text);
    }

    // Afiseaza textul litera cu litera, cate o litera la 100 ms
    private void afiseazaLent(String nume, String s) {
        System.out.print("\n" + nume + ": ");
        for (int k = 0; k < s.length(); k++) {
            System.out.print(s.charAt(k));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
        }
        System.out.println();
    }
}