import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
  // IO streams
  DataOutputStream toServer = null;
  DataInputStream fromServer = null;
  String message;
  String serverMessage;


  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Panel p to hold the label and text field
    BorderPane paneForTextField = new BorderPane();
    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
    paneForTextField.setStyle("-fx-border-color: green");
    paneForTextField.setLeft(new Label("Enter your message"));
    
    TextField tf = new TextField();
    tf.setAlignment(Pos.BOTTOM_RIGHT);
    paneForTextField.setCenter(tf);
    
    BorderPane mainPane = new BorderPane();
    // Text area to display contents
    TextArea ta = new TextArea();
    mainPane.setCenter(new ScrollPane(ta));
    mainPane.setTop(paneForTextField);
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

      try {
          Socket socket = new Socket("192.168.43.92", 8001);
          System.out.println("client connected");


          // Create an input stream to receive data from the server
          fromServer = new DataInputStream(socket.getInputStream());

          // Create an output stream to send data to the server
          toServer = new DataOutputStream(socket.getOutputStream());
      }
      catch (IOException ex) {
          ta.appendText(ex.toString() + '\n');
      }
    tf.setOnAction(e -> {
      try {

          System.out.println("sending");
        // Get the message from the text field
        message = (tf.getText().trim());
  
        // Send the message to the server
        toServer.writeUTF(message);
        toServer.flush();
  
        // Get message from the server
        serverMessage = fromServer.readUTF();
  
        // Display to the text area
        ta.appendText("Server: " + serverMessage + "\n");

          System.out.println("done");
          tf.clear();
      }
      catch (IOException ex) {
        System.out.println(ex);
      }
    });


  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}