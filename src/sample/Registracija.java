package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sun.util.calendar.LocalGregorianCalendar;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Registracija implements Initializable {
    public Registracija() {

    }
    @FXML
    private Button btn_registracija;

    @FXML
    private TextField text_ime;
    @FXML
    private TextField text_priimek;
    @FXML
    private TextField text_eposta;
    @FXML
    private TextField text_username;
    @FXML
    private TextField text_tel;

    @FXML
    private TextField text_geslo;
    @FXML
    private PasswordField text_preveri;

    @FXML
    private Label ime_w;
    @FXML
    private Label priimek_w;
    @FXML
    private Label eposta_w;
    @FXML
    private Label u_w;
    @FXML
    private Label telefon_w;
    @FXML
    private Label g_w;
    @FXML
    private Label p_w;

    private String usrnm;
    private String geslo;
    private String preveri;
    private boolean aktiven = false;
    private String encryptpass = null;
    private String ecnryptpass1 = null;

    

    public void ustvari_racun(ActionEvent event) throws IOException {
        //java.sql.Date datumroj = java.sql.Date.valueOf(text_datumroj.getValue());
        //System.out.println(text_ime.getText() + " " + text_priimek.getText() + " " + text_eposta.getText() + " " + text_username.getText() + " " + text_tel.getText() + " " + text_geslo.getText());
        //Baza.signUpUser(event,text_ime.getText(),text_priimek.getText(),text_eposta.getText(),text_username.getText(),text_tel.getText(), text_geslo.getText());
        //System.out.println(text_username.getText());
        //Main.changeScene("home-page.fxml");


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_registracija.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


                if(text_ime.getText().isEmpty())
                {
                    aktiven = false;
                    ime_w.setText("Vnesite ime!");
                }
                else {
                    aktiven = true;
                    ime_w.setText("");
                }
                if(text_priimek.getText().isEmpty())
                {
                    aktiven = false;
                    priimek_w.setText("Vnesite priimek!");
                }
                else {
                    priimek_w.setText("");
                    aktiven = true;
                }
                if(text_eposta.getText().isEmpty())
                {

                    eposta_w.setText("Vnesite email!");
                    aktiven = false;
                }
                else {
                    eposta_w.setText("");
                    aktiven = true;
                }
                if(text_username.getText().isEmpty())
                {

                    u_w.setText("Vnesite uporabniško ime!");
                    aktiven = false;
                }
                else {
                    u_w.setText("");
                    aktiven = true;
                }
                if(text_tel.getText().isEmpty())
                {

                    telefon_w.setText("Vnesite tel. stevilko!");
                    aktiven = false;
                }
                else {
                    telefon_w.setText("");
                    aktiven = true;
                }
                if(text_geslo.getText().isEmpty())
                {

                    g_w.setText("Vnesite geslo!");
                    aktiven = false;
                }
                else {
                    g_w.setText("");
                    geslo = text_geslo.getText();
                    aktiven = true;

                    /* MessageDigest instance for MD5. */
                    MessageDigest m = null;
                    try {
                        m = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    /* Add plain-text password bytes to digest using MD5 update() method. */
                    m.update(geslo.getBytes());

                    /* Convert the hash value into bytes */
                    byte[] bytes = m.digest();

                    /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
                    StringBuilder s = new StringBuilder();
                    for(int i=0; i< bytes.length ;i++)
                    {
                        s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    /* Complete hashed password in hexadecimal format */
                    encryptpass = s.toString();
                }
                if(text_preveri.getText().isEmpty())
                {

                    p_w.setText("Ponovno vnesite geslo!");
                    aktiven = false;
                }
                else {
                    p_w.setText("");
                    preveri = text_preveri.getText();
                    aktiven = true;
                    /* MessageDigest instance for MD5. */
                    MessageDigest m = null;
                    try {
                        m = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    /* Add plain-text password bytes to digest using MD5 update() method. */
                    m.update(preveri.getBytes());

                    /* Convert the hash value into bytes */
                    byte[] bytes = m.digest();

                    /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
                    StringBuilder s = new StringBuilder();
                    for(int i=0; i< bytes.length ;i++)
                    {
                        s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    /* Complete hashed password in hexadecimal format */
                    ecnryptpass1 = s.toString();
                }

                if(encryptpass.equals(ecnryptpass1) && aktiven == true)
                {
                    Baza.signUpUser(text_ime.getText(),text_priimek.getText(),text_eposta.getText(),text_username.getText(),text_tel.getText(), encryptpass);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR: Geslo se ne ujema!");
                    alert.show();
                }
            }
        });

    }
}
