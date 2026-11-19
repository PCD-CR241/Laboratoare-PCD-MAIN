import java.util.Random;

public class Main {
    static Thread th1, th2, th3, th4;

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

        th1 = new Pislari("Th1", v, false, "Andreea, Laura");
th2 = new Pislari("Th2", v, true, "Pislari, Volcovschi");
        th3 = new Thread(new Volcovschi(222, 999, true, "Programarea Concurenta si Distribuita"), "Th3");
        th4 = new Thread(new Volcovschi(3333, 3999, false, "CR-241"), "Th4");

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