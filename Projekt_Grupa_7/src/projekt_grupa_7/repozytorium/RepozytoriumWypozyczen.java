package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.model.Wypozyczenie;
import java.util.List;

/**
 * Interfejs definiujący kontrakt dla repozytorium zarządzającego obiektami Wypozyczenie.
 */

public interface RepozytoriumWypozyczen {
     /**
     * Zapisuje nowe lub aktualizuje istniejące wypożyczenie.
     * @param wypozyczenie Obiekt Wypozyczenie do zapisania.
     */
    void zapisz(Wypozyczenie wypozyczenie);
     /**
     * Wyszukuje wypożyczenie na podstawie jego ID.
     * @param id Identyfikator wypożyczenia.
     * @return Znaleziony obiekt Wypozyczenie lub null.
     */
    Wypozyczenie znajdzWgId(int id);
     /**
     * Wyszukuje wszystkie wypożyczenia powiązane z danym klientem.
     * @param klient Obiekt Klient.
     * @return Lista wypożyczeń dla danego klienta.
     */
    List<Wypozyczenie> znajdzWgKlienta(Klient klient);
    /**
     * Wyszukuje wszystkie wypożyczenia powiązane z danym pojazdem.
     * @param pojazd Obiekt Pojazd.
     * @return Lista wypożyczeń dla danego pojazdu.
     */
    List<Wypozyczenie> znajdzWgPojazdu(Pojazd pojazd);
     /**
     * Wyszukuje wszystkie aktywne (niezakończone) wypożyczenia.
     * @return Lista aktywnych wypożyczeń.
     */
    List<Wypozyczenie> znajdzAktywne();
    /**
     * Zwraca listę wszystkich wypożyczeń (aktywnych i zakończonych).
     * @return Lista wszystkich obiektów Wypozyczenie.
     */
    List<Wypozyczenie> znajdzWszystkie();
     /**
     * Usuwa wypożyczenie z repozytorium na podstawie jego ID.
     * @param id Identyfikator wypożyczenia do usunięcia.
     */
    void usun(int id);
}