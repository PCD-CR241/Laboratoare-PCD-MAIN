//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
                Lucky Window = new Lucky("lab2");
               Window.setVisible(true);
               Collar D=new Collar("Test",2,300,1,Window,"#d40b0b");
               D.start();

    }
}

class Collar extends Thread{
    int a;
    int b;
    int p;
    String color;
    Lucky mimir;
    Collar(String name,int a,int b,int p,Lucky world,String pain){
        super(name);

        this.a= a<b ? a:b;
        this.b=a>b ? a:b;
        this.p=p;
        mimir=world;
        color = pain;
    }
    @Override
    public void run(){
        for (int i = p<0?b:a;(i<=b)&&(i>=a);i+=p){
            mimir.write(" "+i,color);
        }

    }

}