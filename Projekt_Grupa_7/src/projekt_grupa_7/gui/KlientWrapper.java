package projekt_grupa_7.gui;

import projekt_grupa_7.model.Klient;

/**
 * Klasa pomocnicza (Wrapper) do przechowywania obiektu Klient w komponencie JComboBox.
 * Umożliwia wyświetlanie czytelnej nazwy klienta (lub placeholdera) w liście rozwijanej,
 * jednocześnie przechowując pełny obiekt Klient (lub null) dla łatwego dostępu do ID
 * lub innych danych po wyborze elementu przez użytkownika.
 */

public class KlientWrapper {
    private Klient klient;
     /**
     * Konstruktor tworzący wrapper dla danego klienta.
     * @param klient Obiekt Klient do opakowania, lub null dla stworzenia placeholdera.
     */

    public KlientWrapper(Klient klient) {
        this.klient = klient;
    }
    /**
     * Zwraca przechowywany obiekt Klient.
     * @return Obiekt Klient lub null, jeśli ten wrapper jest placeholderem.
     */
    public Klient getKlient() {
        return klient;
    }
    /**
     * Zwraca tekstową reprezentację obiektu, która będzie wyświetlana w JComboBox.
     * Jeśli klient nie jest null, pokazuje nazwę firmy i ID.
     * Jeśli klient jest null, pokazuje tekst zastępczy (placeholder).
     * @return String do wyświetlenia w JComboBox.
     */
    @Override
    public String toString() {
        // To będzie wyświetlane w JComboBox
        return klient != null ? klient.getNazwaFirmy() + " (ID: " + klient.getIdKlienta() + ")" : "Wybierz klienta";
    }
    /**
     * Porównuje ten KlientWrapper z innym obiektem.
     * Dwa KlientWrapper są równe, jeśli:
     * - Oba są tym samym obiektem.
     * - Oba reprezentują placeholder (klient == null).
     * - Oba reprezentują rzeczywistych klientów o tym samym ID.
     * Metoda ta jest kluczowa dla poprawnego działania JComboBox (np. przy ustawianiu wybranego elementu).
     * @param obj Obiekt do porównania.
     * @return true, jeśli obiekty są równe, false w przeciwnym razie.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KlientWrapper that = (KlientWrapper) obj;

        // Poprawiona logika porównania
        if (this.klient == null && that.klient == null) {
            return true; // Dwa placeholdery są sobie równe
        }
        if (this.klient == null || that.klient == null) {
            return false; // Jeden jest placeholderem, a drugi nie - nie są równe
        }
        // Oba this.klient i that.klient nie są null, więc możemy bezpiecznie porównać ich ID
        return this.klient.getIdKlienta() == that.klient.getIdKlienta();
    }
    /**
     * Zwraca kod hash dla obiektu KlientWrapper.
     * Zgodnie z kontraktem Java, jeśli equals() zwraca true dla dwóch obiektów,
     * ich hashCode() musi być taki sam.
     * Dla placeholdera (klient == null) zwracamy stałą wartość (np. 0).
     * Dla rzeczywistego klienta zwracamy hashCode jego ID.
     * @return Kod hash obiektu.
     */
    @Override
    public int hashCode() {
        return klient != null ? Integer.hashCode(klient.getIdKlienta()) : 0; // Stały hash dla null
    }
}
