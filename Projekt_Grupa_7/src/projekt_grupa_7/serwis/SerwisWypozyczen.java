package projekt_grupa_7.serwis;

import projekt_grupa_7.model.Klient;
import projekt_grupa_7.model.Pojazd;
import projekt_grupa_7.model.Wypozyczenie;
import projekt_grupa_7.repozytorium.RepozytoriumKlientow;
import projekt_grupa_7.repozytorium.RepozytoriumPojazdow;
import projekt_grupa_7.repozytorium.RepozytoriumWypozyczen;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Główna klasa serwisowa aplikacji, enkapsulująca logikę biznesową.
 * Działa jako fasada dla operacji związanych z wypożyczeniami, zarządzaniem flotą
 * i klientami. Koordynuje interakcje między różnymi repozytoriami.
 * Jest zaprojektowana do pracy z interfejsami repozytoriów, co umożliwia
 * łatwą zmianę implementacji warstwy dostępu do danych (np. z In-Memory na bazę danych).
 *
 * @see projekt_grupa_7.repozytorium.RepozytoriumPojazdow
 * @see projekt_grupa_7.repozytorium.RepozytoriumKlientow
 * @see projekt_grupa_7.repozytorium.RepozytoriumWypozyczen
 * @see projekt_grupa_7.gui.RentalAppGUI
 */

public class SerwisWypozyczen {
    /** Referencja do repozytorium pojazdów (interfejs). */
    private final RepozytoriumPojazdow repoPojazdow;
    /** Referencja do repozytorium klientów (interfejs). */
    private final RepozytoriumKlientow repoKlientow;
    /** Referencja do repozytorium wypożyczeń (interfejs). */
    private final RepozytoriumWypozyczen repoWypozyczen;
    /**
     * Prosty, statyczny licznik ID dla nowych wypożyczeń.
     * Nadaje ID przed zapisem do repozytorium.
     */
    private static int nextWypozyczenieId = 1;

    /**
     * Konstruktor serwisu, realizujący wstrzykiwanie zależności (Dependency Injection).
     * Przyjmuje konkretne implementacje repozytoriów (choć typowane przez interfejsy).
     *
     * @param repoPojazdow Instancja repozytorium pojazdów. Nie powinna być null.
     * @param repoKlientow Instancja repozytorium klientów. Nie powinna być null.
     * @param repoWypozyczen Instancja repozytorium wypożyczeń. Nie powinna być null.
     */
    
    public SerwisWypozyczen(RepozytoriumPojazdow repoPojazdow, RepozytoriumKlientow repoKlientow, RepozytoriumWypozyczen repoWypozyczen) {
        this.repoPojazdow = repoPojazdow;
        this.repoKlientow = repoKlientow;
        this.repoWypozyczen = repoWypozyczen;
    }
    
    /**
     * Rejestruje nowe wypożyczenie dla podanego klienta i pojazdu w określonym terminie.
     * Przeprowadza walidację: sprawdza istnienie klienta i pojazdu, dostępność pojazdu.
     * Jeśli walidacja przebiegnie pomyślnie, tworzy nowy obiekt {@link Wypozyczenie},
     * nadaje mu unikalne ID, oblicza wstępny koszt, zapisuje go w repozytorium,
     * a następnie zmienia status dostępności pojazdu na "niedostępny" i zapisuje tę zmianę.
     *
     * @param idKlienta ID klienta dokonującego wypożyczenia.
     * @param idPojazdu ID (np. numer rejestracyjny) wypożyczanego pojazdu.
     * @param dataOd Data i czas rozpoczęcia wypożyczenia.
     * @param dataDo Planowana data i czas zakończenia wypożyczenia.
     * @return Obiekt nowo utworzonego {@link Wypozyczenie} lub {@code null}, jeśli operacja się nie powiodła
     *         (np. z powodu braku klienta, pojazdu lub jego niedostępności). Komunikaty o błędach
     *         są logowane na standardowe wyjście błędów.
     */
    
    public Wypozyczenie zarejestrujWypozyczenie(int idKlienta, String idPojazdu, Date dataOd, Date dataDo) {
        Klient klient = repoKlientow.znajdzWgId(idKlienta);
        if (klient == null) {
            System.err.println("Błąd: Klient o ID " + idKlienta + " nie znaleziony.");
            return null;
        }

        Pojazd pojazd = repoPojazdow.znajdzWgId(idPojazdu);
        if (pojazd == null) {
            System.err.println("Błąd: Pojazd o ID " + idPojazdu + " nie znaleziony.");
            return null;
        }

        if (!pojazd.isCzyDostepny()) {
            System.err.println("Błąd: Pojazd " + pojazd.getMarka() + " " + pojazd.getModel() + " nie jest obecnie dostępny.");
            return null;
        }
        
        // TODO: Dodać bardziej zaawansowane sprawdzanie dostępności pojazdu w zakresie dat [dataOd, dataDo]
        // Wymagałoby to odpytania repoWypozyczen o istniejące wypożyczenia dla tego pojazdu i sprawdzenia kolizji.
        
        Wypozyczenie wypozyczenie = new Wypozyczenie(nextWypozyczenieId++, klient, pojazd, dataOd, dataDo);
        wypozyczenie.obliczKoszt(); // Oblicz wstępny koszt
        repoWypozyczen.zapisz(wypozyczenie);
        pojazd.zmienDostepnosc(false);
        repoPojazdow.zapisz(pojazd); // Zapisz zmianę statusu pojazdu

        System.out.println("Zarejestrowano wypożyczenie: " + wypozyczenie.getIdWypozyczenia());
        return wypozyczenie;
    }
     /**
     * Finalizuje (kończy) proces aktywnego wypożyczenia.
     * Znajduje wypożyczenie o podanym ID. Jeśli istnieje i jest aktywne:
     * - Ustawia faktyczną datę zakończenia.
     * - Oznacza wypożyczenie jako zakończone.
     * - Przelicza ostateczny koszt na podstawie faktycznej daty zwrotu.
     * - Zapisuje zaktualizowany obiekt wypożyczenia w repozytorium.
     * - Zmienia status powiązanego pojazdu na "dostępny" i zapisuje tę zmianę.
     *
     * @param idWypozyczenia ID wypożyczenia, które ma zostać zakończone.
     * @param dataFaktycznegoZakonczenia Rzeczywista data i czas zwrotu pojazdu.
     */
    
    public void zakonczWypozyczenie(int idWypozyczenia, Date dataFaktycznegoZakonczenia) {
        Wypozyczenie wypozyczenie = repoWypozyczen.znajdzWgId(idWypozyczenia);
        if (wypozyczenie == null) {
            System.err.println("Błąd: Wypożyczenie o ID " + idWypozyczenia + " nie znalezione.");
            return;
        }
        if (wypozyczenie.isCzyZakonczone()){
            System.err.println("Błąd: Wypożyczenie o ID " + idWypozyczenia + " jest już zakończone.");
            return;
        }

        wypozyczenie.zakonczWypozyczenie(dataFaktycznegoZakonczenia);
        // Koszt jest przeliczany w metodzie zakonczWypozyczenie() obiektu Wypozyczenie

        Pojazd pojazd = wypozyczenie.getPojazd();
        pojazd.zmienDostepnosc(true);
        repoPojazdow.zapisz(pojazd); // Zapisz zmianę statusu pojazdu
        repoWypozyczen.zapisz(wypozyczenie); // Zapisz zaktualizowane wypożyczenie

        System.out.println("Zakończono wypożyczenie: " + wypozyczenie.getIdWypozyczenia() + ". Ostateczny koszt: " + String.format("%.2f", wypozyczenie.getCalkowityKoszt()) + " PLN");
    }
    
    /**
     * Usuwa wszystkie zakończone wypożyczenia z repozytorium.
     * Iteruje po wszystkich wypożyczeniach, identyfikuje te, które są oznaczone jako zakończone,
     * a następnie usuwa je z repozytorium.
     *
     * @return Liczba usuniętych wypożyczeń.
     */
    public int usunZakonczoneWypozyczenia() {
        List<Wypozyczenie> wszystkie = repoWypozyczen.znajdzWszystkie();
        int licznikUsunietych = 0;
        // Używamy iteratora lub kopiujemy listę ID do usunięcia, aby uniknąć ConcurrentModificationException
        // podczas usuwania elementów z listy, po której iterujemy.
        List<Integer> idDoUsuniecia = new ArrayList<>();
        for (Wypozyczenie w : wszystkie) {
            if (w.isCzyZakonczone()) {
                idDoUsuniecia.add(w.getIdWypozyczenia());
            }
        }

        for (Integer id : idDoUsuniecia) {
            repoWypozyczen.usun(id); // Zakładając, że repoWypozyczen ma metodę usun(int id)
            licznikUsunietych++;
        }
        if (licznikUsunietych > 0) {
            System.out.println("Usunięto " + licznikUsunietych + " zakończonych wypożyczeń z historii.");
        } else {
            System.out.println("Nie znaleziono zakończonych wypożyczeń do usunięcia.");
        }
        return licznikUsunietych;
    }
     /**
     * Zwraca listę wszystkich pojazdów aktualnie oznaczonych jako dostępne.
     * Deleguje wywołanie do {@link RepozytoriumPojazdow#znajdzDostepne()}.
     * @return Lista dostępnych obiektów {@link Pojazd}.
     */
    public List<Pojazd> znajdzDostepnePojazdy() {
        return repoPojazdow.znajdzDostepne();
    }

    /**
     * Oblicza i zwraca całkowitą należność za wypożyczenie o podanym ID.
     * Jeśli wypożyczenie nie zostanie znalezione, loguje błąd i zwraca 0.
     * Koszt jest pobierany bezpośrednio z obiektu {@link Wypozyczenie},
     * który powinien mieć go już obliczonego (np. po zakończeniu wypożyczenia).
     *
     * @param idWypozyczenia ID wypożyczenia, dla którego ma być obliczona należność.
     * @return Całkowity koszt wypożyczenia lub 0, jeśli wypożyczenie nie istnieje.
     */
    public double obliczNaleznosc(int idWypozyczenia) {
        Wypozyczenie wypozyczenie = repoWypozyczen.znajdzWgId(idWypozyczenia);
        if (wypozyczenie != null) {
            return wypozyczenie.getCalkowityKoszt(); // Koszt jest już obliczony
        }
        System.err.println("Nie można obliczyć należności - wypożyczenie nie znalezione.");
        return 0;
    }
    
    /**
     * Dodaje nowego klienta do repozytorium lub aktualizuje istniejącego.
     * Metoda {@link RepozytoriumKlientow#zapisz(Klient)} powinna zawierać logikę
     * rozróżniającą dodanie nowego klienta od aktualizacji istniejącego na podstawie ID.
     * Loguje informację o dodaniu lub aktualizacji klienta.
     *
     * @param klient Obiekt {@link Klient} do dodania lub zaktualizowania. Nie powinien być null.
     */
    public void dodajKlienta(Klient klient) {
        if (klient == null) {
            System.err.println("Błąd: Próba dodania pustego obiektu klienta (null).");
            return;
        }
        // Metoda zapisz w repozytorium powinna obsłużyć logikę
        // dodania nowego lub aktualizacji istniejącego na podstawie ID klienta.
        repoKlientow.zapisz(klient);
        System.out.println("Dodano/Zaktualizowano klienta: " + klient.getNazwaFirmy() + " (ID: " + klient.getIdKlienta() + ")");
    }

    
    /**
     * Dodaje nowego klienta lub aktualizuje dane istniejącego klienta na podstawie numeru NIP.
     * Jeśli klient o podanym NIP już istnieje:
     * - Gdy {@code czyAktualizowacIstniejacego} jest {@code true}, aktualizuje dane kontaktowe klienta.
     * - Gdy {@code czyAktualizowacIstniejacego} jest {@code false}, nie dokonuje zmian i zwraca istniejącego klienta.
     * Jeśli klient o podanym NIP nie istnieje, tworzy nowego klienta z podanymi danymi.
     * Próbuje dynamicznie uzyskać kolejne ID dla nowego klienta z implementacji
     * {@code InMemoryKlientRepository} za pomocą refleksji. W przypadku niepowodzenia,
     * generuje ID na podstawie ostatniego klienta w repozytorium.
     *
     * @param nazwaFirmy Nazwa firmy klienta.
     * @param nip Numer NIP klienta (unikalny identyfikator). Nie może być pusty.
     * @param imieKontakt Imię osoby kontaktowej.
     * @param nazwiskoKontakt Nazwisko osoby kontaktowej.
     * @param telefonKontakt Telefon kontaktowy.
     * @param emailKontakt Adres e-mail kontaktowy.
     * @param czyAktualizowacIstniejacego Flaga określająca, czy aktualizować dane istniejącego klienta.
     * @return Obiekt {@link Klient} (nowo utworzony lub zaktualizowany) lub {@code null}, jeśli NIP jest pusty.
     *         W przypadku, gdy klient istnieje a {@code czyAktualizowacIstniejacego} jest {@code false}, zwraca istniejącego klienta.
     */
    public Klient dodajLubAktualizujKlienta(String nazwaFirmy, String nip, String imieKontakt,
                                          String nazwiskoKontakt, String telefonKontakt, String emailKontakt,
                                          boolean czyAktualizowacIstniejacego) {
        if (nip == null || nip.trim().isEmpty()) {
            System.err.println("NIP nie może być pusty.");
            return null;
        }

        Klient istniejacyKlient = repoKlientow.znajdzWgNIP(nip);

        if (istniejacyKlient != null) {
            if (czyAktualizowacIstniejacego) {
                // Aktualizuj dane kontaktowe istniejącego klienta
                // Można by też zaktualizować nazwę firmy, jeśli się zmieniła, ale to kwestia decyzji.
                // istniejacyKlient.setNazwaFirmy(nazwaFirmy); // Opcjonalnie
                istniejacyKlient.setImieKontakt(imieKontakt);
                istniejacyKlient.setNazwiskoKontakt(nazwiskoKontakt);
                istniejacyKlient.setTelefonKontakt(telefonKontakt);
                istniejacyKlient.setEmailKontakt(emailKontakt);
                repoKlientow.zapisz(istniejacyKlient); // Zapisz zmiany
                System.out.println("Zaktualizowano dane kontaktowe dla klienta (NIP: " + nip + "): " + istniejacyKlient.getNazwaFirmy());
                return istniejacyKlient;
            } else {
                System.out.println("Klient o NIP: " + nip + " już istnieje (Nazwa: " + istniejacyKlient.getNazwaFirmy() + "). Nie podjęto akcji.");
                return istniejacyKlient; // Zwracamy istniejącego, aby GUI mogło poinformować
            }
        } else {
            // Dodaj nowego klienta
            // Potrzebujemy sposobu na uzyskanie nowego ID. Zakładając, że repozytorium sobie z tym radzi
            // lub mamy metodę w InMemoryKlientRepository.
            // W uproszczeniu, jeśli Klient ma konstruktor, który przyjmuje ID, a repozytorium je generuje:
            int noweId = 0; // Sygnalizacja dla repo, by nadało ID lub wartość domyślna
             if (repoKlientow instanceof projekt_grupa_7.repozytorium.implementacje.InMemoryKlientRepository) {
                 try {
                    // Próba wywołania statycznej metody getNextId() z InMemoryKlientRepository za pomocą refleksji.
                    // Jest to specyficzne dla tej implementacji repozytorium i pozwala na centralne zarządzanie ID.
                    java.lang.reflect.Method m = projekt_grupa_7.repozytorium.implementacje.InMemoryKlientRepository.class.getMethod("getNextId");
                    noweId = (Integer) m.invoke(null); // Wywołanie metody statycznej, stąd null jako argument obiektu
                 } catch (Exception reflectionEx) {
                    System.err.println("Nie udało się pobrać nextId przez refleksję dla nowego klienta: " + reflectionEx.getMessage());
                    // Awaryjne generowanie ID, jeśli refleksja zawiedzie: ID ostatniego klienta + 1.
                    // Jest to mniej bezpieczne i może prowadzić do kolizji ID, jeśli repozytorium ma inną logikę.
                    List<Klient> wszyscyKlienci = repoKlientow.znajdzWszystkie();
                    noweId = wszyscyKlienci.isEmpty() ? 1 : wszyscyKlienci.get(wszyscyKlienci.size()-1).getIdKlienta() + 1;
                 }
            }
            // Jeśli repozytorium nie jest instancją InMemoryKlientRepository lub refleksja zawiodła,
            // a nie zaimplementowano innego mechanizmu generowania ID, 'noweId' może pozostać 0.
            // W takim przypadku, konstruktor Klienta lub metoda zapisz w repozytorium powinny obsłużyć nadanie ID.


            Klient nowyKlient = new Klient(noweId, nazwaFirmy, nip, imieKontakt, nazwiskoKontakt, telefonKontakt, emailKontakt);
            repoKlientow.zapisz(nowyKlient);
            System.out.println("Dodano nowego klienta (NIP: " + nip + "): " + nowyKlient.getNazwaFirmy());
            return nowyKlient;
        }
    }

    public void dodajPojazd(Pojazd pojazd) {
        repoPojazdow.zapisz(pojazd);
        System.out.println("Dodano pojazd: " + pojazd.getMarka() + " " + pojazd.getModel());
    }
    
    public RepozytoriumPojazdow getRepoPojazdow() {
        return repoPojazdow;
    }

    public RepozytoriumKlientow getRepoKlientow() {
        return repoKlientow;
    }

    public RepozytoriumWypozyczen getRepoWypozyczen() {
        return repoWypozyczen;
    }
}
