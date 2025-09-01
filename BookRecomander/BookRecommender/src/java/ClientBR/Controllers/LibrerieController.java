package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.Optional;

/**
 * Controller per la gestione dell'elenco librerie di un utente registrato.
 * <p>Consente creazione, rinomina, eliminazione e apertura di una libreria.</p>
 */
public class LibrerieController {

    // ---- UI ----
    @FXML private TableView<Libreria> tblLibrerie;
    @FXML private TableColumn<Libreria, String> tNome;
    @FXML private TableColumn<Libreria, String> tNumero;
    @FXML private Button btnCrea;
    @FXML private Button btnRinomina;
    @FXML private Button btnElimina;
    @FXML private Button btnIndietro;
    @FXML private Button btnLogout;
    @FXML private Label lblErr;

    /** Modello dati visualizzato nella tabella. */
    private final ObservableList<Libreria> librerie = FXCollections.observableArrayList();

    /** UserID corrente (fornito da SceneNavigator). */
    private static String userId;

    /** Lunghezza massima consentita per il nome della libreria. */
    private static final int MAX_NAME = 40;

    /**
     * Inizializza tabella, creazione colonne e listener UI.
     * <p>Carica le librerie dell'utente e aggiorna lo stato dei bottoni.</p>
     */
    @FXML private void initialize() {
        userId = SceneNavigator.getUserID();

        tNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tNumero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Libreria, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Libreria, String> cdf) {
                int size = cdf.getValue() != null && cdf.getValue().getContenuto() != null
                        ? cdf.getValue().getContenuto().size() : 0;
                return new SimpleStringProperty(String.valueOf(size));
            }
        });

        tblLibrerie.setItems(librerie);
        tblLibrerie.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblLibrerie.setPlaceholder(new Label("Nessuna libreria"));
        tblLibrerie.setOnMouseClicked(this::onTableClick);
        tblLibrerie.getSelectionModel().selectedItemProperty().addListener(this::onSelezioneCambiata);

        caricaLibrerie(SceneNavigator.getUserID());
        refreshUI();
    }

    /**
     * Doppio click: apre la libreria selezionata.
     * @param e evento mouse
     */
    private void onTableClick(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            apriSelezionata();
        }
    }

    /** Aggiorna lo stato dei bottoni quando cambia la selezione. */
    private void onSelezioneCambiata(ObservableValue<? extends Libreria> obs, Libreria oldV, Libreria newV) {
        refreshUI();
    }

    /**
     * Crea una nuova libreria chiedendo il nome all'utente.
     * <p>Valida nome, controlla duplicati e aggiunge alla lista.</p>
     */
    @FXML private void onCrea() {
        Helpers.clearError(lblErr);

        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Crea nuova libreria");
        dlg.setHeaderText(null);
        dlg.setContentText("Nome libreria:");
        Optional<String> result = dlg.showAndWait();
        if (!result.isPresent()) return;

        String nome = result.get().trim().replaceAll("\\s+", " ");
        if (!nomeValido(nome)) {
            Helpers.showError("Nome non valido (1–" + MAX_NAME + " caratteri).", lblErr);
            return;
        }
        if (esisteNome(nome)) {
            Helpers.showError("Esiste già una libreria con questo nome.", lblErr);
            return;
        }

        Libreria nuova = new Libreria(nome);
        boolean ok = false;
        try {
            // --- STUB chiamata al server ---
            ok = true;
        } catch (Exception e) {
            // ignorato nello stub
        }
        if (ok) {
            librerie.add(nuova);
            tblLibrerie.getSelectionModel().select(nuova);
            Helpers.showInfo("Libreria creata.", lblErr);
            refreshUI();
        }
    }

    /**
     * Rinomina la libreria selezionata.
     * <p>Evita nomi vuoti/duplicati e aggiorna la tabella (stub DB).</p>
     */
    @FXML private void onRinomina() {
        Helpers.clearError(lblErr);
        Libreria sel = tblLibrerie.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Helpers.showError("Seleziona una libreria da rinominare.", lblErr);
            return;
        }

        String nomeAttuale = sel.getNome();
        TextInputDialog dlg = new TextInputDialog(nomeAttuale != null ? nomeAttuale : "");
        dlg.setTitle("Rinomina libreria");
        dlg.setHeaderText(null);
        dlg.setContentText("Nuovo nome:");
        Optional<String> result = dlg.showAndWait();
        if (!result.isPresent()) return;

        String nuovo = result.get().trim().replaceAll("\\s+", " ");
        if (nomeAttuale != null && nuovo.equals(nomeAttuale)) return;

        if (!nomeValido(nuovo)) {
            Helpers.showError("Nome non valido (1–" + MAX_NAME + " caratteri).", lblErr);
            return;
        }
        if (esisteNome(nuovo)) {
            Helpers.showError("Esiste gia una libreria con questo nome.", lblErr);
            return;
        }

        sel.setNome(nuovo);
        boolean ok = false;
        try {
            // --- STUB chiamata al server ---
            ok = true;
        } catch (Exception e) {
            // ignorato nello stub
        }
        if (ok) {
            tblLibrerie.refresh();
            Helpers.showInfo("Libreria rinominata.", lblErr);
            refreshUI();
        }
    }

    /**
     * Elimina la libreria selezionata dopo conferma utente.
     * <p>Mostra numero di libri e richiede conferma (stub DB).</p>
     */
    @FXML private void onElimina() {
        Helpers.clearError(lblErr);
        Libreria sel = tblLibrerie.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Helpers.showError("Seleziona una libreria da eliminare.", lblErr);
            return;
        }
        int count = sel.getContenuto() != null ? sel.getContenuto().size() : 0;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimina libreria");
        alert.setHeaderText(null);
        alert.setContentText("Stai per eliminare \"" + sel.getNome() + "\".\nContiene: " + count +
                " libri.\n\nL'operazione non è reversibile.");
        Optional<ButtonType> conferma = alert.showAndWait();
        if (!conferma.isPresent() || conferma.get() != ButtonType.OK) return;

        boolean ok = false;
        try {
            // --- STUB chiamata al server ---
            ok = true;
        } catch (Exception e) {
            // ignorato nello stub
        }
        if (ok) {
            librerie.remove(sel);
            Helpers.showInfo("Libreria eliminata.", lblErr);
            refreshUI();
        }
    }

    /** Apre la libreria selezionata, passando l'oggetto alla schermata successiva. */
    private void apriSelezionata() {
        Libreria sel = tblLibrerie.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Helpers.showError("Seleziona una libreria da aprire.", lblErr);
            return;
        }
        SceneNavigator.setLibreria(sel);
        SceneNavigator.switchToVisualizzaLibreria();
    }

    /** Torna alla schermata dell'utente registrato. */
    @FXML private void onIndietro() {
        SceneNavigator.switchToUtenteRegistrato();
    }

    /** Esegue il logout. */
    @FXML private void onLogout() {
        SceneNavigator.logout();
    }

    /** Verifica la validità del nome. */
    private boolean nomeValido(String nome) {
        if (nome == null) return false;
        return nome.length() > 1 && nome.length() <= MAX_NAME;
    }

    /** Controlla l'esistenza di un nome libreria. */
    private boolean esisteNome(String nome) {
        for (Libreria l : librerie) {
            if (l.getNome() != null && l.getNome().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    /** Abilita/disabilita i pulsanti in base alla selezione. */
    private void refreshUI() {
        boolean hasSel = tblLibrerie.getSelectionModel().getSelectedItem() != null;
        btnRinomina.setDisable(!hasSel);
        btnElimina.setDisable(!hasSel);
    }

    /**
     * Carica (stub) le librerie dell'utente.
     * @param userId identificativo utente
     */
    private void caricaLibrerie(String userId) {
        librerie.clear();

        // STUB DI TEST
        Libreria l1 = new Libreria("Fantasy");
        Libreria l2 = new Libreria("Classici italiani");
        Libreria l3 = new Libreria("Saggistica");

        Libro f1 = new Libro(); f1.setTitolo("Il Signore degli Anelli");
        l1.getContenuto().add(f1);

        Libro f2 = new Libro(); f2.setTitolo("I Promessi Sposi");
        l2.getContenuto().add(f2);

        Libro f3 = new Libro(); f3.setTitolo("Storia della filosofia");
        l3.getContenuto().add(f3);
        l3.getContenuto().add(new Libro());

        librerie.addAll(l1, l2, l3);
    }
}
