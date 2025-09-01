package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import ClassiCondivise.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Controller per la visualizzazione dei dettagli di un libro e dei consigliati correlati.
 * <p>Mostra voti, note aggregate e consente di aprire un libro consigliato.</p>
 */
public class VisualizzaLibroController {

    @FXML private Label libroVisionato;
    @FXML private Label stile;
    @FXML private Label contenuto;
    @FXML private Label gradevolezza;
    @FXML private Label originalita;
    @FXML private Label edizione;
    @FXML private Label votoFinale;
    @FXML private TextArea note;
    @FXML private Button home;
    @FXML private Button cercaLibri;

    @FXML private TableView<Libro> tblViewConsigli;
    @FXML private TableColumn<Libro, String> tTitolo;
    @FXML private TableColumn<Libro, String> tAutore;
    @FXML private TableColumn<Libro, String> tAnno;

    /** Libro attualmente mostrato. */
    private Libro libro;
    /** Modello per la tabella dei libri consigliati. */
    private final ObservableList<Libro> consigliatiData = FXCollections.observableArrayList();

    /**
     * Inizializza costruzione colonne/tabella, recupera il libro da {@link SceneNavigator} e imposta i listener.
     */
    @FXML
    private void initialize() {
        this.libro = SceneNavigator.getLibro();
        refreshUI();

        note.setEditable(false);
        note.setWrapText(true);

        tTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        tAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        tblViewConsigli.setItems(consigliatiData);

        tblViewConsigli.setOnMouseClicked(this::clickTabella);
    }

    /** Doppio click sulla tabella per aprire il libro consigliato selezionato. */
    private void clickTabella(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            apriLibro();
        }
    }
    /** Torna alla ricerca libri. */
    @FXML private void onCercaLibri() { SceneNavigator.switchToCercaLibri(); }

    /** Torna alla Home. */
    @FXML private void onHome() { SceneNavigator.switchToHome(); }

    /**
     * Apre nei dettagli il libro selezionato dalla tabella consigliati.
     * Aggiorna l'UI con il nuovo libro.
     */
    public void apriLibro() {
        Libro sel = tblViewConsigli.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        this.libro = sel;
        refreshUI();
    }

    /** Aggiorna tutte le label, textarea e la tabella consigliati in base al libro corrente. */
    private void refreshUI() {
        if (libro == null) {
            clearUI();
            return;
        }

        libroVisionato.setText(
            Objects.toString(libro.getTitolo(), "") + " — " +
            Objects.toString(libro.getAutore(), "") + " (" +
            Objects.toString(libro.getAnnoPubblicazione(), "") + ")"
        );

        stile.setText("voto stile: " + libro.getStile());
        contenuto.setText("voto contenuto: " + libro.getContenuto());
        gradevolezza.setText("voto gradevolezza: " + libro.getGradevolezza());
        edizione.setText("voto edizione: " + libro.getEdizione());
        originalita.setText("voto originalità: " + libro.getOriginalita());

        int media = (libro.getStile() + libro.getContenuto() + libro.getGradevolezza()
                   + libro.getEdizione() + libro.getOriginalita()) / 5;
        votoFinale.setText("voto finale: " + media);

        note.setText(concatenaNote(
            libro.getNoteStile(),
            libro.getNoteContenuto(),
            libro.getNoteGradevolezza(),
            libro.getNoteOriginalità(),  // metodo con accento nel tuo model
            libro.getNoteEdizione()
        ));

        LinkedList<Libro> consigliati = libro.getLibriConsigliati();
        consigliatiData.setAll(consigliati == null ? List.of() : consigliati);
    }

    /** Ripulisce l'UI quando nessun libro è selezionato. */
    private void clearUI() {
        libroVisionato.setText("");
        stile.setText("");
        contenuto.setText("");
        gradevolezza.setText("");
        edizione.setText("");
        originalita.setText("");
        votoFinale.setText("");
        note.setText("");
        consigliatiData.clear();
    }

    /**
     * Concatena le note provenienti da più liste in un unico testo.
     * @param liste liste di note
     * @return testo aggregato
     */
    private String concatenaNote(List<String>... liste) {
        StringBuilder sb = new StringBuilder();
        for (List<String> l : liste) {
            if (l == null) continue;
            for (String n : l) {
                if (n == null || n.isBlank()) continue;
                if (sb.length() > 0) sb.append("\n---\n");
                sb.append(n.trim());
            }
        }
        return sb.toString();
    }
}
