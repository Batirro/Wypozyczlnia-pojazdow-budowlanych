package projekt_grupa_7;

import projekt_grupa_7.gui.SerwisWypozyczenGUI; // Importuj klasę GUI
import projekt_grupa_7.model.*;
import projekt_grupa_7.repozytorium.RepozytoriumKlientow;
import projekt_grupa_7.repozytorium.RepozytoriumPojazdow;
import projekt_grupa_7.repozytorium.RepozytoriumWypozyczen;
import projekt_grupa_7.repozytorium.implementacje.InMemoryKlientRepository;
import projekt_grupa_7.repozytorium.implementacje.InMemoryPojazdRepository;
import projekt_grupa_7.repozytorium.implementacje.InMemoryWypozyczenieRepository;
import projekt_grupa_7.serwis.SerwisWypozyczen;

import javax.swing.SwingUtilities; // Dla uruchomienia GUI w wątku EDT
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Główna klasa aplikacji wypożyczalni.
 * Odpowiada za inicjalizację komponentów (repozytoria, serwis),
 * ewentualne dodanie danych początkowych oraz uruchomienie
 * graficznego interfejsu użytkownika (GUI).
 */

public class Projekt_Grupa_7 {
    /**
     * Główna metoda uruchomieniowa aplikacji.
     * @param args Argumenty wiersza poleceń (nieużywane w tej aplikacji).
     */
    public static void main(String[] args) {
        // Inicjalizacja warstwy dostępu do danych (Repozytoria)
        // Używamy implementacji "InMemory", które przechowują dane w listach w pamięci RAM.
        // W wersji produkcyjnej tutaj tworzylibyśmy instancje repozytoriów bazodanowych.
        RepozytoriumPojazdow repoPojazdow = new InMemoryPojazdRepository();
        RepozytoriumKlientow repoKlientow = new InMemoryKlientRepository();
        RepozytoriumWypozyczen repoWypozyczen = new InMemoryWypozyczenieRepository();

        // Inicjalizacja warstwy logiki biznesowej (Serwis)
        // Tworzymy serwis, wstrzykując do niego utworzone repozytoria.
        // Serwis będzie używał repozytoriów do operacji na danych.
        // Użycie final zapewnia, że referencja do serwisu nie zostanie zmieniona później.
        final SerwisWypozyczen serwis = new SerwisWypozyczen(repoPojazdow, repoKlientow, repoWypozyczen);

        // Dodajmy przykładowe dane
        dodajDanePoczatkowe(serwis);


        // Uruchomienie Graficznego Interfejsu Użytkownika (GUI)
        // GUI Swinga powinno być tworzone i modyfikowane w specjalnym wątku - Event Dispatch Thread (EDT).
        // SwingUtilities.invokeLater() zapewnia, że kod tworzący i pokazujący okno GUI
        // zostanie wykonany właśnie w tym wątku, co zapobiega problemom z odświeżaniem interfejsu.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Tworzymy instancję okna GUI, przekazując do niego referencję do serwisu.
                // GUI będzie używać serwisu do wykonywania wszystkich operacji biznesowych.
                new SerwisWypozyczenGUI(serwis).setVisible(true);
            }
        });
    }
    /**
     * Metoda pomocnicza do dodawania początkowych danych do repozytoriów
     * za pośrednictwem serwisu. Używana do celów demonstracyjnych i testowych.
     * @param serwis Instancja serwisu wypożyczeń, przez którą dodawane są dane.
     */
    private static void dodajDanePoczatkowe(SerwisWypozyczen serwis) {
        System.out.println("--- DODAWANIE DANYCH POCZĄTKOWYCH ---");
        // --- Dodawanie Klientów ---
        // Uwaga: Zarządzanie ID w repozytoriach InMemory może wymagać uwagi.
        // Tutaj tworzymy obiekty Klient i używamy metody serwis.dodajKlienta(Klient).
        // Metoda ta powinna poprawnie obsłużyć zapis w repozytorium.

        // Pobieranie ID za pomocą refleksji (mniej idealne, ale działa dla przykładu)
        int idKlienta1 = 1, idKlienta2 = 2;
        try {
            idKlienta1 = (Integer) serwis.getRepoKlientow().getClass().getMethod("getNextId").invoke(null);
            // Pamiętaj, że getNextId w InMemoryKlientRepository powinno być statyczne i publiczne
            // dla tego podejścia. A samo nextId powinno być inkrementowane w zapisz.
        } catch (Exception e) { System.err.println("Problem z getNextId dla Klienta1: " + e.getMessage());}
        
        Klient klient1 = new Klient(idKlienta1, "Budimex GUI S.A.", "111-222-33-44", "Piotr", "Zieliński", "501502503", "piotr@budimexgui.pl");
        serwis.dodajKlienta(klient1);

        try {
             idKlienta2 = (Integer) serwis.getRepoKlientow().getClass().getMethod("getNextId").invoke(null);
        } catch (Exception e) { System.err.println("Problem z getNextId dla Klienta2: " + e.getMessage());}
        Klient klient2 = new Klient(idKlienta2, "Skanska GUI AB", "555-666-77-88", "Ewa", "Lewandowska", "601602603", "ewa@skanskagui.com");
        serwis.dodajKlienta(klient2);


        Pojazd koparkaGUI = new Koparka("GUI001", "Volvo", "EC220E", 2021, 550.00, 1.8, 9.5, 6.5);
        Pojazd dzwigGUI = new Dzwig("GUI002", "Grove", "GMK4100L", 2019, 1300.00, 100.0, 60.0, 70.0, "Mobilny AT");
        serwis.dodajPojazd(koparkaGUI);
        serwis.dodajPojazd(dzwigGUI);

        // Przykładowe wypożyczenie, aby coś było widać
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dataOd = sdf.parse("2024-01-10");
            Date dataDo = sdf.parse("2024-01-15");
            // Używamy ID klienta, które zostało potencjalnie nadane przez repozytorium (ważne, by pobrać je z obiektu)
            serwis.zarejestrujWypozyczenie(klient1.getIdKlienta(), koparkaGUI.getId(), dataOd, dataDo);
        } catch (ParseException e) {
            System.err.println("Błąd parsowania daty przy dodawaniu danych początkowych: " + e.getMessage());
        }
        System.out.println("-------------------------------------------------");
    }
}
