package projekt_grupa_7.model;

public class Wywrotka extends Pojazd {
    private double ladownosc_tony;
    private String typNapedu;
    private double pojemnoscSkrzyni_m3;

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

    @Override
    public String wyswietlSzczegoly() {
        return String.format(
            "Typ Pojazdu: WYWROTKA\n" +
            "%s\n" +
            "\tŁadowność: %.1f t\n" +
            "\tTyp napędu: %s\n" +
            "\tPojemność skrzyni: %.1f m3",
            super.formatujWspolneSzczegoly(),
            ladownosc_tony,
            typNapedu != null ? typNapedu : "-",
            pojemnoscSkrzyni_m3
        );
    }
}