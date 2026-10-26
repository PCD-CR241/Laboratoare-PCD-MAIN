import java.util.Arrays;
import java.util.Random;

// Alexandru: colecteaza cate 4 elemente, apoi calculeaza P1 + P2.
class Conditia1 implements Runnable {
    private final int[] mas;
    private final int from, to, step;

    public Conditia1(int[] mas, int from, int to, int step) {
        this.mas = mas;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();

        int[] valori = new int[4];
        int k = 0;

        for (int i = from; i != to + step; i += step) {
            // Pozitii impare: 1, 3, 5...
            if ((i + 1) % 2 != 0) {
                valori[k] = mas[i];
                k++;

                if (k == 4) {
                    int p1 = valori[0] * valori[1];
                    int p2 = valori[2] * valori[3];
                    int s = p1 + p2;

                    Afisare.scrie(true,
                            nume
                                    + ": (" + valori[0] + "*" + valori[1]
                                    + ") + (" + valori[2] + "*" + valori[3]
                                    + ") | P1=" + p1
                                    + "  P2=" + p2
                                    + "  S=P1+P2=" + s
                    );

                    k = 0;
                }
            }
        }
    }
}

// Vitalie: formeaza produsele pe rand, fara un tablou auxiliar.
class Conditia2 implements Runnable {
    private final int[] mas;
    private final int from, to, step;

    public Conditia2(int[] mas, int from, int to, int step) {
        this.mas = mas;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();

        int primulNumar = 0;
        int p1 = 0;

        boolean avemNumar = false;
        boolean avemProdus = false;

        for (int i = from; i != to + step; i += step) {
            // Ignoram pozitiile pare.
            if ((i + 1) % 2 == 0) {
                continue;
            }

            if (!avemNumar) {
                primulNumar = mas[i];
                avemNumar = true;
            } else {
                int produs = primulNumar * mas[i];
                avemNumar = false;

                if (!avemProdus) {
                    // Memoram primul produs.
                    p1 = produs;
                    avemProdus = true;
                } else {
                    // Adunam primul produs cu al doilea.
                    int p2 = produs;
                    int s = p1 + p2;

                    Afisare.scrie(false,
                            nume
                                    + ": P1=" + p1
                                    + "  P2=" + p2
                                    + "  S=P1+P2=" + s
                    );

                    avemProdus = false;
                }
            }
        }
    }
}

// Alternare Alexandru -> Vitalie, fara semafor.
// Fiecare student afiseaza 12 rezultate.
class Afisare {
    private static boolean randulLuiAlexandru = true;

    public static synchronized void scrie(
            boolean esteAlexandru, String mesaj) {

        boolean intrerupt = false;

        while (randulLuiAlexandru != esteAlexandru) {
            try {
                Afisare.class.wait();
            } catch (InterruptedException e) {
                intrerupt = true;
            }
        }

        System.out.println(mesaj);

        randulLuiAlexandru = !randulLuiAlexandru;
        Afisare.class.notifyAll();

        if (intrerupt) {
            Thread.currentThread().interrupt();
        }
    }
}

public class lab1 {
    public static void main(String[] args)
            throws InterruptedException {

        int[] mas = new int[100];
        Random rnd = new Random();

        for (int i = 0; i < mas.length; i++) {
            mas[i] = rnd.nextInt(100) + 1;
        }
        System.out.println("Tabloul comun:");
        System.out.println(Arrays.toString(mas));
        System.out.println();

        // Impartim intervalele la granita dintre grupuri.
        // Fiecare fir calculeaza 6 sume de cate doua produse.

        // Alexandru: conditia 1, de la inceput.
        Thread th1 = new Thread(
                new Conditia1(mas, 0, 47, 1),
                "Alexandru - Th1"
        );

        Thread th2 = new Thread(
                new Conditia1(mas, 48, 99, 1),
                "Alexandru - Th2"
        );

        // Vitalie: conditia 2, de la sfarsit.
        Thread th3 = new Thread(
                new Conditia2(mas, 99, 52, -1),
                "Vitalie - Th1"
        );

        Thread th4 = new Thread(
                new Conditia2(mas, 51, 0, -1),
                "Vitalie - Th2"
        );

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        // Asteptam terminarea tuturor firelor.
        th1.join();
        th2.join();
        th3.join();
        th4.join();

        // Exista 50 de elemente pe pozitii impare:
        // 12 grupuri de 4 elemente si 2 elemente ramase.
        System.out.println("\n--- ELEMENTE RAMASE ---");

        System.out.println(
                "Conditia 1: pozitiile 97 si 99"
                        + " | Produs ramas: " + mas[96] + "*" + mas[98]
                        + "=" + (mas[96] * mas[98])
                        + " | Nu exista al doilea produs pentru suma."
        );

        System.out.println(
                "Conditia 2: pozitiile 3 si 1"
                        + " | Produs ramas: " + mas[2] + "*" + mas[0]
                        + "=" + (mas[2] * mas[0])
                        + " | Nu exista al doilea produs pentru suma."
        );

        String info =
                "\nLucrarea de laborator nr. 1\n"
                        + "Varianta 6\n"
                        + "Realizata de: Alexandru si Vitalie\n"
                        + "Grupa: CR-241\n";

        for (char c : info.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            Thread.sleep(100);
        }
    }
}
