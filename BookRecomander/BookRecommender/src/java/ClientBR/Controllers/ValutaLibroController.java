/**
 * @author Adrian Gabriel Soare n: 749483
 * @author Matteo Sorrentino n: 753775
 * 
 * 
 * 
 */
package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ClassiCondivise.UtenteRegistrato;
import ClassiCondivise.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controller per la valutazione di un libro.
 * <p>Gestisce voti (1–5), note per categoria e salvataggio su db.</p>
 */
public class ValutaLibroController {

    // ---- UI ----
    @FXML private ComboBox<Libro>   cbLibro;
    @FXML private ComboBox<Integer> cbContenuto;
    @FXML private ComboBox<Integer> cbStile;
    @FXML private ComboBox<Integer> cbGradevolezza;
    @FXML private ComboBox<Integer> cbOriginalita;
    @FXML private ComboBox<Integer> cbEdizione;
    @FXML private TextArea taContenuto;
    @FXML private TextArea taStile;
    @FXML private TextArea taGradevolezza;
    @FXML private TextArea taOriginalita;
    @FXML private TextArea taEdizione;
    @FXML private Button btnSalva;
    @FXML private Label lblErr;
    @FXML private TextField tfVotoFinale;

    /** Libri dell'utente caricati dal db. */
    private ObservableList<Libro> mieiLibri = FXCollections.observableArrayList();
    private LinkedList<Libro> libri = new LinkedList<>();

    /** Inizializza combo, listener, limiti note e stato pulsante. */
    @FXML
    private void initialize() {
        Helpers.clearError(lblErr);
        libri = caricaMieiLibri(SceneNavigator.getUserID());
        mieiLibri.setAll(libri);
        System.out.println(mieiLibri);

        cbLibro.setItems(mieiLibri);
        cbLibro.valueProperty().addListener(this::onLibroChanged);

        var voti = FXCollections.observableArrayList(1,2,3,4,5);
        cbContenuto.setItems(voti); cbContenuto.getSelectionModel().clearSelection();
        cbStile.setItems(voti);     cbStile.getSelectionModel().clearSelection();
        cbGradevolezza.setItems(voti); cbGradevolezza.getSelectionModel().clearSelection();
        cbOriginalita.setItems(voti);  cbOriginalita.getSelectionModel().clearSelection();
        cbEdizione.setItems(voti);     cbEdizione.getSelectionModel().clearSelection();

        cbContenuto.valueProperty().addListener(this::onVotoChanged);
        cbStile.valueProperty().addListener(this::onVotoChanged);
        cbGradevolezza.valueProperty().addListener(this::onVotoChanged);
        cbOriginalita.valueProperty().addListener(this::onVotoChanged);
        cbEdizione.valueProperty().addListener(this::onVotoChanged);

        maxLen(taContenuto);
        maxLen(taStile);
        maxLen(taGradevolezza);
        maxLen(taOriginalita);
        maxLen(taEdizione);

        tfVotoFinale.setText("");
        btnSalva.setDisable(true);
    }

    /** Logout dell’utente. */
    @FXML private void onLogout() { SceneNavigator.logout(); }

    /** Torna all’area utente. */
    @FXML private void onAnnulla() { SceneNavigator.switchToUtenteRegistrato(); }

    /** reset UI, ricalcolo media e bottone salva. */
    private void onLibroChanged(javafx.beans.value.ObservableValue<? extends Libro> obs,
                                Libro oldV, Libro newV) {
        onLibroChanged();
    }

    /** Reset campi e ricalcoli quando cambia il libro. */
    private void onLibroChanged() {
        Helpers.clearError(lblErr);
        refreshUI();
        aggiornaVotoFinale();
        aggiornaBtnSalva();
    }

    /**
     * Salva voti e note nel libro selezionato e invia al db.
     */
    @FXML
    private void onSalva() {
        Helpers.clearError(lblErr);
        Libro l = cbLibro.getValue();

        if (l == null) {
            Helpers.showError("Seleziona un libro da valutare.", lblErr);
            return;
        }
        if (!tuttiVotiPresenti()) {
            Helpers.showError("Completa tutti i voti (1–5) prima di salvare.", lblErr);
            return;
        }

        // salva voti nel model
        l.setContenuto(cbContenuto.getValue());
        l.setStile(cbStile.getValue());
        l.setGradevolezza(cbGradevolezza.getValue());
        l.setOriginalita(cbOriginalita.getValue());
        l.setEdizione(cbEdizione.getValue());

        // salva note (se presenti)
        String autore = SceneNavigator.getUserID();
        if (!isBlank(taContenuto.getText()))    l.setNoteContenuto(taContenuto.getText().trim());
        if (!isBlank(taStile.getText()))        l.setNoteStile(taStile.getText().trim());
        if (!isBlank(taGradevolezza.getText())) l.setNoteGradevolezza(taGradevolezza.getText().trim());
        if (!isBlank(taOriginalita.getText()))  l.setNoteOriginalita(taOriginalita.getText().trim());
        if (!isBlank(taEdizione.getText()))     l.setNoteEdizione(taEdizione.getText().trim());

        // invio al db
        boolean ok = false;
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            out.writeObject("INSETISCI VALUTAZIONE");
            out.writeObject(l);
            System.out.println(l.getNoteContenuto() + l.getNoteEdizione());
            UtenteRegistrato u = new UtenteRegistrato();
            u.setUserId(autore);
            out.writeObject(u);
            ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(1);
        }
        if (ok) {
            Helpers.showInfo("Valutazione salvata!", lblErr);
            SceneNavigator.switchToUtenteRegistrato();
            

        }
    }

    /** Aggiorna media e stato del pulsante Salva. */
    private void onVotoChanged(javafx.beans.value.ObservableValue<? extends Integer> obs,
                               Integer oldV, Integer newV) {
        aggiornaVotoFinale();
        aggiornaBtnSalva();
    }

    /** Calcola la media (se tutti i voti sono presenti) e la mostra nel campo dedicato. */
    private void aggiornaVotoFinale() {
        if (!tuttiVotiPresenti()) {
            tfVotoFinale.clear();
            return;
        }
        Integer media = (cbContenuto.getValue() + cbStile.getValue()
                + cbGradevolezza.getValue() + cbOriginalita.getValue() + cbEdizione.getValue()) / 5;
        tfVotoFinale.setText(media.toString());
    }

    /** Abilita il bottone Salva solo se libro e tutti i voti sono presenti. */
    private void aggiornaBtnSalva() {
        boolean ok = cbLibro.getValue() != null && tuttiVotiPresenti();
        btnSalva.setDisable(!ok);
    }

    /** Verifica che i 5 voti siano stati impostati. */
    private boolean tuttiVotiPresenti() {
        return cbContenuto.getValue() != null &&
               cbStile.getValue() != null &&
               cbGradevolezza.getValue() != null &&
               cbOriginalita.getValue() != null &&
               cbEdizione.getValue() != null;
    }

    /** Ripulisce note e resetta le combobox voto. */
    private void refreshUI() {
        taContenuto.clear();
        taStile.clear();
        taGradevolezza.clear();
        taOriginalita.clear();
        taEdizione.clear();
        cbContenuto.getSelectionModel().clearSelection();
        cbStile.getSelectionModel().clearSelection();
        cbGradevolezza.getSelectionModel().clearSelection();
        cbOriginalita.getSelectionModel().clearSelection();
        cbEdizione.getSelectionModel().clearSelection();
    }

    /** Limita TextArea a 256 caratteri. */
    private void onTextChanged(javafx.beans.value.ObservableValue<? extends String> obs,
                               String oldV, String newV) {
        if (newV != null && newV.length() > 256) {
            ((javafx.scene.control.TextArea)((javafx.beans.property.StringProperty)obs).getBean())
                .setText(newV.substring(0, 256));
        }
    }

    /** Aggiunge listener di limite caratteri a una TextArea. */
    private void maxLen(TextArea ta) { ta.textProperty().addListener(this::onTextChanged); }

    /** True se stringa nulla o vuota. */
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    /**
     * Carica i libri dell’utente dal backend e resetta la lista locale.
     * @param userId identificativo utente
     */
    private LinkedList<Libro> caricaMieiLibri(String userId) {
        LinkedList<Libro> prova = new LinkedList<>();
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setUserId(SceneNavigator.getUserID());
            out.writeObject("CARICA LIBRI LIBRERIE CLIENT");
            out.writeObject(ur);
            prova = (LinkedList<Libro> ) in.readObject();
            System.out.println("prova" + prova);

            out.close();
            in.close();
            socket.close();

        } catch (Exception e) {
           System.out.println(1);
        }
        return prova;
    }
}
