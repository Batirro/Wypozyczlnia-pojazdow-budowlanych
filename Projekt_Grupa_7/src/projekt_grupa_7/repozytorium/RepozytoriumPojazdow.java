package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Pojazd;
import java.util.List;

public interface RepozytoriumPojazdow {
    void zapisz(Pojazd pojazd);
    Pojazd znajdzWgId(String id);
    List<Pojazd> znajdzWszystkie();
    List<Pojazd> znajdzDostepne();
    void usun(String id);
}