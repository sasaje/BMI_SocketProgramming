package com.example.bmi_socketprogramming;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Application {

    @Override
    public void start(Stage stage) {

        // Text area for displaying contents
        TextArea content = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new AnchorPane(content), 500, 200);
        stage.setTitle("Server | BMI Server"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage

        new Thread (() -> {

            try {
                //Step 1 create serverSocket
                ServerSocket serverSocket = new ServerSocket(1709);
                Platform.runLater(() ->
                        content.appendText("Server started at " + new Date() + '\n' + "Server waiting for request..." + '\n'));

                //Step 2 make it listen for requests
                Socket socket = serverSocket.accept();

                //Step 3 establish connections to and from the server
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                System.out.println("Accepting connection on port 1709");
                System.out.println("Connection established " + socket.getRemoteSocketAddress().toString());

                while (true){

                    //Step 4 data calculation   |   kn = k0*(1+r)^n
                    double height = (inputFromClient.readDouble())/100; //input from client
                    System.out.println("Height from client: " + height);

                    double weight = inputFromClient.readDouble(); //input from client
                    System.out.println("Weight from client: " + weight);

                    double bmi = weight / (height*height);

                    outputToClient.writeDouble(bmi); //output bmi to client
                    System.out.println("Your BMI is " + bmi + "\n");

                    Platform.runLater(() -> content.appendText("Height from client: " + height + "\n" +
                            "Weight from client: " + weight + "\n" +
                            "Your BMI is " + bmi + "\n"));
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
