public class Taran {
    static int[] numere = new int[100];

    static void afiseaza(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    static void diferentaProduselor(String fir, int p1, int p2, int p3, int p4) {
        int rezultat = numere[p1] * numere[p2] - numere[p3] * numere[p4];

        System.out.println(fir + " [poz. " + p1 + ", " + p2 + ", " + p3 + ", " + p4 + "]: ("
                + numere[p1] + " * " + numere[p2] + ") - ("
                + numere[p3] + " * " + numere[p4] + ") = " + rezultat);
    }

    static class Th1 extends Thread {
        private final Thread th4; 

        Th1(Thread th4) {
            super("Th1");
            this.th4 = th4;
        }

        @Override
        public void run() {
            for (int i = 1; i + 6 < numere.length; i += 8) {
                diferentaProduselor(getName(), i, i + 2, i + 4, i + 6);
            }

            try {
                while (th4.isAlive()) {
                    Thread.sleep(10);
                }

                System.out.print(getName() + ": ");
                afiseaza("Daniel");
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    static class Th2 extends Thread {
        Th2() {
            super("Th2");
        }

        @Override
        public void run() {
            for (int i = numere.length - 1; i - 6 >= 0; i -= 8) {
                diferentaProduselor(getName(), i, i - 2, i - 4, i - 6);
            }

            try {
                while (getPriority() != Thread.MAX_PRIORITY) {
                    Thread.sleep(10);
                }

                System.out.print(getName() + ": ");
                afiseaza("Taran");
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}