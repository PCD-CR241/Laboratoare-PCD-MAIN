//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {



        My work=new My("main");
        work.setDaemon(true);
        work.start();

        System.out.println(work.isDaemon());
        Thread d=new Thread("f");
        d.start();
        try {
            d.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }







    }
}

class My extends Thread{


    My( String  name,int i){
        super(name);
        setPriority(i);
        start();
    }
    My(String name){
        super(name);

    }
    @Override
      public void run(){
        ThreadGroup sistem =
                Thread.currentThread().getThreadGroup();
        //sistem.list();
        sistem.setMaxPriority(Thread.MAX_PRIORITY- 1);
       // sistem.list();

        ThreadGroup GF=new ThreadGroup("GF");

        ThreadGroup GO=new ThreadGroup("GO");

        ThreadGroup GZ=new ThreadGroup(GO,"GZ");

        Thread main = Thread.currentThread();//fir etalon
        Thread Th2 = new Threada("Th2");
        Th2.start();
        //System.out.println(Th2.isInterrupted());

        //try {
        //    Th2.wait();
         //   System.out.println(Th2.isInterrupted());
        //}catch (InterruptedException e){ e.printStackTrace();}




        //GF

        Thread Th11=new Threada(GF,"Th1");
        Th11.start();
        main.setPriority(9);
        Thread Th31=new Threada(GF,"Th3");
        Th31.setPriority(Thread.MAX_PRIORITY);
        Th31.start();
        Thread Th21=new Threada(GF,"Th2");
        //Th21.setPriority(3);
        Th21.start();


        main.setPriority(7);
        //GZ
        Thread Thd=new Threada(GZ,"Thd");
        Thd.start();
        main.setPriority(3);
        Thread Tha=new Threada(GZ,"Tha");
        Tha.setPriority(1);
        Tha.start();
        Thread Thb=new Threada(GZ,"Thb");
        Thb.start();
        Thread Thc=new Threada(GZ,"Thc");
        Thc.start();

        Thread Th1=new Threada("Th1");
        Th1.setDaemon(true);
        Th1.start();

        ThreadGroup GV=new ThreadGroup("GV");
        Thread ThA=new Thread(GV,"ThA");
        ThA.start();

        sistem.list();
        //GF.list();
        //GV.list();
        //GO.list();
        //GZ.list();

      }
}
class Threada extends Thread{
    Threada(String name){
        super(name);
    }
    Threada(ThreadGroup h,String name){
        super(h,name);
    }
    @Override
    public void run(){
        while(2+2==4){
            int t=3;
        }
    }
}
