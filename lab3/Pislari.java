public class Pislari extends Thread {
    int[] v;
    boolean invers;
    String text;
    Thread asteapta;

    public Pislari(String nume, int[] v, boolean invers, String text, Thread asteapta) {
        super(nume);
        this.v = v;
        this.invers = invers;
        this.text = text;
        this.asteapta = asteapta;
    }

    @Override
    public void run() {
        System.out.println("Porneste " + getName());
        calcul();

        // Sincronizare: asteptam sa se termine firul precedent
        if (asteapta != null) {
            try {
                asteapta.join();
            } catch (InterruptedException e) { }
        }

        afiseazaLent(text);
    }

    void calcul() {
        int i = 0, pas = 2;
        if (invers) {
            i = v.length - 1;
            if (i % 2 == 1) i--;
            pas = -2;
        }
        while (i + 3 * pas >= 0 && i + 3 * pas < v.length) {
            int a = i, b = i + pas, c = i + 2 * pas, d = i + 3 * pas;
            int p1 = v[a] * v[b];
            int p2 = v[c] * v[d];
            System.out.println(getName() + ": (" + v[a] + "*" + v[b] + ") - (" + v[c] + "*" + v[d] + ") = "
                    + p1 + " - " + p2 + " = " + (p1 - p2) + "   poz: " + a + "," + b + "," + c + "," + d);
            i += 4 * pas;
            try {
                sleep(20);
            } catch (InterruptedException e) { }
        }
    }

    void afiseazaLent(String s) {
        System.out.print("\n" + getName() + ": ");
        for (int k = 0; k < s.length(); k++) {
            System.out.print(s.charAt(k));
            try {
                sleep(100);
            } catch (InterruptedException e) { }
        }
        System.out.println();
    }
}
