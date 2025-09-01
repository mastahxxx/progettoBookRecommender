package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

/**
 * Controller della schermata di visualizzazione/modifica di una libreria.
 * <p>Mostra i libri, consente aggiunta e rimozione di elementi.</p>
 */
public class VisualizzaLibrerieController {

    @FXML private Label lblNomeLibreria;
    @FXML private Label lblTotale;
    @FXML private Label lblErr;
    @FXML private Button btnAggiungi;
    @FXML private Button btnRimuovi;
    @FXML private Button btnIndietro;
    @FXML private Button btnLogout;

    @FXML private TableView<Libro> tblLibri;
    @FXML private TableColumn<Libro, String> tTitolo;
    @FXML private TableColumn<Libro, String> tAutore;
    @FXML private TableColumn<Libro, String> tAnno;

    /** Modello dati mostrato nella tabella. */
    private final ObservableList<Libro> libri = FXCollections.observableArrayList();

    /** Libreria correntemente visualizzata. */
    private Libreria libreriaCorrente;

    /**
     * Inizializza tabella, listener e carica i libri dalla libreria corrente.
     */
    @FXML
    private void initialize() {
        libreriaCorrente = SceneNavigator.getLibreria();

        tTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        tAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        tblLibri.setItems(libri);
        tblLibri.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblLibri.setPlaceholder(new Label("Nessun libro in questa libreria"));

        // abilita/disabilita Rimuovi in base alla selezione
        tblLibri.getSelectionModel().selectedItemProperty().addListener(this::onSelezioneLibroCambiata);

        // carica dati dalla libreria
        caricaLibriDaLibreria();

        lblNomeLibreria.setText(libreriaCorrente.getNome() != null ? libreriaCorrente.getNome() : "(senza nome)");
        refreshTotale();
        refreshUI();
    }

    /** Aggiorna UI. */
    private void onSelezioneLibroCambiata(ObservableValue<? extends Libro> obs, Libro oldV, Libro newV) {
        refreshUI();
    }

    /** Copia i libri dalla libreria corrente nel modello della tabella. */
    private void caricaLibriDaLibreria() {
        libri.clear();
        if (libreriaCorrente.getContenuto() != null) {
            libri.addAll(libreriaCorrente.getContenuto());
        }
    }

    /** Aggiorna il contatore totale libri. */
    private void refreshTotale() {
        lblTotale.setText(String.valueOf(libri.size()));
    }

    /** Abilita/disabilita pulsanti in base alla selezione. */
    private void refreshUI() {
        boolean hasSel = tblLibri.getSelectionModel().getSelectedItem() != null;
        btnRimuovi.setDisable(!hasSel);
    }

    /**
     * Aggiunge un libro alla libreria chiedendo titolo, autore e anno (opzionale).
     */
    @FXML
    private void onAggiungi() {
        Helpers.clearError(lblErr);

        TextInputDialog dTitolo = new TextInputDialog();
        dTitolo.setTitle("Aggiungi libro");
        dTitolo.setHeaderText(null);
        dTitolo.setContentText("Titolo:");
        Optional<String> r1 = dTitolo.showAndWait();
        if (!r1.isPresent()) return;
        String titolo = r1.get().trim();
        if (titolo.isEmpty()) {
            Helpers.showError("Inserisci un titolo valido.", lblErr);
            return;
        }

        TextInputDialog dAutore = new TextInputDialog();
        dAutore.setTitle("Aggiungi libro");
        dAutore.setHeaderText(null);
        dAutore.setContentText("Autore:");
        Optional<String> r2 = dAutore.showAndWait();
        if (!r2.isPresent()) return;
        String autore = r2.get().trim();
        if (autore.isEmpty()) {
            Helpers.showError("Inserisci un autore valido.", lblErr);
            return;
        }

        TextInputDialog dAnno = new TextInputDialog();
        dAnno.setTitle("Aggiungi libro");
        dAnno.setHeaderText(null);
        dAnno.setContentText("Anno di pubblicazione:");
        Optional<String> r3 = dAnno.showAndWait();
        if (!r3.isPresent()) return;
        String anno = r3.get().trim();

        Libro nuovo = new Libro();
        nuovo.setTitolo(titolo);
        nuovo.setAutore(autore);
        nuovo.setAnnoPubblicazione(anno);

        libreriaCorrente.getContenuto().add(nuovo);
        libri.add(nuovo);

        Helpers.showInfo("Libro aggiunto.", lblErr);
        refreshTotale();
        refreshUI();
    }

    /**
     * Rimuove il libro selezionato dalla libreria.
     */
    @FXML
    private void onRimuovi() {
        Helpers.clearError(lblErr);

        Libro sel = tblLibri.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Helpers.showError("Seleziona un libro da rimuovere.", lblErr);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rimuovi libro");
        alert.setHeaderText(null);
        alert.setContentText("Vuoi rimuovere \"" + sel.getTitolo() + "\" dalla libreria?");
        Optional<ButtonType> conferma = alert.showAndWait();
        if (!conferma.isPresent() || conferma.get() != ButtonType.OK) return;

        libreriaCorrente.getContenuto().remove(sel);
        libri.remove(sel);

        Helpers.showInfo("Libro rimosso.", lblErr);
        refreshTotale();
        refreshUI();
    }

    /** Torna alla lista delle librerie. */
    @FXML private void onIndietro() { SceneNavigator.switchToLibrerie(); }

    /** Esegue il logout. */
    @FXML private void onLogout() { SceneNavigator.logout(); }
}
