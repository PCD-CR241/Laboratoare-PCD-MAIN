import java.util.*;

public class ProducatorConsumator {
    public static void main(String[] args){
        Depozit depozit = new Depozit();
        Consumator c1 = new Consumator(depozit,"Cons1");
        Consumator c2 = new Consumator(depozit,"Cons2");
        Producator p1 = new Producator(depozit,"Prod1");
        Producator p2 = new Producator(depozit,"Prod2");
        Producator p3 = new Producator(depozit,"Prod3");
        Producator p4 = new Producator(depozit,"Prod4");
        c1.start();
        c2.start();
        p1.start();
        p2.start();
        p3.start();
        p4.start();

    }
}

class Depozit{
    private final int SIZE=5;
    Queue<Character> data;

    public Depozit(){
        data = new ArrayDeque<>(SIZE);
    }

    public boolean isFull(){
        return data.size()==SIZE;
    }
    public boolean isEmpty(){ return data.size()==0;}
    public synchronized Character get(){
        while(isEmpty()){
            System.out.println("Depozitul e gol!");
            try{
                wait();
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        Character c = data.poll();
        notifyAll();
        return c;
    }
    public synchronized void store(char caracter){
        while(isFull()){
            System.out.println("Depozitul e full!");
            try{
                wait();

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        data.offer(caracter);
        notifyAll();
    }
}

class Consumator extends Thread{
    private Depozit depozit;
    private int counter=0;

    public Consumator(Depozit d,String nume){
        super(nume);
        depozit = d;
    }
    public void run(){
        while(counter < 10){
            Character c = depozit.get();
            counter++;
            System.out.println(this.getName() + " a consumat " + c);
        }
        System.out.println(this.getName() + " a consumat " + counter + " consoane");
    }
}
class Producator extends Thread{
    private Depozit depozit;
    private String consoane = "bcdfghjklmnpqrstvwxz";
    Producator(Depozit d, String nume){
        super(nume);
        depozit = d;
        setDaemon(true);
    }

    public void run(){
        while(true){
            for(int i =0;i<2;i++){
                Character c = consoane.charAt((int)(Math.random() * consoane.length()));
                depozit.store(c);
                System.out.println(this.getName()+ " a produs caracterul " + c);
            }
        }
    }
}
