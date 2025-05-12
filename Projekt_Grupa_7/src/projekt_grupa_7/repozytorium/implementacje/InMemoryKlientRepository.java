package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.repozytorium.RepozytoriumKlientow;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacja interfejsu {@link RepozytoriumKlientow} przechowująca dane w pamięci operacyjnej.
 * Klienci są przechowywani w synchronizowanej liście.
 * Zawiera prosty, bezpieczny wątkowo mechanizm generowania kolejnych ID dla nowych klientów.
 * Dane nie są trwałe i zostaną utracone po zakończeniu działania aplikacji.
 */

public class InMemoryKlientRepository implements RepozytoriumKlientow {
    /** Lista przechowująca obiekty klientów w pamięci. */
    private final List<Klient> klienci = new ArrayList<>();
     /**
     * Bezpieczny wątkowo licznik do generowania unikalnych ID dla nowych klientów.
     * Zaczyna od 1.
     */ 
    private static int nextId = 1; 
    /**
     * Zwraca następne dostępne ID dla nowego klienta.
     * Ta metoda jest statyczna i bezpieczna wątkowo.
     * UWAGA: Używanie tej metody bezpośrednio z zewnątrz do ustawiania ID
     * przed zapisem może prowadzić do problemów w środowiskach wielowątkowych,
     * lepiej polegać na ID nadawanym przez metodę {@code zapisz}.
     * @return Następny unikalny identyfikator.
     */
    public static synchronized int getNextId() {
        return nextId;
    }
    /**
     * {@inheritDoc}
     * W tej implementacji:
     * - Jeśli {@code klient.getIdKlienta()} jest 0 lub wskazuje na nieistniejącego klienta,
     *   klient jest traktowany jako nowy: nadawane jest mu nowe ID z licznika {@code nextId}
     *   (jeśli ID było 0) i jest dodawany do listy. Wewnętrzny licznik `nextId` jest aktualizowany.
     * - Jeśli klient o podanym ID już istnieje, jego dane są aktualizowane na liście
     *   poprzez zastąpienie starego obiektu nowym.
     * Operacja jest synchronizowana.
     *
     * @param klient Obiekt {@link Klient} do zapisania. Nie powinien być null.
     */
    @Override
    public void zapisz(Klient klient) {

        Optional<Klient> istniejacyKlientOpt = klienci.stream()
                .filter(k -> k.getIdKlienta() == klient.getIdKlienta())
                .findFirst();

        if (istniejacyKlientOpt.isPresent()) {
            // Aktualizacja istniejącego klienta
            Klient istniejacyKlient = istniejacyKlientOpt.get();
            istniejacyKlient.setNazwaFirmy(klient.getNazwaFirmy());
        } 
        else {
            // Jeśli użyte ID jest >= niż następne planowane, ustaw następne planowane na użyte ID + 1
            if (klient.getIdKlienta() >= nextId) {
                 synchronized (InMemoryKlientRepository.class) {
                    nextId = klient.getIdKlienta() + 1;
                }
            }
            klienci.add(klient);
        }
    }

    /**
     * {@inheritDoc}
     * Wyszukuje klienta po ID w liście w pamięci.
     */
    @Override
    public Klient znajdzWgId(int id) {
        return klienci.stream()
                .filter(k -> k.getIdKlienta() == id)
                .findFirst()
                .orElse(null);
    }
     /**
     * {@inheritDoc}
     * Wyszukuje klienta po NIP w liście w pamięci. Porównuje NIP używając {@code equals}.
     */
    @Override
    public Klient znajdzWgNIP(String nip) {
        if (nip == null || nip.trim().isEmpty()) {
            return null;
        }
        return klienci.stream()
                .filter(k -> nip.equals(k.getNip()))
                .findFirst()
                .orElse(null);
    }
    /**
     * {@inheritDoc}
     * Zwraca kopię listy wszystkich klientów.
     */
    @Override
    public List<Klient> znajdzWszystkie() {
        return new ArrayList<>(klienci); // Zwracamy kopię, aby uniknąć modyfikacji oryginalnej listy z zewnątrz
    }

    /**
     * {@inheritDoc}
     * Usuwa klienta z listy w pamięci na podstawie ID.
     */
    @Override
    public void usun(int id) {
        klienci.removeIf(k -> k.getIdKlienta() == id);
    }
}