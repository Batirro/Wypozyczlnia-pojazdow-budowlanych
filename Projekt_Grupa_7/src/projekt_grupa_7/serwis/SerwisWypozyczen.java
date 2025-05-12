/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

public class SerwisWypozyczen {
    private final RepozytoriumPojazdow repoPojazdow;
    private final RepozytoriumKlientow repoKlientow;
    private final RepozytoriumWypozyczen repoWypozyczen;
    private static int nextWypozyczenieId = 1;


    public SerwisWypozyczen(RepozytoriumPojazdow repoPojazdow, RepozytoriumKlientow repoKlientow, RepozytoriumWypozyczen repoWypozyczen) {
        this.repoPojazdow = repoPojazdow;
        this.repoKlientow = repoKlientow;
        this.repoWypozyczen = repoWypozyczen;
    }

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

        // Tutaj można dodać bardziej zaawansowane sprawdzanie kolizji dat, jeśli potrzebne

        Wypozyczenie wypozyczenie = new Wypozyczenie(nextWypozyczenieId++, klient, pojazd, dataOd, dataDo);
        wypozyczenie.obliczKoszt(); // Oblicz wstępny koszt

        repoWypozyczen.zapisz(wypozyczenie);
        pojazd.zmienDostepnosc(false);
        repoPojazdow.zapisz(pojazd); // Zapisz zmianę statusu pojazdu

        System.out.println("Zarejestrowano wypożyczenie: " + wypozyczenie.getIdWypozyczenia());
        return wypozyczenie;
    }

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
    
    public int usunZakonczoneWypozyczenia() {
        List<Wypozyczenie> wszystkie = repoWypozyczen.znajdzWszystkie();
        int licznikUsunietych = 0;
        // Używamy iteratora lub kopiujemy listę ID do usunięcia, aby uniknąć ConcurrentModificationException
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

    public List<Pojazd> znajdzDostepnePojazdy() {
        return repoPojazdow.znajdzDostepne();
    }

    public double obliczNaleznosc(int idWypozyczenia) {
        Wypozyczenie wypozyczenie = repoWypozyczen.znajdzWgId(idWypozyczenia);
        if (wypozyczenie != null) {
            return wypozyczenie.getCalkowityKoszt(); // Koszt jest już obliczony
        }
        System.err.println("Nie można obliczyć należności - wypożyczenie nie znalezione.");
        return 0;
    }
    
    // Dodaj metody do zarządzania Klientami i Pojazdami bezpośrednio (jeśli potrzebne)
    
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
            int noweId = 0; // Sygnalizacja dla repo, by nadało ID
             if (repoKlientow instanceof projekt_grupa_7.repozytorium.implementacje.InMemoryKlientRepository) {
                 try {
                    // Próba wywołania statycznej metody getNextId() z InMemoryKlientRepository
                    java.lang.reflect.Method m = projekt_grupa_7.repozytorium.implementacje.InMemoryKlientRepository.class.getMethod("getNextId");
                    noweId = (Integer) m.invoke(null);
                 } catch (Exception reflectionEx) {
                    System.err.println("Nie udało się pobrać nextId przez refleksję dla nowego klienta: " + reflectionEx.getMessage());
                    // Awaryjnie, jeśli refleksja zawiedzie, np. ID=ostatni+1
                    List<Klient> wszyscyKlienci = repoKlientow.znajdzWszystkie();
                    noweId = wszyscyKlienci.isEmpty() ? 1 : wszyscyKlienci.get(wszyscyKlienci.size()-1).getIdKlienta() + 1;
                 }
            }


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
