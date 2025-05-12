package projekt_grupa_7.model;

public class Klient {
private int idKlienta;
    private String nazwaFirmy;
    private String nip;
    private String imieKontakt;
    private String nazwiskoKontakt;
    private String telefonKontakt;
    private String emailKontakt;

    public Klient(int idKlienta, String nazwaFirmy, String nip, String imieKontakt, String nazwiskoKontakt, String telefonKontakt, String emailKontakt) {
        this.idKlienta = idKlienta;
        this.nazwaFirmy = nazwaFirmy;
        this.nip = nip;
        this.imieKontakt = imieKontakt;
        this.nazwiskoKontakt = nazwiskoKontakt;
        this.telefonKontakt = telefonKontakt;
        this.emailKontakt = emailKontakt;
    }

    // Gettery
    public int getIdKlienta() { return idKlienta; }
    public String getNazwaFirmy() { return nazwaFirmy; }
    public String getNip() { return nip; }
    public String getImieKontakt() { return imieKontakt; }
    public String getNazwiskoKontakt() { return nazwiskoKontakt; }
    public String getTelefonKontakt() { return telefonKontakt; }
    public String getEmailKontakt() { return emailKontakt; }

    // Settery (jeśli potrzebne do edycji)
    public void setIdKlienta(int idKlienta) { this.idKlienta = idKlienta;} // Dodane na wypadek, gdyby repozytorium nadawało ID
    public void setNazwaFirmy(String nazwaFirmy) { this.nazwaFirmy = nazwaFirmy; }
    public void setNip(String nip) { this.nip = nip; }
    public void setImieKontakt(String imieKontakt) { this.imieKontakt = imieKontakt; }
    public void setNazwiskoKontakt(String nazwiskoKontakt) { this.nazwiskoKontakt = nazwiskoKontakt; }
    public void setTelefonKontakt(String telefonKontakt) { this.telefonKontakt = telefonKontakt; }
    public void setEmailKontakt(String emailKontakt) { this.emailKontakt = emailKontakt; }


    @Override
    public String toString() {
        return String.format(
            "Klient ID: %d\n" +
            "\tNazwa Firmy: %s\n" +
            "\tNIP: %s\n" +
            "\tOsoba kontaktowa: %s %s\n" +
            "\tTelefon: %s\n" +
            "\tEmail: %s",
            idKlienta,
            nazwaFirmy != null ? nazwaFirmy : "-",
            nip != null ? nip : "-",
            imieKontakt != null ? imieKontakt : "",
            nazwiskoKontakt != null ? nazwiskoKontakt : "",
            telefonKontakt != null ? telefonKontakt : "-",
            emailKontakt != null ? emailKontakt : "-"
        );
    }
}