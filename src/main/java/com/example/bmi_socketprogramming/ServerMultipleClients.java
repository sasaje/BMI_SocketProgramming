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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMultipleClients extends Application {
    private int clientNo;

    // Text area for displaying contents
    TextArea content = new TextArea();

    @Override
    public void start(Stage stage) {
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

                while (true){ //listens infinity loop for new clients to join
                //Step 2 make it listen for requests
                Socket socket = serverSocket.accept();

                clientNo++; //adds one to clientNo

                //Display clientNo joined server and their host names and IP's
                Platform.runLater(()->{
                    content.appendText("Starting new thread for client " + clientNo + " at " + new Date() + "\n");

                    //Find clients host name and IP
                    InetAddress inetAddress = socket.getInetAddress();
                    content.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                    content.appendText("Client " + clientNo + "'s IP address is " + inetAddress.getHostAddress() + "\n");
                });

                    //Create and start a new thread for the connection
                    new Thread(new HandleAClient(socket)).start();
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();

//                System.err.println(ex);
            }
        }).start();
    }

    /*public static void main(String[] args) {
        launch();
    }*/

    class HandleAClient implements Runnable{
        private Socket socket; // A connected socket

        // Construct a thread - constructor
        public HandleAClient(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                //Step 3 establish connections to and from the server
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                //Continuously serve the client
                while(true){
                    //inputs from every client
                    //Step 4 data calculation
                    double height = (inputFromClient.readDouble())/100; //input from client
                    System.out.println("Height from client: " + height);

                    double weight = inputFromClient.readDouble(); //input from client
                    System.out.println("Weight from client: " + weight);

                    double bmi = weight / (height*height);

                    outputToClient.writeDouble(bmi); //output bmi to client
                    System.out.println("Your BMI is " + bmi + "\n");

                    Platform.runLater(() -> {
                            content.appendText("Height from client: " + height + "\n" +
                            "Weight from client: " + weight + "\n" +
                            "Your BMI is " + bmi + "\n");
                    });
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
