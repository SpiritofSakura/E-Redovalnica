package sample;

import com.sun.org.glassfish.external.statistics.annotations.Reset;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class PasswordReset implements Initializable {

    @FXML
    private TextField text_email;

    @FXML
    private TextField text_koda;

    @FXML
    private Button button_poslji;

    @FXML
    private Button button_preveri;

    @FXML
    private Label label;

    @FXML
    private Label _koda;
    private Integer randomCode;

    @FXML
    private Label vnesi_kodo;

    public static String email;
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        final int[] x = new int[1];



        button_poslji.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(text_email.getText().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("V polja vnesite vaš email!");
                    alert.show();
                }
                else {
                    try {
                        if(Baza.pridobiEmail(text_email.getText())==true)
                        {
                            email = text_email.getText();
                            try {

                                Random rand = new Random();
                                randomCode = rand.nextInt(999999);
//
//                                String host = "smtp.gmail.com";
//                                String user = "tjas.jelen@gmail.com";
//                                //String pass = "Missouri63";
//                                String to = text_email.getText();
//                                String subject = "Resetiranje kode";
//                                String message = "Vaša koda za geslo je " + randomCode;
//                                boolean sessionDebug = false;
//                                Properties pros = System.getProperties();
//                                pros.put("mail.smtp.starttls.enable", true);
//                                pros.put("mail.smtp.starttls.host", host);
//                                pros.put("mail.smtp.port", 587);
//                                pros.put("mail.smtp.auth", true);
//                                pros.put("mail.smtp.starttls.required", true);
//
//                                java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//                                Session mailSession = Session.getDefaultInstance(pros, null);
//                                mailSession.setDebug(sessionDebug);
//                                Message msg = new MimeMessage(mailSession);
//                                msg.setFrom(new InternetAddress(user));
//                                InternetAddress [] address = {new InternetAddress(to)};
//                                msg.setRecipient(Message.RecipientType.TO, address[0]);
//                                msg.setSubject(subject);
//                                msg.setText(message);
//                                Transport transport = mailSession.getTransport("smtp");
//                                transport.connect(host, user, pass);
//                                transport.sendMessage(msg, msg.getAllRecipients());
//                                transport.close();

//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                                alert.setContentText("Koda je bila poslana na vaš email.");
//                                alert.show();
                                button_preveri.setDisable(false);
                                text_koda.setDisable(false);
                                button_poslji.setDisable(true);
                                text_email.setDisable(true);
                                _koda.setDisable(false);
                                vnesi_kodo.setDisable(false);
                                label.setText(String.valueOf(randomCode));
                            }catch (Exception ex){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("NAPAKA");
                                alert.show();
                            }
                        }
                        else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Ta email ne obstaja!");
                            alert.show();
                            text_email.clear();
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });


        button_preveri.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int koda=0;
                try{
                    koda = Integer.parseInt(text_koda.getText());
                }
                catch(Exception ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Vnesili ste črke. Vnesite SAMO številke.");
                    alert.show();
                }
                if(koda == randomCode)
                {
                    try {
                        Main.changeScene("reset-pass.fxml",377,570);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


        };
    });


    }
}
