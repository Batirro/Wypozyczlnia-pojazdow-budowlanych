package projekt_grupa_7.model;

/**
 * Abstrakcyjna klasa bazowa reprezentująca ogólny pojazd w wypożyczalni.
 * Definiuje wspólne cechy i zachowania dla wszystkich typów pojazdów.
 * Klasy dziedziczące muszą zaimplementować metodę {@link #wyswietlSzczegoly()}.
 */

public abstract class Pojazd {
    protected String id; // Konkretnie numer rejstracyjny
    protected String marka;
    protected String model;
    protected int rokProdukcji;
    protected double stawkaDobowa;
    protected boolean czyDostepny;
    
    /**
     * Konstruktor inicjalizujący wspólne pola dla wszystkich pojazdów.
     * Ustawia pojazd jako domyślnie dostępny.
     *
     * @param id           Unikalny identyfikator pojazdu.
     * @param marka        Marka pojazdu.
     * @param model        Model pojazdu.
     * @param rokProdukcji Rok produkcji pojazdu.
     * @param stawkaDobowa Stawka dobowa za wypożyczenie.
     */
    
    public Pojazd(String id, String marka, String model, int rokProdukcji, double stawkaDobowa) {
        this.id = id;
        this.marka = marka;
        this.model = model;
        this.rokProdukcji = rokProdukcji;
        this.stawkaDobowa = stawkaDobowa;
        this.czyDostepny = true;
    }

    // Gettery
    public String getId() { return id; }
    public String getMarka() { return marka; }
    public String getModel() { return model; }
    public int getRokProdukcji() { return rokProdukcji; }
    public double getStawkaDobowa() { return stawkaDobowa; }
    public boolean isCzyDostepny() { return czyDostepny; }

    // Metody
    public void zmienDostepnosc(boolean dostepny) {
        this.czyDostepny = dostepny;
    }

    /**
     * Formatuje wspólne szczegóły dla wszystkich typów pojazdów w czytelny sposób.
     * Używane przez metody {@code wyswietlSzczegoly()} w podklasach.
     * Zawiera ID, markę, model, rok, stawkę i dostępność, poprzedzone tabulatorem.
     * @return Sformatowany string z wspólnymi danymi, gotowy do włączenia do pełnego opisu.
     */
    protected String formatujWspolneSzczegoly() {
        return String.format(
            "\tID (Rejestracja): %s\n" +
            "\tMarka: %s\n" +
            "\tModel: %s\n" +
            "\tRok Produkcji: %d\n" +
            "\tStawka Dobowa: %.2f PLN\n" +
            "\tDostępny: %s",
            id != null ? id : "-",
            marka != null ? marka : "-",
            model != null ? model : "-",
            rokProdukcji, // int, więc nie będzie null, ale można by dać warunek jeśli 0 jest niepoprawne
            stawkaDobowa,
            (czyDostepny ? "Tak" : "Nie")
        );
    }

    /**
     * Abstrakcyjna metoda, którą muszą zaimplementować konkretne klasy pojazdów (Koparka, Dzwig, Wywrotka).
     * Powinna zwracać pełny, sformatowany opis pojazdu, zawierający zarówno wspólne,
     * jak i specyficzne atrybuty danego typu. Zalecane użycie {@link #formatujWspolneSzczegoly()}.
     * @return Sformatowany, wieloliniowy string ze wszystkimi szczegółami pojazdu.
     */
    
    public abstract String wyswietlSzczegoly();

    
     /**
     * Zwraca podstawową, jedno-liniową reprezentację tekstową obiektu Pojazd.
     * Przydatne do szybkiej identyfikacji lub logowania.
     * @return String w formacie "Marka Model (ID: id)".
     */
    
    @Override
    public String toString() {
        return String.format("%s %s (ID: %s)", marka, model, id); // Prosty identyfikator
    }
}
