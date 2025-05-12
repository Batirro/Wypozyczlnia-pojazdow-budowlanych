package projekt_grupa_7.gui;

import projekt_grupa_7.model.Klient;
 
public class KlientWrapper {
    private Klient klient;

    public KlientWrapper(Klient klient) {
        this.klient = klient;
    }

    public Klient getKlient() {
        return klient;
    }

    @Override
    public String toString() {
        // To będzie wyświetlane w JComboBox
        return klient != null ? klient.getNazwaFirmy() + " (ID: " + klient.getIdKlienta() + ")" : "Wybierz klienta";
    }

    // Ważne dla poprawnego działania JComboBox, jeśli będziesz porównywać obiekty
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

    @Override
    public int hashCode() {
        return klient != null ? Integer.hashCode(klient.getIdKlienta()) : 0;
    }
}
