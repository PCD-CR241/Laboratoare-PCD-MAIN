/*
 * Laboratorul 3, varianta 7 - firele Th3 si Th4 (Lavreca Artiom)
 *
 * Th3 - Sarcina 2: sumele produselor numerelor pare doua cate doua,
 *       incepand cautarea si sumarea cu ultimul element. La final afiseaza disciplina.
 * Th4 - Sarcina 4: parcurge de la sfarsit intervalul [456, 1234]. La final afiseaza grupa.
 * Literele textului final apar cu un interval de 100 ms.
 *
 * Sincronizarea Th3 - Th4 (numai metode ale clasei Thread):
 *  1) yield(): Th4, dupa interval, cedeaza procesorul cu Thread.yield() pana cand
 *     Th3 isi termina calculul, apoi afiseaza grupa;
 *  2) sleep(): Th3, dupa calcul, doarme cate 50 ms cu Thread.sleep() pana cand
 *     Th4 a afisat grupa, apoi afiseaza denumirea disciplinei.
 *
 * Programul principal si fereastra sunt in Bogdan.java.
 */
public class Artiom {

    static final String GRUPA = "CR-241";
    static final String DISCIPLINA = "Programarea concurenta si distribuita";
    static final int TH4_DE_LA = 1234;
    static final int TH4_PANA_LA = 456;

    // ---------------- Th3: Sarcina 2, produse pare de la ultimul element ----------------
    static class Th3 extends Thread {

        // devine true cand Th3 a terminat calculul (verificat de Th4 in bucla cu yield())
        private volatile boolean calculTerminat = false;
        private Th4 th4;

        Th3() {
            super("Th3");
        }

        void setTh4(Th4 th4) {
            this.th4 = th4;
        }

        boolean isCalculTerminat() {
            return calculTerminat;
        }

        @Override
        public void run() {
            Bogdan.scrie(getName(), "a pornit (de la ultimul element)");
            int[] a = Bogdan.a;

            // 1) alegem numerele pare, de la ultimul element spre primul
            int[] pare = new int[a.length];
            int n = 0;
            for (int i = a.length - 1; i >= 0; i--) {
                if (a[i] % 2 == 0) {
                    pare[n++] = a[i];
                }
            }
            Bogdan.scrie(getName(), n + " numere pare in tablou");

            // 2) le luam cate doua consecutive, le inmultim si adunam produsele
            long sumaProduselor = 0;
            for (int k = 0; k + 1 < n; k += 2) {
                int produs = pare[k] * pare[k + 1];
                sumaProduselor += produs;
                Bogdan.scrie(getName(), pare[k] + " * " + pare[k + 1] + " = " + produs
                        + "   (suma: " + sumaProduselor + ")");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
            if (n % 2 == 1) {
                Bogdan.scrie(getName(), "numarul " + pare[n - 1] + " ramane fara pereche");
            }
            Bogdan.scrie(getName(), "suma finala a produselor: " + sumaProduselor);

            calculTerminat = true;
            Bogdan.scrie(getName(), "a terminat calculul, asteapta cu sleep() ca Th4 sa afiseze grupa");

            // sincronizarea 2: sleep() - Th3 doarme cate 50 ms pana cand Th4 a afisat grupa
            while (!th4.isGrupaAfisata()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    return;
                }
            }

            Bogdan.scrie(getName(), "Th4 a afisat grupa, afisez disciplina");
            Bogdan.literaCuLitera(getName(), DISCIPLINA);
            Bogdan.scrie(getName(), "s-a terminat");
        }
    }

    // ---------------- Th4: Sarcina 4, intervalul [456, 1234] de la sfarsit ----------------
    static class Th4 extends Thread {

        // devine true cand Th4 a afisat grupa (verificat de Th3 in bucla cu sleep())
        private volatile boolean grupaAfisata = false;
        private final Th3 th3;

        Th4(Th3 th3) {
            super("Th4");
            this.th3 = th3;
        }

        boolean isGrupaAfisata() {
            return grupaAfisata;
        }

        @Override
        public void run() {
            Bogdan.scrie(getName(), "a pornit (intervalul [" + TH4_PANA_LA + ", " + TH4_DE_LA + "] de la sfarsit)");

            // cate 10 numere pe un rand, cu o pauza de 40 ms dupa fiecare rand
            String rand = "";
            int contor = 0;
            for (int i = TH4_DE_LA; i >= TH4_PANA_LA; i--) {
                rand += i + " ";
                contor++;
                if (contor == 10 || i == TH4_PANA_LA) {
                    Bogdan.scrie(getName(), rand.trim());
                    rand = "";
                    contor = 0;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            // sincronizarea 1: yield() - Th4 cedeaza procesorul pana cand Th3 termina calculul
            if (!th3.isCalculTerminat()) {
                Bogdan.scrie(getName(), "a terminat intervalul, asteapta cu yield() calculul lui Th3");
                while (!th3.isCalculTerminat()) {
                    Thread.yield();
                }
            }

            Bogdan.scrie(getName(), "Th3 a terminat calculul, afisez grupa");
            Bogdan.literaCuLitera(getName(), GRUPA);
            grupaAfisata = true;
            Bogdan.scrie(getName(), "s-a terminat");
        }
    }
}
