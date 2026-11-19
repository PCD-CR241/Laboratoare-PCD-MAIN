import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
 * Laboratorul 3, varianta 7 - programul principal + firele Th1 si Th2 (Manoli Bogdan)
 *
 * Th1 - Sarcina 1: sumele produselor numerelor pare doua cate doua,
 *       incepand cautarea si sumarea cu primul element. La final afiseaza Prenumele.
 * Th2 - Sarcina 3: parcurge de la inceput intervalul [234, 1000]. La final afiseaza Numele.
 * Literele textului final apar cu un interval de 100 ms.
 *
 * Sincronizarea Th1 - Th2 (numai metode ale clasei Thread):
 *  1) interrupt(): dupa interval, Th2 "doarme" pana cand Th1 il trezeste cu th2.interrupt(),
 *     adica pana cand Th1 si-a terminat calculul. Abia apoi Th2 afiseaza numele.
 *  2) join(): dupa ce l-a trezit pe Th2, Th1 asteapta cu th2.join() ca Th2 sa se termine
 *     complet. Abia apoi Th1 afiseaza prenumele.
 *
 * Firele Th3 si Th4 sunt in fisierul Artiom.java.
 */
public class Bogdan {

    static final String NUMELE = "Manoli";
    static final String PRENUMELE = "Bogdan";
    static final int TH2_DE_LA = 234;
    static final int TH2_PANA_LA = 1000;

    static int[] a = new int[100];

    // elemente din fereastra (cate unul pentru fiecare fir: index 0 = Th1 ... 3 = Th4)
    static JLabel[] celule = new JLabel[100];
    static JTextArea[] jurnale = new JTextArea[4];
    static JLabel[] stari = new JLabel[4];
    static JLabel[] texte = new JLabel[4];
    static JButton butonStart;
    static Thread[] fire = new Thread[4];




    static int index(String numeFir) {
        return Integer.parseInt(numeFir.substring(2)) - 1;
    }

    // scrie un rand in casuta firului (si in consola)
    static void scrie(String numeFir, String text) {
        System.out.println(numeFir + ": " + text);
        SwingUtilities.invokeLater(() -> {
            JTextArea j = jurnale[index(numeFir)];
            j.append(text + "\n");
            j.setCaretPosition(j.getDocument().getLength());   // derulare in jos
        });
    }


    static void literaCuLitera(String numeFir, String text) {
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            String bucata = text.substring(0, i + 1);
            SwingUtilities.invokeLater(() -> texte[index(numeFir)].setText(bucata));
        }
    }


    static class Th1 extends Thread {

        private final Th2 th2;

        Th1(Th2 th2) {
            super("Th1");
            this.th2 = th2;
        }

        @Override
        public void run() {
            scrie(getName(), "a pornit (de la primul element)");

            int primul = -1;
            long sumaProduselor = 0;
            for (int i = 0; i < a.length; i++) {
                if (a[i] % 2 == 0) {
                    if (primul == -1) {
                        primul = a[i];
                    } else {
                        int produs = primul * a[i];
                        sumaProduselor += produs;
                        scrie(getName(), primul + " * " + a[i] + " = " + produs
                                + "   (suma: " + sumaProduselor + ")");
                        primul = -1;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            }
            scrie(getName(), "suma finala a produselor: " + sumaProduselor);

            // sincronizarea 1: interrupt() - il anunt pe Th2 ca mi-am terminat calculul
            scrie(getName(), "calcul terminat -> th2.interrupt()");
            th2.interrupt();

            // sincronizarea 2: join() - astept ca Th2 sa se termine complet
            scrie(getName(), "astept cu th2.join() ca Th2 sa termine");
            try {
                th2.join();
            } catch (InterruptedException e) {
                return;
            }
            scrie(getName(), "Th2 s-a terminat (isAlive = " + th2.isAlive() + "), afisez prenumele");
            literaCuLitera(getName(), PRENUMELE);
            scrie(getName(), "s-a terminat");
        }
    }


    static class Th2 extends Thread {

        Th2() {
            super("Th2");
        }

        @Override
        public void run() {
            scrie(getName(), "a pornit (intervalul [" + TH2_DE_LA + ", " + TH2_PANA_LA + "] de la inceput)");

            boolean semnalPrimit = false;   // a venit deja interrupt() de la Th1?
            String rand = "";
            int contor = 0;
            for (int i = TH2_DE_LA; i <= TH2_PANA_LA; i++) {
                rand += i + " ";
                contor++;
                if (contor == 10 || i == TH2_PANA_LA) {     // cate 10 numere pe un rand
                    scrie(getName(), rand.trim());
                    rand = "";
                    contor = 0;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        // Th1 a terminat calculul cat timp eu inca parcurgeam intervalul
                        semnalPrimit = true;
                        scrie(getName(), "(am primit interrupt() de la Th1, termin intervalul)");
                    }
                }
            }


            if (!semnalPrimit) {
                scrie(getName(), "interval terminat, astept interrupt() de la Th1");
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    scrie(getName(), "m-a trezit interrupt() de la Th1");
                }
            }

            scrie(getName(), "Th1 a terminat calculul, afisez numele");
            literaCuLitera(getName(), NUMELE);
            scrie(getName(), "s-a terminat");
        }
    }



    static void genereazaTablou() {
        Random rnd = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rnd.nextInt(100) + 1;
            celule[i].setText(String.valueOf(a[i]));
            celule[i].setBackground(a[i] % 2 == 0 ? new Color(200, 230, 201) : Color.WHITE);
        }
    }

    static void pornesteFirele() {
        genereazaTablou();
        for (int i = 0; i < 4; i++) {
            jurnale[i].setText("");
            texte[i].setText(" ");
        }


        Th2 th2 = new Th2();
        Th1 th1 = new Th1(th2);
        Artiom.Th3 th3 = new Artiom.Th3();
        Artiom.Th4 th4 = new Artiom.Th4(th3);
        th3.setTh4(th4);

        fire = new Thread[]{th1, th2, th3, th4};
        for (Thread t : fire) {
            t.setDaemon(true);
            t.start();
        }
    }



    static JPanel panouFir(int i, String titlu, Color culoare) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(culoare, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel lbTitlu = new JLabel(titlu);
        lbTitlu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbTitlu.setForeground(culoare);
        stari[i] = new JLabel(" ");
        stari[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JPanel sus = new JPanel(new GridLayout(2, 1));
        sus.add(lbTitlu);
        sus.add(stari[i]);
        p.add(sus, BorderLayout.NORTH);

        jurnale[i] = new JTextArea();
        jurnale[i].setEditable(false);
        jurnale[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        p.add(new JScrollPane(jurnale[i]), BorderLayout.CENTER);

        texte[i] = new JLabel(" ");
        texte[i].setFont(new Font("Segoe UI", Font.BOLD, 20));
        texte[i].setForeground(culoare);
        p.add(texte[i], BorderLayout.SOUTH);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Laboratorul 3 - varianta 7 - sincronizarea firelor");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1250, 760);
            f.setLocationRelativeTo(null);

            JPanel radacina = new JPanel(new BorderLayout(10, 10));
            radacina.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            f.setContentPane(radacina);


            JLabel titlu = new JLabel("Lab 3, varianta 7  |  Th1-Th2: Manoli Bogdan (interrupt, join)"
                    + "  |  Th3-Th4: Lavreca Artiom (yield, sleep)");
            titlu.setFont(new Font("Segoe UI", Font.BOLD, 15));
            butonStart = new JButton("Porneste firele");
            butonStart.setFont(new Font("Segoe UI", Font.BOLD, 14));
            butonStart.addActionListener(e -> pornesteFirele());
            JPanel sus = new JPanel(new BorderLayout());
            sus.add(titlu, BorderLayout.WEST);
            sus.add(butonStart, BorderLayout.EAST);
            radacina.add(sus, BorderLayout.NORTH);


            JPanel grila = new JPanel(new GridLayout(10, 10, 2, 2));
            for (int i = 0; i < 100; i++) {
                celule[i] = new JLabel("", SwingConstants.CENTER);
                celule[i].setOpaque(true);
                celule[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                grila.add(celule[i]);
            }
            JPanel stanga = new JPanel(new BorderLayout(0, 6));
            stanga.add(new JLabel("Tabloul a[100]  (verde = numar par)"), BorderLayout.NORTH);
            stanga.add(grila, BorderLayout.CENTER);
            stanga.setPreferredSize(new Dimension(380, 0));
            radacina.add(stanga, BorderLayout.WEST);


            Color albastru = new Color(21, 101, 192);
            Color verde = new Color(46, 125, 50);
            JPanel centru = new JPanel(new GridLayout(2, 2, 10, 10));
            centru.add(panouFir(0, "Th1 - Sarcina 1: produse pare, de la primul  -> Prenumele", albastru));
            centru.add(panouFir(1, "Th2 - Sarcina 3: [234, 1000] de la inceput  -> Numele", albastru));
            centru.add(panouFir(2, "Th3 - Sarcina 2: produse pare, de la ultimul  -> Disciplina", verde));
            centru.add(panouFir(3, "Th4 - Sarcina 4: [456, 1234] de la sfarsit  -> Grupa", verde));
            radacina.add(centru, BorderLayout.CENTER);

            genereazaTablou();


            new Timer(100, e -> {
                boolean vreunulViu = false;
                for (int i = 0; i < 4; i++) {
                    if (fire[i] != null) {
                        stari[i].setText("stare: " + fire[i].getState() + "   isAlive = " + fire[i].isAlive());
                        if (fire[i].isAlive()) {
                            vreunulViu = true;
                        }
                    }
                }
                butonStart.setEnabled(!vreunulViu);
            }).start();

            f.setVisible(true);
        });
    }
}
