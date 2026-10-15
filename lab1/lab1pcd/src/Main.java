import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int[] mas = new int[100];
        Random random = new Random();

        // Generam 100 de numere intre 1 si 100.
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }

        System.out.println("Tabloul:");
        System.out.println(Arrays.toString(mas));

        // 50 de pozitii impare formeaza 25 de perechi.
        // Primul fir calculeaza 13 perechi, al doilea 12.
        CalculProduse calcul1 = new CalculProduse(mas, 0, 13);
        CalculProduse calcul2 = new CalculProduse(mas, 13, 25);

        Thread th1 = new Thread(calcul1, "Th1");
        Thread th2 = new Thread(calcul2, "Th2");

        th1.start();
        th2.start();

        try {
            // Asteptam terminarea ambelor fire.
            th1.join();
            th2.join();

            long sumaTotala = calcul1.getSuma() + calcul2.getSuma();

            System.out.println("\nSuma Th1: " + calcul1.getSuma());
            System.out.println("Suma Th2: " + calcul2.getSuma());
            System.out.println("Suma totala: " + sumaTotala);

            String studenti =
                    "\nStudent 1: Cirnat Alexandru\n"
                            + "Student 2: Elisavetchi Vitalie\n"
                            + "Grupa: CR-241\n";

            // Firul principal afiseaza cate un caracter la 100 ms.
            for (char caracter : studenti.toCharArray()) {
                System.out.print(caracter);
                System.out.flush();
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Firul principal a fost intrerupt.");
        }
    }
}

class CalculProduse implements Runnable {

    private final int[] mas;
    private final int perecheStart;
    private final int perecheFinal;
    private long suma;

    public CalculProduse(int[] mas, int perecheStart, int perecheFinal) {
        this.mas = mas;
        this.perecheStart = perecheStart;
        this.perecheFinal = perecheFinal;
    }

    @Override
    public void run() {
        suma = 0;

        for (int p = perecheStart; p < perecheFinal; p++) {
            // Indicii 98, 94, 90... corespund primului
            // element din fiecare pereche, de la sfarsit.
            int i = 98 - 4 * p;
            int j = i - 2;

            long produs = (long) mas[i] * mas[j];
            suma += produs;

            System.out.printf(
                    "%s | pozitii %d si %d | %d * %d = %d%n",
                    Thread.currentThread().getName(),
                    i + 1,
                    j + 1,
                    mas[i],
                    mas[j],
                    produs
            );
        }
    }

    public long getSuma() {
        return suma;
    }
}