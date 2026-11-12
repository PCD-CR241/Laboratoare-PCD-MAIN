
import java.util.ArrayList;

public class Depozit {
    private final ArrayList<Integer> obiecte = new ArrayList<>();
    private final int capacitate;

    public Depozit(int capacitate) {
        this.capacitate = capacitate;
    }

    public synchronized void put(String nume, int a, int b) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            int numar = i == 0 ? a : b;

            while (obiecte.size() == capacitate) {
                System.out.println("Depozitul este plin. " + nume + " asteapta.");
                wait();
            }

            obiecte.add(numar);
            System.out.println(nume + " a pus " + numar + ". Depozit: " + obiecte);
            notifyAll();
        }
    }

    public synchronized void get(String nume) throws InterruptedException {
        while (obiecte.isEmpty()) {
            System.out.println("Depozitul este gol. " + nume + " asteapta.");
            wait();
        }

        int numar = obiecte.remove(0);
        System.out.println(nume + " a consumat " + numar + ". Depozit: " + obiecte);
        notifyAll();
    }
}
