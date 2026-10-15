package Stecalovici;

import javax.swing.*;
import java.awt.*;


public class GuiFrame {

        private JTextArea areaMas, areaOut;
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

                        // Panoul comun in care scriu concomitent toate thread-urile
                        areaOut = new JTextArea();
                        areaOut.setEditable(false);
                        areaOut.setLineWrap(true);
                        areaOut.setFont(mono);
                        JScrollPane spOut = new JScrollPane(areaOut);
                        spOut.setBorder(BorderFactory.createTitledBorder("Iesirea thread-urilor (Th1, Th2, main)"));

                        b1 = new JButton("Reset");
                        b1.addActionListener(e -> onReset.run());

                        frame.add(spMas, BorderLayout.NORTH);
                        frame.add(spOut, BorderLayout.CENTER);
                        frame.add(b1, BorderLayout.SOUTH);

                        frame.setSize(900, 650);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                });
        }

        public void showArray(String text) {
                SwingUtilities.invokeLater(() -> areaMas.setText(text));
        }

        public void append(String text) {
                SwingUtilities.invokeLater(() -> areaOut.append(text));
        }

        public void setResetEnabled(boolean enabled) {
                SwingUtilities.invokeLater(() -> b1.setEnabled(enabled));
        }

        // Curata zona de iesire si reactiveaza butonul Reset
        public void clear() {
                SwingUtilities.invokeLater(() -> {
                        areaOut.setText("");
                        b1.setEnabled(true);
                });
        }
}
