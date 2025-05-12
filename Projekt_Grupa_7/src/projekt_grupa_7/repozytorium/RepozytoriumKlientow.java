package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Klient;
import java.util.List;

public interface RepozytoriumKlientow {
    void zapisz(Klient klient);
    Klient znajdzWgId(int id);
    Klient znajdzWgNIP(String nip);
    List<Klient> znajdzWszystkie();
    void usun(int id);
}