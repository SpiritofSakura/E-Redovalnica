package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OceneDijaka implements Initializable {

    //POLJA
    @FXML
    private Label text_ime;

    @FXML
    private Label text_priimek;

    @FXML
    private Label text_naslov;

    @FXML
    private Label text_email;

    @FXML
    private Label text_razred;

    @FXML
    private Label text_telefon;

    @FXML
    private Label text_kraj;

    @FXML
    private Label text_razrednik;

    //GUBMI

    @FXML
    private Button button_dodaj;

    @FXML
    private Button button_spremeni;

    @FXML
    private Button button_izbrisi;

    @FXML
    private Button button_nazaj;

    //TABELA
    @FXML
    private TableView<Ocene> tabela;

    @FXML
    private TableColumn<Ocene,Text> row_ocena;

    @FXML
    private TableColumn<Ocene,String> row_vrsta;

    @FXML
    private TableColumn<Ocene,String> row_sklop;

    @FXML
    private TableColumn<Ocene, String> row_opis;

    public static Ocene ocene;
    public static boolean aktivator;
    private Integer ocena_id;
    public static Integer id;
    public static String vrsta;
    public static String sklop;
    public static String opis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tabela.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        try {
            Baza.fillOcene(Home.getId(),tabela);
        } catch (SQLException throwables) {


            throwables.printStackTrace();
        }
        //NASTAVIM VREDNOSTI V POLJA
        text_ime.setText(Home.getIme());
        text_priimek.setText(Home.getPriimek());
        text_email.setText(DIjakPodatki.getEmail());
        text_razred.setText(DIjakPodatki.getKratica());
        text_naslov.setText(DIjakPodatki.getNaslov());
        text_kraj.setText(DIjakPodatki.getKraj());
        text_telefon.setText(DIjakPodatki.getTel_st());

        try {
            text_razrednik.setText(Baza.pridobiRazrednika(DIjakPodatki.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        row_ocena.setCellValueFactory(new PropertyValueFactory<Ocene, Text>("Ocena"));
        row_vrsta.setCellValueFactory(new PropertyValueFactory<Ocene,String>("Vrsta"));
        row_sklop.setCellValueFactory(new PropertyValueFactory<Ocene,String>("Sklop"));
        row_opis.setCellValueFactory(new PropertyValueFactory<Ocene,String>("Opis"));

        //Ko pritisnem gumb nazaj me odpelje na prejšnjo stran
        button_nazaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("home-page.fxml",829,451);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button_dodaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("dodaj-spremeni.fxml",377,684);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        tabela.setRowFactory( tv -> { //Če uporabnik 1x pritisne na tabelo, se mu zabeleži vrednost vrstice ki jo je pritisnil
            TableRow<Ocene> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    ocene = new Ocene(row.getItem().getId(),row.getItem().getOcena(), row.getItem().getVrsta(), row.getItem().getSklop(),row.getItem().getOpis());
                    ocena_id = row.getItem().getId();
                    id = ocena_id;
                    vrsta = row.getItem().getVrsta();
                    sklop = row.getItem().getSklop();
                    opis = row.getItem().getOpis();
                    button_izbrisi.setDisable(false);
                    button_spremeni.setDisable(false);
                    button_dodaj.setDisable(true);
                    button_spremeni.setDisable(true);
                }

                if(event.getClickCount() == 2 && (! row.isEmpty()))
                {
                    button_spremeni.setDisable(false);
                }
            });
            return row;
        });

        //Če uporanbik prisloni miško stran od tabele, se table počisti.
        tabela.setOnMouseExited(event -> {


            button_dodaj.setDisable(false);
        });

        //Če uporabnik pritisne na tabelo
        tabela.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            if(event.isShortcutDown() || event.isShiftDown())
                event.consume();
        });

        button_spremeni.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    aktivator = true;
                    Main.changeScene("dodaj-spremeni.fxml",377,684);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        button_izbrisi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {

                    Baza.izbrisiOceno(ocena_id);
                    Main.changeScene("dijaki-page.fxml",829,451);
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

    }
}
