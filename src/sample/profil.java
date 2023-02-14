package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profil implements Initializable {


    @FXML private Button button_nazaj;

    @FXML private Button button_odjava;

    @FXML private Label text_ime;

    @FXML private Label text_priimek;

    @FXML private Label text_email;

    @FXML private Label text_telefon;

    @FXML private Label text_username;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        text_ime.setText(Ucitelj.getIme());
        text_priimek.setText(Ucitelj.getPriimek());
        text_email.setText(Ucitelj.getEposta());
        text_username.setText(Ucitelj.getUsername());
        text_telefon.setText(Ucitelj.getTelefon());
        button_nazaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("home-page.fxml", 829,451);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button_odjava.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("sample.fxml",377,570);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
