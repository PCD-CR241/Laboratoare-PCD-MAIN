import Stecalovici.GuiFrame;
import Stecalovici.LThread;


public class Main {

        private static final String INFO = "Lucrare de laborator 1 - Varianta 8\n"
                + "Student 1: Stecalovici Lucian, grupa CR-241\n"
                + "Student 2: Macovetchi Bogdan, grupa CR-241\n";

        private final GuiFrame gui = new GuiFrame();
        private volatile Thread th1, th2;
        private volatile Thread runner;

        public static void main(String[] args) throws Exception {
                Main lab = new Main();

                lab.gui.build(lab::reset);
                lab.runAll();
        }

        public void runAll() {
                runner = Thread.currentThread();
                try {
                        gui.showArray(LThread.generateArray());

                        th1 = new Thread(new LThread(0, +1, gui), "Th1");
                        th2 = new Thread(new LThread(LThread.SIZE - 1, -1, gui), "Th2");
                        th1.start();
                        th2.start();

                        for (char c : INFO.toCharArray()) {
                                gui.append(String.valueOf(c));
                                Thread.sleep(100);
                        }
                } catch (InterruptedException e) {
                }
        }

        private void reset() {
                gui.setResetEnabled(false);
                new Thread(() -> {
                        stopAndJoin(runner);
                        stopAndJoin(th1);
                        stopAndJoin(th2);
                        runner = Thread.currentThread();

                        gui.clear();
                        runAll();
                }, "Main-reset").start();
        }

        private static void stopAndJoin(Thread t) {
                if (t == null) return;
                t.interrupt();
                try {
                        t.join();
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }
        }
}
