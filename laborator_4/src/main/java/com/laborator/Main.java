package com.laborator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

Varianta 2

Sunt dați 3 producători care generează aleatoriu 2 obiecte care sunt consumate de 4
consumatori. De afişat informaţia despre producerea şi consumarea obiectelor,
mesajele despre cazurile când “depozitul e gol sau plin”.
Toate operaţiile se efectuează până când fiecare consumator
este îndestulat cu 2 obiecte.

Dimensiunea depozitului este 5 obiecte.

Tip obiecte: Numere impare

Producator: Bargan Ilie
Consumator: Țurcan Ion
*/

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
    static final int X = 3;   // producatori
    static final int Y = 4;   // consumatori
    static final int Z = 2;   // obiecte pentru fiecare consumator
    static final int D = 5;   // dimensiunea depozitului

    static Deposit depozit = new Deposit(D);

    // Elementele interfetei
    static ObservableList<LogEntry> logProducatori = FXCollections.observableArrayList();
    static ObservableList<LogEntry> logConsumatori = FXCollections.observableArrayList();
    static Label[] locuri = new Label[D];
    static Label stareDepozit = new Label();
    static Map<String, Label[]> carduri = new HashMap<>();   // nume thread -> {stare, contor}

    public void start(Stage stage){

        BorderPane bdRoot = new BorderPane();
        bdRoot.setPadding(new Insets(16));
        Scene scene = new Scene(bdRoot, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Sus: depozitul cu cele D locuri si butonul Start
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
            cardProd.getChildren().add(createCard("Producător " + i, "p" + i, "pus"));
        }
        HBox cardCons = new HBox(10);
        for (int i = 1; i <= Y; i++){
            cardCons.getChildren().add(createCard("Consumator " + i, "c" + i, "luat"));
        }

        SplitPane sp = new SplitPane(
                createPane("Producători (" + X + ") — Bargan Ilie", cardProd, createLogView(logProducatori)),
                createPane("Consumatori (" + Y + ") — Țurcan Ion", cardCons, createLogView(logConsumatori)));
        sp.setDividerPosition(0, 0.45);
        BorderPane.setMargin(sp, new Insets(16, 0, 0, 0));
        bdRoot.setCenter(sp);

        showDeposit(new ArrayList<>());

        stage.setTitle("Sincronizarea thread-urilor cu ajutorul funcțiilor");
        stage.setScene(scene);
        stage.show();
    }

    // Creeaza si porneste thread-urile
    private static void pornesteThreadurile(){

        for (int i = 1; i <= X; i++){
            new Producator(depozit, "Producător " + i).start();
        }

        // Consumatorii (Țurcan Ion)
        Consumator[] consumatori = new Consumator[Y];
        for (int i = 0; i < Y; i++){
            consumatori[i] = new Consumator(depozit, "Consumator " + (i + 1));
            consumatori[i].start();
        }

        // Asteapta (join) ca toti consumatorii sa fie indestulati si anunta finalul
        Thread final_ = new Thread(() -> {
            for (Consumator c : consumatori){
                try {
                    c.join();
                } catch (InterruptedException e) {
                    return;
                }
            }
            Main.log("Toți consumatorii au primit câte " + Z + " numere. Gata!", true);
        }, "Consumator final");
        final_.setDaemon(true);
        final_.start();
    }

    private static VBox createDepositPane(Button btnStart){

        Label titlu = new Label("Depozit (capacitate " + D + " numere impare)");
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
        Region spatiu = new Region();
        HBox.setHgrow(spatiu, Priority.ALWAYS);
        rand.getChildren().addAll(stareDepozit, spatiu, btnStart);

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
                getStyleClass().removeAll("p1", "p2", "p3", "c1", "c2", "c3", "c4", "warn");
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

    // Clasa de stil (culoarea) dupa numele thread-ului: "Producător 2" -> "p2"
    private static String styleOf(String thread){
        String nr = thread.substring(thread.lastIndexOf(' ') + 1);
        return (thread.startsWith("Producător") ? "p" : "c") + nr;
    }

    // Afiseaza un mesaj in consola si in lista thread-ului curent.
    // warn = true pentru mesajele "depozitul e plin / gol".
    static void log(String msg, boolean warn){

        System.out.println(msg);
        String thread = Thread.currentThread().getName();
        ObservableList<LogEntry> list = thread.startsWith("Producător") ? logProducatori : logConsumatori;
        LogEntry entry = new LogEntry(msg, warn ? "warn" : styleOf(thread));
        Platform.runLater(() -> list.add(entry));
    }

    // Actualizeaza cardul unui thread (stare si numarul de obiecte puse / luate).
    // contor < 0 -> se schimba doar starea.
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

    // Afiseaza continutul depozitului (se trimite o copie a listei)
    static void showDeposit(List<Integer> obiecte){

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
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


class Deposit{

    private final int capacitate;                         // D = 5
    private final ArrayList<Integer> obiecte = new ArrayList<>();

    public Deposit(int capacitate){
        this.capacitate = capacitate;
    }

    // Producatorul pune 2 numere impare in depozit.
    // Daca nu incap ambele (depozitul e plin), asteapta pana un consumator ia ceva.
    public synchronized void put(String producator, int a, int b){

        boolean anuntat = false;

        while (obiecte.size() + 2 > capacitate){
            
            if (!anuntat){
                Main.log("Depozitul e plin " + obiecte + " -> " + producator + " așteaptă", true);
                Main.setState(producator, "așteaptă (plin)", -1);
                anuntat = true;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        obiecte.add(a);
        obiecte.add(b);
        Main.log(producator + " a pus în depozit: " + a + ", " + b
                + " -> " + obiecte.size() + "/" + capacitate + " " + obiecte, false);
        if (obiecte.size() == capacitate){
            Main.log("Depozitul e plin " + obiecte, true);
        }
        Main.showDeposit(new ArrayList<>(obiecte));
        notifyAll();   // anunta consumatorii ca au aparut obiecte noi
    }

    // Consumatorul ia 2 numere impare deodata din depozit (simetric cu put(a, b)).
    // Daca nu sunt cel putin 2 numere, asteapta pana un producator pune ceva.
    // (Țurcan Ion)
    public synchronized int[] get(String consumator){

        boolean anuntat = false;

        while (obiecte.size() < 2){
            if (!anuntat){
                Main.log((obiecte.isEmpty() ? "Depozitul e gol" : "În depozit e doar " + obiecte)
                        + " -> " + consumator + " așteaptă", true);
                Main.setState(consumator, "așteaptă (gol)", -1);
                anuntat = true;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        // se iau primele 2 numere puse (cele mai vechi)
        int a = obiecte.remove(0);
        int b = obiecte.remove(0);
        Main.log(consumator + " a luat din depozit: " + a + ", " + b
                + " -> " + obiecte.size() + "/" + capacitate + " " + obiecte, false);
        if (obiecte.isEmpty()){
            Main.log("Depozitul e gol", true);
        }
        Main.showDeposit(new ArrayList<>(obiecte));
        notifyAll();   // anunta producatorii ca s-a eliberat loc
        return new int[]{a, b};
    }
}

// Consumatorul: ia cate 2 numere impare din depozit pana primeste Z obiecte.
// (Țurcan Ion)
class Consumator extends Thread{

    private final Deposit depozit;
    private int luate = 0; 

    public Consumator(Deposit depozit, String nume){
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run(){

        while (luate < Main.Z){
            Main.setState(getName(), "vrea să ia 2 numere...", luate);
            try {
                sleep(300 + (int)(Math.random() * 700));   
            } catch (InterruptedException e) {
                return;
            }
            int[] luat = depozit.get(getName());
            if (luat == null) return;
            luate += 2;
            Main.setState(getName(), "a luat " + luat[0] + ", " + luat[1], luate);
        }
        Main.log(getName() + " a primit " + luate + " numere și a terminat", false);
        Main.setState(getName(), "îndestulat ✓", luate);
    }
}

// Producatorul: genereaza aleatoriu cate 2 numere impare (F = 2) si le pune in depozit.
// Este thread daemon: lucreaza pana cand toti consumatorii au primit cate 2 obiecte
// si programul se termina.
class Producator extends Thread{

    private final Deposit depozit;
    private int puse = 0;   // cate numere a pus acest producator

    public Producator(Deposit depozit, String nume){
        super(nume);
        this.depozit = depozit;
        setDaemon(true);
    }

    // Numar impar aleatoriu intre 1 si 99
    private int numarImpar(){
        return 2 * (int)(Math.random() * 50) + 1;
    }

    @Override
    public void run(){
        while (true){
            Main.setState(getName(), "produce...", puse);
            try {
                sleep(500 + (int)(Math.random() * 1000));   // timpul de "producere"
            } catch (InterruptedException e) {
                return;
            }
            int a = numarImpar();
            int b = numarImpar();
            depozit.put(getName(), a, b);
            puse += 2;
            Main.setState(getName(), "a pus " + a + ", " + b, puse);
        }
    }
}
