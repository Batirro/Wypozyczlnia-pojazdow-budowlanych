package projekt_grupa_7.model;

/**
 * Reprezentuje konkretny typ pojazdu - Dźwig.
 * Dziedziczy po klasie {@link Pojazd} i dodaje specyficzne atrybuty
 * takie jak maksymalny udźwig, długość wysięgnika, maksymalna wysokość podnoszenia i typ dźwigu.
 */

public class Dzwig extends Pojazd {
    private double maksymalnyUdzwig_tony;
    private double dlugoscWysiegnika_m;
    private double maksymalnaWysokoscPodnoszenia_m;
    private String typDzwigu; // Przykładowe typy: samojezdny, wieżowy, HDS, terenowy AT/RT

     /**
     * Konstruktor tworzący nową instancję Dźwigu.
     * Wywołuje konstruktor klasy bazowej {@link Pojazd} i inicjalizuje specyficzne pola dźwigu.
     *
     * @param id                             Unikalny identyfikator pojazdu (np. numer rejestracyjny).
     * @param marka                          Marka dźwigu.
     * @param model                          Model dźwigu.
     * @param rokProdukcji                   Rok produkcji.
     * @param stawkaDobowa                   Stawka dobowa za wypożyczenie.
     * @param maksymalnyUdzwig_tony          Maksymalny udźwig w tonach.
     * @param dlugoscWysiegnika_m            Długość wysięgnika w metrach.
     * @param maksymalnaWysokoscPodnoszenia_m Maksymalna wysokość podnoszenia w metrach.
     * @param typDzwigu                      Typ dźwigu (np. "Samojezdny", "HDS").
     */
    
    public Dzwig(String id, String marka, String model, int rokProdukcji, double stawkaDobowa,
                 double maksymalnyUdzwig_tony, double dlugoscWysiegnika_m,
                 double maksymalnaWysokoscPodnoszenia_m, String typDzwigu) {
        super(id, marka, model, rokProdukcji, stawkaDobowa);
        this.maksymalnyUdzwig_tony = maksymalnyUdzwig_tony;
        this.dlugoscWysiegnika_m = dlugoscWysiegnika_m;
        this.maksymalnaWysokoscPodnoszenia_m = maksymalnaWysokoscPodnoszenia_m;
        this.typDzwigu = typDzwigu;
    }

    // Gettery dla specyficznych pól
    public double getMaksymalnyUdzwig_tony() { return maksymalnyUdzwig_tony; }
    public double getDlugoscWysiegnika_m() { return dlugoscWysiegnika_m; }
    public double getMaksymalnaWysokoscPodnoszenia_m() { return maksymalnaWysokoscPodnoszenia_m; }
    public String getTypDzwigu() { return typDzwigu; }

    /**
     * Generuje szczegółowy, sformatowany opis dźwigu.
     * Wykorzystuje metodę {@link #formatujWspolneSzczegoly()} z klasy bazowej
     * i dodaje informacje specyficzne dla dźwigu.
     *
     * @return Wieloliniowy string z pełnym opisem dźwigu.
     */
    
    @Override
    public String wyswietlSzczegoly() {
        return String.format(
            "Typ Pojazdu: DŹWIG\n" +
            "%s\n" +
            "\tRodzaj dźwigu: %s\n" + // Zmieniona etykieta dla jasności
            "\tMax udźwig: %.1f t\n" +
            "\tDługość wysięgnika: %.1f m\n" +
            "\tMax wys. podnoszenia: %.1f m",
            super.formatujWspolneSzczegoly(), // Pobranie sformatowanych wspólnych danych
            typDzwigu != null ? typDzwigu : "-", // Zabezpieczenie przed null
            maksymalnyUdzwig_tony,
            dlugoscWysiegnika_m,
            maksymalnaWysokoscPodnoszenia_m
        );
    }
}