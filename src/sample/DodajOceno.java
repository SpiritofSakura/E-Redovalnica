package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DodajOceno implements Initializable {

    @FXML
    private ChoiceBox choice_sklop;

    @FXML
    private ChoiceBox choice_vrsta;

    @FXML
    private TextArea text_message;


    @FXML
    private Label m1;

    @FXML
    private Label m2;

    @FXML
    private Label m3;

    //RADIO BTN
    @FXML
    private RadioButton radio_nps,radio_1,radio_2,radio_3,radio_4,radio_5;

    //GUMBI
    @FXML
    private Button button_preklici;

    @FXML
    private Button button_dodaj;


    @FXML
    private Button button_spremeni;

    @FXML
    private Button sklop_dodaj;

    @FXML
    private Button vrsta_dodaj;

    private String ocena;

    Ocene ocene;
    private String vrstica_sklop = null;
    private String vrstica_vrsta = null;
    private boolean check = true;

    private boolean preveri = true;



    public void Ocena(ActionEvent event)
    {

        if(radio_nps.isSelected())
        {
            ocena = "NPS";
             preveri = false;

        }

        if(radio_1.isSelected())
        {
            ocena = "1";
            preveri = false;
        }

        if(radio_2.isSelected())
        {
            ocena = "2";
             preveri = false;
        }

        if(radio_3.isSelected())
        {
            ocena = "3";
             preveri = false;
        }

        if(radio_4.isSelected())
        {
            ocena = "4";
             preveri = false;
        }

        if(radio_5.isSelected())
        {
            ocena = "5";
             preveri = false;
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if(OceneDijaka.aktivator == true)
        {
            choice_sklop.setValue(OceneDijaka.sklop);
            choice_vrsta.setValue(OceneDijaka.vrsta);
            text_message.setText(OceneDijaka.opis);
            button_dodaj.setDisable(true);
            button_spremeni.setDisable(false);
            choice_sklop.setDisable(true);
            choice_vrsta.setDisable(true);
            text_message.setDisable(true);
            m1.setDisable(true);
            m2.setDisable(true);
            m3.setDisable(true);
        }
        else
        {
            button_dodaj.setDisable(false);

        }

        //NAPOLNI CHOICE BOX KAR JE IZ BAZE
        try {
            Baza.fillSklop(choice_sklop);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Baza.fillVrsta(choice_vrsta);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        button_preklici.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("dijaki-page.fxml",829,451);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        choice_sklop.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                vrstica_sklop = (String) choice_sklop.getItems().get((Integer) t1);


            }


        });

        choice_vrsta.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                vrstica_vrsta = (String) choice_vrsta.getItems().get((Integer) t1);

            }
        });



        button_dodaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boolean sklop = choice_sklop.getSelectionModel().isEmpty();
                boolean vrsta = choice_vrsta.getSelectionModel().isEmpty();
                String opis = text_message.getText();


                if(sklop == true && vrsta == true && preveri == true)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("POZORILO: Prosim vnesite podatke v vsa pomembna polja!");
                    alert.show();
                }
                else if(sklop == true && vrsta == true)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("POZORILO: Prosim vnesite podatke v vsa pomembna polja!");
                    alert.show();
                }
                else if(sklop == true)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("POZORILO: Prosim vnesite podatke v vsa pomembna polja!");
                    alert.show();
                }
                else if(vrsta == true)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("POZORILO: Prosim vnesite podatke v vsa pomembna polja!");
                    alert.show();
                }
                else if(preveri == true)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("POZORILO: Prosim vnesite podatke v vsa pomembna polja!");
                    alert.show();
                }
                else
                {

                    try {
                        Baza.dodajOceno(vrstica_sklop,vrstica_vrsta,opis,ocena,DIjakPodatki.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Ocena uspešno vpisana");
                    alert.show();
                    try {
                        Main.changeScene("dijaki-page.fxml",829,451);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        button_spremeni.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                try {
                    Baza.spremeniOceno(OceneDijaka.id,ocena);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    Main.changeScene("dijaki-page.fxml",829,451);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
