package com.albom.application;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class UpriseApplication extends Application {

	public static void main(String[] args) {
		launch(args);

		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 400, 250);
        primaryStage.setTitle("UPRISE 2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
