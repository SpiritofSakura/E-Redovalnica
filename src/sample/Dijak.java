package sample;

import javafx.scene.text.Text;

public class Dijak{
    private Integer id;
    private String ime;
    private String priimek;

    private String razred;

    private String ocene;

    public Dijak(Integer id,String ime, String priimek, String razred, String ocene) {
        this.id = id;
        this.ime = ime;
        this.priimek = priimek;
        this.razred = razred;
        this.ocene = ocene;
    }

    public Integer getDijakId() { return id;}
    public String getIme() {
        return ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public String getRazred() {
        return razred;
    }

    public String getOcene() {
        return ocene;
    }
}
