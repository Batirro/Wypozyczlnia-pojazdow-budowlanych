package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.model.Wypozyczenie;
import projekt_grupa_7.repozytorium.RepozytoriumWypozyczen;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryWypozyczenieRepository implements RepozytoriumWypozyczen {
    private final List<Wypozyczenie> wypozyczenia = new ArrayList<>();
    // ID dla wypożyczeń jest generowane w SerwisWypozyczen (nextWypozyczenieId)
    // więc tutaj nie potrzebujemy oddzielnego licznika ID.

    @Override
    public void zapisz(Wypozyczenie wypozyczenie) {
        // Aktualizacja lub dodanie nowego
        Optional<Wypozyczenie> istniejaceWypozyczenieOpt = wypozyczenia.stream()
                .filter(w -> w.getIdWypozyczenia() == wypozyczenie.getIdWypozyczenia())
                .findFirst();

        if (istniejaceWypozyczenieOpt.isPresent()) {
            // W przypadku wypożyczenia, zazwyczaj chcemy nadpisać istniejące danymi z nowego
            // ponieważ obiekt Wypozyczenie mógł zostać zmodyfikowany (np. data zwrotu, koszt)
            wypozyczenia.remove(istniejaceWypozyczenieOpt.get());
            wypozyczenia.add(wypozyczenie);
        } else {
            wypozyczenia.add(wypozyczenie);
        }
    }

    @Override
    public Wypozyczenie znajdzWgId(int id) {
        return wypozyczenia.stream()
                .filter(w -> w.getIdWypozyczenia() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Wypozyczenie> znajdzWgKlienta(Klient klient) {
        if (klient == null) {
            return new ArrayList<>();
        }
        return wypozyczenia.stream()
                .filter(w -> w.getKlient().getIdKlienta() == klient.getIdKlienta())
                .collect(Collectors.toList());
    }

    @Override
    public List<Wypozyczenie> znajdzWgPojazdu(Pojazd pojazd) {
        if (pojazd == null) {
            return new ArrayList<>();
        }
        return wypozyczenia.stream()
                .filter(w -> w.getPojazd().getId().equals(pojazd.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Wypozyczenie> znajdzAktywne() {
        return wypozyczenia.stream()
                .filter(w -> !w.isCzyZakonczone())
                .collect(Collectors.toList());
    }

    @Override
    public List<Wypozyczenie> znajdzWszystkie() {
        return new ArrayList<>(wypozyczenia); // Zwracamy kopię
    }

    @Override
    public void usun(int id) {
        wypozyczenia.removeIf(w -> w.getIdWypozyczenia() == id);
    }
}