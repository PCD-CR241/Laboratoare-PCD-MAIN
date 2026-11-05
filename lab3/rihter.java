
public class rihter {
    static volatile boolean gata3 = false;

    static void afiseaza(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    static class Th3 extends Thread {
        private final Th4 th4;

        Th3(Th4 th4) {
            super("Th3");
            this.th4 = th4;
        }

        public void run() {
            StringBuilder rezultat = new StringBuilder("Th3:\n");

            for (int i = 11; i <= 548; i++) {
                rezultat.append(i).append(" ");
                if ((i - 10) % 10 == 0) rezultat.append("\n");
            }

            System.out.println(rezultat);
            gata3 = true;

            try {
                th4.join();
                afiseaza("Th3: Programarea concurenta si distribuita");
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    static class Th4 extends Thread {
        Th4() {
            super("Th4");
        }

        public void run() {
            StringBuilder rezultat = new StringBuilder("Th4:\n");

            for (int i = 1234; i >= 678; i--) {
                rezultat.append(i).append(" ");
                if ((1235 - i) % 10 == 0) rezultat.append("\n");
            }

            System.out.println(rezultat);

            try {
                while (!gata3) {
                    Thread.sleep(10);
                }

                afiseaza("Th4: CR-241");
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Th4 th4 = new Th4();
        Th3 th3 = new Th3(th4);

        th3.start();
        th4.start();

        th3.join();
        th4.join();
    }
}
