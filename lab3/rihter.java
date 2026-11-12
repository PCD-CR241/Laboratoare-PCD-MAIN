
public class rihter {
    static void afiseaza(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    static class Th3 extends Thread {
        Th3() {
            super("Th3");
        }

        public void run() {
            StringBuilder rezultat = new StringBuilder("Th3:\n");

            for (int i = 11; i <= 548; i++) {
                rezultat.append(i).append(" ");
                if ((i - 10) % 10 == 0) rezultat.append("\n");
            }

            System.out.println(rezultat);

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                try {
                    afiseaza("Th3: Programarea concurenta si distribuita");
                } catch (InterruptedException ex) {
                    interrupt();
                }
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
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                try {
                    afiseaza("Th4: CR-241");
                } catch (InterruptedException ex) {
                    interrupt();
                }
            }
        }
    }
}
