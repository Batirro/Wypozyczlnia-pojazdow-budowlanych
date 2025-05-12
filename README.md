# System Zarządzania Wypożyczalnią Pojazdów Budowlanych

Prosta aplikacja desktopowa napisana w Javie z wykorzystaniem biblioteki Swing do zarządzania procesami w firmie wypożyczającej pojazdy budowlane. Projekt implementuje podstawowe operacje CRUD dla pojazdów i klientów oraz zarządzanie cyklem życia wypożyczenia. Dane przechowywane są w pamięci aplikacji (implementacja In-Memory wzorca Repozytorium). Projekt wykorzystuje Apache Ant jako narzędzie do budowania.

## Główne Funkcjonalności

*   **Zarządzanie Flotą Pojazdów:**
    *   Dodawanie nowych pojazdów budowlanych (Koparka, Dźwig, Wywrotka) wraz z ich specyficznymi atrybutami.
    *   Wyświetlanie listy wszystkich pojazdów w systemie (w widoku tabelarycznym).
    *   Usuwanie pojazdów z systemu (za pomocą ID).
*   **Zarządzanie Bazą Klientów:**
    *   Dodawanie nowych klientów (firm).
    *   Automatyczne sprawdzanie, czy firma o podanym NIP już istnieje.
    *   Możliwość aktualizacji danych osoby kontaktowej dla istniejącej firmy.
    *   Wyświetlanie listy wszystkich klientów.
    *   Usuwanie klientów (za pomocą ID, z podstawowym sprawdzeniem aktywnych wypożyczeń).
*   **Obsługa Wypożyczeń:**
    *   Rejestrowanie nowego wypożyczenia z wyborem klienta z listy rozwijanej (`JComboBox`).
    *   Wybór daty rozpoczęcia i zakończenia za pomocą komponentu kalendarza (`JDateChooser`).
    *   Rejestrowanie zakończenia wypożyczenia z podaniem faktycznej daty zwrotu.
    *   Automatyczne obliczanie kosztu wypożyczenia.
    *   Wyświetlanie listy wszystkich wypożyczeń.
*   **Zarządzanie Historią:**
    *   Możliwość usunięcia wszystkich zakończonych wypożyczeń z systemu.
*   **Interfejs Użytkownika:**
    *   Graficzny interfejs użytkownika (GUI) oparty na Java Swing.
    *   Podział funkcjonalności na zakładki (`JTabbedPane`).

## Technologie

*   **Język:** Java (JDK 8 lub nowszy)
*   **GUI:** Java Swing
*   **Wybór Daty:** Biblioteka JCalendar (komponent `JDateChooser`)
*   **Narzędzie Budowania:** Apache Ant
*   **Wzorce Projektowe:** Wzorzec Repozytorium, Wstrzykiwanie Zależności (proste, przez konstruktor serwisu), Dziedziczenie, Polimorfizm.
*   **Przechowywanie Danych:** W pamięci aplikacji (`ArrayList` w implementacjach repozytoriów).
*   **IDE (deweloperskie):** NetBeans IDE (przystosowane do projektów Ant)

## Jak Zacząć? (Setup i Uruchomienie)

### Wymagania Wstępne

*   Zainstalowany **Java Development Kit (JDK)** - wersja 8 lub nowsza.
*   Zainstalowany **Apache Ant** (jeśli chcesz budować projekt z linii komend poza NetBeans).
*   Zainstalowane **NetBeans IDE** (wersja wspierająca projekty "Java with Ant").
*   **Git** do sklonowania repozytorium.

### Kroki Uruchomienia

1.  **Sklonuj repozytorium:**
    ```bash
    git clone [adres-url-twojego-repozytorium]
    cd [nazwa-katalogu-projektu]
    ```
2.  **Pobierz i dodaj bibliotekę JCalendar:**
    *   Pobierz plik `.jar` biblioteki JCalendar (np. `jcalendar-1.4.jar`) ze strony [toedter.com](https://toedter.com/jcalendar/) lub innego źródła.
    *   W głównym katalogu projektu utwórz folder `lib`.
    *   Skopiuj pobrany plik `jcalendar-x.x.jar` do folderu `lib`.
3.  **Otwórz projekt w NetBeans IDE:**
    *   W NetBeans wybierz `File -> Open Project...` i wskaż folder projektu. NetBeans powinien rozpoznać projekt oparty na Ant (`build.xml`).
    *   **Dodaj bibliotekę JCalendar do projektu:**
        *   W panelu "Projects", kliknij prawym przyciskiem na węzeł "Libraries".
        *   Wybierz "Add JAR/Folder...".
        *   Przejdź do folderu `lib` wewnątrz projektu i wybierz plik `jcalendar-x.x.jar`.
        *   Kliknij "Open".
4.  **Zbuduj projekt:**
    *   Kliknij prawym przyciskiem na projekt w NetBeans i wybierz `Clean and Build`. Ant wykona zadania zdefiniowane w `build.xml`.
5.  **Uruchom aplikację:**
    *   Kliknij prawym przyciskiem na projekt w NetBeans i wybierz `Run`. Spowoduje to uruchomienie głównej klasy aplikacji (`MainApp` lub odpowiednik) zdefiniowanej w `build.xml` lub właściwościach projektu.

## Struktura Projektu

Projekt zorganizowany jest w pakiety zgodnie z przeznaczeniem klas:

*   `com.wypozyczalnia.main` (lub `projekt_grupa_7`): Zawiera główną klasę uruchomieniową (`MainApp`).
*   `com.wypozyczalnia.model` (lub `projekt_grupa_7.model`): Definicje klas domenowych (encji) - `Pojazd`, `Koparka`, `Dzwig`, `Wywrotka`, `Klient`, `Wypozyczenie`.
*   `com.wypozyczalnia.repozytorium` (lub `projekt_grupa_7.repozytorium`):
    *   Interfejsy repozytoriów (`RepozytoriumPojazdow`, `RepozytoriumKlientow`, `RepozytoriumWypozyczen`).
    *   `impl`: Implementacje repozytoriów (`InMemoryPojazdRepository`, `InMemoryKlientRepository`, `InMemoryWypozyczenieRepository`).
*   `com.wypozyczalnia.serwis` (lub `projekt_grupa_7.serwis`): Zawiera klasę `SerwisWypozyczen` odpowiedzialną za logikę biznesową.
*   `com.wypozyczalnia.gui` (lub `projekt_grupa_7.gui`): Zawiera klasy związane z interfejsem graficznym (`RentalAppGUI`, `PojazdTableModel`, `KlientWrapper`).
