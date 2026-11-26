package com.laborator;

/*

Varianta 2

Sunt dați 3 producători care generează aleatoriu câte 1 obiect care sunt consumate de 4
consumatori. De afişat informaţia despre producerea şi consumarea obiectelor,
mesajele despre cazurile când “depozitul e gol sau plin”.
Dimensiunea depozitului este 5 obiecte.
Producătorii completează depozitul cu 5 obiecte. Consumatorii nu pot consuma,
până când depozitul nu va fi plin. După care consumatorii consumă.
Producătorii nu pot produce, până când depozitul nu va fi gol.
Toate operaţiile se efectuează până când nu vor fi produse și consumate 45 de obiecte.

Sincronizarea se realizează numai prin clase de sincronizare din Java
(ReentrantLock și Condition).

Tip obiecte: Numere impare

Producator: Bargan Ilie
Consumator: Țurcan Ion

*/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

    // Valorile din varianta 2
    static final int X = 3;    // producatori
    static final int Y = 4;    // consumatori
    static final int Z = 45;   // obiecte produse si consumate in total
    static final int D = 5;    // dimensiunea depozitului
    static final int F = 1;    // cate obiecte produce un producator o data

    static Deposit depozit = new Deposit(D, Z);

    // Toate firele pornite (producatori + consumatori); raportul final asteapta dupa ele
    static List<Thread> fire = new ArrayList<>();

    // Elementele interfetei
    static ObservableList<LogEntry> logProducatori = FXCollections.observableArrayList();
    static ObservableList<LogEntry> logConsumatori = FXCollections.observableArrayList();
    static Label[] locuri = new Label[D];
    static Label stareDepozit = new Label();
    static Label fazaDepozit = new Label();
    static Label baraStare = new Label();
    static Map<String, Label[]> carduri = new HashMap<>();   // nume thread -> {stare, contor}
    static long timpStart;
    static long timpFinal = -1;

    public void start(Stage stage){

        BorderPane bdRoot = new BorderPane();
        bdRoot.setPadding(new Insets(16));
        Scene scene = new Scene(bdRoot, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Sus: depozitul cu cele D locuri, faza si butonul Start
        Button btnStart = new Button("Start");
        btnStart.getStyleClass().add("start-btn");
        btnStart.setOnAction(e -> {
            btnStart.setDisable(true);
            pornesteThreadurile();
        });
        bdRoot.setTop(createDepositPane(btnStart));

        // Centru: producatorii (stanga) si consumatorii (dreapta)
        HBox cardProd = new HBox(10);
        for (int i = 1; i <= X; i++){
            cardProd.getChildren().add(createCard("Producător " + i, "p" + i, "produs"));
        }
        HBox cardCons = new HBox(10);
        for (int i = 1; i <= Y; i++){
            cardCons.getChildren().add(createCard("Consumator " + i, "c" + i, "consumat"));
        }

        SplitPane sp = new SplitPane(
                createPane("Producători (" + X + ") — Bargan Ilie", cardProd, createLogView(logProducatori)),
                createPane("Consumatori (" + Y + ") — Țurcan Ion", cardCons, createLogView(logConsumatori)));
        sp.setDividerPosition(0, 0.5);
        BorderPane.setMargin(sp, new Insets(16, 0, 16, 0));
        bdRoot.setCenter(sp);

        // Jos: totalurile si timpul
        baraStare.getStyleClass().add("status-bar");
        baraStare.setMaxWidth(Double.MAX_VALUE);
        bdRoot.setBottom(baraStare);

        depozit.lock.lock();
        try {
            showDeposit(depozit);
        } finally {
            depozit.lock.unlock();
        }

        stage.setTitle("Sincronizarea firelor prin clase de sincronizare");
        stage.setScene(scene);
        stage.show();
    }

    // Creeaza si porneste thread-urile
    private static void pornesteThreadurile(){

        timpStart = System.currentTimeMillis();

        for (int i = 1; i <= X; i++){
            Producator p = new Producator(depozit, "Producător " + i);
            fire.add(p);
            p.start();
        }

        // Consumatorii (Țurcan Ion):
        //   Consumator c = new Consumator(depozit, "Consumator " + i); fire.add(c); c.start();

        // Asteapta (join) toate firele si afiseaza raportul final
        Thread final_ = new Thread(() -> {
            for (Thread t : fire){
                try {
                    t.join();
                } catch (InterruptedException e) {
                    return;
                }
            }
            timpFinal = System.currentTimeMillis() - timpStart;
            addLog(logProducatori, "===== RAPORT FINAL =====", "info");
            addLog(logProducatori, "Total produse: " + depozit.totalProduse + "/" + Z, "info");
            addLog(logProducatori, "Total consumate: " + depozit.totalConsumate + "/" + Z, "info");
            addLog(logProducatori, "Cicluri umplere -> golire: " + depozit.ciclu + "/" + (Z / D)
                    + ", timp " + timpFinal + " ms", "info");
            updateStatus();
        }, "Raport final");
        final_.setDaemon(true);
        final_.start();

        // Actualizeaza timpul din bara de jos
        Thread ceas = new Thread(() -> {
            while (timpFinal < 0){
                updateStatus();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "Ceas");
        ceas.setDaemon(true);
        ceas.start();
    }

    private static VBox createDepositPane(Button btnStart){

        Label titlu = new Label("Depozit (capacitate " + D + " numere impare) — ReentrantLock + Condition");
        titlu.getStyleClass().add("pane-title");

        HBox rand = new HBox(10);
        rand.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < D; i++){
            locuri[i] = new Label();
            locuri[i].getStyleClass().add("slot");
            locuri[i].setAlignment(Pos.CENTER);
            rand.getChildren().add(locuri[i]);
        }

        stareDepozit.getStyleClass().add("deposit-state");
        fazaDepozit.getStyleClass().add("phase");
        VBox stare = new VBox(8, stareDepozit, fazaDepozit);
        stare.setAlignment(Pos.CENTER_LEFT);
        stare.setPadding(new Insets(0, 0, 0, 16));
        stareDepozit.setStyle("-fx-padding: 0;");

        Region spatiu = new Region();
        HBox.setHgrow(spatiu, Priority.ALWAYS);
        rand.getChildren().addAll(stare, spatiu, btnStart);

        VBox box = new VBox(8, titlu, rand);
        box.getStyleClass().add("panel");
        return box;
    }

    private static VBox createCard(String nume, String culoare, String verb){

        Label lbNume = new Label(nume);
        lbNume.getStyleClass().add("card-name");
        Label lbStare = new Label("nu a pornit");
        lbStare.getStyleClass().add("card-state");
        Label lbContor = new Label("A " + verb + ": 0");
        lbContor.getStyleClass().add("card-count");

        carduri.put(nume, new Label[]{lbStare, lbContor});

        VBox card = new VBox(4, lbNume, lbStare, lbContor);
        card.getStyleClass().addAll("card", culoare);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private static VBox createPane(String title, HBox cards, ListView<LogEntry> log){

        Label lb = new Label(title);
        lb.getStyleClass().add("pane-title");
        VBox.setVgrow(log, Priority.ALWAYS);
        VBox box = new VBox(10, lb, cards, log);
        box.setPadding(new Insets(0, 8, 0, 8));
        return box;
    }

    private static ListView<LogEntry> createLogView(ObservableList<LogEntry> list){

        ListView<LogEntry> view = new ListView<>(list);
        view.getStyleClass().add("log-list");

        view.setCellFactory(lv -> new ListCell<LogEntry>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("p1", "p2", "p3", "c1", "c2", "c3", "c4", "warn", "info");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.text);
                    getStyleClass().add(item.style);
                }
            }
        });

        list.addListener((javafx.collections.ListChangeListener<LogEntry>) c ->
                Platform.runLater(() -> view.scrollTo(list.size() - 1)));

        return view;
    }

    static class LogEntry {

        final String text;
        final String style;

        LogEntry(String text, String style){
            this.text = text;
            this.style = style;
        }

        @Override
        public String toString(){
            return text;
        }
    }

    // Clasa de stil (culoarea) dupa nume: "Producător 2" -> "p2", "Consumator 3" -> "c3"
    private static String styleOf(String cine){
        String nr = cine.substring(cine.lastIndexOf(' ') + 1);
        return (cine.startsWith("Producător") ? "p" : "c") + nr;
    }

    private static void addLog(ObservableList<LogEntry> list, String msg, String style){

        System.out.println(msg);
        LogEntry entry = new LogEntry(msg, style);
        Platform.runLater(() -> list.add(entry));
    }

    // Afiseaza un mesaj in consola si in lista thread-ului curent.
    // warn = true pentru mesajele "depozitul e plin / gol".
    static void log(String msg, boolean warn){

        String thread = Thread.currentThread().getName();
        ObservableList<LogEntry> list = thread.startsWith("Producător") ? logProducatori : logConsumatori;
        addLog(list, msg, warn ? "warn" : styleOf(thread));
    }

    // Actualizeaza cardul unui thread (stare si contor). contor < 0 -> se schimba doar starea.
    static void setState(String thread, String stare, int contor){

        Platform.runLater(() -> {
            Label[] lb = carduri.get(thread);
            if (lb == null) return;
            lb[0].setText(stare);
            if (contor < 0) return;
            String text = lb[1].getText();
            lb[1].setText(text.substring(0, text.indexOf(':') + 1) + " " + contor);
        });
    }

    // Afiseaza depozitul si faza lui. Se apeleaza cand firul detine depozit.lock
    // (dupa fiecare producere / consumare), ca sa se citeasca o stare corecta.
    static void showDeposit(Deposit d){

        List<Integer> obiecte = new ArrayList<>(d.obiecte);
        boolean umplere = d.umplere;
        int ciclu = d.ciclu;

        Platform.runLater(() -> {
            for (int i = 0; i < D; i++){
                locuri[i].getStyleClass().remove("full");
                if (i < obiecte.size()){
                    locuri[i].setText(String.valueOf(obiecte.get(i)));
                    locuri[i].getStyleClass().add("full");
                } else {
                    locuri[i].setText("");
                }
            }
            stareDepozit.getStyleClass().removeAll("plin", "gol");
            if (obiecte.size() == D){
                stareDepozit.setText("Depozitul e plin (" + D + "/" + D + ")");
                stareDepozit.getStyleClass().add("plin");
            } else if (obiecte.isEmpty()){
                stareDepozit.setText("Depozitul e gol (0/" + D + ")");
                stareDepozit.getStyleClass().add("gol");
            } else {
                stareDepozit.setText(obiecte.size() + "/" + D + " numere");
            }
            fazaDepozit.getStyleClass().removeAll("umplere", "golire");
            if (umplere){
                fazaDepozit.setText("Faza: UMPLERE — produc producătorii");
                fazaDepozit.getStyleClass().add("umplere");
            } else {
                fazaDepozit.setText("Faza: GOLIRE — consumă consumatorii (ciclul " + ciclu + "/" + (Z / D) + ")");
                fazaDepozit.getStyleClass().add("golire");
            }
        });
        updateStatus();
    }

    // Bara de jos: cate obiecte s-au produs / consumat si timpul
    static void updateStatus(){

        long timp = 0;
        if (timpStart > 0){
            timp = timpFinal >= 0 ? timpFinal : System.currentTimeMillis() - timpStart;
        }
        String text = "Produse: " + depozit.totalProduse + "/" + Z
                + "   |   Consumate: " + depozit.totalConsumate + "/" + Z
                + "   |   Cicluri: " + depozit.ciclu + "/" + (Z / D)
                + "   |   Timp: " + String.format("%.1f", timp / 1000.0) + " s";
        Platform.runLater(() -> baraStare.setText(text));
    }

    public static void main(String[] args) {
        launch(args);
    }
}


// Depozitul (resursa comuna): datele si obiectele de sincronizare folosite de
// producatori si consumatori. Toate campurile se citesc / modifica doar cu lock-ul luat.
class Deposit{

    final int capacitate;     // D = 5
    final int totalNecesar;   // Z = 45

    final ArrayDeque<Integer> obiecte;

    // Lacatul explicit (fair = firele primesc lacatul in ordinea in care l-au cerut)
    final ReentrantLock lock = new ReentrantLock(true);

    // Producatorii asteapta pe aceasta conditie pana cand depozitul devine gol
    // (o semnaleaza consumatorul care ia ultimul obiect).
    final Condition depozitGol = lock.newCondition();

    // Consumatorii asteapta pe aceasta conditie pana cand depozitul devine plin
    // (o semnaleaza producatorul care pune al D-lea obiect).
    final Condition depozitPlin = lock.newCondition();

    // true = faza de UMPLERE (produc producatorii), false = faza de GOLIRE (consuma consumatorii)
    boolean umplere = true;

    int totalProduse = 0;
    int totalConsumate = 0;   // il incrementeaza consumatorii (Țurcan Ion)
    int ciclu = 0;            // de cate ori s-a umplut depozitul

    public Deposit(int capacitate, int totalNecesar){
        this.capacitate = capacitate;
        this.totalNecesar = totalNecesar;
        this.obiecte = new ArrayDeque<>(capacitate);
    }

    boolean ePlin(){
        return obiecte.size() == capacitate;
    }

    boolean eGol(){
        return obiecte.isEmpty();
    }
}

// Producatorul (Bargan Ilie): thread clasic care produce cate 1 numar impar (F = 1).
// Sincronizarea producatorului:
//   - produce doar in faza de UMPLERE; in faza de GOLIRE asteapta cu depozitGol.await();
//   - cand depozitul devine plin (D obiecte), trece in faza de GOLIRE si trezeste
//     consumatorii cu depozitPlin.signalAll();
//   - se opreste cand s-au produs Z obiecte.
class Producator extends Thread{

    private final Deposit depozit;
    private int produse = 0;   // cate numere a produs acest producator
    private int ultimul;       // ultimul numar produs

    public Producator(Deposit depozit, String nume){
        super(nume);
        this.depozit = depozit;
    }

    // Numar impar aleatoriu intre 1 si 99
    private int numarImpar(){
        return 2 * (int)(Math.random() * 50) + 1;
    }

    @Override
    public void run(){
        try {
            while (produce()){
                Main.setState(getName(), "a produs " + ultimul, produse);
                Thread.sleep(300 + (int)(Math.random() * 500));   // timpul pana la urmatorul obiect
            }
        } catch (InterruptedException e) {
            Main.setState(getName(), "oprit", -1);
            return;
        }
        Main.log(getName() + " s-a finalizat (a produs " + produse + " numere)", false);
        Main.setState(getName(), "finalizat", produse);
    }

    // Produce 1 obiect si il pune in depozit.
    // Intoarce false daca s-au produs deja toate cele Z obiecte.
    private boolean produce() throws InterruptedException {
        depozit.lock.lock();
        try {
            // Producatorii nu pot produce pana cand depozitul nu va fi gol
            boolean anuntat = false;
            while (!depozit.umplere && depozit.totalProduse < depozit.totalNecesar){
                if (!anuntat){
                    Main.log(getName() + " așteaptă golirea depozitului", false);
                    Main.setState(getName(), "așteaptă (golire)", -1);
                    anuntat = true;
                }
                depozit.depozitGol.await();   // elibereaza lacatul si asteapta semnalul
            }

            if (depozit.totalProduse >= depozit.totalNecesar){
                return false;   // s-au produs toate obiectele
            }

            int numar = numarImpar();
            ultimul = numar;
            depozit.obiecte.addLast(numar);
            depozit.totalProduse++;
            produse += Main.F;
            Main.log(getName() + " a produs " + numar + " -> " + depozit.obiecte.size() + "/"
                    + depozit.capacitate + " " + depozit.obiecte
                    + " (total " + depozit.totalProduse + "/" + depozit.totalNecesar + ")", false);

            // Depozitul s-a umplut: incepe faza de golire, se trezesc consumatorii
            if (depozit.ePlin() || depozit.totalProduse == depozit.totalNecesar){
                depozit.umplere = false;
                depozit.ciclu++;
                Main.log("Depozitul e plin " + depozit.obiecte + " -> ciclul " + depozit.ciclu
                        + "/" + (depozit.totalNecesar / depozit.capacitate) + ", consumă consumatorii", true);
                depozit.depozitPlin.signalAll();
            }

            // S-au produs toate cele Z obiecte: trezeste producatorii care asteapta,
            // ca sa vada ca nu mai au ce produce si sa se termine
            if (depozit.totalProduse == depozit.totalNecesar){
                depozit.depozitGol.signalAll();
            }

            Main.showDeposit(depozit);
            return true;
        } finally {
            depozit.lock.unlock();   // lacatul se elibereaza mereu, chiar si la exceptie
        }
    }
}
