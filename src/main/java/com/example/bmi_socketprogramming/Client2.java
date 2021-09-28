package com.example.bmi_socketprogramming;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client2 extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    Label bmiServerLabel = new Label("BMI Server");
    Label hb1Label = new Label("Enter height (cm): ");
    Label hb2Label = new Label("Enter weight (kg): ");
    TextField hb1Textfield = new TextField();
    TextField hb2Textfield = new TextField();
    Button calculateButton = new Button("Calculate BMI");
    TextArea textArea = new TextArea();

    @Override
    public void start(Stage stage) {

        bmiServerLabel.setStyle("-fx-font-size: 30px");
        bmiServerLabel.setPadding(new Insets(5,5,5,5));
        calculateButton.setPadding(new Insets(10,10,10,10));

        hb1Label.setPadding(new Insets(5,0,0,0));
        hb2Label.setPadding(new Insets(5,0,0,0));

        HBox hb1 = new HBox(hb1Label, hb1Textfield);
        HBox hb2 = new HBox(hb2Label, hb2Textfield);
        VBox vb2 = new VBox(calculateButton);
        VBox vb3 = new VBox(textArea);

        vb2.setPadding(new Insets(5,5,5,5));
        vb3.setPadding(new Insets(5,5,5,5));
        hb1.setPadding(new Insets(5,5,5,5));
        hb2.setPadding(new Insets(5,5,5,5));

        VBox vBox = new VBox(bmiServerLabel, hb1, hb2, vb2, vb3);

        BorderPane vboxPane = new BorderPane();
        // Text area to display contents
        vboxPane.setCenter(new AnchorPane(vBox));

        // Create a scene and place it in the stage
        Scene scene = new Scene(vboxPane, 650, 400);
        stage.setTitle("Client | BMI Server"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage

        //SERVER CONNECTION
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 1709);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

        }
        catch (IOException ex) {
            System.out.println("Client error!");
        }

        new Thread (() -> calculateButton.setOnAction(e -> {
            try {
                //Get Annual Interest rate r
                double height = Double.parseDouble(hb1Textfield.getText().trim());
                //Send to server
                System.out.println("Height er " + height);
                toServer.writeDouble(height);
                toServer.flush();

                //Get Number of years
                double weight = Double.parseDouble(hb2Textfield.getText().trim());
                System.out.println("Weight er " + weight);
                //Send to server
                toServer.writeDouble(weight);
                toServer.flush();

                //Get kn from the server
                double bmi = fromServer.readDouble();
                System.out.println("BMI is calculated to be: " + bmi);
                //     fromServer.close();

//                    Platform.runLater(() -> {
                // Display to the text area
                textArea.appendText("Height: " + height + "\n");
                textArea.appendText("Weight: " + weight + "\n");
                textArea.appendText("Calculated BMI is: " + bmi + "\n");
                //        });
            }
            catch (IOException ex) {
                System.out.println("An error has occurred!");
                //   System.err.println(ex);
            }
        })).start();
    }

    public static void main(String[] args) {
        launch();
    }
}

