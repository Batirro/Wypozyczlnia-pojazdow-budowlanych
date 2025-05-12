package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Pojazd;
import java.util.List;

/**
 * Interfejs definiujący kontrakt dla repozytorium zarządzającego obiektami typu {@link Pojazd}.
 * Określa operacje CRUD (Create, Read, Update, Delete) oraz specyficzne metody wyszukiwania pojazdów.
 * Abstrakcjonuje sposób przechowywania danych pojazdów (np. baza danych, pamięć).
 */

public interface RepozytoriumPojazdow {
    /**
     * Zapisuje nowy pojazd lub aktualizuje istniejący w repozytorium.
     * Jeśli pojazd o danym ID już istnieje, jego dane powinny zostać zaktualizowane.
     * Jeśli nie istnieje, powinien zostać dodany jako nowy.
     *
     * @param pojazd Obiekt {@link Pojazd} do zapisania lub zaktualizowania. Nie powinien być null.
     */
    void zapisz(Pojazd pojazd);
     /**
     * Wyszukuje i zwraca pojazd na podstawie jego unikalnego identyfikatora.
     *
     * @param id Unikalny identyfikator pojazdu (numer rejestracyjny) do wyszukania.
     * @return Znaleziony obiekt {@link Pojazd} lub {@code null}, jeśli pojazd o podanym ID nie istnieje.
     */
    Pojazd znajdzWgId(String id);
    /**
     * Zwraca listę wszystkich pojazdów znajdujących się w repozytorium.
     * @return Lista obiektów Pojazd. Może być pusta, jeśli brak pojazdów.
     */
    List<Pojazd> znajdzWszystkie();
    /**
     * Zwraca listę pojazdów, które są aktualnie dostępne do wypożyczenia.
     * (Uproszczona wersja - sprawdza flagę dostępności).
     * @return Lista dostępnych obiektów Pojazd.
     */
    List<Pojazd> znajdzDostepne();
    /**
     * Usuwa pojazd z repozytorium na podstawie jego identyfikatora.
     * @param id Identyfikator pojazdu do usunięcia.
     */
    void usun(String id);
}