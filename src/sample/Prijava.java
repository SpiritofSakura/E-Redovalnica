package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class Prijava implements Initializable {
    public Prijava() {

    }

    @FXML
    private TextField text_username;
    @FXML
    private PasswordField text_password;

    @FXML
    private Label pozabil_geslo;

    private String encryptedpassword = null;
    private boolean aktiven = false;

//KO UPORABNIK PRITISNE GUMB PRIJAVA
    public void login_click(ActionEvent event) throws IOException, NoSuchAlgorithmException {

        if(text_username.getText().isEmpty())
        {
            aktiven = false;
        }
        else
        {
            aktiven = true;
        }

        if(text_password.getText().isEmpty())
        {
            aktiven = false;
        }
        else
        {
            aktiven = true;
        }
        if(aktiven == true)
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(text_password.getText().getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
            Baza.LogIn(event,text_username.getText(),encryptedpassword);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("V polja vnesite vse podatke!");
            alert.show();
        }




    }

    //KO UPORABNIK PRITISNE GUMB REGISTRACIJA
    public void registracija_click(ActionEvent event ) throws IOException {
        Main.changeScene("sign-up.fxml",377,570);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pozabil_geslo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("pass.fxml",377,570);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
