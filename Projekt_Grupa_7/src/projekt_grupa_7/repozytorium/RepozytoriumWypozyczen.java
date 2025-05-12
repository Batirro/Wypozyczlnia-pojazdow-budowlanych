package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.model.Wypozyczenie;
import java.util.List;

public interface RepozytoriumWypozyczen {
    void zapisz(Wypozyczenie wypozyczenie);
    Wypozyczenie znajdzWgId(int id);
    List<Wypozyczenie> znajdzWgKlienta(Klient klient);
    List<Wypozyczenie> znajdzWgPojazdu(Pojazd pojazd);
    List<Wypozyczenie> znajdzAktywne(); // Wypożyczenia, które nie są zakończone
    List<Wypozyczenie> znajdzWszystkie();
    void usun(int id);
}