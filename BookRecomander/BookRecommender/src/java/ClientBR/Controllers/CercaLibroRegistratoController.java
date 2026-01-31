package ClientBR.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import ClientBR.SceneNavigator;
import ClassiCondivise.Libro;

import java.util.List;
import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controller della ricerca libri per utente registrato.
 */
public class CercaLibroRegistratoController { // classe quasi identica a CercaLibro, con logout/indietro

    /** Campo: titolo da cercare. */
    @FXML private TextField fTitolo;
    /** Campo: autore da cercare. */
    @FXML private TextField fAutore;
    /** Campo: anno di pubblicazione da cercare. */
    @FXML private TextField fAnno;
    @FXML private Button btnCerca;
    @FXML private Button btnPulisci;
    @FXML private TableView<Libro> tblView;
    @FXML private TableColumn<Libro, String> tTitolo;
    @FXML private TableColumn<Libro, String> tAutore;
    @FXML private TableColumn<Libro, String> tAnno;


    /** Risultati mostrati nella tabella. */
    private final ObservableList<Libro> risultati = FXCollections.observableArrayList();

    /**
     * Inizializza tabella e listener UI.
     */
    @FXML
    private void initialize() {
        tTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        tAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        tblView.setItems(risultati);
        tblView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblView.setPlaceholder(new Label("Nessun risultato"));

        // doppio click per aprire il libro
        tblView.setOnMouseClicked(this::clickTabella);
    }

    /**
     * Doppio click su riga: apre il dettaglio.
     * @param e evento mouse
     */
    private void clickTabella(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            apriInfoLibro();
        }
    }

    /**
     * Esegue la ricerca: legge i campi, interroga il backend e aggiorna la tabella.
     * <p>Mostra messaggi in {@code lblErr} e disabilita il pulsante durante la ricerca.</p>
     */
    @FXML
    private void onCerca() {
        String titolo = fTitolo.getText().trim();
        String autore = fAutore.getText().trim();
        String anno   = fAnno.getText().trim();


        btnCerca.setDisable(true);
        tblView.setPlaceholder(new Label("Ricerca in corso..."));

        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            Libro l = new Libro();
            l.setTitolo(titolo);
            l.setAutore(autore);
            l.setAnnoPubblicazione(anno);

            out.writeObject("CERCA LIBRO");
            out.writeObject(l);

            @SuppressWarnings("unchecked")
            List<Libro> res = (List<Libro>) in.readObject();

            risultati.setAll(res);
            if (res.isEmpty()) {
                tblView.setPlaceholder(new Label("Nessun risultato..."));
            }

            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            tblView.setPlaceholder(new Label("Errore durante la ricerca"));
            Helpers.showError("Errore durante la ricerca");
            System.exit(1); 
        } finally {
            btnCerca.setDisable(false);
        }
    }

    /** Pulisce i campi e svuota la tabella. */
    @FXML
    private void onPulisci() {
        Helpers.clearFields(fTitolo, fAutore, fAnno);
        risultati.clear();
        tblView.setPlaceholder(new Label(""));
    }

    /** Effettua il logout e torna alla schermata iniziale. */
    @FXML
    private void onLogout() {
        SceneNavigator.logout();
    }

    /** Torna alla schermata dell'utente registrato. */
    @FXML
    private void onIndietro() {
        SceneNavigator.switchToUtenteRegistrato();
    }

    /**
     * Apre la scena visualiizza libro, se presente.
     */
    private void apriInfoLibro() {
        Libro sel = tblView.getSelectionModel().getSelectedItem();
        if (sel == null) { return; }
        SceneNavigator.setLibro(sel);
        SceneNavigator.switchToVisualizzaLibroRegistrato();
    }
}
