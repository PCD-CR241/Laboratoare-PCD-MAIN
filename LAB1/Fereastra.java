import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;


public class Fereastra extends JFrame implements Afisaj {


    static final Color FUNDAL      = new Color(244, 246, 250);
    static final Color CARD        = Color.WHITE;
    static final Color TEXT        = new Color(30, 36, 48);
    static final Color TEXT_SLAB   = new Color(110, 118, 132);
    static final Color LINIE       = new Color(225, 229, 236);
    static final Color ALBASTRU    = new Color(47, 111, 219);   // Manoli Bogdan
    static final Color VERDE       = new Color(20, 150, 125);   // Lavreca Artiom
    static final Color PAR_FUNDAL  = new Color(222, 235, 255);
    static final Color IMPAR_FUNDAL = new Color(240, 241, 244);

    static final String FONT = "Segoe UI";

    private int[] mas = new int[100];
    private JLabel[] celule = new JLabel[100];
    private HashMap<String, CardFir> carduri = new HashMap<>();
    private HashMap<String, Long> sume = new HashMap<>();
    private DefaultListModel<String> jurnal = new DefaultListModel<>();   // randurile din lista comuna
    private JList<String> listaJurnal;

    private JButton butonGenereaza;
    private JButton butonPorneste;
    private JLabel status;
    private JLabel autori;

    public Fereastra() {
        super("Laboratorul 1 - Fire de executie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);   // fereastra apare in centrul ecranului

        JPanel radacina = new JPanel(new BorderLayout(0, 16));
        radacina.setBackground(FUNDAL);
        radacina.setBorder(new EmptyBorder(20, 24, 20, 24));
        setContentPane(radacina);

        radacina.add(creeazaAntet(), BorderLayout.NORTH);
        radacina.add(creeazaCentru(), BorderLayout.CENTER);
        radacina.add(creeazaSubsol(), BorderLayout.SOUTH);

        genereazaTablou();   // ca fereastra sa nu porneasca goala
    }

    // ================= CONSTRUIREA FERESTREI =================

    private JPanel creeazaAntet() {
        JPanel antet = new JPanel(new BorderLayout());
        antet.setOpaque(false);

        JPanel texte = new JPanel(new GridLayout(2, 1));
        texte.setOpaque(false);
        texte.add(eticheta("Fire de executie in Java", 26, Font.BOLD, TEXT));
        texte.add(eticheta("Varianta 7  ·  Sumele produselor numerelor pare, luate doua cate doua",
                15, Font.PLAIN, TEXT_SLAB));
        antet.add(texte, BorderLayout.WEST);

        JPanel butoane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        butoane.setOpaque(false);
        butonGenereaza = buton("Genereaza tabloul", false);
        butonPorneste  = buton("Porneste firele  ▶", true);
        butonGenereaza.addActionListener(e -> genereazaTablou());
        butonPorneste.addActionListener(e -> pornesteFirele());
        butoane.add(butonGenereaza);
        butoane.add(butonPorneste);
        antet.add(butoane, BorderLayout.EAST);

        return antet;
    }

    private JPanel creeazaCentru() {
        JPanel centru = new JPanel(new BorderLayout(16, 0));
        centru.setOpaque(false);

        // stanga: tabloul de 10 x 10
        PanouRotunjit panouTablou = new PanouRotunjit(CARD);
        panouTablou.setLayout(new BorderLayout(0, 12));
        panouTablou.setBorder(new EmptyBorder(16, 16, 16, 16));
        panouTablou.setPreferredSize(new Dimension(390, 0));

        panouTablou.add(eticheta("Tabloul mas[ ]  ·  100 numere", 15, Font.BOLD, TEXT), BorderLayout.NORTH);

        JPanel grila = new JPanel(new GridLayout(10, 10, 4, 4));
        grila.setOpaque(false);
        for (int i = 0; i < 100; i++) {
            JLabel c = new JLabel("", SwingConstants.CENTER);
            c.setOpaque(true);
            c.setFont(new Font(FONT, Font.PLAIN, 13));
            celule[i] = c;
            grila.add(c);
        }
        panouTablou.add(grila, BorderLayout.CENTER);

        JPanel legenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        legenda.setOpaque(false);
        legenda.add(punctLegenda(PAR_FUNDAL, ALBASTRU, "numar par"));
        legenda.add(punctLegenda(IMPAR_FUNDAL, TEXT_SLAB, "numar impar"));
        panouTablou.add(legenda, BorderLayout.SOUTH);

        centru.add(panouTablou, BorderLayout.WEST);

        // dreapta: sus cele 4 fire (cate un card), jos lista comuna in ordinea executiei
        JPanel dreapta = new JPanel(new BorderLayout(0, 16));
        dreapta.setOpaque(false);

        JPanel fire = new JPanel(new GridLayout(1, 4, 12, 0));
        fire.setOpaque(false);
        adaugaCard(fire, "Bogdan-Th1", "Manoli Bogdan", "Th1  \u2192  de la primul", "extends Thread", ALBASTRU);
        adaugaCard(fire, "Bogdan-Th2", "Manoli Bogdan", "Th2  \u2190  de la ultimul", "extends Thread", ALBASTRU);
        adaugaCard(fire, "Artiom-Th1", "Lavreca Artiom", "Th1  \u2192  de la primul", "implements Runnable", VERDE);
        adaugaCard(fire, "Artiom-Th2", "Lavreca Artiom", "Th2  \u2190  de la ultimul", "implements Runnable", VERDE);
        dreapta.add(fire, BorderLayout.NORTH);

        PanouRotunjit panouJurnal = new PanouRotunjit(CARD);
        panouJurnal.setLayout(new BorderLayout(0, 10));
        panouJurnal.setBorder(new EmptyBorder(14, 16, 14, 16));
        panouJurnal.add(eticheta("Firele, in ordinea in care au lucrat", 15, Font.BOLD, TEXT), BorderLayout.NORTH);

        listaJurnal = new JList<>(jurnal);
        listaJurnal.setCellRenderer(new RandJurnal());
        listaJurnal.setBackground(new Color(249, 250, 252));
        JScrollPane scroll = new JScrollPane(listaJurnal);
        scroll.setBorder(BorderFactory.createLineBorder(LINIE));
        panouJurnal.add(scroll, BorderLayout.CENTER);
        dreapta.add(panouJurnal, BorderLayout.CENTER);

        centru.add(dreapta, BorderLayout.CENTER);

        return centru;
    }

    private JPanel creeazaSubsol() {
        PanouRotunjit subsol = new PanouRotunjit(CARD);
        subsol.setLayout(new BorderLayout());
        subsol.setBorder(new EmptyBorder(14, 18, 14, 18));

        autori = eticheta(" ", 20, Font.BOLD, TEXT);
        status = eticheta("Apasa \"Porneste firele\" pentru a incepe.", 13, Font.PLAIN, TEXT_SLAB);
        status.setBorder(new EmptyBorder(6, 0, 0, 0));
        subsol.add(autori, BorderLayout.CENTER);
        subsol.add(status, BorderLayout.SOUTH);
        return subsol;
    }

    private void adaugaCard(JPanel parinte, String numeFir, String student,
                            String directie, String metoda, Color culoare) {
        CardFir card = new CardFir(student, directie, metoda, culoare);
        carduri.put(numeFir, card);
        parinte.add(card);
    }

    // ================= ACTIUNILE BUTOANELOR =================

    private void genereazaTablou() {
        Random rnd = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rnd.nextInt(100) + 1;
            celule[i].setText(String.valueOf(mas[i]));
            if (mas[i] % 2 == 0) {
                celule[i].setBackground(PAR_FUNDAL);
                celule[i].setForeground(ALBASTRU);
            } else {
                celule[i].setBackground(IMPAR_FUNDAL);
                celule[i].setForeground(TEXT_SLAB);
            }
        }
        for (CardFir card : carduri.values()) {
            card.reseteaza();
        }
        sume.clear();
        jurnal.clear();
        autori.setText(" ");
        status.setText("Tablou nou generat. Apasa \"Porneste firele\".");
    }

    private void pornesteFirele() {
        butonGenereaza.setEnabled(false);
        butonPorneste.setEnabled(false);
        for (CardFir card : carduri.values()) {
            card.reseteaza();
            card.porneste();
        }
        sume.clear();
        jurnal.clear();
        autori.setText(" ");
        status.setText("Firele lucreaza in paralel...");

        // Un fir "coordonator" separat, ca fereastra sa nu se blocheze in timp ce asteptam.
        // Daca am apela join() direct aici, fereastra ar ingheta pana termina firele.
        Thread coordonator = new Thread(() -> {
            try {
                Student1 s1 = new Student1(mas, this);   // Manoli Bogdan
                Student2 s2 = new Student2(mas, this);   // Lavreca Artiom

                s1.pornesteFirele();
                s2.pornesteFirele();

                s1.asteaptaFirele();
                s2.asteaptaFirele();

                SwingUtilities.invokeLater(() -> status.setText(verificare()));

                // autorii, litera cu litera, abia dupa ce au terminat toate firele
                String text = "Lucrarea a fost realizata de: Manoli Bogdan si Lavreca Artiom";
                for (int i = 1; i <= text.length(); i++) {
                    String bucata = text.substring(0, i);
                    SwingUtilities.invokeLater(() -> autori.setText(bucata));
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                butonGenereaza.setEnabled(true);
                butonPorneste.setEnabled(true);
            });
        });
        coordonator.start();
    }

    private String verificare() {
        long b1 = sume.get("Bogdan-Th1"), a1 = sume.get("Artiom-Th1");
        long b2 = sume.get("Bogdan-Th2"), a2 = sume.get("Artiom-Th2");
        String th1 = (b1 == a1) ? "Th1: Bogdan = Artiom ✓" : "Th1: rezultate diferite!";
        String th2 = (b2 == a2) ? "Th2: Bogdan = Artiom ✓" : "Th2: rezultate diferite!";
        return "Toate firele au terminat.   " + th1 + "   ·   " + th2;
    }

    // ================= METODELE DIN INTERFATA Afisaj =================
    // Sunt apelate de FIRE, nu de fereastra. Componentele Swing au voie sa fie
    // modificate doar din firul ferestrei, de aceea trimitem modificarea prin invokeLater.

    public void afiseaza(String numeFir, String text) {
        SwingUtilities.invokeLater(() -> {
            carduri.get(numeFir).adaugaRand(text);
            adaugaInJurnal(numeFir + ":  " + text);
        });
    }

    public void sumaFinala(String numeFir, long suma) {
        SwingUtilities.invokeLater(() -> {
            sume.put(numeFir, suma);
            carduri.get(numeFir).termina(suma);
            adaugaInJurnal(numeFir + " a terminat. Suma finala a produselor: " + suma);
        });
    }

    // adauga un rand numerotat la sfarsitul listei si deruleaza pana la el
    private void adaugaInJurnal(String rand) {
        jurnal.addElement(String.format("%3d   %s", jurnal.size() + 1, rand));
        listaJurnal.ensureIndexIsVisible(jurnal.size() - 1);
    }

    // ================= COMPONENTE AJUTATOARE =================

    static JLabel eticheta(String text, int marime, int stil, Color culoare) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(FONT, stil, marime));
        l.setForeground(culoare);
        return l;
    }

    static JButton buton(String text, boolean principal) {
        JButton b = new JButton(text);
        b.setFont(new Font(FONT, Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setOpaque(true);
        b.setBorderPainted(false);
        if (principal) {
            b.setBackground(ALBASTRU);
            b.setForeground(Color.WHITE);
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(TEXT);
        }
        return b;
    }

    static JPanel punctLegenda(Color fundal, Color text, String descriere) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);
        JLabel patrat = new JLabel("  ");
        patrat.setOpaque(true);
        patrat.setBackground(fundal);
        patrat.setBorder(BorderFactory.createLineBorder(text));
        p.add(patrat);
        p.add(eticheta(descriere, 12, Font.PLAIN, TEXT_SLAB));
        return p;
    }

    // Un panou alb cu colturi rotunjite
    static class PanouRotunjit extends JPanel {
        private Color culoare;

        PanouRotunjit(Color culoare) {
            this.culoare = culoare;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(culoare);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(LINIE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Cardul unui fir: cine il face, directia, starea, ultima pereche si suma
    static class CardFir extends PanouRotunjit {
        private Color culoare;
        private JLabel stare;
        private JLabel perechi;
        private JLabel ultima;
        private JLabel suma;
        private int numarPerechi = 0;

        CardFir(String student, String directie, String metoda, Color culoare) {
            super(CARD);
            this.culoare = culoare;
            setLayout(new GridLayout(7, 1, 0, 2));
            setBorder(new EmptyBorder(12, 14, 12, 14));

            add(eticheta(student, 15, Font.BOLD, culoare));
            add(eticheta(directie, 12, Font.PLAIN, TEXT));
            add(eticheta(metoda, 12, Font.PLAIN, TEXT_SLAB));
            stare   = eticheta("asteapta", 12, Font.BOLD, TEXT_SLAB);
            perechi = eticheta("Perechi: 0", 12, Font.PLAIN, TEXT);
            ultima  = eticheta(" ", 12, Font.PLAIN, TEXT_SLAB);
            suma    = eticheta("Suma:  -", 16, Font.BOLD, TEXT);
            add(stare);
            add(perechi);
            add(ultima);
            add(suma);
        }

        void reseteaza() {
            numarPerechi = 0;
            perechi.setText("Perechi: 0");
            ultima.setText(" ");
            suma.setText("Suma:  -");
            stare.setText("asteapta");
            stare.setForeground(TEXT_SLAB);
        }

        void porneste() {
            stare.setText("\u25CF  ruleaza");
            stare.setForeground(culoare);
        }

        void adaugaRand(String text) {
            numarPerechi++;
            perechi.setText("Perechi: " + numarPerechi);
            ultima.setText(text.substring(0, text.indexOf("   (")));   // doar "a * b = produs"
        }

        void termina(long valoare) {
            suma.setText("Suma:  " + valoare);
            stare.setText("\u2713  gata");
            stare.setForeground(new Color(20, 150, 80));
        }
    }

    // Cum arata un rand din lista: albastru pentru Bogdan, verde pentru Artiom,
    // ingrosat cand un fir a terminat, cu dungi alternative pentru citire usoara
    static class RandJurnal extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> lista, Object valoare,
                                                      int index, boolean selectat, boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(lista, valoare, index, selectat, focus);
            String text = valoare.toString();
            boolean final_ = text.contains("a terminat");
            l.setFont(new Font(Font.MONOSPACED, final_ ? Font.BOLD : Font.PLAIN, 13));
            l.setForeground(text.contains("Bogdan") ? ALBASTRU : VERDE);
            l.setBackground(index % 2 == 0 ? Color.WHITE : new Color(246, 248, 251));
            if (final_) {
                l.setBackground(new Color(234, 247, 238));
            }
            l.setBorder(new EmptyBorder(3, 8, 3, 8));
            return l;
        }
    }

    // ================= PORNIREA PROGRAMULUI =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Fereastra().setVisible(true));
    }
}
