package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.repozytorium.RepozytoriumPojazdow;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacja interfejsu {@link RepozytoriumPojazdow} przechowująca dane w pamięci operacyjnej (RAM).
 * Dane są przechowywane w synchronizowanej liście obiektów {@link Pojazd}.
 * Ta implementacja jest prosta i szybka, idealna do celów demonstracyjnych, prototypowania
 * lub testów jednostkowych/integracyjnych, gdzie nie jest wymagana trwałość danych.
 * Wszystkie dane zostaną utracone po zakończeniu działania aplikacji.
 */

public class InMemoryPojazdRepository implements RepozytoriumPojazdow {
    /**
     * Lista przechowująca obiekty pojazdów w pamięci.
     * Użycie {@code Collections.synchronizedList} zapewnia podstawowe bezpieczeństwo wątkowe
     * dla operacji na liście, chociaż poszczególne operacje złożone (np. removeIf + add)
     * nie są atomowe bez dodatkowej synchronizacji.
     */
    private final List<Pojazd> pojazdy = new ArrayList<>();
    /**
     * {@inheritDoc}
     * W tej implementacji, zapis działa jako "dodaj lub zastąp". Jeśli pojazd o danym ID
     * już istnieje na liście, jest on najpierw usuwany, a następnie nowy obiekt jest dodawany.
     * Gwarantuje to unikalność ID i obsługę aktualizacji. Operacja nie jest atomowa.
     *
     * @param pojazd Obiekt {@link Pojazd} do zapisania. Jeśli null, metoda nic nie robi.
     */
    @Override
    public void zapisz(Pojazd pojazd) {
        // Prosta implementacja: jeśli istnieje, usuń i dodaj (aktualizacja)
        pojazdy.removeIf(p -> p.getId().equals(pojazd.getId()));
        pojazdy.add(pojazd);
    }
    /**
     * {@inheritDoc}
     * Wyszukuje pojazd po ID, iterując po liście w pamięci.
     * Porównuje ID za pomocą metody {@code equals}.
     *
     * @param id Identyfikator pojazdu. Może być null.
     * @return Znaleziony pojazd lub {@code null}, jeśli ID jest null lub pojazd nie został znaleziony.
     */
    @Override
    public Pojazd znajdzWgId(String id) {
        return pojazdy.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * {@inheritDoc}
     * Zwraca nową listę (kopię) zawierającą wszystkie pojazdy aktualnie przechowywane w pamięci.
     * Kopia jest tworzona w celu ochrony wewnętrznej struktury danych repozytorium przed
     * modyfikacjami z zewnątrz. Operacja jest bezpieczna wątkowo.
     *
     * @return Kopia listy wszystkich obiektów {@link Pojazd}.
     */
    @Override
    public List<Pojazd> znajdzWszystkie() {
        return new ArrayList<>(pojazdy); // Zwracamy kopię
    }
    /**
     * {@inheritDoc}
     * Filtruje listę pojazdów w pamięci, zwracając tylko te, dla których metoda
     * {@link Pojazd#isCzyDostepny()} zwraca {@code true}.
     * Operacja jest bezpieczna wątkowo.
     *
     * @return Lista dostępnych pojazdów.
     */
    @Override
    public List<Pojazd> znajdzDostepne() {
        return pojazdy.stream()
                .filter(Pojazd::isCzyDostepny)
                .collect(Collectors.toList());
    }
    /**
     * {@inheritDoc}
     * Usuwa pojazd z listy w pamięci na podstawie podanego ID.
     * Jeśli ID jest null lub pojazd o danym ID nie istnieje, metoda nic nie robi.
     * Operacja jest bezpieczna wątkowo.
     *
     * @param id Identyfikator pojazdu do usunięcia.
     */
    @Override
    public void usun(String id) {
        pojazdy.removeIf(p -> p.getId().equals(id));
    }
}