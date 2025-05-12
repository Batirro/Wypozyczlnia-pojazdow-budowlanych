package projekt_grupa_7.gui;

import projekt_grupa_7.model.*;
import projekt_grupa_7.serwis.SerwisWypozyczen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class SerwisWypozyczenGUI extends JFrame {

    private SerwisWypozyczen serwis;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    // Komponenty GUI
    private JTabbedPane tabbedPane;

    // Zakładka Pojazdy
    private JPanel pojazdyPanel;
    private JTextArea pojazdyTextArea;
    private JTextField pojazdIdField, pojazdMarkaField, pojazdModelField, pojazdRokField, pojazdStawkaField;
    private JComboBox<String> pojazdTypComboBox;
    // Pola specyficzne dla typów pojazdów (pokażemy/ukryjemy)
    private JPanel koparkaSpecPanel, dzwigSpecPanel, wywrotkaSpecPanel;
    private JTextField koparkaPojemnoscLyzeczkiField, koparkaZasiegRamieniaField, koparkaGlebokoscKopaniaField;
    private JTextField dzwigMaxUdzwigField, dzwigDlugoscWysiegnikaField, dzwigMaxWysokoscPodnoszeniaField, dzwigTypField;
    private JTextField wywrotkaLadownoscField, wywrotkaTypNapeduField, wywrotkaPojemnoscSkrzyniField;
    private JTextField pojazdIdDoUsunieciaField;


    // Zakładka Klienci
    private JPanel klienciPanel;
    private JTextArea klienciTextArea;
    private JTextField klientNazwaFirmyField, klientNipField, klientImieKontaktField, klientNazwiskoKontaktField, klientTelefonField, klientEmailField;
    private JTextField klientIdDoUsunieciaField;

    // Zakładka Wypożyczenia - ZMODYFIKOWANE/NOWE POLA
    private JPanel wypozyczeniaPanel;
    private JTextArea wypozyczeniaTextArea; // lub JTable
    private JComboBox<KlientWrapper> wypozyczenieKlientComboBox; // NOWE - do wyboru klienta
    private JTextField wypozyczeniePojazdIdField;
    private JDateChooser wypozyczenieDataOdChooser;
    private JDateChooser wypozyczenieDataDoChooser;
    private JTextField wypozyczenieIdDoZakonczeniaField;
    private JDateChooser wypozyczenieDataZwrotuChooser;
    private JButton wyczyscZakonczoneButton;


    public SerwisWypozyczenGUI(SerwisWypozyczen serwis) {
        this.serwis = serwis;
        setTitle("System Wypożyczalni Pojazdów Budowlanych");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // centruj okno

        initComponents();
        loadInitialData();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        createPojazdyPanel();
        createKlienciPanel();
        createWypozyczeniaPanel();

        tabbedPane.addTab("Pojazdy", pojazdyPanel);
        tabbedPane.addTab("Klienci", klienciPanel);
        tabbedPane.addTab("Wypożyczenia", wypozyczeniaPanel);

        add(tabbedPane);
    }

    // --- ZAKŁADKA POJAZDY ---
    private void createPojazdyPanel() {
        pojazdyPanel = new JPanel(new BorderLayout(5, 5));
        pojazdyTextArea = new JTextArea(20, 70);
        pojazdyTextArea.setEditable(false);
        JScrollPane scrollPojazdy = new JScrollPane(pojazdyTextArea);
        pojazdyPanel.add(scrollPojazdy, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Pola wspólne
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("ID (Rejestracja):"), gbc);
        pojazdIdField = new JTextField(15); gbc.gridx = 1; inputPanel.add(pojazdIdField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Marka:"), gbc);
        pojazdMarkaField = new JTextField(15); gbc.gridx = 1; inputPanel.add(pojazdMarkaField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Model:"), gbc);
        pojazdModelField = new JTextField(15); gbc.gridx = 1; inputPanel.add(pojazdModelField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Rok Produkcji:"), gbc);
        pojazdRokField = new JTextField(5); gbc.gridx = 1; inputPanel.add(pojazdRokField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Stawka Dobowa:"), gbc);
        pojazdStawkaField = new JTextField(8); gbc.gridx = 1; inputPanel.add(pojazdStawkaField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Typ Pojazdu:"), gbc);
        pojazdTypComboBox = new JComboBox<>(new String[]{"Koparka", "Dźwig", "Wywrotka"});
        gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(pojazdTypComboBox, gbc);
        row++;

        // Panele dla specyficznych atrybutów
        koparkaSpecPanel = createKoparkaSpecPanel();
        dzwigSpecPanel = createDzwigSpecPanel();
        wywrotkaSpecPanel = createWywrotkaSpecPanel();

        gbc.gridx = 0; gbc.gridy = row; // Teraz panele specyfikacji będą w NOWYM wierszu, pod ComboBoxem
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Chcemy, żeby panel wypełnił szerokość
        JPanel specContainerPanel = new JPanel(new GridBagLayout()); // Kontener na panele specyfikacji
        GridBagConstraints gbcSpec = new GridBagConstraints();
        gbcSpec.gridx=0; gbcSpec.gridy=0; gbcSpec.weightx=1.0; gbcSpec.fill = GridBagConstraints.HORIZONTAL;
        specContainerPanel.add(koparkaSpecPanel, gbcSpec);
        specContainerPanel.add(dzwigSpecPanel, gbcSpec);
        specContainerPanel.add(wywrotkaSpecPanel, gbcSpec);

        inputPanel.add(specContainerPanel, gbc); // Dodaj kontener do głównego inputPanel

        row++; // Inkrementuj wiersz po panelach specyfikacji
        gbc.gridwidth = 1; // Resetuj gridwidth
        gbc.fill = GridBagConstraints.NONE; // Resetuj fill


        // Listener do zmiany widoczności paneli specyficznych
        pojazdTypComboBox.addActionListener(e -> updatePojazdSpecFieldsVisibility());
        updatePojazdSpecFieldsVisibility(); // Ustawienie początkowej widoczności
        // Przycisk "Dodaj Pojazd"
        JButton dodajPojazdButton = new JButton("Dodaj Pojazd");
        dodajPojazdButton.addActionListener(this::dodajPojazdAction);

        // Ustawienia GridBagConstraints dla przycisku, aby go wyśrodkować
        gbc.gridx = 0; // Zacznij od pierwszej kolumny
        gbc.gridy = row; // W bieżącym, nowym wierszu
        gbc.gridwidth = 2; // Przycisk obejmie 2 kolumny (zakładając, że formularz ma 2 kolumny: etykiety i pola)
        gbc.anchor = GridBagConstraints.CENTER; // WYŚRODKUJ komponent w jego przydzielonej przestrzeni
        gbc.fill = GridBagConstraints.NONE;     // NIE rozciągaj przycisku, użyj jego preferowanego rozmiaru
        gbc.insets = new Insets(10, 0, 10, 0); // Opcjonalnie: dodaj trochę marginesu górnego/dolnego
        inputPanel.add(dodajPojazdButton, gbc);
        row++; // Inkrementuj wiersz dla kolejnych elementów
        // Resetuj ustawienia gbc dla kolejnych komponentów, jeśli będą dodawane do tego samego inputPanel
        gbc.gridwidth = 1; // Wróć do zajmowania jednej kolumny
        gbc.anchor = GridBagConstraints.WEST; // Wyrównaj do lewej (lub inne domyślne)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Domyślnie pola tekstowe mogą się rozciągać
        gbc.insets = new Insets(2,2,2,2); // Wróć do standardowych marginesów   
        // Oddzielenie sekcji
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        // Pola do usuwania pojazdu - dodane do istniejącego inputPanel
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("ID do Usunięcia:"), gbc);
        pojazdIdDoUsunieciaField = new JTextField(15); // Możesz dostosować szerokość
        gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL; inputPanel.add(pojazdIdDoUsunieciaField, gbc);
        row++;
        // Przycisk usunięcia
        JButton usunPojazdButton = new JButton("Usuń Wybrany Pojazd");
        usunPojazdButton.addActionListener(this::usunPojazdAction); // Metoda akcji z poprzedniej odpowiedzi
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(usunPojazdButton, gbc);
        row++;
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.VERTICAL;
        inputPanel.add(new JLabel(""), gbc);

        pojazdyPanel.add(inputPanel, BorderLayout.EAST);

        JButton odswiezPojazdyButton = new JButton("Odśwież Listę Pojazdów");
        odswiezPojazdyButton.addActionListener(e -> refreshPojazdyArea());
        pojazdyPanel.add(odswiezPojazdyButton, BorderLayout.SOUTH);
    }

    private JPanel createKoparkaSpecPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Specyfikacja Koparki"));
        koparkaPojemnoscLyzeczkiField = new JTextField(10);
        koparkaZasiegRamieniaField = new JTextField(10);
        koparkaGlebokoscKopaniaField = new JTextField(10);
        panel.add(new JLabel("Poj. łyżki (m3):")); panel.add(koparkaPojemnoscLyzeczkiField);
        panel.add(new JLabel("Zasięg ramienia (m):")); panel.add(koparkaZasiegRamieniaField);
        panel.add(new JLabel("Głęb. kopania (m):")); panel.add(koparkaGlebokoscKopaniaField);
        return panel;
    }
     private JPanel createDzwigSpecPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Specyfikacja Dźwigu"));
        dzwigMaxUdzwigField = new JTextField(10);
        dzwigDlugoscWysiegnikaField = new JTextField(10);
        dzwigMaxWysokoscPodnoszeniaField = new JTextField(10);
        dzwigTypField = new JTextField(10);
        panel.add(new JLabel("Max udźwig (t):")); panel.add(dzwigMaxUdzwigField);
        panel.add(new JLabel("Dł. wysięgnika (m):")); panel.add(dzwigDlugoscWysiegnikaField);
        panel.add(new JLabel("Max wys. podn. (m):")); panel.add(dzwigMaxWysokoscPodnoszeniaField);
        panel.add(new JLabel("Typ dźwigu:")); panel.add(dzwigTypField);
        return panel;
    }
    private JPanel createWywrotkaSpecPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Specyfikacja Wywrotki"));
        wywrotkaLadownoscField = new JTextField(10);
        wywrotkaTypNapeduField = new JTextField(10);
        wywrotkaPojemnoscSkrzyniField = new JTextField(10);
        panel.add(new JLabel("Ładowność (t):")); panel.add(wywrotkaLadownoscField);
        panel.add(new JLabel("Typ napędu:")); panel.add(wywrotkaTypNapeduField);
        panel.add(new JLabel("Poj. skrzyni (m3):")); panel.add(wywrotkaPojemnoscSkrzyniField);
        return panel;
    }

    private void updatePojazdSpecFieldsVisibility() {
        String selectedType = (String) pojazdTypComboBox.getSelectedItem();
        koparkaSpecPanel.setVisible("Koparka".equals(selectedType));
        dzwigSpecPanel.setVisible("Dźwig".equals(selectedType));
        wywrotkaSpecPanel.setVisible("Wywrotka".equals(selectedType));
        // Proste ponowne walidowanie panelu nadrzędnego
        if (pojazdyPanel != null) {
             pojazdyPanel.revalidate();
             pojazdyPanel.repaint();
        }
    }

    private void dodajPojazdAction(ActionEvent e) {
        try {
            String id = pojazdIdField.getText();
            String marka = pojazdMarkaField.getText();
            String model = pojazdModelField.getText();
            int rok = Integer.parseInt(pojazdRokField.getText());
            double stawka = Double.parseDouble(pojazdStawkaField.getText());
            String typ = (String) pojazdTypComboBox.getSelectedItem();

            if (id.isEmpty() || marka.isEmpty() || model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Marka i Model są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pojazd nowyPojazd = null;
            if ("Koparka".equals(typ)) {
                double pojLyzeczki = Double.parseDouble(koparkaPojemnoscLyzeczkiField.getText());
                double zasRamienia = Double.parseDouble(koparkaZasiegRamieniaField.getText());
                double glKopania = Double.parseDouble(koparkaGlebokoscKopaniaField.getText());
                nowyPojazd = new Koparka(id, marka, model, rok, stawka, pojLyzeczki, zasRamienia, glKopania);
            } else if ("Dźwig".equals(typ)) {
                double maxUdzwig = Double.parseDouble(dzwigMaxUdzwigField.getText());
                double dlWysiegnika = Double.parseDouble(dzwigDlugoscWysiegnikaField.getText());
                double maxWysPodn = Double.parseDouble(dzwigMaxWysokoscPodnoszeniaField.getText());
                String typDzwigu = dzwigTypField.getText();
                nowyPojazd = new Dzwig(id, marka, model, rok, stawka, maxUdzwig, dlWysiegnika, maxWysPodn, typDzwigu);
            } else if ("Wywrotka".equals(typ)) {
                double ladownosc = Double.parseDouble(wywrotkaLadownoscField.getText());
                String typNapedu = wywrotkaTypNapeduField.getText();
                double pojSkrzyni = Double.parseDouble(wywrotkaPojemnoscSkrzyniField.getText());
                nowyPojazd = new Wywrotka(id, marka, model, rok, stawka, ladownosc, typNapedu, pojSkrzyni);
            }

            if (nowyPojazd != null) {
                serwis.dodajPojazd(nowyPojazd);
                refreshPojazdyArea();
                JOptionPane.showMessageDialog(this, "Dodano pojazd: " + nowyPojazd.getMarka() + " " + nowyPojazd.getModel());
                // Wyczyść pola
                pojazdIdField.setText(""); pojazdMarkaField.setText(""); pojazdModelField.setText("");
                pojazdRokField.setText(""); pojazdStawkaField.setText("");
                koparkaPojemnoscLyzeczkiField.setText("");koparkaZasiegRamieniaField.setText("");koparkaGlebokoscKopaniaField.setText("");
                dzwigMaxUdzwigField.setText("");dzwigDlugoscWysiegnikaField.setText("");dzwigMaxWysokoscPodnoszeniaField.setText("");dzwigTypField.setText("");
                wywrotkaLadownoscField.setText("");wywrotkaTypNapeduField.setText("");wywrotkaPojemnoscSkrzyniField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Błąd formatu liczby: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd dodawania pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void usunPojazdAction(ActionEvent e) {
        String idPojazdu = pojazdIdDoUsunieciaField.getText().trim();
        if (idPojazdu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Proszę podać ID pojazdu do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz usunąć pojazd o ID: " + idPojazdu + "?",
                "Potwierdzenie usunięcia",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            try {
                // Zakładamy, że serwis będzie miał metodę do usuwania pojazdu
                // lub repozytorium będzie miało metodę usun(String id)
                Pojazd pojazdDoUsuniecia = serwis.getRepoPojazdow().znajdzWgId(idPojazdu);
                if (pojazdDoUsuniecia != null) {
                    // Dodatkowe sprawdzenie: czy pojazd nie jest aktualnie wypożyczony?
                    // To wymagałoby sprawdzenia w repozytorium wypożyczeń.
                    // Na razie uproszczone usuwanie.
                    serwis.getRepoPojazdow().usun(idPojazdu); // Zakładając, że repo ma taką metodę
                    refreshPojazdyArea();
                    pojazdIdDoUsunieciaField.setText(""); // Wyczyść pole
                    JOptionPane.showMessageDialog(this, "Pojazd o ID: " + idPojazdu + " został usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono pojazdu o ID: " + idPojazdu, "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd podczas usuwania pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    // --- ZAKŁADKA KLIENCI ---
    private void createKlienciPanel() {
        klienciPanel = new JPanel(new BorderLayout(5,5));
        klienciTextArea = new JTextArea(20,70);
        klienciTextArea.setEditable(false);
        JScrollPane scrollKlienci = new JScrollPane(klienciTextArea);
        klienciPanel.add(scrollKlienci, BorderLayout.CENTER);

        // Zmieniony inputPanel dla klientów z GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5); // Marginesy (góra, lewo, dół, prawo)
        gbc.anchor = GridBagConstraints.WEST; // Wyrównanie komponentów do lewej
        gbc.fill = GridBagConstraints.HORIZONTAL;

        inputPanel.setBorder(BorderFactory.createTitledBorder("Zarządzaj Klientami"));

        int row = 0; // Licznik wierszy dla GridBagLayout

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Nazwa Firmy:"), gbc);
        klientNazwaFirmyField = new JTextField(15); // Zmniejszona szerokość
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientNazwaFirmyField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("NIP:"), gbc);
        klientNipField = new JTextField(12); // Zmniejszona szerokość
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientNipField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Imię (kontakt):"), gbc);
        klientImieKontaktField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientImieKontaktField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Nazwisko (kontakt):"), gbc);
        klientNazwiskoKontaktField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientNazwiskoKontaktField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Telefon:"), gbc);
        klientTelefonField = new JTextField(10); // Zmniejszona szerokość
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientTelefonField, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Email:"), gbc);
        klientEmailField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(klientEmailField, gbc);
        row++;

        JButton dodajKlientaButton = new JButton("Dodaj Klienta");
        dodajKlientaButton.addActionListener(this::dodajKlientaAction);
        // Ustawienia GridBagConstraints dla przycisku, aby go wyśrodkować
        gbc.gridx = 0; // Zacznij od pierwszej kolumny
        gbc.gridy = row; // W bieżącym, nowym wierszu
        gbc.gridwidth = 2; // Przycisk obejmie 2 kolumny (zakładając, że formularz ma 2 kolumny: etykiety i pola)
        gbc.anchor = GridBagConstraints.CENTER; // WYŚRODKUJ komponent w jego przydzielonej przestrzeni
        gbc.fill = GridBagConstraints.NONE;     // NIE rozciągaj przycisku, użyj jego preferowanego rozmiaru
        gbc.insets = new Insets(10, 0, 10, 0); // Opcjonalnie: dodaj trochę marginesu górnego/dolnego
        inputPanel.add(dodajKlientaButton, gbc);
        row++; // Inkrementuj wiersz dla kolejnych elementów
        // Resetuj ustawienia gbc dla kolejnych komponentów, jeśli będą dodawane do tego samego inputPanel
        gbc.gridwidth = 1; // Wróć do zajmowania jednej kolumny
        gbc.anchor = GridBagConstraints.WEST; // Wyrównaj do lewej (lub inne domyślne)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Domyślnie pola tekstowe mogą się rozciągać
        gbc.insets = new Insets(2,2,2,2); // Wróć do standardowych marginesów   
        // Dodanie separatora
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
        row++;
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        // Pola do usuwania klienta - dodane do istniejącego inputPanel
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("ID Klienta do Usunięcia:"), gbc);
        klientIdDoUsunieciaField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL; inputPanel.add(klientIdDoUsunieciaField, gbc);
        row++;

        JButton usunKlientaButton = new JButton("Usuń Wybranego Klienta");
        usunKlientaButton.addActionListener(this::usunKlientaAction); // Metoda akcji z poprzedniej odpowiedzi
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(usunKlientaButton, gbc);
        row++;
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Dodajemy pusty komponent na dole, aby wypchnąć formularz do góry
        gbc.gridx = 0; gbc.gridy = row + 1; gbc.weighty = 1.0;
        inputPanel.add(new JLabel(""), gbc);
        

        klienciPanel.add(inputPanel, BorderLayout.EAST);

        JButton odswiezKlientowButton = new JButton("Odśwież Listę Klientów");
        odswiezKlientowButton.addActionListener(e -> refreshKlienciArea());
        klienciPanel.add(odswiezKlientowButton, BorderLayout.SOUTH);
    }

    private void dodajKlientaAction(ActionEvent e) {
        String nazwaFirmy = klientNazwaFirmyField.getText().trim();
        String nip = klientNipField.getText().trim();
        String imieKontakt = klientImieKontaktField.getText().trim();
        String nazwiskoKontakt = klientNazwiskoKontaktField.getText().trim();
        String telefonKontakt = klientTelefonField.getText().trim();
        String emailKontakt = klientEmailField.getText().trim();

        if (nazwaFirmy.isEmpty() || nip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nazwa firmy i NIP są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (imieKontakt.isEmpty() || nazwiskoKontakt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Imię i nazwisko osoby kontaktowej są wymagane.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Klient istniejacyKlientZNipem = serwis.getRepoKlientow().znajdzWgNIP(nip);

            if (istniejacyKlientZNipem != null) {
                // NIP istnieje, zapytaj użytkownika
                int response = JOptionPane.showConfirmDialog(this,
                        "Firma o NIP " + nip + " ('" + istniejacyKlientZNipem.getNazwaFirmy() + "') już istnieje.\n" +
                        "Czy chcesz zaktualizować dane osoby kontaktowej dla tej firmy?\n" +
                        "Obecna osoba kontaktowa: " + istniejacyKlientZNipem.getImieKontakt() + " " + istniejacyKlientZNipem.getNazwiskoKontakt(),
                        "NIP istnieje",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Użytkownik chce zaktualizować osobę kontaktową
                    serwis.dodajLubAktualizujKlienta(
                            istniejacyKlientZNipem.getNazwaFirmy(), // Użyj istniejącej nazwy firmy
                            nip,
                            imieKontakt, nazwiskoKontakt, telefonKontakt, emailKontakt,
                            true // Tak, aktualizuj istniejącego
                    );
                    JOptionPane.showMessageDialog(this, "Dane kontaktowe dla firmy '" + istniejacyKlientZNipem.getNazwaFirmy() + "' zostały zaktualizowane.");
                } else {
                    // Użytkownik nie chce aktualizować, można wyczyścić pola lub nic nie robić
                    JOptionPane.showMessageDialog(this, "Nie podjęto akcji dla istniejącej firmy.");
                    return; // Zakończ, aby nie dodawać nowego klienta
                }
            } else {
                // NIP nie istnieje, dodaj nowego klienta
                serwis.dodajLubAktualizujKlienta(
                        nazwaFirmy, nip,
                        imieKontakt, nazwiskoKontakt, telefonKontakt, emailKontakt,
                        false // Nie, to nowy klient (chociaż w tym przypadku nie ma to znaczenia, bo istniejacyKlient jest null)
                );
                JOptionPane.showMessageDialog(this, "Dodano nowego klienta: " + nazwaFirmy);
            }

            refreshKlienciArea();
            // Wyczyść pola formularza
            klientNazwaFirmyField.setText("");
            klientNipField.setText("");
            klientImieKontaktField.setText("");
            klientNazwiskoKontaktField.setText("");
            klientTelefonField.setText("");
            klientEmailField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd: " + ex.getMessage(), "Błąd operacji", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
     private void usunKlientaAction(ActionEvent e) {
        String idText = klientIdDoUsunieciaField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Proszę podać ID klienta do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idKlienta = Integer.parseInt(idText);

            int response = JOptionPane.showConfirmDialog(this,
                    "Czy na pewno chcesz usunąć klienta o ID: " + idKlienta + "?\n" +
                    "UWAGA: Sprawdź, czy klient nie ma aktywnych wypożyczeń!",
                    "Potwierdzenie usunięcia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                Klient klientDoUsuniecia = serwis.getRepoKlientow().znajdzWgId(idKlienta);
                if (klientDoUsuniecia != null) {
                    // UWAGA: Tutaj powinna być logika sprawdzająca, czy klient nie ma powiązanych,
                    // aktywnych wypożyczeń. Jeśli ma, usuwanie powinno być zablokowane lub
                    // powinna być opcja "miękkiego usunięcia" (np. oznaczenie jako nieaktywny).
                    // Na potrzeby tego przykładu wykonujemy twarde usunięcie.
                    boolean czyMaAktywneWypozyczenia = false;
                    if (serwis.getRepoWypozyczen() != null) {
                        List<Wypozyczenie> wypozyczeniaKlienta = serwis.getRepoWypozyczen().znajdzWgKlienta(klientDoUsuniecia);
                        for (Wypozyczenie wyp : wypozyczeniaKlienta) {
                            if (!wyp.isCzyZakonczone()) {
                                czyMaAktywneWypozyczenia = true;
                                break;
                            }
                        }
                    }

                    if (czyMaAktywneWypozyczenia) {
                        JOptionPane.showMessageDialog(this,
                                "Klient o ID: " + idKlienta + " ma aktywne wypożyczenia i nie może zostać usunięty.",
                                "Błąd usuwania", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    serwis.getRepoKlientow().usun(idKlienta); // Zakładając, że repo ma metodę usun(int id)
                    refreshKlienciArea();
                    klientIdDoUsunieciaField.setText("");
                    JOptionPane.showMessageDialog(this, "Klient o ID: " + idKlienta + " został usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono klienta o ID: " + idKlienta, "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID klienta musi być liczbą.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas usuwania klienta: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
     
    private void populateKlientComboBox() {
        wypozyczenieKlientComboBox.removeAllItems(); // Wyczyść poprzednie elementy
        List<Klient> klienci = serwis.getRepoKlientow().znajdzWszystkie();
        if (klienci.isEmpty()) {
            wypozyczenieKlientComboBox.addItem(new KlientWrapper(null)); // Placeholder
        } else {
            wypozyczenieKlientComboBox.addItem(new KlientWrapper(null)); // Dodaj opcję "Wybierz klienta"
            for (Klient k : klienci) {
                wypozyczenieKlientComboBox.addItem(new KlientWrapper(k));
            }
        }
    }

    // --- ZAKŁADKA WYPOŻYCZENIA ---
    private void createWypozyczeniaPanel() {
        wypozyczeniaPanel = new JPanel(new BorderLayout(5,5));
        // Jeśli używasz JTable dla wypożyczeń, zachowaj JTable
        wypozyczeniaTextArea = new JTextArea(15,70);
        wypozyczeniaTextArea.setEditable(false);
        JScrollPane scrollWypozyczenia = new JScrollPane(wypozyczeniaTextArea);
        wypozyczeniaPanel.add(scrollWypozyczenia, BorderLayout.CENTER);

        JPanel actionsOuterPanel = new JPanel(new BorderLayout());
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Panel Rejestracji Wypożyczenia
        JPanel rejestracjaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(3, 5, 3, 5);
        gbcReg.anchor = GridBagConstraints.WEST;
        gbcReg.fill = GridBagConstraints.HORIZONTAL; // Aby JDateChooser wypełnił szerokość
        rejestracjaPanel.setBorder(BorderFactory.createTitledBorder("Zarejestruj Nowe Wypożyczenie"));
        int regRow = 0;

        gbcReg.gridx = 0; gbcReg.gridy = regRow; rejestracjaPanel.add(new JLabel("Klient:"), gbcReg);
        wypozyczenieKlientComboBox = new JComboBox<>(); // Inicjalizacja JComboBox
        gbcReg.gridx = 1; gbcReg.gridy = regRow; rejestracjaPanel.add(wypozyczenieKlientComboBox, gbcReg);
        regRow++;

        gbcReg.gridx = 0; gbcReg.gridy = regRow; rejestracjaPanel.add(new JLabel("Numer Rejestracyjny:"), gbcReg);
        wypozyczeniePojazdIdField = new JTextField(10);
        gbcReg.gridx = 1; gbcReg.gridy = regRow; rejestracjaPanel.add(wypozyczeniePojazdIdField, gbcReg);
        regRow++;

        gbcReg.gridx = 0; gbcReg.gridy = regRow; rejestracjaPanel.add(new JLabel("Data Od:"), gbcReg);
        wypozyczenieDataOdChooser = new JDateChooser();
        wypozyczenieDataOdChooser.setDateFormatString("dd-MM-yyyy"); // Ustaw format wyświetlania
        gbcReg.gridx = 1; gbcReg.gridy = regRow; rejestracjaPanel.add(wypozyczenieDataOdChooser, gbcReg);
        regRow++;

        gbcReg.gridx = 0; gbcReg.gridy = regRow; rejestracjaPanel.add(new JLabel("Data Do:"), gbcReg);
        wypozyczenieDataDoChooser = new JDateChooser();
        wypozyczenieDataDoChooser.setDateFormatString("dd-MM-yyyy");
        gbcReg.gridx = 1; gbcReg.gridy = regRow; rejestracjaPanel.add(wypozyczenieDataDoChooser, gbcReg);
        regRow++;

        JButton zarejestrujButton = new JButton("Zarejestruj");
        zarejestrujButton.addActionListener(this::zarejestrujWypozyczenieAction);
        gbcReg.gridx = 0; gbcReg.gridy = regRow; gbcReg.gridwidth = 2; gbcReg.anchor = GridBagConstraints.CENTER; gbcReg.fill = GridBagConstraints.NONE;
        rejestracjaPanel.add(zarejestrujButton, gbcReg);
        gbcReg.gridwidth = 1; gbcReg.anchor = GridBagConstraints.WEST; // Reset

        actionsPanel.add(rejestracjaPanel);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel Zakończenia Wypożyczenia
        JPanel zakonczeniePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcZak = new GridBagConstraints();
        gbcZak.insets = new Insets(3, 5, 3, 5);
        gbcZak.anchor = GridBagConstraints.WEST;
        gbcZak.fill = GridBagConstraints.HORIZONTAL; // Aby JDateChooser wypełnił szerokość
        zakonczeniePanel.setBorder(BorderFactory.createTitledBorder("Zakończ Wypożyczenie"));
        int zakRow = 0;

        gbcZak.gridx = 0; gbcZak.gridy = zakRow; zakonczeniePanel.add(new JLabel("ID Wypożyczenia:"), gbcZak);
        wypozyczenieIdDoZakonczeniaField = new JTextField(5);
        gbcZak.gridx = 1; gbcZak.gridy = zakRow; zakonczeniePanel.add(wypozyczenieIdDoZakonczeniaField, gbcZak);
        zakRow++;

        gbcZak.gridx = 0; gbcZak.gridy = zakRow; zakonczeniePanel.add(new JLabel("Data Zwrotu:"), gbcZak);
        wypozyczenieDataZwrotuChooser = new JDateChooser();
        wypozyczenieDataZwrotuChooser.setDateFormatString("dd-MM-yyyy");
        gbcZak.gridx = 1; gbcZak.gridy = zakRow; zakonczeniePanel.add(wypozyczenieDataZwrotuChooser, gbcZak);
        zakRow++;

        JButton zakonczButton = new JButton("Zakończ");
        zakonczButton.addActionListener(this::zakonczWypozyczenieAction);
        gbcZak.gridx = 0; gbcZak.gridy = zakRow; gbcZak.gridwidth = 2; gbcZak.anchor = GridBagConstraints.CENTER; gbcZak.fill = GridBagConstraints.NONE;
        zakonczeniePanel.add(zakonczButton, gbcZak);
        gbcZak.gridwidth = 1; gbcZak.anchor = GridBagConstraints.WEST; // Reset

        actionsPanel.add(zakonczeniePanel);
        actionsPanel.add(Box.createVerticalGlue());
        actionsOuterPanel.add(actionsPanel, BorderLayout.NORTH);
        wypozyczeniaPanel.add(actionsOuterPanel, BorderLayout.EAST);
        
        JPanel czyszczeniePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        czyszczeniePanel.setBorder(BorderFactory.createTitledBorder("Zarządzanie Historią"));
        wyczyscZakonczoneButton = new JButton("Usuń Zakończone Wypożyczenia");
        wyczyscZakonczoneButton.addActionListener(this::wyczyscZakonczoneAction);
        czyszczeniePanel.add(wyczyscZakonczoneButton);
        actionsPanel.add(czyszczeniePanel);

        JButton odswiezWypozyczeniaButton = new JButton("Odśwież Listę Wypożyczeń");
        odswiezWypozyczeniaButton.addActionListener(e -> refreshWypozyczeniaArea());
        wypozyczeniaPanel.add(odswiezWypozyczeniaButton, BorderLayout.SOUTH);
        
        populateKlientComboBox();
    }

    private void zarejestrujWypozyczenieAction(ActionEvent e) {
        try {
            KlientWrapper selectedKlientWrapper = (KlientWrapper) wypozyczenieKlientComboBox.getSelectedItem();
            if (selectedKlientWrapper == null || selectedKlientWrapper.getKlient() == null) {
                JOptionPane.showMessageDialog(this, "Proszę wybrać klienta.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idKlienta = selectedKlientWrapper.getKlient().getIdKlienta(); // Pobierz ID z wrappera

            String idPojazdu = wypozyczeniePojazdIdField.getText().trim();
            Date dataOd = wypozyczenieDataOdChooser.getDate();
            Date dataDo = wypozyczenieDataDoChooser.getDate();

            if (idPojazdu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Proszę podać ID pojazdu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dataOd == null || dataDo == null) {
                JOptionPane.showMessageDialog(this, "Proszę wybrać datę Od i Do.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Wypozyczenie w = serwis.zarejestrujWypozyczenie(idKlienta, idPojazdu, dataOd, dataDo);
            if (w != null) {
                refreshWypozyczeniaArea();
                refreshPojazdyArea();
                JOptionPane.showMessageDialog(this, "Zarejestrowano wypożyczenie ID: " + w.getIdWypozyczenia() + " dla klienta: " + selectedKlientWrapper.getKlient().getNazwaFirmy());
                // wypozyczenieKlientComboBox.setSelectedIndex(0); // Resetuj wybór klienta
                wypozyczeniePojazdIdField.setText("");
                wypozyczenieDataOdChooser.setDate(null);
                wypozyczenieDataDoChooser.setDate(null);
            } else {
                JOptionPane.showMessageDialog(this, "Nie udało się zarejestrować wypożyczenia. Sprawdź dane i dostępność.", "Błąd", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) { // Nadal potrzebne dla ID Pojazdu, jeśli ktoś wpisze tekst
            JOptionPane.showMessageDialog(this, "Błędny format ID pojazdu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd rejestracji wypożyczenia: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void zakonczWypozyczenieAction(ActionEvent e) {
        try {
            int idWypozyczenia = Integer.parseInt(wypozyczenieIdDoZakonczeniaField.getText());
            Date dataZwrotu = wypozyczenieDataZwrotuChooser.getDate(); // Pobierz datę z JDateChooser

            if (dataZwrotu == null) {
                JOptionPane.showMessageDialog(this, "Proszę wybrać datę zwrotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            serwis.zakonczWypozyczenie(idWypozyczenia, dataZwrotu);
            refreshWypozyczeniaArea();
            refreshPojazdyArea();
            JOptionPane.showMessageDialog(this, "Zakończono wypożyczenie ID: " + idWypozyczenia);
            wypozyczenieIdDoZakonczeniaField.setText("");
            wypozyczenieDataZwrotuChooser.setDate(null); // Wyczyść JDateChooser

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Błędny format ID: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        // Usunęliśmy ParseException
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd kończenia wypożyczenia: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Dla debugowania
        }
    }
    
    private void wyczyscZakonczoneAction(ActionEvent e) {
        int response = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz usunąć WSZYSTKIE zakończone wypożyczenia z historii?",
                "Potwierdzenie czyszczenia historii",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            try {
                // Potrzebujemy metody w serwisie lub repozytorium do tej operacji
                int liczbaUsunietych = serwis.usunZakonczoneWypozyczenia(); // Ta metoda musi zostać dodana do serwisu

                refreshWypozyczeniaArea();
                JOptionPane.showMessageDialog(this, "Usunięto " + liczbaUsunietych + " zakończonych wypożyczeń.", "Czyszczenie zakończone", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd podczas czyszczenia zakończonych wypożyczeń: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // --- METODY ODŚWIEŻAJĄCE ---
private void refreshPojazdyArea() {
        if (pojazdyTextArea != null) {
            pojazdyTextArea.setText(""); // Wyczyść
            List<Pojazd> pojazdy = serwis.getRepoPojazdow().znajdzWszystkie();
            if (pojazdy.isEmpty()) {
                pojazdyTextArea.append("Brak pojazdów w systemie.");
            } else {
                pojazdyTextArea.append("--- WSZYSTKIE POJAZDY ---\n\n");
                for (Pojazd p : pojazdy) {
                    pojazdyTextArea.append(p.wyswietlSzczegoly() + "\n\n");
                }
            }
        }
    }

    private void refreshKlienciArea() {
        klienciTextArea.setText(""); // Wyczyść
        List<Klient> klienci = serwis.getRepoKlientow().znajdzWszystkie();
        if (klienci.isEmpty()) {
            klienciTextArea.append("Brak klientów w systemie.");
        } else {
            klienciTextArea.append("--- WSZYSCY KLIENCI ---\n\n");
            for (Klient k : klienci) {
                klienciTextArea.append(k.toString() + "\n\n");
            }
        }
        populateKlientComboBox();
    }

    private void refreshWypozyczeniaArea() {
        wypozyczeniaTextArea.setText(""); // Wyczyść
        List<Wypozyczenie> wypozyczenia = serwis.getRepoWypozyczen().znajdzWszystkie();
        if (wypozyczenia.isEmpty()) {
            wypozyczeniaTextArea.append("Brak zarejestrowanych wypożyczeń.");
        } else {
            wypozyczeniaTextArea.append("--- WSZYSTKIE WYPOŻYCZENIA ---\n\n");
            for (Wypozyczenie w : wypozyczenia) {
                wypozyczeniaTextArea.append(w.toString() + "\n\n");
            }
        }
    }

    private void loadInitialData() {
        refreshPojazdyArea();
        refreshKlienciArea();
        refreshWypozyczeniaArea();
        populateKlientComboBox();
    }
}