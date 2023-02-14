package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URLConnection;
import java.sql.*;
import java.util.List;

import org.postgresql.*;

public class Baza {



    public static void spremeniOkno(ActionEvent event, String fxmlFile, String naslov, String username) {
        Parent root = null;

        if(username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(Baza.class.getResource(fxmlFile));
                root = loader.load();
//                Home loggedInController = loader.getController();
//                loggedInController.setUserInfromation(username);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        else {
            try {
                root = FXMLLoader.load(Baza.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(naslov);
        stage.setScene(new Scene(root, 829,451));
        stage.show();
    }

    public static void signUpUser(String ime, String priimek, String eposta, String username, String telefon, String geslo){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement check = null;
        ResultSet resultSet = null;
        Ucitelj ucitelj;

        try {

            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki","admin","IWiKDGw5tSP6");

            check = connection.prepareStatement("SELECT * FROM get_ucitelj_info(?)");
            check.setString(1,username);
            resultSet = check.executeQuery();

            if (resultSet.isBeforeFirst()) { //Če uporabnik že obstaja
                System.out.println("Uporabnik že obstaja.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ne moraš uporabiti tega uporabniškega gesla, ker že obstaja.");
                alert.show();
            }
            else {
                psInsert = connection.prepareStatement("select insert_ucitelj(?,?,?,?,?,?);");
                psInsert.setString(1,ime);
                psInsert.setString(2,priimek);
                psInsert.setString(3,eposta);
                psInsert.setString(4,username);
                psInsert.setString(5,telefon);
                psInsert.setString(6,geslo);

                psInsert.execute();
                ucitelj = new Ucitelj(ime,priimek,eposta,telefon,username);
                Main.changeScene("home-page.fxml",829,451);
            }
        } catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (check != null)
            {
                try {
                    check.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null)
            {
                try {
                    psInsert.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
    }

    public static void LogIn(ActionEvent event, String username, String geslo)
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Ucitelj ucitelj;
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki","admin","IWiKDGw5tSP6");
            stmt = connection.prepareStatement("SELECT * FROM get_ucitelj_info(?);");
            stmt.setString(1,username);
            resultSet = stmt.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("User not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Vnešeni podatki so napačni!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String pridobljeno_geslo = resultSet.getString("geslo");
                    //Še pridobiš vse ostale parametre...
                    //..
                    if (pridobljeno_geslo.equals(geslo)) {
                        String ime = resultSet.getString("ime");
                        String priimek = resultSet.getString("priimek");
                        String uporabnik = resultSet.getString("username");
                        String tel_st = resultSet.getString("tel_st");
                        String eposta = resultSet.getString("eposta");
                        ucitelj = new Ucitelj(ime,priimek,uporabnik,tel_st,eposta);
                        Main.changeScene("home-page.fxml",829,451);
                        //Main.changeScene("dodaj-spremeni.fxml",377,684);
                    } else {
                        System.out.println("Geslo se ne ujema!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Vnešeni podatki so napačni!");
                        alert.show();
                    }
                }
            }
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
    }


    public static void fillRazred(Integer vrsta, ChoiceBox combo_razred) throws SQLException {

            Connection connection = null;
            PreparedStatement stmt = null;
            ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_razredi_kratica();");
            resultSet = stmt.executeQuery();

            switch (vrsta) {
                case 0:
                    if (combo_razred.getItems() != null) {
                        combo_razred.getItems().clear();
                        while (resultSet.next()) {
                            String razredi = resultSet.getString("kratica");
                            char a = razredi.charAt(0);
                            String letnik = Character.toString(a);
                            if (letnik.equals("1")) {
                                combo_razred.getItems().add(razredi);
                            }
                        }
                    }
                    while (resultSet.next()) {
                        String razredi = resultSet.getString("kratica");
                        char a = razredi.charAt(0);
                        String letnik = Character.toString(a);
                        if (letnik.equals("1")) {
                            combo_razred.getItems().add(razredi);
                        }
                    }

                    break;

                case 1:
                    if (combo_razred.getItems() != null) {
                        combo_razred.getItems().clear();
                        while (resultSet.next()) {
                            String razredi = resultSet.getString("kratica");
                            char a = razredi.charAt(0);
                            String letnik = Character.toString(a);
                            if (letnik.equals("2")) {
                                combo_razred.getItems().add(razredi);
                            }
                        }
                    }
                    while (resultSet.next()) {
                        String razredi = resultSet.getString("kratica");
                        char a = razredi.charAt(0);
                        String letnik = Character.toString(a);
                        if (letnik.equals("2")) {
                            combo_razred.getItems().add(razredi);
                        }
                    }
                    break;

                case 2:
                    if (combo_razred.getItems() != null) {
                        combo_razred.getItems().clear();
                        while (resultSet.next()) {
                            String razredi = resultSet.getString("kratica");
                            char a = razredi.charAt(0);
                            String letnik = Character.toString(a);
                            if (letnik.equals("3")) {
                                combo_razred.getItems().add(razredi);
                            }
                        }
                    }
                    while (resultSet.next()) {
                        String razredi = resultSet.getString("kratica");
                        char a = razredi.charAt(0);
                        String letnik = Character.toString(a);
                        if (letnik.equals("3")) {
                            combo_razred.getItems().add(razredi);
                        }
                    }
                    break;

                case 3:
                    if (combo_razred.getItems() != null) {
                        combo_razred.getItems().clear();
                        while (resultSet.next()) {
                            String razredi = resultSet.getString("kratica");
                            char a = razredi.charAt(0);
                            String letnik = Character.toString(a);
                            if (letnik.equals("4")) {
                                combo_razred.getItems().add(razredi);
                            }
                        }
                    }
                    while (resultSet.next()) {
                        String razredi = resultSet.getString("kratica");
                        char a = razredi.charAt(0);
                        String letnik = Character.toString(a);
                        if (letnik.equals("4")) {
                            combo_razred.getItems().add(razredi);
                        }
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void fillDijak(String razred, TableView<Dijak> tabela_dijaki) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Dijak dijak;

        try{
            ObservableList<Dijak>list = FXCollections.observableArrayList();
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_dijaki_by_kratica(?);");
            stmt.setString(1,razred);
            resultSet = stmt.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");

                String ime = resultSet.getString("ime");
                String priimek = resultSet.getString("priimek");
                String kratica = resultSet.getString("kratica");
                String ocene = VnesiOcene(id); //Vpiše ocene za posameznega dijaka

                list.add(dijak = new Dijak(id,ime, priimek, kratica,ocene ));

            }
            if (list.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("V tem razredu ni nobenega dijaka!");
                alert.show();
            }
            tabela_dijaki.setItems(list);

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }



    }

    public static String VnesiOcene(int id) throws  SQLException {

        String ocena = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {


            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_ocene_by_dijak_id(?);");
            stmt.setInt(1, id);
            resultSet = stmt.executeQuery();


            boolean found = false;
            while (resultSet.next()) {

                if (!found) {

                    ocena = resultSet.getString("ocena");
                    found = true;



                } else {
                    ocena = ocena + "  " + resultSet.getString("ocena");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ocena;
    }

    public static void PoisciDijaka(String ime_dijaka, String priimek_dijaka, TableView<Dijak> tabela) throws SQLException { //NAREDI SEARCH ZA IME IN PRIIMEK POSEBEJ.


        String ime = null;
        String priimek = null;
        String kratica = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try{
        ObservableList<Dijak>list = FXCollections.observableArrayList();
        connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
        if(ime_dijaka.equals("") ||ime_dijaka.equals(null))
        {
            stmt = connection.prepareStatement("select * from get_dijaki_by_priimek(?);");
            stmt.setString(1,priimek_dijaka);
        }
        else if(priimek_dijaka.equals("") ||priimek_dijaka.equals(null))
        {
            stmt = connection.prepareStatement("select * from get_dijaki_by_ime(?)");
            stmt.setString(1,ime_dijaka);
        }
        else
        {
            stmt = connection.prepareStatement("select * from get_dijaki_by_ime_priimek(?,?);");
            stmt.setString(1,ime_dijaka);
            stmt.setString(2,priimek_dijaka);
        }

        resultSet = stmt.executeQuery();

        while(resultSet.next())
        {
            int id = resultSet.getInt("id");
            ime = resultSet.getString("ime");
            priimek = resultSet.getString("priimek");
            kratica = resultSet.getString("kratica");
            String ocene = VnesiOcene(id); //Vpiše ocene za posameznega dijaka
            list.add( new Dijak(id,ime, priimek, kratica,ocene ));




        }

        if(list.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("V sistemu ni nobenega dijaka z tem imenom in priimkom!");
            alert.show();
        }

        else
        {
            tabela.setItems(list);
        }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void fillOcene(Integer dijak_id,TableView<Ocene> tabela_ocene) throws SQLException {


        int stevec = 0;
        DIjakPodatki dijak;
        Ocene ocene;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try{
        ObservableList<Ocene>list = FXCollections.observableArrayList();
        connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
        stmt = connection.prepareStatement("select * from get_ocene(?);");
        stmt.setInt(1,dijak_id);

        resultSet = stmt.executeQuery();


        while(resultSet.next())
        {
            stevec = stevec + 1;
            if(stevec == 1)
            {
                Integer id = resultSet.getInt("dijak_id");

                String email = resultSet.getString("email");
                String tel_st = resultSet.getString("tel_st");
                String naslov = resultSet.getString("naslov");
                String kratica = resultSet.getString("kratica");
                String kraj = resultSet.getString("ime");
                dijak = new DIjakPodatki(id,email,tel_st,naslov,kratica,kraj);

            }

            Integer o_id = resultSet.getInt("ocena_id");
            String  x = resultSet.getString("ocena");
            String vrsta = resultSet.getString("vrsta");
            String sklop = resultSet.getString("sklop");
            String opis = resultSet.getString("opis");
            Text ocena = new Text();
            ocena.setText(x);
            if (vrsta.equals("Ustna"))
            {

                ocena.setFill(Color.RED);
            }
            else if (vrsta.equals("Pisna"))
            {
                ocena.setFill(Color.BLUE);
            }
            else
            {
                ocena.setFill(Color.ORANGE);
            }

            ocene = new Ocene(o_id,ocena,vrsta,sklop,opis);

            list.add(ocene);

        }
        if(list.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("POZOR: Ta dijak nima se nobene ocene.");
            alert.show();
        }
        else
        {
            tabela_ocene.setItems(list);
        }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public static void fillSklop(ChoiceBox box) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try{
        connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
        stmt = connection.prepareStatement("select * from get_sklopi();");
        resultSet = stmt.executeQuery();

        if(resultSet.equals(null))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("POZOR: Ocene nimajo nobenega sklopa. Prosim, da dodajte sklop!");
            alert.show();
        }
        else
        {
            while(resultSet.next()) {
                String sklop = resultSet.getString("sklop");
                box.getItems().add(sklop);
            }
        }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void fillVrsta(ChoiceBox box) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_vrsta();");
            resultSet = stmt.executeQuery();

            if(resultSet.equals(null))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("POZOR: Ocene nimajo nobenega sklopa. Prosim, da dodajte sklop!");
                alert.show();
            }
            else
            {
                while(resultSet.next()) {
                    String vrsta = resultSet.getString("vrsta");
                    box.getItems().add(vrsta);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void dodajOceno(String sklop,String vrsta, String opis,String ocena, Integer id) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            if(opis.equals(null) ||opis.equals(""))
            {
                stmt = connection.prepareStatement("select insert_ocene(?,?,?,?);");
                stmt.setString(1,ocena);
                stmt.setString(2,sklop);
                stmt.setString(3,vrsta);
                stmt.setInt(4,id);

            }
            else
            {
                stmt = connection.prepareStatement("select insert_ocene_with_opis(?,?,?,?,?);");
                stmt.setString(1,ocena);
                stmt.setString(2,sklop);
                stmt.setString(3,opis);
                stmt.setString(4,vrsta);
                stmt.setInt(5,id);

            }

            stmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }



    public static void izbrisiOceno(Integer ocena_id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select delete_ocene_by_id(?);");
            stmt.setInt(1, ocena_id);
            stmt.execute();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Ocena je uspešno izbrisana.");
                alert.show();

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NAPAKA. Poskusite ponovno.");
            alert.show();
        }

        stmt.close();
        connection.close();


    }

    public static String pridobiRazrednika(Integer dijak_id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String razrednik = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_razrednik_by_dijak_id(?)");
            stmt.setInt(1, dijak_id);
            resultSet = stmt.executeQuery();

            while(resultSet.next())
            {
                razrednik = resultSet.getString("razrednik");
            }

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NAPAKA. Poskusite ponovno.");
            alert.show();
        }
        resultSet.close();
        stmt.close();
        connection.close();

        return razrednik;
    }

    public static void spremeniOceno(Integer ocena_id,String vnos) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("SELECT update_ocena_by_id(?,?)");
            stmt.setString(1, vnos);
            stmt.setInt(2, ocena_id);
            stmt.execute();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Ocena je uspešno spremenjena.");
            alert.show();

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NAPAKA. Poskusite ponovno.");
            alert.show();
        }

        stmt.close();
        connection.close();
    }

    public static void spremeniGeslo(String geslo, String email) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select update_geslo(?,?); ");
            stmt.setString(1, geslo);
            stmt.setString(2, email);
            stmt.execute();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Vaše geslo je uspešno spremenjeno");
            alert.show();

            Main.changeScene("sample.fxml",377,570);;

        } catch (SQLException | IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NAPAKA. Poskusite ponovno.");
            alert.show();
        }

        stmt.close();
        connection.close();
    }

    public static boolean pridobiEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        boolean check = false;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
            stmt = connection.prepareStatement("select * from get_email(?);");
            stmt.setString(1, email);
            resultSet = stmt.executeQuery();

            while(resultSet.next())
            {
                String eposta = resultSet.getString("eposta");
                check = true;
//                if(eposta.equals(email))
//                {
//
//                }
//                else
//                {
//                    return false;
//                }
            }

        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NAPAKA. Taksnega emaila ni v bazi. Prosim da poskusite ponovno");
            alert.show();
            check = false;
        }

        resultSet.close();
        stmt.close();
        connection.close();
        return check;

    }

//    public static void DodajSklop() throws SQLException{
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        ResultSet resultSet = null;
//
//        try{
//            connection = DriverManager.getConnection("jdbc:postgresql://ep-lucky-wildflower-153335.eu-central-1.aws.neon.tech/dijaki", "admin", "IWiKDGw5tSP6");
//            stmt = connection.prepareStatement("");
//            resultSet = stmt.executeQuery();
//
//            if(resultSet.equals(null))
//            {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("POZOR: Ocene nimajo nobenega sklopa. Prosim, da dodajte sklop!");
//                alert.show();
//            }
//            else
//            {
//                while(resultSet.next()) {
//                    String sklop = resultSet.getString("sklop");
//                    box.getItems().add(sklop);
//                }
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        } finally {
//            if (resultSet != null)
//            {
//                try {
//                    resultSet.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//            if (stmt != null)
//            {
//                try {
//                    stmt.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null)
//            {
//                try {
//                    connection.close();
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
