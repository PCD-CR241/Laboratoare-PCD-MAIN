package Stecalovici;

import javax.swing.*;
import java.awt.*;

/**
 * Interfata grafica: tabloul mas[], zonele pentru Th1/Th2,
 * zona thread-ului principal si butonul Reset.
 */
public class GuiFrame {

        private JTextArea areaMas, areaTh1, areaTh2, areaInfo;
        private JLabel lblTh1, lblTh2;
        private JButton b1;

        public void build(Runnable onReset) throws Exception {
                SwingUtilities.invokeAndWait(() -> {
                        JFrame frame = new JFrame("Laborator 1 - Thread-uri (Varianta 8)");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BorderLayout(8, 8));

                        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);

                        areaMas = new JTextArea(4, 60);
                        areaMas.setEditable(false);
                        areaMas.setLineWrap(true);
                        areaMas.setFont(mono);
                        JScrollPane spMas = new JScrollPane(areaMas);
                        spMas.setBorder(BorderFactory.createTitledBorder("Tabloul mas[] (100 elemente)"));

                        areaTh1 = new JTextArea();
                        areaTh2 = new JTextArea();
                        areaTh1.setEditable(false);
                        areaTh2.setEditable(false);
                        areaTh1.setFont(mono);
                        areaTh2.setFont(mono);

                        lblTh1 = new JLabel("Th1: se executa...");
                        lblTh2 = new JLabel("Th2: se executa...");

                        JPanel p1 = new JPanel(new BorderLayout());
                        p1.setBorder(BorderFactory.createTitledBorder("Th1 - de la primul element"));
                        p1.add(new JScrollPane(areaTh1), BorderLayout.CENTER);
                        p1.add(lblTh1, BorderLayout.SOUTH);

                        JPanel p2 = new JPanel(new BorderLayout());
                        p2.setBorder(BorderFactory.createTitledBorder("Th2 - de la ultimul element"));
                        p2.add(new JScrollPane(areaTh2), BorderLayout.CENTER);
                        p2.add(lblTh2, BorderLayout.SOUTH);

                        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));
                        center.add(p1);
                        center.add(p2);

                        areaInfo = new JTextArea(5, 60);
                        areaInfo.setEditable(false);
                        areaInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
                        JScrollPane spInfo = new JScrollPane(areaInfo);
                        spInfo.setBorder(BorderFactory.createTitledBorder("Thread-ul principal"));

                        frame.add(spMas, BorderLayout.NORTH);
                        frame.add(center, BorderLayout.CENTER);
                        b1 = new JButton("Reset");
                        b1.addActionListener(e -> onReset.run());

                        JPanel south = new JPanel(new BorderLayout(8, 8));
                        south.add(spInfo, BorderLayout.CENTER);
                        south.add(b1, BorderLayout.EAST);

                        frame.add(south, BorderLayout.SOUTH);

                        frame.setSize(900, 650);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                });
        }

        public JTextArea getAreaTh1() { return areaTh1; }
        public JTextArea getAreaTh2() { return areaTh2; }
        public JLabel getLblTh1() { return lblTh1; }
        public JLabel getLblTh2() { return lblTh2; }

        public void showArray(String text) {
                SwingUtilities.invokeLater(() -> areaMas.setText(text));
        }

        public void appendInfoChar(char c) {
                SwingUtilities.invokeLater(() -> areaInfo.append(String.valueOf(c)));
        }

        public void setResetEnabled(boolean enabled) {
                SwingUtilities.invokeLater(() -> b1.setEnabled(enabled));
        }

        // Curata zonele de iesire si reactiveaza butonul Reset
        public void clear() {
                SwingUtilities.invokeLater(() -> {
                        areaTh1.setText("");
                        areaTh2.setText("");
                        areaInfo.setText("");
                        lblTh1.setText("Th1: se executa...");
                        lblTh2.setText("Th2: se executa...");
                        b1.setEnabled(true);
                });
        }
}
