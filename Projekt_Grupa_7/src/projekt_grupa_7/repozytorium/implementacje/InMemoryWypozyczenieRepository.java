package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.model.Wypozyczenie;
import projekt_grupa_7.repozytorium.RepozytoriumWypozyczen;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementacja interfejsu {@link RepozytoriumWypozyczen} przechowująca dane w pamięci operacyjnej.
 * Wypożyczenia są przechowywane w synchronizowanej liście.
 * ID dla wypożyczeń jest nadawane zewnętrznie (np. przez serwis) i przekazywane
 * do metody {@code zapisz}.
 * Dane nie są trwałe.
 */

public class InMemoryWypozyczenieRepository implements RepozytoriumWypozyczen {
    private final List<Wypozyczenie> wypozyczenia = new ArrayList<>();
    // ID dla wypożyczeń jest generowane w SerwisWypozyczen (nextWypozyczenieId)
    // więc tutaj nie potrzebujemy oddzielnego licznika ID.

    /**
     * {@inheritDoc}
     * Działa jako "dodaj lub zastąp". Jeśli wypożyczenie o danym ID już istnieje,
     * jest usuwane i zastępowane nowym obiektem (aktualizacja).
     * Wymaga, aby obiekt Wypozyczenie miał już ustawione poprawne ID.
     * Operacja nie jest w pełni atomowa bez dodatkowej zewnętrznej synchronizacji,
     * ale operacje na liście są chronione.
     *
     * @param wypozyczenie Obiekt {@link Wypozyczenie} do zapisania. Nie powinien być null.
     */    
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
    /**
     * {@inheritDoc}
     * Wyszukuje wypożyczenie po ID w liście w pamięci.
     */
    @Override
    public Wypozyczenie znajdzWgId(int id) {
        return wypozyczenia.stream()
                .filter(w -> w.getIdWypozyczenia() == id)
                .findFirst()
                .orElse(null);
    }
     /**
     * {@inheritDoc}
     * Filtruje listę wypożyczeń, zwracając te, które są powiązane z danym klientem (po ID klienta).
     * @param klient Obiekt Klient. Jeśli null, zwracana jest pusta lista.
     * @return Lista wypożyczeń dla klienta.
     */
    @Override
    public List<Wypozyczenie> znajdzWgKlienta(Klient klient) {
        if (klient == null) {
            return new ArrayList<>();
        }
        return wypozyczenia.stream()
                .filter(w -> w.getKlient().getIdKlienta() == klient.getIdKlienta())
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     * Filtruje listę wypożyczeń, zwracając te, które są powiązane z danym pojazdem (po ID pojazdu).
     * @param pojazd Obiekt Pojazd. Jeśli null lub jego ID jest null, zwracana jest pusta lista.
     * @return Lista wypożyczeń dla pojazdu.
     */
    @Override
    public List<Wypozyczenie> znajdzWgPojazdu(Pojazd pojazd) {
        if (pojazd == null) {
            return new ArrayList<>();
        }
        return wypozyczenia.stream()
                .filter(w -> w.getPojazd().getId().equals(pojazd.getId()))
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     * Zwraca listę wypożyczeń, które nie są oznaczone jako zakończone.
     */
    @Override
    public List<Wypozyczenie> znajdzAktywne() {
        return wypozyczenia.stream()
                .filter(w -> !w.isCzyZakonczone())
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     * Zwraca kopię listy wszystkich wypożyczeń.
     */
    @Override
    public List<Wypozyczenie> znajdzWszystkie() {
        return new ArrayList<>(wypozyczenia); // Zwracamy kopię
    }
     /**
     * {@inheritDoc}
     * Usuwa wypożyczenie z listy w pamięci na podstawie ID.
     */
    @Override
    public void usun(int id) {
        wypozyczenia.removeIf(w -> w.getIdWypozyczenia() == id);
    }
}