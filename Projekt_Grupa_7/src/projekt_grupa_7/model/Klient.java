package projekt_grupa_7.model;

/**
 * Reprezentuje klienta wypożyczalni (zazwyczaj firmę w tej branży).
 * Przechowuje dane identyfikacyjne firmy oraz dane osoby do kontaktu.
 */

public class Klient {
private int idKlienta;
    private String nazwaFirmy;
    private String nip;
    private String imieKontakt;
    private String nazwiskoKontakt;
    private String telefonKontakt;
    private String emailKontakt;
    
    
    // Historia wypożyczeń nie jest przechowywana bezpośrednio w obiekcie Klient,
    // jest dostępna poprzez zapytania do RepozytoriumWypozyczen.
    
     /**
     * Konstruktor tworzący nowy obiekt Klient.
     * @param idKlienta Unikalne ID klienta.
     * @param nazwaFirmy Nazwa firmy.
     * @param nip Numer Identyfikacji Podatkowej.
     * @param imieKontakt Imię osoby do kontaktu.
     * @param nazwiskoKontakt Nazwisko osoby do kontaktu.
     * @param telefonKontakt Numer telefonu kontaktowego.
     * @param emailKontakt Adres email kontaktowy.
     */
    
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

    // Settery (potrzebne do edycji)
    /**
     * Ustawia ID klienta. Może być używane przez repozytorium przy nadawaniu ID.
     * @param idKlienta Nowe ID klienta.
     */
    public void setIdKlienta(int idKlienta) { this.idKlienta = idKlienta;}
    public void setNazwaFirmy(String nazwaFirmy) { this.nazwaFirmy = nazwaFirmy; }
    public void setNip(String nip) { this.nip = nip; }
    public void setImieKontakt(String imieKontakt) { this.imieKontakt = imieKontakt; }
    public void setNazwiskoKontakt(String nazwiskoKontakt) { this.nazwiskoKontakt = nazwiskoKontakt; }
    public void setTelefonKontakt(String telefonKontakt) { this.telefonKontakt = telefonKontakt; }
    public void setEmailKontakt(String emailKontakt) { this.emailKontakt = emailKontakt; }


    
    /**
     * Generuje czytelną, wieloliniową reprezentację tekstową obiektu Klient.
     * Przydatne do wyświetlania informacji w GUI lub logach.
     * Zawiera ID, nazwę firmy, NIP oraz pełne dane kontaktowe.
     *
     * @return Sformatowany string z danymi klienta.
     */
    
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
            nazwaFirmy != null ? nazwaFirmy : "-", // Zabezpieczenie przed null
            nip != null ? nip : "-",
            imieKontakt != null ? imieKontakt : "",
            nazwiskoKontakt != null ? nazwiskoKontakt : "", // Puste stringi dla imienia/nazwiska jeśli null
            telefonKontakt != null ? telefonKontakt : "-",
            emailKontakt != null ? emailKontakt : "-"
        );
    }
}