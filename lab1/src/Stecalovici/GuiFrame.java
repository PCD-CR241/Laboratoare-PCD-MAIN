package Stecalovici;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class GuiFrame {

        private JTextArea areaTab, areaOut;

        public void build() throws Exception {
                SwingUtilities.invokeAndWait(() -> {
                        JFrame frame = new JFrame("Laborator 1 - Thread-uri (Varianta 8)");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BorderLayout(8, 8));

                        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);

                        areaTab = new JTextArea(4, 60);
                        areaTab.setEditable(false);
                        areaTab.setLineWrap(true);
                        areaTab.setFont(mono);
                        JScrollPane spMas = new JScrollPane(areaTab);
                        spMas.setBorder(BorderFactory.createTitledBorder("Tabloul  (100 elemente)"));

                        // Panoul comun in care scriu concomitent toate thread-urile
                        areaOut = new JTextArea();
                        areaOut.setEditable(false);
                        areaOut.setLineWrap(true);
                        areaOut.setFont(mono);
                        JScrollPane spOut = new JScrollPane(areaOut);
                        spOut.setBorder(BorderFactory.createTitledBorder("Iesirea thread-urilor (Th1-Th4, main)"));


                        frame.add(spMas, BorderLayout.NORTH);
                        frame.add(spOut, BorderLayout.CENTER);

                        frame.setSize(900, 650);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                });
        }

        public void showArray(int[] arr) {

                SwingUtilities.invokeLater(() -> areaTab.setText(Arrays.toString(arr)));
        }

        public void append(String text) {
                SwingUtilities.invokeLater(() -> areaOut.append(text));
        }



        // Curata zona de iesire si reactiveaza butonul Reset
        public void clear() {
                SwingUtilities.invokeLater(() -> {
                        areaOut.setText("");
                });
        }
}
