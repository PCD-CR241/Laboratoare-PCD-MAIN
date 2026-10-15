import java.util.Arrays;
import java.util.Random;

public class lab1 {

    public static void main(String[] args) {
        int[] mas = new int[100];
        Random random = new Random();

        // Generam un singur tablou comun: valori intre 1 si 100.
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }

        System.out.println("Tabloul comun:");
        System.out.println(Arrays.toString(mas));
        System.out.println();

        // Colegul: conditia 1, de la inceput.
        Conditia1 colegCalcul1 = new Conditia1(mas, 0, 13);
        Conditia1 colegCalcul2 = new Conditia1(mas, 13, 25);

        // Tu: conditia 2, de la sfarsit.
        Conditia2 calcul1 = new Conditia2(mas, 0, 13);
        Conditia2 calcul2 = new Conditia2(mas, 13, 25);

        Thread colegTh1 = new Thread(colegCalcul1, "Colegul - Th1");
        Thread colegTh2 = new Thread(colegCalcul2, "Colegul - Th2");

        Thread th1 = new Thread(calcul1, "Eu - Th1");
        Thread th2 = new Thread(calcul2, "Eu - Th2");

        // Pornim toate cele 4 fire.
        colegTh1.start();
        colegTh2.start();
        th1.start();
        th2.start();

        try {
            // Asteptam terminarea tuturor firelor.
            colegTh1.join();
            colegTh2.join();
            th1.join();
            th2.join();

            int sumaConditia1 =
                    colegCalcul1.getSuma() + colegCalcul2.getSuma();

            int sumaConditia2 =
                    calcul1.getSuma() + calcul2.getSuma();

            System.out.println("\n--- REZULTATE ---");
            System.out.println(
                    "Conditia 1 - de la inceput: " + sumaConditia1
            );
            System.out.println(
                    "Conditia 2 - de la sfarsit: " + sumaConditia2
            );

            // Inlocuiti cu datele voastre.
            String studenti =
                    "\nLucrarea de laborator nr. 1\n"
                            + "Varianta 6\n"
                            + "Student 1: NUME PRENUME\n"
                            + "Student 2: NUME PRENUME\n"
                            + "Grupa: CR-241\n";

            // Executat de firul principal dupa cele 4 join().
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