import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // 1. Creăm tabloul de 100 de elemente cu valori aleatoare între 1 și 100 inclusiv
        int[] mas = new int[100];
        Random random = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1; // nextInt(100) dă 0..99, +1 => 1..100
        }

        // 2. Afișăm tabloul generat
        System.out.println("Tabloul mas:");
        for (int i = 0; i < mas.length; i++) {
            System.out.print(mas[i] + " ");
            if ((i + 1) % 20 == 0) {
                System.out.println(); // trecem pe rând nou la fiecare 20 de elemente
            }
        }
        System.out.println();

        // 3. Creăm cele două thread-uri; fiecare primește un obiect Runnable și un nume
        Thread th1 = new Thread(new Conditia1(mas), "Th1");
        Thread th2 = new Thread(new Conditia2(mas), "Th2");

        // 4. Pornim thread-urile; start() creează un fir nou care execută run()
        th1.start();
        th2.start();

        // 5. Thread-ul principal așteaptă terminarea ambelor thread-uri cu join()
        try {
            th1.join();
            th2.join();
        } catch (InterruptedException e) {
            // Dacă thread-ul principal este întrerupt în timpul așteptării,
            // restabilim flag-ul de întrerupere și oprim programul
            Thread.currentThread().interrupt();
            System.out.println("Thread-ul principal a fost intrerupt.");
            return;
        }

        // 6. După terminarea thread-urilor, afișăm textul literă cu literă
        String text = "Lucrarea a fost realizată de: Codrean Chiril, Cucereavîi Valerian, grupa CR-241";
        System.out.println();
        for (int k = 0; k < text.length(); k++) {
            System.out.print(text.charAt(k)); // afișăm un singur caracter
            System.out.flush();               // forțăm afișarea imediată pe ecran
            try {
                Thread.sleep(100);            // pauză de 100 ms între litere
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }
}
