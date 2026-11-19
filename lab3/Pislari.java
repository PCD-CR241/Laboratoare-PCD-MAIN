// Membrul 1 - Pislari Andreea: firele Th1 si Th2
// Metodele de sincronizare ale clasei Thread folosite aici: interrupt() si join()
public class Pislari extends Thread {
    int[] v;
    boolean invers;               
    String text;                  
    boolean semnalPrimit = false; 

    public Pislari(String nume, int[] v, boolean invers, String text) {
        super(nume);
        this.v = v;
        this.invers = invers;
        this.text = text;
    }

    @Override
    public void run() {
        System.out.println("Porneste " + getName());
        calcul();

        if (!invers) {
            Main.th2.interrupt();
            try {
                Main.th4.join();
            } catch (InterruptedException e) { }
            afiseazaLent(text);   // prenumele
        } else {
            
            if (!semnalPrimit) {
                try {
                    sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    semnalPrimit = true;
                }
            }
         
            while (Main.th3.getPriority() != Thread.MAX_PRIORITY
                    || Main.th4.getPriority() != Thread.MAX_PRIORITY) {
                try {
                   sleep(10);
                } catch (InterruptedException e) { }
            }
            afiseazaLent(text);   // numele
        }
    }

    void calcul() {
        int i = 0, pas = 2;
        if (invers) {
            i = v.length - 1;
            if (i % 2 == 1) i--;   // ultima pozitie para
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
            } catch (InterruptedException e) {
               
                semnalPrimit = true;
            }
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