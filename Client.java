import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {
	
	// IO Stream
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	
	// Override the start method for the Application class. 
	@Override
	public void start(Stage primaryStage) {
		// Text Area to display contents
		TextArea textAreaClient = new TextArea();
		TextArea textAreaServer = new TextArea();
		textAreaClient.setPadding(new Insets(5, 5, 5, 5));
		textAreaServer.setPadding(new Insets(5, 5, 5, 5));
		textAreaServer.setEditable(false);
		
		// Label for client
		Label labelClient = new Label("Client");
		// Label for server
		Label labelServer = new Label("Server");
		
		GridPane pane = new GridPane();
		pane.add(labelClient, 1, 1);
		pane.add(textAreaClient, 1, 2);
		pane.add(labelServer, 1, 3);
		pane.add(textAreaServer, 1, 4);

		
		// Create a scene and place it on the stage.
		Scene scene = new Scene(pane, 450, 200);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene); // Set the scene
		primaryStage.show(); // Display the scene
		
		// Creates a new thread
		new Thread(() -> {
			try {
				// Create a socket to connect to the server. 
				@SuppressWarnings("resource")
				Socket socket = new Socket("localhost", 8080);
				// Create an input stream to receive data from the server. 
				fromServer = new DataInputStream(socket.getInputStream());
				// Create an output stream to send data to the server.
				toServer = new DataOutputStream(socket.getOutputStream());
				
				while(true) {
					// Text is sent to the server when the enter key is pressed. 
					textAreaClient.setOnKeyReleased(e -> {
						if(e.getCode() == KeyCode.ENTER) {
							try {
								// Send message to the server 
								toServer.writeUTF(textAreaClient.getText().trim());
								toServer.flush();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
					
					// Receive message from the server
					String serverMessage = fromServer.readUTF();
					textAreaServer.appendText(serverMessage);
					
					//socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
