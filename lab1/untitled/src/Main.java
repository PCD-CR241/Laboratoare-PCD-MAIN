public class Main {

    public static void main(String args[]) {

        // Crearea tabloului
        int[] mas = new int[101];

        // Generarea celor 100 de numere
        for (int i = 0; i < 100; i++) {
            mas[i] = (int)(Math.random() * 99);
            System.out.print(mas[i] + " ");
        }

        System.out.println("\n");

        // Primul fir de execuție
        Thread Th1 = new Thread() {

            public void run() {

                int suma = 0;

                // Pornim de la primul element
                for (int i = 0; i < 100 - 2; i += 4) {

                    int produs = mas[i] * mas[i + 2];

                    suma = suma + produs;

                    System.out.println(
                            "Th1: " + mas[i] + " * " + mas[i + 2]
                                    + " = " + produs
                    );
                }

                System.out.println("Suma Th1 = " + suma);
            }
        };

        // Al doilea fir de execuție
        Thread Th2 = new Thread() {

            public void run() {

                int suma = 0;

                // Pornim de la ultimul element de pe poziție impară
                for (int i = 98; i >= 2; i -= 4) {

                    int produs = mas[i] * mas[i - 2];

                    suma = suma + produs;

                    System.out.println(
                            "Th2: " + mas[i] + " * " + mas[i - 2]
                                    + " = " + produs
                    );
                }

                System.out.println("Suma Th2 = " + suma);
            }
        };

        // Pornirea celor două fire
        Th1.start();
        Th2.start();

        // Așteptăm terminarea ambelor fire
        try {
            Th1.join();
            Th2.join();
        } catch (InterruptedException e) {
            System.out.println("Firul a fost întrerupt.");
        }

        System.out.println("\nAmbele fire au terminat executia.");
    }
}