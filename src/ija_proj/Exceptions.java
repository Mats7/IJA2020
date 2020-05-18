/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Trieda pre výpis výnimiek a chýb
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class Exceptions {
    /**
     * Vypíše exception aj s podrobnosťami. (Umožní detailnejší popis chyby cez trace)
     * @param e Exception.
     */
    public static void show(Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CHYBA");
        alert.setHeaderText("Neznáma chyba!");
        alert.setContentText(e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("BackTrace chyby:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    /**
     * Základné zobrazenie výnimky (s input hláškou)
     * @param message Vlastná hláška
     * @throws Exception e
     */
    public static void throwException(String message) throws Exception
    {
        Exception e = new Exception(message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CHYBA");
        alert.setHeaderText("Nastala chyba!");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
        throw e;
    }
    
    /**
     * Základné zobrazenie chyby.
     * @param e e
     */
    public static void showError(Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred!");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }
    
    /**
     * Vypíše upozornenie (program pokračuje)
     * @param e Exception
     */
    public static void showWarning(Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Upozornenie");
        alert.setHeaderText("Pozor, nastala chyba! ");
        alert.setContentText(e.getMessage()+"\n\nProgram sa neukončí...");

        alert.showAndWait();
    }
}
