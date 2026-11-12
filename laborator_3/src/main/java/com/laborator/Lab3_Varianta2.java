public class Lab3_Varianta2 {

    // Date pentru array
    static final int SIZE = 100;
    static int[] b = new int[SIZE];          // array-ul original
    static int[] evenPositions;              // pozițiile numerelor pare
    static int evenCount = 0;

    // Thread-uri
    static Thread1 th1 = new Thread1();
    static Thread2 th2 = new Thread2();
    static Thread3 th3 = new Thread3();
    static Thread4 th4 = new Thread4();

    // Informații student (modifică după tine)
    static String prenume   = "\nIon";
    static String nume      = "\nPopescu";
    static String grupa     = "\nTI-21";
    static String disciplina = "\nProgramarea concurenta si distribuita";

    // ===================== THREAD 1 =====================
    // Suma pozițiilor numerelor pare două câte două de la început
    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println("Starting Thread 1 (Suma pozitii pare de la inceput)");
            try {
                for (int i = 0; i + 1 < evenCount; i += 2) {
                    int suma = evenPositions[i] + evenPositions[i + 1];
                    System.out.println("Th1: poz[" + evenPositions[i] + "] + poz[" + evenPositions[i + 1] + "] = " + suma);
                    Thread.sleep(80); // sincronizare 1: sleep
                }
            } catch (InterruptedException e) {
                System.out.println("Th1 a fost întrerupt");
            }

            // Așteaptă ca Th2 să termine (sincronizare cu isAlive)
            while (th2.isAlive()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
            }

            // Afișare Prenume letter-by-letter
            System.out.print("Th1 afiseaza Prenumele: ");
            for (char c : prenume.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
            System.out.println();
            this.interrupt(); // sincronizare 2: interrupt
        }
    }

    // ===================== THREAD 2 =====================
    // Suma pozițiilor numerelor pare două câte două de la sfârșit
    static class Thread2 extends Thread {
        @Override
        public void run() {
            System.out.println("Starting Thread 2 (Suma pozitii pare de la sfarsit)");
            try {
                for (int i = evenCount - 1; i - 1 >= 0; i -= 2) {
                    int suma = evenPositions[i] + evenPositions[i - 1];
                    System.out.println("Th2: poz[" + evenPositions[i] + "] + poz[" + evenPositions[i - 1] + "] = " + suma);
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                System.out.println("Th2 a fost întrerupt");
            }

            // Afișare Nume letter-by-letter
            System.out.print("Th2 afiseaza Numele: ");
            for (char c : nume.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
            System.out.println();
            this.interrupt();
        }
    }

    // ===================== THREAD 3 =====================
    // Parcurgere [120, 690] de la început
    static class Thread3 extends Thread {
        @Override
        public void run() {
            System.out.println("Starting Thread 3 (Interval [120, 690] de la inceput)");
            int count = 0;
            for (int i = 120; i <= 690; i++) {
                System.out.print(i + " ");
                count++;
                if (count % 15 == 0) System.out.println();
                try {
                    Thread.sleep(5); // sleep ușor ca să nu fie prea rapid
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println();

            // Așteaptă Th4 cu isAlive
            while (th4.isAlive()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
            }

            // Afișare Disciplină
            System.out.print("Th3 afiseaza Disciplina: ");
            for (char c : disciplina.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
            System.out.println();
            this.interrupt();
        }
    }

    // ===================== THREAD 4 =====================
    // Parcurgere [1000, 1567] de la sfârșit
    static class Thread4 extends Thread {
        @Override
        public void run() {
            System.out.println("Starting Thread 4 (Interval [1000, 1567] de la sfarsit)");
            int count = 0;
            for (int i = 1567; i >= 1000; i--) {
                System.out.print(i + " ");
                count++;
                if (count % 15 == 0) System.out.println();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println();

            // Afișare Grupa
            System.out.print("Th4 afiseaza Grupa: ");
            for (char c : grupa.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
            System.out.println();
            this.interrupt();
        }
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        // Generare array random
        System.out.println("=== Array generat ===");
        for (int i = 0; i < SIZE; i++) {
            b[i] = (int) (Math.random() * 100) + 15; // 15..114
            System.out.print(b[i] + " ");
            if ((i + 1) % 20 == 0) System.out.println();
        }
        System.out.println("\n");

        // Extragem pozițiile numerelor pare
        int[] temp = new int[SIZE];
        evenCount = 0;
        for (int i = 0; i < SIZE; i++) {
            if (b[i] % 2 == 0) {
                temp[evenCount++] = i; // salvăm poziția (indexul)
            }
        }
        evenPositions = new int[evenCount];
        System.arraycopy(temp, 0, evenPositions, 0, evenCount);

        System.out.println("Numere pare gasite: " + evenCount);
        System.out.print("Pozitiile lor: ");
        for (int p : evenPositions) System.out.print(p + " ");
        System.out.println("\n");

        // Pornire thread-uri
        th1.setName("Th1");
        th2.setName("Th2");
        th3.setName("Th3");
        th4.setName("Th4");

        // Setare priorități (sincronizare suplimentară)
        th1.setPriority(Thread.NORM_PRIORITY);
        th2.setPriority(Thread.NORM_PRIORITY + 1);
        th3.setPriority(Thread.NORM_PRIORITY);
        th4.setPriority(Thread.NORM_PRIORITY + 1);

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        // Sincronizare finală cu join()
        try {
            th1.join();
            th2.join();
            th3.join();
            th4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Toate thread-urile s-au terminat ===");
    }
}