
import java.util.Random;

class Th1 implements Runnable {
    private final int[] mas;

    public Th1(int[] mas) {
        this.mas = mas;
    }

    @Override
    public void run() {
        int suma = 0;

        for (int i = 0; i + 3 < mas.length; i += 4) {
            int p1 = mas[i] * mas[i + 1];
            int p2 = mas[i + 2] * mas[i + 3];

            suma += p1 + p2;

            System.out.printf(
                    "Th1: [%d,%d] P1=%d; [%d,%d] P2=%d; Suma=%d%n",
                    i, i + 1, p1, i + 2, i + 3, p2, p1 + p2
            );
        }

        System.out.println("Th1: Suma totala = " + suma);
    }
}

class Th2 implements Runnable {
    private final int[] mas;

    public Th2(int[] mas) {
        this.mas = mas;
    }

    @Override
    public void run() {
        int diferentaTotala = 0;

        for (int i = 0; i + 3 < mas.length; i += 4) {
            int p1 = mas[i] * mas[i + 1];
            int p2 = mas[i + 2] * mas[i + 3];

            diferentaTotala += p1 - p2;

            System.out.printf(
                    "Th2: P1=%d; P2=%d; Diferenta=%d%n",
                    p1, p2, p1 - p2
            );
        }

        System.out.println(
                "Th2: Diferenta totala = " + diferentaTotala
        );
    }
}

class Th3 implements Runnable {
    private final int[] mas;

    public Th3(int[] mas) {
        this.mas = mas;
    }

    @Override
    public void run() {
        int suma = 0;

        for (int i = 1; i + 3 < mas.length; i += 4) {
            int p1 = mas[i] * mas[i + 1];
            int p2 = mas[i + 2] * mas[i + 3];

            suma += p1 + p2;

            System.out.printf(
                    "Th3: P1=%d; P2=%d; Suma=%d%n",
                    p1, p2, p1 + p2
            );
        }

        System.out.println("Th3: Suma totala = " + suma);
    }
}

class Th4 implements Runnable {
    private final int[] mas;

    public Th4(int[] mas) {
        this.mas = mas;
    }

    @Override
    public void run() {
        int diferentaTotala = 0;

        for (int i = 1; i + 3 < mas.length; i += 4) {
            int p1 = mas[i] * mas[i + 1];
            int p2 = mas[i + 2] * mas[i + 3];

            diferentaTotala += p1 - p2;

            System.out.printf(
                    "Th4: P1=%d; P2=%d; Diferenta=%d%n",
                    p1, p2, p1 - p2
            );
        }

        System.out.println(
                "Th4: Diferenta totala = " + diferentaTotala
        );
    }
}

public class lab1 {
    public static void main(String[] args)
            throws InterruptedException {

        int[] mas = new int[100];
        Random random = new Random();

        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }

        System.out.println("Tabloul mas:");
        for (int valoare : mas) {
            System.out.print(valoare + " ");
        }
        System.out.println("\n");

        Thread th1 = new Thread(new Th1(mas), "Th1");
        Thread th2 = new Thread(new Th2(mas), "Th2");
        Thread th3 = new Thread(new Th3(mas), "Th3");
        Thread th4 = new Thread(new Th4(mas), "Th4");

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        th1.join();
        th2.join();
        th3.join();
        th4.join();

        String studenti =
                "Studenti: Numele Prenumele - Laboratorul de programare paralela.";

        System.out.println("\nInformatii despre studenti:");

        for (int i = 0; i < studenti.length(); i++) {
            System.out.print(studenti.charAt(i));
            Thread.sleep(100);
        }

        System.out.println();
    }
}
