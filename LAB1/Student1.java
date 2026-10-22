// Manoli Bogdan: firele sunt create prin EXTINDEREA clasei Thread

public class Student1 {

    private ProdusePareThread th1;
    private ProdusePareThread th2;

    public Student1(int[] mas, Afisaj afisaj) {
        // Th1 porneste de la primul element (indexul 0) si merge inainte (+1)
        th1 = new ProdusePareThread("Bogdan-Th1", mas, 0, 1, afisaj);
        // Th2 porneste de la ultimul element (indexul 99) si merge inapoi (-1)
        th2 = new ProdusePareThread("Bogdan-Th2", mas, mas.length - 1, -1, afisaj);
    }

    public void pornesteFirele() {
        th1.start();
        th2.start();
    }

    public void asteaptaFirele() throws InterruptedException {
        th1.join();
        th2.join();
    }
}

class ProdusePareThread extends Thread {

    private int[] mas;
    private int start;
    private int pas;
    private Afisaj afisaj;

    public ProdusePareThread(String nume, int[] mas, int start, int pas, Afisaj afisaj) {
        super(nume);   // numele firului
        this.mas = mas;
        this.start = start;
        this.pas = pas;
        this.afisaj = afisaj;
    }

    public void run() {
        int i = start;
        int primul = -1;
        long sumaProduselor = 0;

        while (i >= 0 && i < mas.length) {
            if (mas[i] % 2 == 0) {
                if (primul == -1) {
                    primul = mas[i];
                } else {
                    int produs = primul * mas[i];
                    sumaProduselor = sumaProduselor + produs;
                    afisaj.afiseaza(getName(), primul + " * " + mas[i] + " = " + produs
                            + " ( SUMA: " + sumaProduselor + ")");
                    primul = -1;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        afisaj.afiseaza(getName(), "a fost intrerupt");
                    }
                }
            }
            i = i + pas;
        }
        afisaj.sumaFinala(getName(), sumaProduselor);
    }
}
