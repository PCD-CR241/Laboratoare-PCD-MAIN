public class Conditia1 implements Runnable {

    private final int[] mas;
    private final int perecheStart;
    private final int perecheFinal;
    private int suma;

    public Conditia1(int[] mas, int perecheStart, int perecheFinal) {
        this.mas = mas;
        this.perecheStart = perecheStart;
        this.perecheFinal = perecheFinal;
    }

    @Override
    public void run() {
        suma = 0;

        // perecheFinal nu este inclusa.
        for (int p = perecheStart; p < perecheFinal; p++) {
            int i = 4 * p;
            int j = i + 2;

            int produs = mas[i] * mas[j];
            suma += produs;

            System.out.printf(
                    "%s | C1 | pozitii %d si %d | %d * %d = %d%n",
                    Thread.currentThread().getName(),
                    i + 1,
                    j + 1,
                    mas[i],
                    mas[j],
                    produs
            );
        }
    }

    public int getSuma() {
        return suma;
    }
}