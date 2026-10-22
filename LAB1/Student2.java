import java.util.ArrayList;


public class Student2 {

    private Thread th1;
    private Thread th2;

    public Student2(int[] mas, Afisaj afisaj) {
        th1 = new Thread(new CautarePare(mas, true, afisaj), "Artiom--Th1");    // de la primul element
        th2 = new Thread(new CautarePare(mas, false, afisaj), "Artiom-Th2");   // de la ultimul element
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

class CautarePare implements Runnable {

    private int[] mas;
    private boolean dinStanga;
    private Afisaj afisaj;

    public CautarePare(int[] mas, boolean dinStanga, Afisaj afisaj) {
        this.mas = mas;
        this.dinStanga = dinStanga;
        this.afisaj = afisaj;
    }

    public void run() {
        String nume = Thread.currentThread().getName();

        // Pasul 1: adunam toate numerele pare intr-o lista, in ordinea cautarii
        ArrayList<Integer> pare = new ArrayList<>();
        if (dinStanga) {
            for (int i = 0; i < mas.length; i++) {
                if (mas[i] % 2 == 0) {
                    pare.add(mas[i]);
                }
            }
        } else {
            for (int i = mas.length - 1; i >= 0; i--) {
                if (mas[i] % 2 == 0) {
                    pare.add(mas[i]);
                }
            }
        }

        // Pasul 2: le luam doua cate doua, le inmultim si adunam produsele
        long sumaProduselor = 0;
        for (int k = 0; k + 1 < pare.size(); k += 2) {
            int a = pare.get(k);
            int b = pare.get(k + 1);
            int produs = a * b;
            sumaProduselor += produs;
            afisaj.afiseaza(nume, a + " * " + b + " = " + produs + "   (suma: " + sumaProduselor + ")");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                afisaj.afiseaza(nume, "a fost intrerupt");
            }
        }

        afisaj.sumaFinala(nume, sumaProduselor);
    }
}
