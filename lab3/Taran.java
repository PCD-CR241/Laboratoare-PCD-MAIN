
public class Taran {
    static int[] numere = new int[100];

    static void afiseaza(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    static class Th1 extends Thread {
        Th1() {
            super("Th1");
        }

        public void run() {
            for (int i = 0; i + 6 < numere.length; i += 8) {
                int p1 = numere[i] * numere[i + 2];
                int p2 = numere[i + 4] * numere[i + 6];

                System.out.println(getName() + ": (" +
                        numere[i] + " * " + numere[i + 2] +
                        ") - (" + numere[i + 4] + " * " +
                        numere[i + 6] + ") = " + (p1 - p2));
            }

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                try {
                    afiseaza("Th1: Daniel");
                } catch (InterruptedException ex) {
                    interrupt();
                }
            }
        }
    }

    static class Th2 extends Thread {
        Th2() {
            super("Th2");
        }

        public void run() {
            for (int i = numere.length - 2; i - 6 >= 0; i -= 8) {
                int p1 = numere[i] * numere[i - 2];
                int p2 = numere[i - 4] * numere[i - 6];

                System.out.println(getName() + ": (" +
                        numere[i] + " * " + numere[i - 2] +
                        ") - (" + numere[i - 4] + " * " +
                        numere[i - 6] + ") = " + (p1 - p2));
            }

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                try {
                    afiseaza("Th2: Taran");
                } catch (InterruptedException ex) {
                    interrupt();
                }
            }
        }
    }
}
