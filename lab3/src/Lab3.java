import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Lucrarea de laborator nr. 3 - Varianta 8
 * Sincronizarea firelor de executie utilizand metodele clasei Thread.
 *
 *  Th1 - Sarcina 1: sumele produselor numerelor impare doua cate doua, incepand cu primul element
 *  Th2 - Sarcina 2: sumele produselor numerelor impare doua cate doua, incepand cu ultimul element
 *  Th3 - Sarcina 3: parcurge de la inceput intervalul [126, 987]
 *  Th4 - Sarcina 4: parcurge de la sfarsit intervalul [213, 899]
 *
 * Th3 si Th4 ruleaza primele; Th1 si Th2 pornesc dupa ce Th3 si Th4 si-au terminat sarcinile.
 * Dupa sarcini: Th2 - numele, Th4 - grupa, Th1 - prenumele, Th3 - disciplina (litera cu litera, 100 ms).
 *
 * Stecalovici (Th1, Th2) - metode: setPriority()/getPriority(), join()
 * Macovetchi  (Th3, Th4) - metode: interrupt(), isAlive()
 */
public class Lab3 {

        static final int DIM = 100;
        static final int PRIORITATE_GATA = 9; // prioritatea folosita ca semnal "sarcina terminata"
        static int[] tablou = new int[DIM];

        static SumatorDirect th1 = new SumatorDirect();
        static SumatorInvers th2 = new SumatorInvers();
        static ParcurgereCrescatoare th3 = new ParcurgereCrescatoare();
        static ParcurgereDescrescatoare th4 = new ParcurgereDescrescatoare();

        static String prenume = "Lucian";
        static String nume = "Stecalovici";
        static String disciplina = "Programarea Concurenta si Distribuita";
        static String grupa = "CR-241";

        static JTextArea panou;

        // ======================= Th1, Th2 (Stecalovici) =======================

        static class SumatorDirect extends Thread {
                SumatorDirect() {
                        super("Th1");
                }

                @Override
                public void run() {
                        asteaptaStart(); // semnal de la Th4: sarcinile 3 si 4 sunt gata
                        scrie(getName() + " a pornit\n");
                        calculeazaSume(getName(), 0, +1);

                        // setPriority(): anunta Th2 ca sarcina 1 s-a terminat
                        th1.setPriority(PRIORITATE_GATA);

                        // join(): prenumele apare dupa ce Th4 a afisat grupa
                        try {
                                th4.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        scrieTreptat(getName(), prenume);
                }
        }

        static class SumatorInvers extends Thread {
                SumatorInvers() {
                        super("Th2");
                }

                @Override
                public void run() {
                        asteaptaStart(); // semnal de la Th4: sarcinile 3 si 4 sunt gata
                        scrie(getName() + " a pornit\n");
                        calculeazaSume(getName(), DIM - 1, -1);

                        // getPriority(): asteapta pana Th1 isi termina sarcina
                        while (th1.getPriority() != PRIORITATE_GATA) {
                                try {
                                        Thread.sleep(10);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                        scrieTreptat(getName(), nume);
                }
        }

        // Th1 si Th2 dorm pana cand Th4 le trimite interrupt()
        static void asteaptaStart() {
                try {
                        Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        // Sarcina comuna Th1/Th2: produsele imparelor luate doua cate doua si suma lor
        static void calculeazaSume(String fir, int start, int pas) {
                long suma = 0;
                int nrPerechi = 0;
                int imparAnterior = 0;
                int nrImpare = 0;

                for (int i = start; i >= 0 && i < DIM; i += pas) {
                        if (tablou[i] % 2 != 0) {
                                nrImpare++;
                                if (nrImpare % 2 == 1) {
                                        imparAnterior = tablou[i];
                                } else {
                                        long produs = (long) imparAnterior * tablou[i];
                                        suma += produs;
                                        nrPerechi++;
                                        scrie(fir + ": " + imparAnterior + " * " + tablou[i] + " = " + produs
                                                + " | suma = " + suma + "\n");
                                }
                        }
                        try {
                                Thread.sleep(20);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                scrie(fir + ": perechi = " + nrPerechi + ", suma produselor = " + suma + "\n");
        }

        // ======================= Th3, Th4 (Macovetchi) =======================

        static class ParcurgereCrescatoare extends Thread {
                ParcurgereCrescatoare() {
                        super("Th3");
                }

                @Override
                public void run() {
                        scrie(getName() + " a pornit\n");
                        parcurgeInterval(getName(), 126, 987);

                        // interrupt(): anunta Th4 ca sarcina 3 s-a terminat
                        th4.interrupt();

                        // isAlive(): disciplina apare dupa ce Th1 a afisat prenumele
                        while (th1.isAlive()) {
                                try {
                                        Thread.sleep(50);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                        scrieTreptat(getName(), disciplina);
                }
        }

        static class ParcurgereDescrescatoare extends Thread {
                ParcurgereDescrescatoare() {
                        super("Th4");
                }

                @Override
                public void run() {
                        scrie(getName() + " a pornit\n");
                        parcurgeInterval(getName(), 899, 213);

                        // asteapta interrupt() de la Th3; daca a venit deja, sleep() iese imediat
                        try {
                                Thread.sleep(Long.MAX_VALUE);
                        } catch (InterruptedException e) {
                                // sarcina 3 s-a terminat
                        }

                        // interrupt(): sarcinile 3 si 4 sunt gata -> pornesc Th1 si Th2
                        th1.interrupt();
                        th2.interrupt();

                        // isAlive(): grupa apare dupa ce Th2 a afisat numele
                        while (th2.isAlive()) {
                                try {
                                        Thread.sleep(50);
                                } catch (InterruptedException e) {
                                        return;
                                }
                        }
                        scrieTreptat(getName(), grupa);
                }
        }

        // Sarcina comuna Th3/Th4: parcurgerea intervalului de la 'deLa' spre 'panaLa'
        static void parcurgeInterval(String fir, int deLa, int panaLa) {
                int pas = (panaLa >= deLa) ? 1 : -1;
                StringBuilder linie = new StringBuilder();
                int parcurse = 0;

                for (int x = deLa; x != panaLa + pas; x += pas) {
                        linie.append(x).append(' ');
                        parcurse++;
                        if (parcurse % 15 == 0) {
                                scrie(fir + ": " + linie + "\n");
                                linie.setLength(0);
                        }
                        try {
                                Thread.sleep(10);
                        } catch (InterruptedException e) {
                                // semnalul de la Th3 a venit in timpul parcurgerii: il pastram
                                Thread.currentThread().interrupt();
                        }
                }
                if (linie.length() > 0) {
                        scrie(fir + ": " + linie + "\n");
                }
                scrie(fir + ": au fost parcurse " + parcurse + " numere\n");
        }

        // ======================= Interfata =======================

        static void scrie(String text) {
                SwingUtilities.invokeLater(() -> {
                        panou.append(text);
                        panou.setCaretPosition(panou.getDocument().getLength());
                });
        }

        // Literele textului apar cu un interval de 100 ms
        static void scrieTreptat(String fir, String mesaj) {
                scrie(fir + ": ");
                for (char litera : mesaj.toCharArray()) {
                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                                return;
                        }
                        scrie(String.valueOf(litera));
                }
                scrie("\n");
        }

        static void creeazaFereastra() throws Exception {
                SwingUtilities.invokeAndWait(() -> {
                        JFrame fereastra = new JFrame("Laborator 3 - Thread-uri (Varianta 8)");
                        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        panou = new JTextArea();
                        panou.setEditable(false);
                        panou.setLineWrap(true);
                        panou.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
                        JScrollPane derulare = new JScrollPane(panou);
                        derulare.setBorder(BorderFactory.createTitledBorder("Iesirea thread-urilor (Th1-Th4)"));
                        fereastra.add(derulare);

                        fereastra.setSize(900, 650);
                        fereastra.setLocationRelativeTo(null);
                        fereastra.setVisible(true);
                });
        }

        public static void main(String[] args) throws Exception {
                for (int k = 0; k < DIM; k++) {
                        tablou[k] = (int) (Math.random() * 100) + 1;
                }

                creeazaFereastra();
                scrie("Tabloul: " + Arrays.toString(tablou) + "\n\n");

                th3.start();
                th4.start();
                th1.start();
                th2.start();
        }
}
