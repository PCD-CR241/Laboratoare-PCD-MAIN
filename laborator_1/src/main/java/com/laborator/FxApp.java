package com.laborator;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FxApp extends Application {

    private static final int SIZE = 100;
    private static final int COLUMNS = 10;

    
    private static final Pattern RESULT = Pattern.compile("^(Unu|Doi): (\\d+) \\+ (\\d+) = (-?\\d+)$");


    record Result(int order, String thread, int first, int second, int sum, String message){

        boolean isPair(){
            return message == null;
        }

        @Override
        public String toString(){
            return isPair() ? first + " + " + second + " = " + sum : message;
        }
    }

    private final VBox[] cells = new VBox[SIZE];
    private final Label[] values = new Label[SIZE];

    private final ObservableList<Result> results = FXCollections.observableArrayList();

    private Button startButton;
    private Label status;

    @Override
    public void start(Stage stage){

        System.setOut(new PrintStream(new LineStream(System.out, this::onLine), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new LineStream(System.err, this::onLine), true, StandardCharsets.UTF_8));

        BorderPane root = new BorderPane();
        root.setTop(createHeader());

        ListView<Result> resultList = createResultList();

        resultList.getSelectionModel().selectedItemProperty().addListener((o, old, r) -> highlight(r));

        VBox resultCard = createResultCard(resultList);

        HBox content = new HBox(16, createArrayCard(), resultCard);
        content.getStyleClass().add("content");
        HBox.setHgrow(resultCard, Priority.ALWAYS);
        root.setCenter(content);

        Scene scene = new Scene(root, 1180, 680);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Laborator 1 - Suma indicilor numerelor pare");
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    private HBox createHeader(){

        Label title = new Label("Suma indicilor numerelor pare");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Doua fire de executie parcurg tabloul din capete opuse si aduna indicii numerelor pare doua cate doua");
        subtitle.getStyleClass().add("subtitle");

        status = new Label("Apasa \"Porneste\" pentru a genera tabloul");
        status.getStyleClass().add("status");

        VBox text = new VBox(4, title, subtitle, status);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        startButton = new Button("Porneste");
        startButton.getStyleClass().add("primary");
        startButton.setDefaultButton(true);
        startButton.setOnAction(e -> runThreads());

        HBox header = new HBox(16, text, spacer, startButton);
        header.getStyleClass().add("header");
        return header;
    }

    private VBox createArrayCard(){

        Label title = new Label("Tablou (" + SIZE + " elemente)");
        title.getStyleClass().add("card-title");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");

        for(int i = 0; i < SIZE; i++){

            Label index = new Label(String.valueOf(i));
            index.getStyleClass().add("cell-index");

            values[i] = new Label("-");
            values[i].getStyleClass().add("cell-value");

            cells[i] = new VBox(index, values[i]);
            cells[i].getStyleClass().add("num-cell");

            grid.add(cells[i], i % COLUMNS, i / COLUMNS);
        }

        HBox legend = new HBox(14,
            legendItem("even", "numar par"),
            legendItem("unu", "pereche Unu"),
            legendItem("doi", "pereche Doi"),
            legendItem("both", "ambele fire")
        );
        legend.getStyleClass().add("legend");

        VBox card = new VBox(10, title, grid, legend);
        card.getStyleClass().add("card");
        return card;
    }

    private HBox legendItem(String styleClass, String text){

        Region box = new Region();
        box.getStyleClass().addAll("legend-box", styleClass);

        Label label = new Label(text);
        label.getStyleClass().add("legend-text");

        HBox item = new HBox(6, box, label);
        item.getStyleClass().add("legend-item");
        return item;
    }

    private ListView<Result> createResultList(){

        ListView<Result> list = new ListView<>(results);
        list.getStyleClass().add("result-list");
        list.setPlaceholder(new Label("Niciun rezultat inca"));

        list.setCellFactory(v -> new ListCell<>(){

            private final Label order = new Label();
            private final Label tag = new Label();
            private final Label text = new Label();
            private final HBox row = new HBox(10, order, tag, text);

            {
                order.getStyleClass().add("order");
                tag.getStyleClass().add("tag");
                text.getStyleClass().add("result-text");
                row.getStyleClass().add("result-row");
            }

            @Override
            protected void updateItem(Result r, boolean empty){

                super.updateItem(r, empty);
                getStyleClass().removeAll("row-unu", "row-doi", "message");

                if(empty || r == null){
                    setGraphic(null);
                    return;
                }

                String styleClass = r.thread().equals("Unu") ? "unu" : "doi";

                order.setText("#" + r.order());
                tag.setText(r.thread());
                tag.getStyleClass().setAll("label", "tag", styleClass);
                text.setText(r.toString());

                getStyleClass().add("row-" + styleClass);

                if(!r.isPair()){
                    getStyleClass().add("message");
                }

                setText(null);
                setGraphic(row);
            }
        });

        return list;
    }

    private VBox createResultCard(ListView<Result> list){

        Label title = new Label("Ordinea executiei");
        title.getStyleClass().add("card-title");

        Label subtitle = new Label("rezultatele ambelor fire, in ordinea afisarii\n Bargan Ilie");
        subtitle.getStyleClass().add("card-subtitle");

        VBox titles = new VBox(2, title, subtitle);
        titles.setMinWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox head = new HBox(14, titles, spacer,
            legendItem("unu", "Unu: 0 → 99"),
            legendItem("doi", "Doi: 99 → 0")
        );
        head.getStyleClass().add("card-head");

        VBox card = new VBox(10, head, list);
        card.getStyleClass().addAll("card", "result-card");
        VBox.setVgrow(list, Priority.ALWAYS);
        return card;
    }

    private void runThreads(){

        startButton.setDisable(true);
        status.setText("Firele ruleaza...");
        results.clear();

        int[] arr = new int[SIZE];

        for(int i = 0; i < arr.length; i++){

            arr[i] = (int)(Math.random() * 99);
            System.out.print(arr[i] + ", ");
        }
        System.out.println();

        showArray(arr);

        SumEvenNum sum1 = new SumEvenNum(0, SIZE - 1, arr);
        SumEvenNum sum2 = new SumEvenNum(SIZE - 1, 0, arr);

        sum1.setName("Unu");
        sum1.start();
        sum2.setName("Doi");
        sum2.start();


        Thread waiter = new Thread(() -> {

            try{
                sum1.join();
                sum2.join();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }

            Platform.runLater(this::finished);
        });
        waiter.setDaemon(true);
        waiter.start();
    }

    private void showArray(int[] arr){

        for(int i = 0; i < SIZE; i++){

            values[i].setText(String.valueOf(arr[i]));
            cells[i].getStyleClass().setAll("num-cell");

            if(arr[i] % 2 == 0){
                cells[i].getStyleClass().add("even");
            }
        }
    }

    private void finished(){

        long unuPairs = results.stream().filter(r -> r.isPair() && r.thread().equals("Unu")).count();
        long doiPairs = results.stream().filter(r -> r.isPair() && r.thread().equals("Doi")).count();


        int switches = 0;

        for(int k = 1; k < results.size(); k++){

            if(!results.get(k).thread().equals(results.get(k - 1).thread())){
                switches++;
            }
        }

        status.setText("Gata  •  Unu: " + unuPairs + " perechi  •  Doi: " + doiPairs + " perechi"
            + "  •  comutari intre fire: " + switches);
        startButton.setDisable(false);
    }


    private void onLine(String line){

        Matcher m = RESULT.matcher(line);

        if(m.matches()){

            String thread = m.group(1);
            Result r = new Result(results.size() + 1, thread,
                Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), null);
            String styleClass = thread.equals("Unu") ? "unu" : "doi";

            results.add(r);
            mark(r.first(), styleClass);
            mark(r.second(), styleClass);

        }else if(line.startsWith("Unu: ") || line.startsWith("Doi: ")){

            results.add(new Result(results.size() + 1, line.substring(0, 3), 0, 0, 0, line.substring(5)));
        }
    }

    private void mark(int index, String styleClass){

        if(index >= 0 && index < SIZE && !cells[index].getStyleClass().contains(styleClass)){
            cells[index].getStyleClass().add(styleClass);
        }
    }

    private void highlight(Result r){

        for(VBox cell : cells){
            cell.getStyleClass().remove("selected");
        }

        if(r != null && r.isPair()){
            cells[r.first()].getStyleClass().add("selected");
            cells[r.second()].getStyleClass().add("selected");
        }
    }

    public static void main(String[] args){

        launch(args);
    }


    private static class LineStream extends OutputStream {

        private final PrintStream terminal;
        private final Consumer<String> onLine;
        private final ByteArrayOutputStream line = new ByteArrayOutputStream();

        LineStream(PrintStream terminal, Consumer<String> onLine){

            this.terminal = terminal;
            this.onLine = onLine;
        }

        @Override
        public synchronized void write(int b){

            terminal.write(b);

            if(b == '\n'){

                String text = line.toString(StandardCharsets.UTF_8).strip();
                line.reset();

                // the threads are not the JavaFX thread, so the window is updated through runLater
                Platform.runLater(() -> onLine.accept(text));

            }else{

                line.write(b);
            }
        }

        @Override
        public synchronized void write(byte[] b, int off, int len){

            for(int k = off; k < off + len; k++){
                write(b[k]);
            }
        }

        @Override
        public synchronized void flush(){

            terminal.flush();
        }
    }
}
