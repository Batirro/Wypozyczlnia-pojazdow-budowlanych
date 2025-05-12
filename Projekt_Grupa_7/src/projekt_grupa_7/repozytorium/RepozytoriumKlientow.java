package projekt_grupa_7.repozytorium;

import projekt_grupa_7.model.Klient;
import java.util.List;

/**
 * Interfejs definiujący kontrakt dla operacji dostępu do danych dla obiektów Klient.
 * Abstrakcjonuje sposób przechowywania danych klientów.
 */

public interface RepozytoriumKlientow {
    /**
     * Zapisuje nowy obiekt klienta lub aktualizuje istniejący w repozytorium.
     * Sposób rozróżnienia między nowym a istniejącym klientem (na podstawie NIP)
     * oraz nadawanie ID nowym klientom.
     *
     * @param klient Obiekt {@link Klient} do zapisania lub zaktualizowania.
     *               Nie powinien być null.
     */
    void zapisz(Klient klient);
        /**
     * Wyszukuje klienta w repozytorium na podstawie jego unikalnego identyfikatora.
     *
     * @param id Unikalny identyfikator klienta.
     * @return Znaleziony obiekt {@link Klient} lub {@code null}, jeśli klient o podanym ID nie istnieje.
     */
    Klient znajdzWgId(int id);
     /**
     * Wyszukuje klienta w repozytorium na podstawie jego numeru NIP.
     * Zakłada się, że NIP może być unikalnym identyfikatorem firmy.
     *
     * @param nip Numer NIP klienta do wyszukania. Nie powinien być null ani pusty.
     * @return Znaleziony obiekt {@link Klient} lub {@code null}, jeśli klient o podanym NIP nie istnieje.
     */
    Klient znajdzWgNIP(String nip);
    /**
     * Zwraca listę wszystkich obiektów klientów znajdujących się w repozytorium.
     *
     * @return Lista obiektów {@link Klient}. Może być pusta, jeśli repozytorium nie zawiera żadnych klientów.
     *         Zwracana lista powinna być bezpieczna do modyfikacji (np. kopia wewnętrznej kolekcji).
     */
    List<Klient> znajdzWszystkie();
     /**
     * Usuwa klienta z repozytorium na podstawie jego unikalnego identyfikatora.
     *
     * @param id Identyfikator klienta, który ma zostać usunięty.
     */
    void usun(int id);
}