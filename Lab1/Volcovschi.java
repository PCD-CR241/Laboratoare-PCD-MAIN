public class Volcovschi implements Runnable {

    private int[] mas;
    private boolean dePeInceput; 

    public Volcovschi(int[] mas, boolean dePeInceput) {
        this.mas = mas;
        this.dePeInceput = dePeInceput;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        int n = mas.length;

        int prima = dePeInceput ? 0 : ((n - 1) % 2 == 0 ? n - 1 : n - 2);
        int pas = dePeInceput ? 2 : -2;

        for (int i = prima; i + 3 * pas >= 0 && i + 3 * pas < n; i += 4 * pas) {
            int a = i, b = i + pas, c = i + 2 * pas, d = i + 3 * pas;

            int p1 = mas[a] * mas[b];
            int p2 = mas[c] * mas[d];
            int dif = p1 - p2; //diferenta produselor

            System.out.println(nume + ": (" + mas[a] + "*" + mas[b] + ") - ("
                    + mas[c] + "*" + mas[d] + ") = "
                    + p1 + " - " + p2 + " = " + dif
                    + "   poz: " + a + "," + b + "," + c + "," + d);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
