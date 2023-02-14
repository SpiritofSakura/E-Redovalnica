package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Reset implements Initializable {
    @FXML
    private PasswordField novo_geslo;

    @FXML
    private PasswordField novo_geslo1;


    @FXML
    private Button button_spremeni;



    private String encryptedpass;
    private String encrpytedpass1;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_spremeni.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /* MessageDigest instance for MD5. */
                MessageDigest m = null;
                try {
                    m = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                /* Add plain-text password bytes to digest using MD5 update() method. */
                m.update(novo_geslo.getText().getBytes());

                /* Convert the hash value into bytes */
                byte[] bytes = m.digest();

                /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
                StringBuilder s = new StringBuilder();
                for(int i=0; i< bytes.length ;i++)
                {
                    s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }

                /* Complete hashed password in hexadecimal format */
                encryptedpass = s.toString();


                /* MessageDigest instance for MD5. */
                MessageDigest mi = null;
                try {
                    mi = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                /* Add plain-text password bytes to digest using MD5 update() method. */
                mi.update(novo_geslo1.getText().getBytes());

                /* Convert the hash value into bytes */
                byte[] bytes1 = mi.digest();

                /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
                StringBuilder si = new StringBuilder();
                for(int i=0; i< bytes1.length ;i++)
                {
                    si.append(Integer.toString((bytes1[i] & 0xff) + 0x100, 16).substring(1));
                }

                /* Complete hashed password in hexadecimal format */
                encrpytedpass1 = si.toString();
                System.out.println("Geslo:" + encryptedpass + " Ponovno geslo: "+ encrpytedpass1 + "EPOŠTA: "+ PasswordReset.email);
                if(encryptedpass.equals(encrpytedpass1))
                {
                    try {
                        Baza.spremeniGeslo(encryptedpass,PasswordReset.email);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Geslo se ne ujema.");
                    alert.show();
                }
            }
        });

    }
}
