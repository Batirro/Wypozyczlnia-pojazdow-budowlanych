package projekt_grupa_7.model;

public class Dzwig extends Pojazd {
    private double maksymalnyUdzwig_tony;
    private double dlugoscWysiegnika_m;
    private double maksymalnaWysokoscPodnoszenia_m;
    private String typDzwigu;

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

    @Override
    public String wyswietlSzczegoly() {
        return String.format(
            "Typ Pojazdu: DŹWIG\n" +
            "%s\n" +
            "\tRodzaj dźwigu: %s\n" + // Zmieniona etykieta dla jasności
            "\tMax udźwig: %.1f t\n" +
            "\tDługość wysięgnika: %.1f m\n" +
            "\tMax wys. podnoszenia: %.1f m",
            super.formatujWspolneSzczegoly(),
            typDzwigu != null ? typDzwigu : "-",
            maksymalnyUdzwig_tony,
            dlugoscWysiegnika_m,
            maksymalnaWysokoscPodnoszenia_m
        );
    }
}