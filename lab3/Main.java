import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random(10);

        for (int i = 0; i < Taran.numere.length; i++) {
            Taran.numere[i] = random.nextInt(101) + 15; 
        }

      
        afiseazaTablou();

        rihter.Th3 th3 = new rihter.Th3();
        rihter.Th4 th4 = new rihter.Th4();
        Taran.Th1 th1 = new Taran.Th1(th4); 
        Taran.Th2 th2 = new Taran.Th2();

        th3.start();
        th4.start(); 
        th1.start();
        th2.start();

  
        Thread[] fire = {th1, th2, th3, th4};
        for (Thread fir : fire) {
            while (fir.getState() != Thread.State.TIMED_WAITING) {
                Thread.sleep(10);
            }
        }


        th2.setPriority(Thread.MAX_PRIORITY);
        th2.join();


        th4.interrupt();
        th4.join();

        th1.join();

        th3.interrupt();
        th3.join();

        System.out.println("Toate cele 4 fire au terminat execuția.");
    }


    static void afiseazaTablou() {
        System.out.println("Tabloul de numere random (câte 10 pe rând):");
        for (int i = 0; i < Taran.numere.length; i++) {
            if (i % 10 == 0) System.out.printf("poz. %2d-%2d: ", i, i + 9);
            System.out.printf("%4d", Taran.numere[i]);
            if (i % 10 == 9) System.out.println();
        }

        System.out.println("\nNumerele de pe pozițiile impare (poziție:număr), folosite de Th1 și Th2:");
        for (int i = 1; i < Taran.numere.length; i += 2) {
            System.out.printf("%2d:%-5d", i, Taran.numere[i]);
            if (i % 20 == 19) System.out.println();
        }
        System.out.println();
    }
}