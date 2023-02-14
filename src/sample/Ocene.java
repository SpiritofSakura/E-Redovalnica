package sample;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.text.Text;

public class Ocene {

    private Text ocena;
    private String vrsta;
    private String sklop;
    private String opis;

    private Integer id;






    public Ocene(Integer id,Text ocena,String vrsta, String sklop, String opis)
    {
        this.id = id;
        this.ocena = ocena;
        this.vrsta = vrsta;
        this.sklop = sklop;
        this.opis = opis;

    }


    public Integer getId() {return id;}
    public Text getOcena() {
        return ocena;
    }

    public String getVrsta() {
        return vrsta;
    }

    public String getSklop() {
        return sklop;
    }

    public String getOpis() {
        return opis;
    }


}
