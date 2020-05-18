/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import javafx.scene.shape.Line;

/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class StreetController {

    @FXML
    public TextField TrafficTextField;
    @FXML
    public Button closedButton;
    private Street street;
    private Stage currentWindow;

    /**
     * Načíta modul pre nastavenie obmedzenia
     * @param street Ulica
     * @param currentWindow okno
     */
    public void startUp(Street street, Stage currentWindow) {
        this.currentWindow = currentWindow;
        this.street = street;
        Platform.runLater(() -> {
            this.TrafficTextField.setText(Integer.toString(street.getTrafficCoefficient()));
        });
        guiRefresh();
    }

    /**
     * Nastavuje button pre otvorenie ulice podla toho, ci je otvorena
     */
    private void guiRefresh() {
        Platform.runLater(() -> {
            if (this.street.isClosed()) {
                this.closedButton.textProperty().setValue("Otvoriť");
            } else {
                this.closedButton.textProperty().setValue("Uzavrieť");
            }
        });

    }

    /**
     * 
     */
    public void closeClick() {
        if (this.street.isClosed()) {
            this.street.setClosed(false);
            for (Line line : this.street.getLines()) {
                line.restoreOld();
                line.getAllConflicts();
            }
        } else {
            this.street.setClosed(true);
            for (Line line : this.street.getLines()) {
                line.restoreOld();
                line.getAllConflicts();
            }

        }
        guiRefresh();
    }

    /**
     * On window close button click.
     * Set street color based on if it is open/closed.
     * Close window.
     */
    public void okClick() {
        try {
            this.street.setTrafficCoefficient(Integer.parseInt(TrafficTextField.textProperty().get()));
            Platform.runLater(() -> {
                if(!street.isClosed())
                {
                    if(street.getTrafficCoefficient()>1)
                    {
                        street.getGui().setStyle("-fx-stroke: orange; -fx-stroke-width: 2;");
                    }
                    else {
                        street.getGui().setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
                    }
                }

            });

            cancelClick();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred!");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    /**
     * Close window.
     */
    public void cancelClick() {
        Stage stage = (Stage) this.closedButton.getScene().getWindow();
        stage.close();
        this.currentWindow.show();
    }
}