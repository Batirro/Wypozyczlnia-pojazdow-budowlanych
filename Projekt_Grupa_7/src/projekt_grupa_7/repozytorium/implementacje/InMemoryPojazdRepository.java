package projekt_grupa_7.repozytorium.implementacje;

import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.repozytorium.RepozytoriumPojazdow;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryPojazdRepository implements RepozytoriumPojazdow {
    private final List<Pojazd> pojazdy = new ArrayList<>();

    @Override
    public void zapisz(Pojazd pojazd) {
        // Prosta implementacja: jeśli istnieje, usuń i dodaj (aktualizacja)
        pojazdy.removeIf(p -> p.getId().equals(pojazd.getId()));
        pojazdy.add(pojazd);
    }

    @Override
    public Pojazd znajdzWgId(String id) {
        return pojazdy.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Pojazd> znajdzWszystkie() {
        return new ArrayList<>(pojazdy); // Zwracamy kopię
    }

    @Override
    public List<Pojazd> znajdzDostepne() {
        return pojazdy.stream()
                .filter(Pojazd::isCzyDostepny)
                .collect(Collectors.toList());
    }

    @Override
    public void usun(String id) {
        pojazdy.removeIf(p -> p.getId().equals(id));
    }
}