package projekt_grupa_7.model;

/**
 * Reprezentuje konkretny typ pojazdu - Koparkę.
 * Dziedziczy po klasie {@link Pojazd} i dodaje specyficzne atrybuty
 * takie jak pojemność łyżki, zasięg ramienia i głębokość kopania.
 */

public class Koparka extends Pojazd {
    private double pojemnoscLyzeczki_m3;
    private double zasiegRamienia_m;
    private double glebokoscKopania_m;

    /**
     * Konstruktor tworzący nową instancję Koparki.
     * Wywołuje konstruktor klasy bazowej {@link Pojazd} i inicjalizuje specyficzne pola koparki.
     *
     * @param id                   Unikalny identyfikator pojazdu (np. numer rejestracyjny).
     * @param marka                Marka koparki.
     * @param model                Model koparki.
     * @param rokProdukcji         Rok produkcji.
     * @param stawkaDobowa         Stawka dobowa za wypożyczenie.
     * @param pojemnoscLyzeczki_m3 Pojemność łyżki w m3.
     * @param zasiegRamienia_m     Zasięg ramienia w metrach.
     * @param glebokoscKopania_m   Głębokość kopania w metrach.
     */
    public Koparka(String id, String marka, String model, int rokProdukcji, double stawkaDobowa,
                   double pojemnoscLyzeczki_m3, double zasiegRamienia_m, double glebokoscKopania_m) {
        super(id, marka, model, rokProdukcji, stawkaDobowa);
        this.pojemnoscLyzeczki_m3 = pojemnoscLyzeczki_m3;
        this.zasiegRamienia_m = zasiegRamienia_m;
        this.glebokoscKopania_m = glebokoscKopania_m;
    }

    // Gettery dla specyficznych pól
    public double getPojemnoscLyzeczki_m3() { return pojemnoscLyzeczki_m3; }
    public double getZasiegRamienia_m() { return zasiegRamienia_m; }
    public double getGlebokoscKopania_m() { return glebokoscKopania_m; }

    @Override
    public String wyswietlSzczegoly() {
        return String.format(
            "Typ Pojazdu: KOPARKA\n" + // Wyraźne określenie typu
            "%s\n" +                     // Wspólne szczegóły
            "\tPojemność łyżki: %.2f m3\n" +
            "\tZasięg ramienia: %.1f m\n" +
            "\tGłębokość kopania: %.1f m",
            super.formatujWspolneSzczegoly(), // Pobranie sformatowanych wspólnych danych
            pojemnoscLyzeczki_m3,
            zasiegRamienia_m,
            glebokoscKopania_m
        );
    }
}