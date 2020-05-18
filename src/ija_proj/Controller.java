/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

/**
 * Hlavný kontroler pre hlavné okno programu
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class Controller extends ControllerExtendable {

    /**
     * Všetky grafické súčasti hlavného okna.
     */
    @FXML
    public Pane mapPane;
    @FXML
    public MyPane myPane;
    @FXML
    public GridPane gridPane;
    @FXML
    public Slider zoomSlider;
    @FXML
    public Label zoomLabel;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Label currentTimeLabel;
    @FXML
    public Button startSimulationButton;
    @FXML
    public TextField simulationTimeTextField;
    @FXML
    public Slider simulationSpeedSlider;
    @FXML
    public Slider refreshIntervalSlider;
    @FXML
    public Label selectedTripLabel;
    @FXML
    public TableView<Object> selectedTripTableView;

    /**
     * Kontroler pre vykreslovanie pohybu vozidiel.
     */
    private MoveController moveCont;
    /**
     * BaseGui that handles most of gui changes.
     */
    private guiClass guic;

    /**
     * Street map that holds all street and stop objects.
     */
    private StreetMap streetMap;

    /**
     * Initialize street map implicitly.
     */
    public Controller() {
        streetMap = new StreetMap();
    }

    /**
     * Add node to {@link Controller#mapPane}. Node is implicitly drawn to gui.
     * @param node Node that will be added.
     */
    private void addNodeToMapPane(Node node) {
        node.setLayoutX(node.getLayoutX());
        node.setLayoutY(node.getLayoutY());
        try{
            mapPane.getChildren().add(node);
        }catch (Exception ignored)
        {

        }

    }

    /**
     * Draw name of stop at specified coordinates.
     * @param label Label object with street name.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    private void addLabelOverStop(Label label, double x, double y) {
        Platform.runLater(() -> {
            addNodeToMapPane(label);
            mapPane.applyCss();
            mapPane.layout();
            double lengthLabel = label.getWidth() / 2;
            label.setLayoutX(x-lengthLabel);
            label.setLayoutY(y - 25);
        });

    }

    /**
     * Vykreslí stopky do scény
     * @param street Stopky vykreslí podľa zadanej ulice
     */
    private void addStopsGui(Street street) {
        for (Stop stop : street.getStops()) {
            Circle circle = new Circle(stop.getCoordinate().getX(), stop.getCoordinate().getY(), 7, Paint.valueOf("blue"));
            Label label = new Label(stop.getId());
            stop.setLabel(label);
            addLabelOverStop(label, stop.getCoordinate().getX(), stop.getCoordinate().getY());
            try{


            Platform.runLater(() -> {
                mapPane.getChildren().remove(label);
            });

            circle.setOnMouseEntered(event -> {
                addLabelOverStop(label, stop.getCoordinate().getX(), stop.getCoordinate().getY());
            });
            circle.setOnMouseExited(event -> {
                Platform.runLater(() -> {
                    mapPane.getChildren().remove(label);
                });
            });

            addNodeToMapPane(circle);
            }catch (Exception ignored)
            {

            }
        }
    }

    /**
     * Max founded X coordinate in map.
     */
    private double maxX = 0;
    /**
     * Max founded Y coordinate in map.
     */
    private double maxY = 0;

    /**
     * Draw full map.
     * @param map JSON map data source.
     * @throws IOException When JSON data file doesn't exist.
     * @throws ParseException When JSON data file is in bad format.
     */
    public void addMapGui(JSONObject map) throws IOException, ParseException {


        JSONArray streets = (JSONArray) map.get("streets");

        for (JSONObject streetJson : (Iterable<JSONObject>) streets) {
            JSONArray coordinates = (JSONArray) streetJson.get("coordinates");

            // Get coordinates array
            List<Coordinate> listCoordinates = new ArrayList<>();
            for (JSONArray coord : (Iterable<JSONArray>) coordinates) {
                listCoordinates.add(new Coordinate((double)coord.get(0),(double)coord.get(1)));
            }

            // Try create street
            Street street = Street.create(streetJson.get("id").toString(), listCoordinates);
            if (street == null) {
                System.err.println("Chyba pri vytváraní ulice");
                continue;
            }

            // Add stops to street
            JSONArray stops = (JSONArray) streetJson.get("stops");

            for (JSONObject stop : (Iterable<JSONObject>) stops) {
                JSONArray coordinate = (JSONArray) stop.get("coordinates");
                Stop stopNew = Stop.defaultStop(stop.get("id").toString(), new Coordinate((double) coordinate.get(0), (double) coordinate.get(1)));

                street.addStop(stopNew);
            }

            // Draw street name
            Platform.runLater(() -> {
                Label streetName = new Label(street.getName());
                addNodeToMapPane(streetName);
                mapPane.applyCss();
                mapPane.layout();
                Coordinate streetNameCoord = getLabelPos(street,streetName.getWidth(),streetName.getHeight());
                streetName.setLayoutX(streetNameCoord.getX());
                streetName.setLayoutY(streetNameCoord.getY());
            });

            // Draw street
            for (int i = 0; i < street.getCoordinates().size() - 1; i++) {
                Coordinate start = street.getCoordinates().get(i);
                Coordinate end = street.getCoordinates().get(i + 1);
                if (start.getX() > this.maxX) {
                    this.maxX = start.getX();
                }
                if (start.getY() > this.maxY) {
                    this.maxY = start.getY();
                }
                if (end.getX() > this.maxX) {
                    this.maxX = end.getX();
                }
                if (end.getY() > this.maxY) {
                    this.maxY = end.getY();
                }
                Line drawableLine = new Line(start.getX(), start.getY(), end.getX(), end.getY());
                drawableLine.setStyle("-fx-stroke-width: 2;");
                addNodeToMapPane(drawableLine);
                street.setGui(drawableLine);
                drawableLine.setOnMouseClicked(event -> {
                    if (!moveCont.getSimulationState()) {
                        try {
                            Stage currentWindow = (Stage) this.selectedTripLabel.getScene().getWindow();

                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("street.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 500, 150);
                            ((StreetController)fxmlLoader.getController()).startUp(street,currentWindow);
                            Stage stage = new Stage();
                            stage.setTitle("Nastaviť ulicu");
                            stage.setScene(scene);
                            stage.initOwner(currentWindow);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();
                            moveCont.updateTraffic();
                            moveCont.setLinesBlock();
                        } catch (Exception e) {
                            Exceptions.show(e);
                        }
                    }

                });
            }

            addStopsGui(street);
            streetMap.addStreet(street);
        }
    }


    /**
     * Nastavuje grafické rozhranie
     */
    @Override
    public void startUp() {
        this.gridPane.setAlignment(Pos.TOP_LEFT);
        this.gridPane.paddingProperty().setValue(new Insets(20, 20, 20, 20));
        this.mapPane = new Pane();
        MyPane myPane = new MyPane(this.mapPane);
        JSONObject map = null;
        try {
            map = JSONLoader.load("data/map.json");
        } catch (Exception e) {
            Exceptions.showError(e);
        }
        try {
            assert map != null;
            addMapGui(map);
        } catch (IOException | ParseException e) {
            Exceptions.showError(e);
        }
        scrollPane.setContent(myPane);
        myPane.zoomC().bind(zoomSlider.valueProperty());
        mapPane.setPrefSize(this.maxX + 500, this.maxY + 500);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                mapPane.setPrefSize((maxX * newValue.doubleValue() + 500), (maxY * newValue.doubleValue() + 500));
            }
        });
        this.myPane = myPane;
        this.simulationSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    moveCont.setMoveUpdateSpeed(newValue.doubleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.refreshIntervalSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    moveCont.setGuiUpdateSpeed((int) Math.round(newValue.doubleValue()));
                } catch (Exception e) {
                    Exceptions.showError(e);
                }
            }
        });
        TableColumn<Object, Object> stopId = new TableColumn<>("Stop");
        stopId.setCellValueFactory(new PropertyValueFactory<>("stopId"));
        TableColumn<Object, Object> plannedTime = new TableColumn<>("Pôvodný čas");
        plannedTime.setCellValueFactory(new PropertyValueFactory<>("plannedTime"));
        TableColumn<Object, Object> actualTime = new TableColumn<>("Aktuálny čas");
        actualTime.setCellValueFactory(new PropertyValueFactory<>("actualTime"));
        this.selectedTripTableView.getColumns().addAll(stopId, plannedTime, actualTime);
        this.guic = new guiClass(this);
        try {
            this.moveCont = new MoveController(streetMap, this.guic);
        } catch (Exception e) {
            Exceptions.show(e);
        }
    }

    /**
     * Volané pri opustení scény
     */
    @Override
    public void close() {
        this.moveCont.oneTimer.cancel();
        this.moveCont.stop();
    }

    /**
     * Funkcia vyvolávaná pri kliku na startButton
     */
    public void startMove() {

        if(this.moveCont.isConflict())
        {
            try {
                Exceptions.throwException("Niektorá ulica je uzavretá - otvorte ulicu...");
            } catch (Exception ignored) {
            }
            return;
        }
        if (this.moveCont.getSimulationState()) {
            this.moveCont.stop();
            this.guic.toggleSimulationButton(false);
        } else {
            try {
                this.moveCont.start(LocalTime.parse(this.simulationTimeTextField.getText()));
            } catch (Exception e) {
                Exceptions.showError(e);
                return;
            }

            this.guic.toggleSimulationButton(true);
        }


    }

    /**
     * Vypočíta polohu mena zobrazovaného pri ulici
     * @param street ulica
     * @param labelLenght dlzka
     * @param labelHeight vyska
     * @return suradnica
     */
    public Coordinate getLabelPos(Street street, double labelLenght, double labelHeight){
        Coordinate returnC = new Coordinate(0,0);
        if ((street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()) != 0){
            if ((street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()) >= 0){
                returnC.setX(((Math.abs(street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()))/2) + (street.getCoordinates().get(1).getX()));
            }
            else{
                returnC.setX(((Math.abs(street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()))/2) + (street.getCoordinates().get(0).getX()));
            }
            returnC.setX(returnC.getX()-(labelLenght/2));
            returnC.setY(street.getCoordinates().get(0).getY()+5);      
        }
        else {
            if ((street.getCoordinates().get(0).getY() - street.getCoordinates().get(1).getY()) >= 0){
                returnC.setY(((Math.abs(street.getCoordinates().get(0).getY() - street.getCoordinates().get(1).getY()))/2) + (street.getCoordinates().get(1).getY()));   
            }
            else{
                returnC.setY(((Math.abs(street.getCoordinates().get(0).getY() - street.getCoordinates().get(1).getY()))/2) + (street.getCoordinates().get(0).getY()));     
            }
            returnC.setY(returnC.getY() - (labelHeight/2));
            returnC.setX(street.getCoordinates().get(0).getX()+10);
        }
        return returnC;
    }
}