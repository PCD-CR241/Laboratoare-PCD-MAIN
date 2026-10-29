import java.util.Arrays;
import java.util.Random;

public class lab1 {
    public static void main(String[] args) throws InterruptedException {
        int[] mas = new int[100];
        Random random = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }
        System.out.println("Tabloul comun: " + Arrays.toString(mas));
        System.out.println("Pozitii impare = indici Java: 1, 3, ..., 99.\n");

        Alexandru c1a = new Alexandru(mas, 1);
        Alexandru c1b = new Alexandru(mas, -1);
        Vitalie c2a = new Vitalie(mas, 1);
        Vitalie c2b = new Vitalie(mas, -1);

        Thread t1 = new Thread(c1a, "Alexandru-Th1-Conditia1");
        Thread t2 = new Thread(c1b, "Alexandru-Th2-Conditia2");
        Thread t3 = new Thread(c2a, "Vitalie-Th1-Conditia1");
        Thread t4 = new Thread(c2b, "Vitalie-Th2-Conditia2");

        // Pornim toate cele patru fire inainte de a le astepta.
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\nTotal Alexandru - conditia 1: " + c1a.getSuma());
        System.out.println("Total Alexandru - conditia 2: " + c1b.getSuma());
        System.out.println("Total Vitalie - conditia 1: " + c2a.getSuma());
        System.out.println("Total Vitalie - conditia 2: " + c2b.getSuma());


        String studenti = "Studenti: Alexandru si Vitalie; Grupa: CR-241";
        for (char litera : studenti.toCharArray()) {
            System.out.print(litera);
            System.out.flush();
            Thread.sleep(100);
        }
        System.out.println();
    }
}


class Alexandru implements Runnable {
    private final int[] mas;
    private final int sens;
    private int suma;

    public Alexandru(int[] mas, int sens) {
        this.mas = mas;
        this.sens = sens;
    }

    @Override
    public void run() {

        int inceput = sens == 1 ? 1 : mas.length - 1;
        if (inceput % 2 == 0) {
            inceput--;
        }
        int pas = 2 * sens;


        for (int i = inceput; i >= 0 && i < mas.length; i += 4 * pas) {
            int j = i + pas;
            if (j < 0 || j >= mas.length) {
                break;
            }
            int p1 = mas[i] * mas[j];
            int k = i + 2 * pas;
            int l = i + 3 * pas;
            if (l >= 0 && l < mas.length) {
                int p2 = mas[k] * mas[l];
                int s = p1 + p2;
                suma += s;
                System.out.printf("%s: [%d,%d,%d,%d] P1 = %d * %d = %d; P2 = %d * %d = %d; S = %d + %d = %d%n",
                        Thread.currentThread().getName(), i, j, k, l,
                        mas[i], mas[j], p1, mas[k], mas[l], p2, p1, p2, s);
            } else {
                suma += p1;
                System.out.printf("%s: [%d,%d] produs ramas = %d * %d = %d%n",
                        Thread.currentThread().getName(), i, j, mas[i], mas[j], p1);
            }
        }
    }

    public int getSuma() {
        return suma;
    }
}


class Vitalie implements Runnable {
    private final int[] mas;
    private final int sens;
    private int suma;

    public Vitalie(int[] mas, int sens) {
        this.mas = mas;
        this.sens = sens;
    }

    @Override
    public void run() {

        int i = sens == 1 ? 0 : mas.length - 1;
        int[] pozitii = new int[4];
        int k = 0;
        while (i >= 0 && i < mas.length) {
            if (i % 2 != 0) {
                pozitii[k++] = i;
                if (k == 4) {
                    int p1 = mas[pozitii[0]] * mas[pozitii[1]];
                    int p2 = mas[pozitii[2]] * mas[pozitii[3]];
                    int s = p1 + p2;
                    suma += s;
                    System.out.printf("%s: %s P1 = %d * %d = %d; P2 = %d * %d = %d; S = %d + %d = %d%n",
                            Thread.currentThread().getName(), Arrays.toString(pozitii),
                            mas[pozitii[0]], mas[pozitii[1]], p1,
                            mas[pozitii[2]], mas[pozitii[3]], p2, p1, p2, s);
                    k = 0;
                }
            }
            i += sens;
        }


        if (k == 2) {
            int produs = mas[pozitii[0]] * mas[pozitii[1]];
            suma += produs;
            System.out.printf("%s: [%d,%d] produs ramas = %d * %d = %d%n",
                    Thread.currentThread().getName(), pozitii[0], pozitii[1],
                    mas[pozitii[0]], mas[pozitii[1]], produs);
        }
    }

    public int getSuma() {
        return suma;
    }
}
