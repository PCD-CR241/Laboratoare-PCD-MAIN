public class Pislari extends Thread {
    int[] v;
    boolean invers;

    public Pislari(String nume, int[] v, boolean invers) {
        super(nume);
        this.v = v;
        this.invers = invers;
    }
@Override
    public void run() {
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
                Thread.sleep(20); 
            } catch (InterruptedException e) { return; }
        }
    }
}