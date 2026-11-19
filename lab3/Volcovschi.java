public class Volcovschi implements Runnable {

    private int inceput, sfarsit;
    private boolean dePeInceput;
    private String text;
    private Thread asteapta;

    public Volcovschi(int inceput, int sfarsit, boolean dePeInceput, String text, Thread asteapta) {
        this.inceput = inceput;
        this.sfarsit = sfarsit;
        this.dePeInceput = dePeInceput;
        this.text = text;
        this.asteapta = asteapta;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        System.out.println("Porneste " + nume);

        // Parcurgerea intervalului, cate 15 numere pe rand
        int n = sfarsit - inceput + 1;
        String linie = "";
        for (int j = 0; j < n; j++) {
            int i = dePeInceput ? inceput + j : sfarsit - j;
            linie += i + " ";
            if ((j + 1) % 15 == 0 || j == n - 1) {
                System.out.println(nume + ": " + linie);
                linie = "";
            }
        }

        // Sincronizare: asteptam sa se termine firul precedent
        try {
            asteapta.join();
        } catch (InterruptedException e) { }

        afiseazaLent(nume, text);
    }

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
