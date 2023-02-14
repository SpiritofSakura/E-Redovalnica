package sample;

public class Ucitelj {


    private static String ime;
    private  static String priimek;
    private static String eposta;
    private static String telefon;
    private static String username;

    public Ucitelj(String ime, String priimek, String eposta, String telefon, String username) {

        this.ime = ime;
        this.priimek = priimek;
        this.eposta = eposta;
        this.telefon = telefon;
        this.username = username;
    }


    public static String getIme() {
        return ime;
    }

    public static String getPriimek() {
        return priimek;
    }

    public static String getEposta() {
        return eposta;
    }

    public static String getTelefon() {
        return telefon;
    }

    public static String getUsername() {
        return username;
    }
}
