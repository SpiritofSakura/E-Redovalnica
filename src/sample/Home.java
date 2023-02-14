package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Home implements Initializable {

    private static String ime_dijaka;
    private static String priimek_dijaka;
    private static Integer dijak_id;



    public static String getIme() {
        return ime_dijaka;
    }

    public static String getPriimek() {
        return priimek_dijaka;
    }

    public static Integer getId() {return dijak_id;}




    @FXML
    private TextField text_ime;

    @FXML
    private TextField text_priimek;


    @FXML
    private Button moj_profil;

    @FXML
    private Button button_izvozi;

    @FXML
    private ChoiceBox combo_razred;
    @FXML
    private ChoiceBox combo_letnik;


    //TABELA
    @FXML
    private TableView<Dijak> tabela;

//    @FXML
//    private TableColumn<Dijak,Integer> row_id;

    @FXML
    private TableColumn<Dijak,String> row_ime;

    @FXML
    private TableColumn<Dijak,String> row_priimek;

    @FXML
    private TableColumn<Dijak,String> row_razred;

    @FXML
    private TableColumn<Dijak, String> row_ocene;



    //EVENTI - AKCIJE
    public void letnik_klik(MouseEvent mouseEvent){
        combo_razred.setDisable(true);
    }



    public void razred_klik(MouseEvent mouseEvent) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moj_profil.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.changeScene("profil-ucitelj.fxml",350,353);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //row_id.setCellValueFactory(new PropertyValueFactory<Dijak,Integer>("ID"));
        row_ime.setCellValueFactory(new PropertyValueFactory<Dijak,String>("Ime"));
        row_priimek.setCellValueFactory(new PropertyValueFactory<Dijak,String>("Priimek"));
        row_razred.setCellValueFactory(new PropertyValueFactory<Dijak,String>("Razred"));
        row_ocene.setCellValueFactory(new PropertyValueFactory<Dijak,String>("Ocene"));

        tabela.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //tabela.getSelectionModel().setCellSelectionEnabled(true);


        combo_razred.setDisable(true);
        combo_letnik.getItems().add("1.");
        combo_letnik.getItems().add("2.");
        combo_letnik.getItems().add("3.");
        combo_letnik.getItems().add("4.");

        combo_letnik.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                combo_razred.setDisable(false);
                tabela.getItems().clear();

                try {
                    Baza.fillRazred((Integer) t1,combo_razred);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


            }


        });

        //Če uporabnik pritisne na tabelo
        tabela.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            if(event.isShortcutDown() || event.isShiftDown())
                event.consume();
        });


        tabela.setRowFactory( tv -> { //Če uporabnik 2x pritisne na tabelo, se mu zabeleži vrednost vrstice ki jo je pritisnil
            TableRow<Dijak> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Dijak dijak = row.getItem();
                    ime_dijaka = dijak.getIme();
                    priimek_dijaka =dijak.getPriimek();
                    dijak_id = dijak.getDijakId();
                    try {
                        Main.changeScene("dijaki-page.fxml",829,451);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        //Če uporanbik prisloni miško stran od tabele, se table počisti.
        tabela.setOnMouseExited(event -> {
            tabela.getSelectionModel().clearSelection();
        });


        text_ime.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)){
                    String ime = text_ime.getText();

                    if(ime.equals(null) || ime.equals(""))
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Prosim vnesite ime v vnesno polje!");
                        alert.show();
                    }
                    else
                    {
                        try {
                            //Baza.PoisciDijaka(vnos,tabela);
                            tabela.getItems().clear();
                            Baza.PoisciDijaka(ime,text_priimek.getText(),tabela);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }


                    }


                }
            }
        });



        text_priimek.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)){
                    String priimek = text_priimek.getText();
                    if(priimek.equals(null) || priimek.equals(""))
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Prosim vnesite priimek v vnesno polje!");
                        alert.show();
                    }
                    else
                    {
                        try {
                            //Baza.PoisciDijaka(vnos,tabela);
                            tabela.getItems().clear();
                            Baza.PoisciDijaka(text_ime.getText(),priimek,tabela);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }


                    }


                }
            }
        });

        combo_razred.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                //Pretvorimo v string kar kliknamo na choicebox.
                String vrstica = (String) combo_razred.getItems().get((Integer) t1);

                try {
                    Baza.fillDijak(vrstica,tabela); //Napolni tabelo iz baze glede na to kaj uporabnik izbere pod razrede

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if(tabela.getItems().isEmpty())
                {
                    button_izvozi.setDisable(true);
                }
                else
                {
                    button_izvozi.setDisable(false);
                }
            }
        });

        button_izvozi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(tabela.getItems().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Tabela je prazna, nič ni za izvozit!");
                    alert.show();
                }
                else
                {
                    Workbook workbook = new HSSFWorkbook();
                    Sheet spreadsheet = workbook.createSheet("dijaki");

                    Row row = spreadsheet.createRow(0);

                    for (int j = 0; j < tabela.getColumns().size(); j++) {
                        row.createCell(j).setCellValue(tabela.getColumns().get(j).getText());
                    }

                    for (int i = 0; i < tabela.getItems().size(); i++) {
                        row = spreadsheet.createRow(i + 1);
                        for (int j = 0; j < tabela.getColumns().size(); j++) {
                            if(tabela.getColumns().get(j).getCellData(i) != null) {
                                row.createCell(j).setCellValue(tabela.getColumns().get(j).getCellData(i).toString());
                            }
                            else {
                                row.createCell(j).setCellValue("");
                            }
                        }
                    }

                    FileOutputStream fileOut = null;
                    try {
                        fileOut = new FileOutputStream("ocene_dijakov.xls");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        workbook.write(fileOut);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Podatki so se uspešno izvozli!");
                    alert.show();
                }

            }
            });
        }


    }

