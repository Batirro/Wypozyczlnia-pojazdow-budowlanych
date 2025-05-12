package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.repozytorium.RepozytoriumKlientow;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryKlientRepository implements RepozytoriumKlientow {
    private final List<Klient> klienci = new ArrayList<>();
    private static int nextId = 1; // Prosty generator ID

    // Statyczna metoda do pobierania następnego ID, aby można było go użyć w MainApp przed zapisem
    public static synchronized int getNextId() {
        return nextId;
    }

    @Override
    public void zapisz(Klient klient) {
        // Sprawdź, czy klient już istnieje (po ID) i zaktualizuj, jeśli tak.
        // Jeśli ID klienta jest 0 lub nie pasuje do żadnego istniejącego,
        // a my chcemy, aby ID było generowane przez repozytorium,
        // to musielibyśmy tu inaczej podejść (np. klient przychodzi bez ID).
        // Na razie zakładamy, że ID jest już ustawione przed wywołaniem zapisz,
        // a jeśli jest to nowy klient, to nextId zostało już użyte (jak w MainApp).

        Optional<Klient> istniejacyKlientOpt = klienci.stream()
                .filter(k -> k.getIdKlienta() == klient.getIdKlienta())
                .findFirst();

        if (istniejacyKlientOpt.isPresent()) {
            // Aktualizacja istniejącego klienta
            Klient istniejacyKlient = istniejacyKlientOpt.get();
            istniejacyKlient.setNazwaFirmy(klient.getNazwaFirmy());
            // Ustaw inne pola...
            // Można by też po prostu usunąć starego i dodać nowego:
            // klienci.remove(istniejacyKlient);
            // klienci.add(klient);
        } else {
            // Dodanie nowego klienta
            // Upewnijmy się, że nextId jest większe niż ID dodawanego klienta, jeśli przyszło z zewnątrz
            if (klient.getIdKlienta() >= nextId) {
                 synchronized (InMemoryKlientRepository.class) {
                    nextId = klient.getIdKlienta() + 1;
                }
            }
            klienci.add(klient);
        }
    }


    @Override
    public Klient znajdzWgId(int id) {
        return klienci.stream()
                .filter(k -> k.getIdKlienta() == id)
                .findFirst()
                .orElse(null);
    }

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

    @Override
    public List<Klient> znajdzWszystkie() {
        return new ArrayList<>(klienci); // Zwracamy kopię, aby uniknąć modyfikacji oryginalnej listy z zewnątrz
    }

    @Override
    public void usun(int id) {
        klienci.removeIf(k -> k.getIdKlienta() == id);
    }
}