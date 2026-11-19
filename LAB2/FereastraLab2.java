import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

// Interfata grafica pentru Laboratorul 2, varianta 7.
// Se porneste de aici: Run pe metoda main din aceasta clasa.

public class FereastraLab2 extends JFrame implements Jurnal {

    // ---------- culori si fonturi ----------
    static final Color FUNDAL    = new Color(244, 246, 250);
    static final Color CARD      = Color.WHITE;
    static final Color TEXT      = new Color(30, 36, 48);
    static final Color TEXT_SLAB = new Color(110, 118, 132);
    static final Color LINIE     = new Color(225, 229, 236);
    static final Color ALBASTRU  = new Color(47, 111, 219);   // nivel mediu
    static final Color VERDE     = new Color(20, 150, 125);   // nivel avansat
    static final String FONT = "Segoe UI";

    // numele firelor si grupurilor din varianta 7 (ca sa nu afisam firele interne ale Java/Swing)
    static final HashSet<String> FIRELE_NOASTRE = new HashSet<>(Arrays.asList(
            "Coordonator", "ThA", "Th11", "Th22", "Th1", "Th2", "Th33", "Thaa", "Thbb", "Thcc", "Thdd"));
    static final HashSet<String> GRUPURILE_NOASTRE = new HashSet<>(Arrays.asList("G1", "G2", "G3"));

    // ---------- componente ----------
    private JButton butonMediu;
    private JButton butonAvansat;
    private JLabel status;
    private JTree schema;
    private DefaultTableModel modelTabel;
    private DefaultListModel<String> jurnal = new DefaultListModel<>();
    private JList<String> listaJurnal;
    private ArrayList<Thread> fireAfisate = new ArrayList<>();
    private Timer actualizare;   // reimprospateaza coloana "Stare" din tabel

    public FereastraLab2() {
        super("Laboratorul 2 - Grupuri de fire si prioritati");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 800);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);

        JPanel radacina = new JPanel(new BorderLayout(0, 16));
        radacina.setBackground(FUNDAL);
        radacina.setBorder(new EmptyBorder(20, 24, 20, 24));
        setContentPane(radacina);

        radacina.add(creeazaAntet(), BorderLayout.NORTH);
        radacina.add(creeazaCentru(), BorderLayout.CENTER);

        status = eticheta("Alege un nivel pentru a construi structura.", 13, Font.PLAIN, TEXT_SLAB);
        radacina.add(status, BorderLayout.SOUTH);

        actualizare = new Timer(300, e -> actualizeazaStari());
    }

    // ================= CONSTRUIREA FERESTREI =================

    private JPanel creeazaAntet() {
        JPanel antet = new JPanel(new BorderLayout());
        antet.setOpaque(false);

        JPanel texte = new JPanel(new GridLayout(2, 1));
        texte.setOpaque(false);
        texte.add(eticheta("Grupuri de fire si prioritati", 26, Font.BOLD, TEXT));
        texte.add(eticheta("Varianta 7  ·  Manoli Bogdan  ·  Lavreca Artiom", 15, Font.PLAIN, TEXT_SLAB));
        antet.add(texte, BorderLayout.WEST);

        JPanel butoane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        butoane.setOpaque(false);
        butonMediu   = buton("Nivel mediu  ▶", ALBASTRU);
        butonAvansat = buton("Nivel avansat  ▶", VERDE);
        butonMediu.addActionListener(e -> ruleaza(false));
        butonAvansat.addActionListener(e -> ruleaza(true));
        butoane.add(butonMediu);
        butoane.add(butonAvansat);
        antet.add(butoane, BorderLayout.EAST);
        return antet;
    }

    private JPanel creeazaCentru() {
        JPanel centru = new JPanel(new BorderLayout(16, 0));
        centru.setOpaque(false);

        // stanga: schema, construita din grupurile reale din Java
        PanouRotunjit panouSchema = new PanouRotunjit();
        panouSchema.setLayout(new BorderLayout(0, 10));
        panouSchema.setBorder(new EmptyBorder(14, 16, 14, 16));
        panouSchema.setPreferredSize(new Dimension(330, 0));
        panouSchema.add(titluPanou("Schema", "construita din grupurile reale"), BorderLayout.NORTH);

        schema = new JTree(new DefaultMutableTreeNode("apasa un buton"));
        schema.setCellRenderer(new NodSchema());
        schema.setRowHeight(26);
        schema.setBorder(new EmptyBorder(6, 4, 6, 4));
        JScrollPane scrollSchema = new JScrollPane(schema);
        scrollSchema.setBorder(BorderFactory.createLineBorder(LINIE));
        panouSchema.add(scrollSchema, BorderLayout.CENTER);
        centru.add(panouSchema, BorderLayout.WEST);

        // dreapta sus: tabelul firelor
        JPanel dreapta = new JPanel(new GridLayout(2, 1, 0, 16));
        dreapta.setOpaque(false);

        PanouRotunjit panouTabel = new PanouRotunjit();
        panouTabel.setLayout(new BorderLayout(0, 10));
        panouTabel.setBorder(new EmptyBorder(14, 16, 14, 16));
        panouTabel.add(titluPanou("Firele de executie", "nume, grup, prioritate si starea in timp real"),
                BorderLayout.NORTH);

        modelTabel = new DefaultTableModel(new String[]{"Fir", "Grup", "Prioritate", "Stare"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabel = new JTable(modelTabel);
        tabel.setRowHeight(26);
        tabel.setFont(new Font(FONT, Font.PLAIN, 13));
        tabel.getTableHeader().setFont(new Font(FONT, Font.BOLD, 13));
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(LINIE);
        tabel.setDefaultRenderer(Object.class, new CelulaTabel());
        JScrollPane scrollTabel = new JScrollPane(tabel);
        scrollTabel.setBorder(BorderFactory.createLineBorder(LINIE));
        panouTabel.add(scrollTabel, BorderLayout.CENTER);
        dreapta.add(panouTabel);

        // dreapta jos: jurnalul programului
        PanouRotunjit panouJurnal = new PanouRotunjit();
        panouJurnal.setLayout(new BorderLayout(0, 10));
        panouJurnal.setBorder(new EmptyBorder(14, 16, 14, 16));
        panouJurnal.add(titluPanou("Jurnalul programului", "rezultatul lui list() dupa fiecare pas (maxpri = prioritatea grupului)"),
                BorderLayout.NORTH);

        listaJurnal = new JList<>(jurnal);
        listaJurnal.setCellRenderer(new RandJurnal());
        JScrollPane scrollJurnal = new JScrollPane(listaJurnal);
        scrollJurnal.setBorder(BorderFactory.createLineBorder(LINIE));
        panouJurnal.add(scrollJurnal, BorderLayout.CENTER);
        dreapta.add(panouJurnal);

        centru.add(dreapta, BorderLayout.CENTER);
        return centru;
    }

    // ================= RULAREA UNUI NIVEL =================

    private void ruleaza(boolean avansat) {
        butonMediu.setEnabled(false);
        butonAvansat.setEnabled(false);
        jurnal.clear();
        modelTabel.setRowCount(0);
        fireAfisate.clear();
        status.setText(avansat ? "Nivel avansat: firele isi suprascriu run() si se prezinta singure..."
                               : "Nivel mediu: firele primesc un Runnable, run() din Thread nu e suprascris...");

        // Structura se construieste intr-un fir separat, ca fereastra sa nu se blocheze.
        // Firul se numeste "Coordonator" si face parte tot din grupul main (sys).
        Thread coordonator = new Thread(() -> {
            try {
                // la a doua rulare, grupul main are inca maxpri=9 de la rularea anterioara;
                // il readucem la 10, ca fiecare rulare sa porneasca de la acelasi etalon
                Thread.currentThread().getThreadGroup().setMaxPriority(Thread.MAX_PRIORITY);
                ThreadGroup sys = avansat ? new NivelAvansat(this).construieste()
                                          : NivelMediu.construieste(this);
                SwingUtilities.invokeLater(() -> afiseazaStructura(sys));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Coordonator");
        coordonator.start();
    }

    // dupa construire: umple schema si tabelul cu ce exista acum in Java
    private void afiseazaStructura(ThreadGroup sys) {
        DefaultMutableTreeNode radacina = nodGrup(sys);
        schema.setModel(new DefaultTreeModel(radacina));
        for (int i = 0; i < schema.getRowCount(); i++) {
            schema.expandRow(i);   // deschidem toate ramurile
        }

        Thread[] toate = new Thread[sys.activeCount() + 10];
        int n = sys.enumerate(toate, true);
        for (int i = 0; i < n; i++) {
            if (FIRELE_NOASTRE.contains(toate[i].getName())) {
                fireAfisate.add(toate[i]);
                modelTabel.addRow(new Object[]{toate[i].getName(), toate[i].getThreadGroup().getName(),
                        toate[i].getPriority(), stare(toate[i])});
            }
        }
        actualizare.start();
    }

    // transforma un ThreadGroup intr-un nod de arbore (recursiv, cu subgrupurile lui)
    private DefaultMutableTreeNode nodGrup(ThreadGroup g) {
        DefaultMutableTreeNode nod = new DefaultMutableTreeNode(
                new ElementSchema(g.getName() + "   maxpri " + g.getMaxPriority(), true));

        Thread[] fire = new Thread[g.activeCount() + 10];
        int n = g.enumerate(fire, false);
        for (int i = 0; i < n; i++) {
            if (FIRELE_NOASTRE.contains(fire[i].getName())) {
                nod.add(new DefaultMutableTreeNode(
                        new ElementSchema(fire[i].getName() + "  (" + fire[i].getPriority() + ")", false)));
            }
        }
        ThreadGroup[] grupuri = new ThreadGroup[g.activeGroupCount() + 10];
        int m = g.enumerate(grupuri, false);
        for (int i = 0; i < m; i++) {
            if (GRUPURILE_NOASTRE.contains(grupuri[i].getName()) && grupuri[i].activeCount() > 0) {
                nod.add(nodGrup(grupuri[i]));
            }
        }
        return nod;
    }

    // la fiecare 300 ms actualizam starea firelor; cand toate au terminat, reactivam butoanele
    private void actualizeazaStari() {
        boolean toateGata = true;
        for (int i = 0; i < fireAfisate.size(); i++) {
            Thread t = fireAfisate.get(i);
            modelTabel.setValueAt(stare(t), i, 3);
            if (t.isAlive()) {
                toateGata = false;
            }
        }
        if (toateGata) {
            actualizare.stop();
            butonMediu.setEnabled(true);
            butonAvansat.setEnabled(true);
            status.setText("Toate firele au terminat. Poti rula din nou oricare nivel.");
        }
    }

    static String stare(Thread t) {
        switch (t.getState()) {
            case NEW:           return "nou";
            case RUNNABLE:      return "ruleaza";
            case TIMED_WAITING: return "doarme (sleep)";
            case WAITING:       return "asteapta";
            case BLOCKED:       return "blocat";
            default:            return "terminat";
        }
    }

    // ================= METODELE DIN INTERFATA Jurnal =================
    // Sunt apelate din firul Coordonator si din firele create; fereastra se modifica doar prin invokeLater.

    public void mesaj(String text) {
        if (!esteDinVarianta7(text)) {
            return;   // sarim peste firele interne ale Java/Swing (ex: AWT-EventQueue-0)
        }
        SwingUtilities.invokeLater(() -> adaugaInJurnal(text));
    }

    // in enumerare apar si firele ferestrei; pastram doar randurile despre firele noastre
    private boolean esteDinVarianta7(String text) {
        String t = text.trim();
        String nume = null;
        if (t.startsWith("Fir: ")) {
            nume = t.substring(5, t.indexOf(" |"));
        } else if (t.startsWith("fir ")) {
            nume = t.substring(4, t.indexOf(","));
        }
        return nume == null || FIRELE_NOASTRE.contains(nume);
    }

    // Aici apelam CHIAR grup.list() din Java. list() scrie in System.out, asa ca
    // redirectam System.out pentru o clipa, prindem textul si il punem in jurnal.
    public synchronized void lista(ThreadGroup grup) {
        PrintStream consola = System.out;
        ByteArrayOutputStream prins = new ByteArrayOutputStream();
        System.setOut(new PrintStream(prins, true));
        try {
            grup.list();
        } finally {
            System.setOut(consola);   // consola revine intotdeauna la loc
        }
        ArrayList<String> randuri = filtreaza(prins.toString().split("\\R"));
        SwingUtilities.invokeLater(() -> {
            for (String r : randuri) {
                adaugaInJurnal("    " + r);
            }
        });
    }

    // list() arata si firele interne ale ferestrei (AWT-EventQueue-0 etc.) -> le scoatem.
    // Un rand de grup ramane doar daca sub el exista macar un fir de-al nostru.
    private ArrayList<String> filtreaza(String[] randuri) {
        boolean[] pastrat = new boolean[randuri.length];
        for (int i = 0; i < randuri.length; i++) {
            String t = randuri[i].trim();
            if (t.startsWith("Thread[")) {
                String[] parti = t.substring(7, t.length() - 1).split(",");   // #22, Th1, 5, G2
                pastrat[i] = parti.length >= 2 && FIRELE_NOASTRE.contains(parti[1]);
            }
        }
        for (int i = 0; i < randuri.length; i++) {
            if (randuri[i].trim().contains("ThreadGroup[")) {
                int nivel = indentare(randuri[i]);
                boolean areFire = false;
                for (int k = i + 1; k < randuri.length && indentare(randuri[k]) > nivel; k++) {
                    if (pastrat[k]) {
                        areFire = true;
                    }
                }
                pastrat[i] = areFire || i == 0;   // primul rand = grupul pe care l-am cerut
            }
        }
        ArrayList<String> rezultat = new ArrayList<>();
        for (int i = 0; i < randuri.length; i++) {
            if (pastrat[i] && !randuri[i].isBlank()) {
                rezultat.add(randuri[i]);
            }
        }
        return rezultat;
    }

    private static int indentare(String r) {
        int n = 0;
        while (n < r.length() && r.charAt(n) == ' ') {
            n++;
        }
        return n;
    }

    private void adaugaInJurnal(String rand) {
        jurnal.addElement(rand);
        listaJurnal.ensureIndexIsVisible(jurnal.size() - 1);
    }

    // ================= COMPONENTE AJUTATOARE =================

    static JLabel eticheta(String text, int marime, int stil, Color culoare) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(FONT, stil, marime));
        l.setForeground(culoare);
        return l;
    }

    static JPanel titluPanou(String titlu, String descriere) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        p.add(eticheta(titlu, 15, Font.BOLD, TEXT));
        p.add(eticheta(descriere, 12, Font.PLAIN, TEXT_SLAB));
        return p;
    }

    static JButton buton(String text, Color culoare) {
        JButton b = new JButton(text);
        b.setFont(new Font(FONT, Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setBackground(culoare);
        b.setForeground(Color.WHITE);
        return b;
    }

    // Un panou alb cu colturi rotunjite
    static class PanouRotunjit extends JPanel {
        PanouRotunjit() {
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(LINIE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ce tine minte un nod din schema: textul si daca e grup sau fir
    static class ElementSchema {
        String text;
        boolean esteGrup;

        ElementSchema(String text, boolean esteGrup) {
            this.text = text;
            this.esteGrup = esteGrup;
        }

        public String toString() { return text; }
    }

    // cum arata un nod din schema: grupurile ingrosate, firele cu un punct colorat
    static class NodSchema extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree arbore, Object valoare, boolean selectat,
                                                      boolean extins, boolean frunza, int rand, boolean focus) {
            super.getTreeCellRendererComponent(arbore, valoare, selectat, extins, frunza, rand, focus);
            Object obiect = ((DefaultMutableTreeNode) valoare).getUserObject();
            setIcon(null);
            if (obiect instanceof ElementSchema && ((ElementSchema) obiect).esteGrup) {
                setFont(new Font(FONT, Font.BOLD, 14));
                setForeground(selectat ? Color.WHITE : TEXT);
                setText("▣  " + obiect);
            } else {
                setFont(new Font(FONT, Font.PLAIN, 14));
                setForeground(selectat ? Color.WHITE : ALBASTRU);
                setText("●  " + obiect);
            }
            return this;
        }
    }

    // celulele tabelului: dungi alternative, starea colorata
    static class CelulaTabel extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object valoare, boolean selectat,
                                                       boolean focus, int rand, int coloana) {
            super.getTableCellRendererComponent(t, valoare, selectat, focus, rand, coloana);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            if (!selectat) {
                setBackground(rand % 2 == 0 ? Color.WHITE : new Color(246, 248, 251));
                setForeground(TEXT);
                if (coloana == 3) {
                    String s = String.valueOf(valoare);
                    setForeground(s.equals("terminat") ? TEXT_SLAB : VERDE);
                }
            }
            setFont(new Font(FONT, coloana == 0 ? Font.BOLD : Font.PLAIN, 13));
            return this;
        }
    }

    // randurile jurnalului: pasii ingrosati, list() gri, mesajele din run() verzi
    static class RandJurnal extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> lista, Object valoare,
                                                      int index, boolean selectat, boolean focus) {
            JLabel l = (JLabel) super.getListCellRendererComponent(lista, valoare, index, selectat, focus);
            String text = valoare.toString();
            l.setBorder(new EmptyBorder(2, 8, 2, 8));
            l.setBackground(index % 2 == 0 ? Color.WHITE : new Color(246, 248, 251));
            if (text.startsWith("(") || text.startsWith("[")) {
                l.setFont(new Font(FONT, Font.BOLD, 13));
                l.setForeground(TEXT);
            } else if (text.startsWith("Sunt firul")) {
                l.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
                l.setForeground(VERDE);
                l.setBackground(new Color(234, 247, 238));
            } else if (text.contains("ThreadGroup[")) {
                l.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
                l.setForeground(ALBASTRU);
            } else if (text.startsWith("Fir:")) {
                l.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
                l.setForeground(ALBASTRU);
            } else {
                l.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
                l.setForeground(TEXT_SLAB);
            }
            return l;
        }
    }

    // ================= PORNIREA PROGRAMULUI =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FereastraLab2().setVisible(true));
    }
}
