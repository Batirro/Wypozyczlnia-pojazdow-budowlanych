package projekt_grupa_7.model;

public class Koparka extends Pojazd {
    private double pojemnoscLyzeczki_m3;
    private double zasiegRamienia_m;
    private double glebokoscKopania_m;

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
            super.formatujWspolneSzczegoly(),
            pojemnoscLyzeczki_m3,
            zasiegRamienia_m,
            glebokoscKopania_m
        );
    }
}