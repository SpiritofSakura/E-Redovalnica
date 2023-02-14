package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    private static Stage stg;


    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml")); //Napišeš katera stran se zažene.
        primaryStage.setTitle("APP");
        primaryStage.setScene(new Scene(root, 377, 570)); //Določiš višino in širino
        primaryStage.show();
    }

    public static void changeScene(String fxml,Integer sirina,Integer visina) throws IOException {
        Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
        stg.setScene(new Scene(pane,sirina,visina));

    }

    public static void main(String[] args){ launch(args);}
}
