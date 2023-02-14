package sample;

public class DIjakPodatki {

    private static Integer id;
    private static String email;
    private static String tel_st;
    private static String naslov;
    private static String kratica;
    private static String kraj;



    public DIjakPodatki(Integer id,String email, String tel_st, String naslov, String kratica, String kraj) {
        this.id = id;
        this.email = email;
        this.tel_st = tel_st;
        this.naslov = naslov;
        this.kratica = kratica;
        this.kraj = kraj;
    }

    public static Integer getId() {return id;}
    public static String getEmail() {
        return email;
    }

    public static String getTel_st() {
        return tel_st;
    }

    public static String getNaslov() {
        return naslov;
    }

    public static String getKratica() {
        return kratica;
    }

    public static String getKraj() {
        return kraj;
    }
}
