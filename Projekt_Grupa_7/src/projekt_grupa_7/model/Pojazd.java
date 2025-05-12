package projekt_grupa_7.model;

public abstract class Pojazd {
protected String id; // Np. numer rejestracyjny
    protected String marka;
    protected String model;
    protected int rokProdukcji;
    protected double stawkaDobowa;
    protected boolean czyDostepny;

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
     * Formatuje wspólne szczegóły dla wszystkich typów pojazdów.
     * @return Sformatowany string z wspólnymi danymi.
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
     * Metoda abstrakcyjna, którą muszą zaimplementować podklasy,
     * aby wyświetlić pełne, sformatowane szczegóły pojazdu.
     * @return Sformatowany string ze wszystkimi szczegółami pojazdu.
     */
    public abstract String wyswietlSzczegoly();

    // Opcjonalnie: Możesz też zmodyfikować domyślną metodę toString(),
    // jeśli jest gdzieś używana do prostego logowania.
    // Ale dla GUI będziemy polegać na wyswietlSzczegoly().
    @Override
    public String toString() {
        return String.format("%s %s (ID: %s)", marka, model, id); // Prosty identyfikator
    }
}
