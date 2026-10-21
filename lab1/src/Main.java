import Macovetchi.Shared;
import Macovetchi.MThread;
import Stecalovici.GuiFrame;
import Stecalovici.LThread;


public class Main {

        private static final String INFO = "Lucrare de laborator 1 - Varianta 8\n"
                + "Student 1: Stecalovici Lucian, grupa CR-241\n"
                + "Student 2: Macovetchi Bogdan, grupa CR-241\n";

        private final GuiFrame gui = new GuiFrame();
        private  Thread th1, th2, th3, th4;
        private static final int SIZE = 100;
        public static void main(String[] args) throws Exception {
                Main lab = new Main();

                lab.gui.build();
                lab.runAll();
        }

        public void runAll() {
//                runner = Thread.currentThread();
                try {
                        gui.showArray(Shared.tablou);

                        // Th1, Th2 - LThread (Stecalovici); Th3, Th4 - MThread (Macovetchi)
                        th1 = new Thread(new LThread(0, +1, gui,Shared.tablou), "Th1");
                        th2 = new Thread(new LThread(SIZE - 1, -1, gui,Shared.tablou), "Th2");
                        th3 = new Thread(new MThread(gui,0,+1, Shared.tablou), "Th3");
                        th4 = new Thread(new MThread(gui, SIZE-1, -1,Shared.tablou), "Th4");
                        th1.start();
                        th2.start();
                        th3.start();
                        th4.start();
                        th1.join();
                        th2.join();
                        th3.join();
                        th4.join();

                        for (char c : INFO.toCharArray()) {
                                gui.append(String.valueOf(c));
                                Thread.sleep(100);
                        }
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }




}

