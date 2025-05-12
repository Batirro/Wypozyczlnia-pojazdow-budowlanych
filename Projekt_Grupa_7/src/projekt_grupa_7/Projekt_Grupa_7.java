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

public class Projekt_Grupa_7 {
    public static void main(String[] args) {
        // Inicjalizacja repozytoriów
        RepozytoriumPojazdow repoPojazdow = new InMemoryPojazdRepository();
        RepozytoriumKlientow repoKlientow = new InMemoryKlientRepository();
        RepozytoriumWypozyczen repoWypozyczen = new InMemoryWypozyczenieRepository();

        // Inicjalizacja serwisu
        final SerwisWypozyczen serwis = new SerwisWypozyczen(repoPojazdow, repoKlientow, repoWypozyczen);

        // Dodajmy przykładowe dane (opcjonalnie, można też dodawać przez GUI)
        dodajDanePoczatkowe(serwis);


        // Uruchomienie GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SerwisWypozyczenGUI(serwis).setVisible(true);
            }
        });
    }

    private static void dodajDanePoczatkowe(SerwisWypozyczen serwis) {
        System.out.println("--- DODAWANIE DANYCH POCZĄTKOWYCH (dla GUI) ---");
        // Użyj metody getNextId z repozytorium, jeśli jest dostępna i potrzebna dla ID
        // W GUI będziemy polegać na tym, że serwis/repozytorium samo zarządza ID
        // lub użytkownik wpisuje istniejące.
        // Poniższe ID są przykładowe, jeśli repozytorium samo je generuje, mogą być inne.

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
            serwis.zarejestrujWypozyczenie(klient1.getIdKlienta(), koparkaGUI.getId(), dataOd, dataDo);
        } catch (ParseException e) {
            System.err.println("Błąd parsowania daty przy dodawaniu danych początkowych: " + e.getMessage());
        }
        System.out.println("-------------------------------------------------");
    }
}
