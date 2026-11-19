import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int[] v = new int[100];
        Random r = new Random();
        System.out.println("Tabloul:");
        for (int i = 0; i < v.length; i++) {
            v[i] = 10 + r.nextInt(90);
            System.out.print(v[i] + " ");
            if ((i + 1) % 20 == 0) System.out.println();
        }
        System.out.println();

        // Ordinea afisarii textelor: Th2 -> Th4 -> Th1 -> Th3
        // Fiecare fir primeste firul dupa care trebuie sa astepte (join)
        Pislari th2 = new Pislari("Th2", v, true, "Pislari, Volcovschi", null);
        Thread th4 = new Thread(new Volcovschi(3333, 3999, false, "CR-241", th2), "Th4");
        Pislari th1 = new Pislari("Th1", v, false, "Andreea, Laura", th4);
        Thread th3 = new Thread(new Volcovschi(222, 999, true, "Programarea Concurenta si Distribuita", th1), "Th3");

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        th1.join();
        th2.join();
        th3.join();
        th4.join();
        System.out.println("\nToate cele 4 fire si-au terminat lucrul.");
    }
}
