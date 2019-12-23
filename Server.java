import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Server extends Application {
	// Override the start method for the Application class.
	@Override
	public void start(Stage primaryStage) {
		// Textarea one for displaying client content
		TextArea textAreaClient = new TextArea();
		textAreaClient.setEditable(false);
		//Textarea two for displaying server content
		TextArea textAreaServer = new TextArea();
		textAreaClient.setPadding(new Insets(5, 5, 5, 5));
		textAreaServer.setPadding(new Insets(5, 5, 5, 5));
		// Label for client
		Label labelClient = new Label("Client");
		// Label for server
		Label labelServer = new Label("Server");
		
		GridPane pane = new GridPane();
		pane.add(labelServer, 1, 1);
		pane.add(textAreaServer, 1, 2);
		pane.add(labelClient, 1, 3);
		pane.add(textAreaClient, 1, 4);
		
		// Create a scene and place it on the stage.
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene); // Set the stage
		primaryStage.show(); // Display the stage
		        
		new Thread(() -> {
			// Create a server socket
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(8080);
				// Update the UI
				try {
			        // Listen for a connection request
			        Socket socket = serverSocket.accept();
			  
			        // Create data input and output streams
			        DataInputStream inputFromClient = new DataInputStream(
			          socket.getInputStream());
			        DataOutputStream outputToClient = new DataOutputStream(
			          socket.getOutputStream());
			  
			        while (true) {
			          // Receive message from the client
			          String clientMessage = inputFromClient.readUTF();
			          textAreaClient.appendText(clientMessage);
			          
			          // Text is sent to the server when the enter key is pressed. 
			          textAreaServer.setOnKeyReleased(e -> {
			        	  if(e.getCode() == KeyCode.ENTER) {
			        		  try {
				        		// Send message to the client 
						          outputToClient.writeUTF(textAreaServer.getText().trim());
						          outputToClient.flush(); 
				        	  } catch(IOException el) {
				        		  el.printStackTrace();
				        	  }
			        	  }
			          });
			        }
				} catch(IOException e) {
					e.printStackTrace();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	public static void main(String args[]) {
		launch(args);
	}
}
