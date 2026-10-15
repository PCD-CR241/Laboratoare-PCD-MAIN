import java.util.*;
import javax.swing.text.*;
import javax.swing.*;
import java.awt.*;


class Luck extends JFrame{
    private JTextPane mirror;
    private StyledDocument doc;

    public Luck(){
        setTitle("Lab1");
        setSize(800,500);
        setLocation(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mirror= new JTextPane();
        mirror.setEditable(false);
        mirror.setBackground( Color.decode("#e8e8e8"));
        doc = mirror.getStyledDocument();//get document in care este pastrat textul cu toate atributele
        add(new JScrollPane(mirror));
        add(mirror);

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
class Numin extends Thread{
    String clr;
    byte t;
    int[] r;
    Luck mirror;

    public Numin(String name,String clr,byte t,int[] rabota,Luck ui){
        super(name);
        this.t=t;
        this.clr=clr;
        r=rabota;
        mirror=ui;
    }
    public void run(){
        boolean center=true, left=true, right=true; //idea paralelizm cu mecanica reala si separator simplu
        int p1 = 0, p2 = 0, s = 0;
        for(int i=99*t;(
                (i<100)&&(i>-1));
            i=i+1- 2*t){
            if (r[i]%2 == 1){
                if(center){
                    if(left){p1=i;left=false;}else{p1+=i;center=false;}}else{
                    if(right){p2=i;right=false;}else{
                        p2+=i; s=p1+p2;  mirror.write( getName()+": P1:"+p1+"  P2:"+p2+"  S=P1+P2="+s,clr);
                        center=left=right=true;
                    }};

            }
        }
    }
}
class Counter1 extends Thread
{
    private int dela, pana, pas;
    private int[] tablou;

    public Counter1(int dela, int pana, int pas, int[] tablou) {
        this.dela = dela;
        this.pana = pana;
        this.pas = pas;
        this.tablou = tablou;
    }

    public void run() {
        int s1 = -1, s2 = -1, s = 0;
        int i = dela;
        while (i < pana) {
            if (tablou[i] % 2 != 0) {
                s1 = i;
                i += pas;
                while (i < pana) {
                    if (tablou[i] % 2 != 0) {
                        s2 = i;
                        s = s1 + s2;
                        System.out.println(
                                getName() + " " +s1 + " " +s2 + " " +s + " " +tablou[s1] + " " +tablou[s2]);
                        break;}
                    i += pas;
                }}
            i += pas;
        }}}

public class Main {
    public static void main(String args[])

    {
        Luck Window = new Luck();
        Window.setVisible(true);

        Counter1 th1, th2;
        int[] tablou = new int[101];
        for(int i=0; i<100; i++){
            tablou[i] = (int)(Math.random()*99);
            System.out.print(tablou[i]+" ");
        }

        System.out.println(" ");
        th1 = new Counter1(0, 99, 1, tablou);
        th2 = new Counter1(99, 0, -1, tablou);
        th1.start();
        th1.setName("Unu");
        th2.start();
        th2.setName("Doi");

        Numin wonk1= new Numin("S1","#d40b0b",(byte)0,tablou,Window);
        wonk1.start();
        Numin wonk2= new Numin("S2","#03183d",(byte)1,tablou,Window);
        wonk2.start();
    }
}
