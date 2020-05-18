/*
 * Authors:
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovič (xpaulo04)
 */
package ija_proj;

//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.Module;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import ija_proj.Controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
//package Main;

import ija_proj.Controller;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hlavná trieda
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader root = new FXMLLoader(getClass().getResource("main.fxml"));
        primaryStage.setTitle("IJA Projekt 2020");
        primaryStage.setScene(new Scene(root.load()));

        this.controller = root.getController();
        controller.startUp();

        primaryStage.setMaximized(true);
        primaryStage.show();

    }
    /**
     * Override pre povodnu stop metodu
     */
    @Override
    public void stop() throws Exception {
        this.controller.close();
    }
    public static void main(String[] args) {
        launch(args);
    }
}