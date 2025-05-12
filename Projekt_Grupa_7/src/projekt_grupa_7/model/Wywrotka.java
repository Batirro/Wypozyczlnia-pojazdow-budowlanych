package projekt_grupa_7.model;

/**
 * Reprezentuje konkretny typ pojazdu - Wywrotkę.
 * Dziedziczy po klasie {@link Pojazd} i dodaje specyficzne atrybuty
 * takie jak ładowność, typ napędu i pojemność skrzyni ładunkowej.
 */

public class Wywrotka extends Pojazd {
    private double ladownosc_tony;
    private String typNapedu;
    private double pojemnoscSkrzyni_m3;
    
    /**
     * Konstruktor tworzący nową instancję Wywrotki.
     * Wywołuje konstruktor klasy bazowej {@link Pojazd} i inicjalizuje specyficzne pola wywrotki.
     *
     * @param id                   Unikalny identyfikator pojazdu (np. numer rejestracyjny).
     * @param marka                Marka wywrotki.
     * @param model                Model wywrotki.
     * @param rokProdukcji         Rok produkcji.
     * @param stawkaDobowa         Stawka dobowa za wypożyczenie.
     * @param ladownosc_tony       Ładowność w tonach.
     * @param typNapedu            Typ napędu (np. "6x4").
     * @param pojemnoscSkrzyni_m3 Pojemność skrzyni ładunkowej w m3.
     */
    
    public Wywrotka(String id, String marka, String model, int rokProdukcji, double stawkaDobowa,
                    double ladownosc_tony, String typNapedu, double pojemnoscSkrzyni_m3) {
        super(id, marka, model, rokProdukcji, stawkaDobowa);
        this.ladownosc_tony = ladownosc_tony;
        this.typNapedu = typNapedu;
        this.pojemnoscSkrzyni_m3 = pojemnoscSkrzyni_m3;
    }

    // Gettery dla specyficznych pól
    public double getLadownosc_tony() { return ladownosc_tony; }
    public String getTypNapedu() { return typNapedu; }
    public double getPojemnoscSkrzyni_m3() { return pojemnoscSkrzyni_m3; }

    /**
     * Generuje szczegółowy, sformatowany opis wywrotki.
     * Wykorzystuje metodę {@link #formatujWspolneSzczegoly()} z klasy bazowej
     * i dodaje informacje specyficzne dla wywrotki.
     *
     * @return Wieloliniowy string z pełnym opisem wywrotki.
     */
    
    @Override
    public String wyswietlSzczegoly() {
        return String.format(
            "Typ Pojazdu: WYWROTKA\n" +
            "%s\n" +
            "\tŁadowność: %.1f t\n" +
            "\tTyp napędu: %s\n" +
            "\tPojemność skrzyni: %.1f m3",
            super.formatujWspolneSzczegoly(), // Pobranie sformatowanych wspólnych danych
            ladownosc_tony,
            typNapedu != null ? typNapedu : "-", // Zabezpieczenie przed null
            pojemnoscSkrzyni_m3
        );
    }
}