import java.util.*;
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
    while (i != pana) {
        if (tablou[i] % 2 != 0) {
            s1 = i;
            i += pas;
            while (i != pana) {
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
    }
}