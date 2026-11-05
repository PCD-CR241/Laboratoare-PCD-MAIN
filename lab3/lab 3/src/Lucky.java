import java.util.*;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;


class Lucky extends JFrame{
    private JTextPane mirror;
    private StyledDocument doc;

    public Lucky(String lobrada){
        setTitle(lobrada);
        setSize(800,500);
        setLocation(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mirror= new JTextPane();
        mirror.setEditable(false);
        mirror.setBackground( Color.decode("#e8e8e8"));
        doc = mirror.getStyledDocument();//get document in care este pastrat textul cu toate atributele
        add(new JScrollPane(mirror));


    }
    public void write(String arg, String c){
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet style = new SimpleAttributeSet(); //implimentare a MutableAttributeSet- colectie de atribute
            StyleConstants.setForeground(style, Color.decode(c));  //atribuirea atributului de foreground la style cu valoare de coloare.decode(c);
            //StyleConstant- "A collection of well known or common attribute keys and methods to apply to an AttributeSet or MutableAttributeSet to get/set the properties"
            //adica instrument de atribuire a atributelor pentru element ce implimenteaza MutableAttributeSet
            try {
                doc.insertString(  //in doc este pastrat textul, noi inseram text nou care(pozitie-capat vechi,text -mesaj+enter, colectie de atribute a acestui bloc de text)
                        doc.getLength(),
                        arg + "\n",
                        style
                );

                mirror.setCaretPosition(doc.getLength());//seteaza indicatorul la end of file
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }
}