package projekt_grupa_7.model;

/**
 * Reprezentuje pojedyncze wypożyczenie pojazdu przez klienta.
 * Łączy obiekt Klienta i Pojazdu oraz przechowuje informacje o datach,
 * kosztach i statusie wypożyczenia.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Wypozyczenie {
    private int idWypozyczenia;
    private Klient klient;
    private Pojazd pojazd;
    private Date dataRozpoczecia;
    private Date planowanaDataZakonczenia;
    private Date faktycznaDataZakonczenia;
    private double calkowityKoszt;
    private boolean czyZakonczone;
    
    /**
     * Konstruktor tworzący nową instancję Wypozyczenia.
     * Ustawia domyślnie status na nieukończone i koszt na 0.
     *
     * @param idWypozyczenia         Unikalny identyfikator wypożyczenia nadawany przez system/serwis.
     * @param klient                 Obiekt Klienta wypożyczającego pojazd.
     * @param pojazd                 Obiekt Pojazdu, który jest wypożyczany.
     * @param dataRozpoczecia        Data rozpoczęcia wypożyczenia.
     * @param planowanaDataZakonczenia Planowana data zakończenia wypożyczenia.
     */
    
    public Wypozyczenie(int idWypozyczenia, Klient klient, Pojazd pojazd, Date dataRozpoczecia, Date planowanaDataZakonczenia) {
        this.idWypozyczenia = idWypozyczenia;
        this.klient = klient;
        this.pojazd = pojazd;
        this.dataRozpoczecia = dataRozpoczecia;
        this.planowanaDataZakonczenia = planowanaDataZakonczenia;
        this.czyZakonczone = false;
        this.calkowityKoszt = 0; // Obliczymy później lub przy żądaniu
    }

    // Gettery
    public int getIdWypozyczenia() { return idWypozyczenia; }
    public Klient getKlient() { return klient; }
    public Pojazd getPojazd() { return pojazd; }
    public Date getDataRozpoczecia() { return dataRozpoczecia; }
    public Date getPlanowanaDataZakonczenia() { return planowanaDataZakonczenia; }
    /**
     * Zwraca faktyczną datę zakończenia wypożyczenia (zwrotu pojazdu).
     * Może zwrócić null, jeśli wypożyczenie jest nadal aktywne.
     * @return Faktyczna data zakończenia lub null.
     */
    public Date getFaktycznaDataZakonczenia() { return faktycznaDataZakonczenia; }
    public double getCalkowityKoszt() {
        // Zawsze przeliczaj koszt, gdy jest pobierany, aby był aktualny, jeśli daty się zmieniły
        // lub wywołuj obliczKoszt() jawnie przed pobraniem. Dla uproszczenia zostawiam poprzednią logikę.
        // Jeśli koszt jest już ustawiony (np. przy zakończeniu), zwróć go.
        // W przeciwnym razie, jeśli to aktywne wypożyczenie, można by liczyć do "teraz" lub do planowanej daty.
        // Na razie polegamy na tym, że obliczKoszt() jest wywoływane w odpowiednich momentach.
        return calkowityKoszt;
    }
    public boolean isCzyZakonczone() { return czyZakonczone; }

    // Metody
    public void zakonczWypozyczenie(Date dataZakonczenia) {
        this.faktycznaDataZakonczenia = dataZakonczenia;
        this.czyZakonczone = true;
        obliczKoszt(); // Przelicz koszt przy zakończeniu
    }
    
    /**
     * Oblicza (lub przelicza) całkowity koszt wypożyczenia.
     * Bierze pod uwagę stawkę dobową pojazdu i liczbę dni między datą rozpoczęcia
     * a datą zakończenia (faktyczną, jeśli dostępna i wypożyczenie zakończone,
     * w przeciwnym razie planowaną).
     * Minimalny okres rozliczeniowy to 1 dzień. Wynik zapisuje w polu {@code calkowityKoszt}.
     *
     * @return Obliczony całkowity koszt. Zwraca 0, jeśli brakuje wymaganych danych (daty, pojazd).
     */
    
    public double obliczKoszt() {
        Date koniec;
        if (czyZakonczone && faktycznaDataZakonczenia != null) {
            koniec = faktycznaDataZakonczenia;
        } else if (planowanaDataZakonczenia != null) {
            koniec = planowanaDataZakonczenia;
        } else {
            // Jeśli nie ma daty zakończenia (ani faktycznej, ani planowanej), nie można obliczyć
            this.calkowityKoszt = 0;
            return 0;
        }

        if (dataRozpoczecia == null) {
            this.calkowityKoszt = 0;
            return 0;
        }

        long diffInMillies = Math.abs(koniec.getTime() - dataRozpoczecia.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // Jeśli wypożyczenie trwa krócej niż jeden dzień (np. kilka godzin tego samego dnia),
        // liczymy jako jeden dzień. Jeśli diffInDays jest 0, ale różnica w milisekundach jest >0.
        if (diffInDays == 0 && diffInMillies > 0) {
            diffInDays = 1;
        } else if (diffInMillies == 0 && dataRozpoczecia.equals(koniec)) { // Jeśli daty są identyczne, a czas nie jest brany pod uwagę
             diffInDays = 1; // Można założyć, że to minimum 1 dzień
        }
        // Jeśli jest to ten sam dzień, ale liczymy od początku dnia, to powyższe może być OK.
        // Dla większej precyzji przydałoby się uwzględnienie godzin.

        // Upewnij się, że pojazd nie jest null
        if (pojazd != null) {
            this.calkowityKoszt = (diffInDays > 0 ? diffInDays : 1) * pojazd.getStawkaDobowa();
        } else {
            this.calkowityKoszt = 0; // Nie można obliczyć kosztu bez pojazdu
        }
        return this.calkowityKoszt;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd-MM-yyyy"); // Prostszy format dla GUI toString

        String dataRozpStr = dataRozpoczecia != null ? sdfOutput.format(dataRozpoczecia) : "-";
        String dataPlanZakStr = planowanaDataZakonczenia != null ? sdfOutput.format(planowanaDataZakonczenia) : "-";
        String dataFaktZakStr = "-";
        if (isCzyZakonczone()) {
            dataFaktZakStr = faktycznaDataZakonczenia != null ? sdfOutput.format(faktycznaDataZakonczenia) : "Zakończone (brak daty)";
        }

        // Oblicz koszt przed wyświetleniem, aby mieć pewność, że jest aktualny
        // Chociaż lepiej polegać na tym, że koszt jest aktualizowany w odpowiednich momentach.
        // double aktualnyKosztDoWyswietlenia = obliczKoszt(); // Można, ale zmienia stan obiektu
        return String.format(
            "Wypożyczenie ID: %d\n" +
            "\tKlient: %s (ID: %d)\n" +
            "\tPojazd: %s %s (ID: %s)\n" +
            "\tData rozpoczęcia: %s\n" +
            "\tPlanowana data zakończenia: %s\n" +
            "\tFaktyczna data zakończenia: %s\n" +
            "\tCałkowity koszt: %.2f PLN\n" +
            "\tStatus: %s",
            idWypozyczenia,
            klient != null ? klient.getNazwaFirmy() : "Brak danych klienta",
            klient != null ? klient.getIdKlienta() : 0,
            pojazd != null ? pojazd.getMarka() : "Brak danych pojazdu",
            pojazd != null ? pojazd.getModel() : "",
            pojazd != null ? pojazd.getId() : "-",
            dataRozpStr,
            dataPlanZakStr,
            dataFaktZakStr,
            calkowityKoszt,
            isCzyZakonczone() ? "Zakończone" : "Aktywne"
        );
    }
}
