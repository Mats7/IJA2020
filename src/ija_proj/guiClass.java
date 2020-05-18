/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda pre prácu s GUI
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class guiClass {
    private Controller controller;
    private List<Node> garbage = new ArrayList<>();

    /**
     * Konštruktor
     * @param controller kontroler
     */
    public guiClass(Controller controller) {
        this.controller = controller;
    }

    /**
     * Vytvorí značku vozidla na mape
     * @param coordinate c
     * @return circle kruh
     */
    public Circle createDot(Coordinate coordinate) {
        Circle circle = new Circle(coordinate.getX(), coordinate.getY(), 7, Paint.valueOf("green"));
        addSimulationNode(circle);
        return circle;
    }

    public void addSimulationNode(Node node) {
        Platform.runLater(() -> {
            node.toFront();
            this.controller.mapPane.getChildren().add(node);
            garbage.add(node);
        });

    }

    /**
     * Zobrazí čas vo formáte hh:mm:ss do timeLabelu
     * @param time čas na zobrazenie
     */
    public void showTime(LocalTime time) {
        Platform.runLater(() -> {
            this.controller.currentTimeLabel.textProperty().setValue(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
    }

    /**
     * Obnoví simuláciu vozidiel
     */
    public void clearSimulationGui() {
        for (Node node : garbage) {
            Platform.runLater(() -> {
                this.controller.mapPane.getChildren().remove(node);
            });
        }
    }

    private void addNodeToMapPane(Node node)
    {
        Platform.runLater(() -> {
            try{
                this.controller.mapPane.getChildren().add(node);
            }catch (Exception ignored)
            {

            }

        });
    }
    
    private List<Node> highlight = new ArrayList<>();

    /**
     * Zvýrazní celú linku, ak je na nej kurzor
     * @param linka linka
     */
    public void highlightLine(Line linka) {
        for (Street street : linka.getStreets()) {
            for(int i=0;i<street.getCoordinates().size()-1;i++)
            {
                javafx.scene.shape.Line line = new javafx.scene.shape.Line(street.getCoordinates().get(i).getX(),street.getCoordinates().get(i).getY(),street.getCoordinates().get(i+1).getX(),street.getCoordinates().get(i+1).getY());
                line.setStyle("-fx-stroke: chartreuse; -fx-stroke-width: 3");
                addNodeToMapPane(line);
                for(Stop stop:linka.getStops()){
                    addNodeToMapPane(stop.getLabel());
                    this.highlight.add(stop.getLabel());
                }

                this.highlight.add(line);
            }
        }
    }

    /**
     * Zobrazí rozpis pre vybrané vozidlo (linku)
     * @param path cesta
     */
    public void showTimetable(Path path)
    {

        Platform.runLater(() -> {
            for(int i =0;i<path.getLine().getRealStops().size();i++)
            {
                TableItem item;
                item = new TableItem(path.getLine().getRealStops().get(i).getId(),path.getBackUpTimetable().get(i).toString(),path.getActualTimetable().get(i).toString());
                this.controller.selectedTripTableView.getItems().add(item);
                this.controller.selectedTripLabel.textProperty().setValue(path.getId());
            }
        });
    }

    /**
     * Vráti farbu a štál čiary do normálu
     */
    public void clearHighlight() {
        for (Node node : this.highlight) {
            Platform.runLater(() -> {
                this.controller.mapPane.getChildren().remove(node);
            });
        }
    }

    public void clearTripTimetable() {
        this.controller.selectedTripTableView.getItems().clear();
    }

    public void toggleSimulationButton(final boolean state) {
        Platform.runLater(() -> {
            if (state) {
                this.controller.startSimulationButton.textProperty().setValue("Stop simulation");
                this.controller.currentTimeLabel.visibleProperty().setValue(true);
                this.controller.simulationTimeTextField.visibleProperty().setValue(false);
                this.controller.simulationSpeedSlider.disableProperty().setValue(true);
                this.controller.refreshIntervalSlider.disableProperty().setValue(true);
            } else {
                this.controller.startSimulationButton.textProperty().setValue("Start simulation");
                this.controller.currentTimeLabel.visibleProperty().setValue(false);
                this.controller.simulationTimeTextField.visibleProperty().setValue(true);
                this.controller.simulationTimeTextField.textProperty().setValue(this.controller.currentTimeLabel.getText());
                this.controller.simulationSpeedSlider.disableProperty().setValue(false);
                this.controller.refreshIntervalSlider.disableProperty().setValue(false);
            }
        });
    }

    public String getTimeFromField() {
        return this.controller.simulationTimeTextField.getText();
    }
}