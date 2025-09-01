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
 * Controller per la visualizzazione dei dettagli di un libro in modalità utente registrato.
 * <p>Mostra voti, note e i libri consigliati, con possibilità di navigare ad altri libri.</p>
 */
public class VisualizzaLibroUtenteRegistratoController {

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

    /** Libro attualmente visualizzato. */
    private Libro libro;
    /** Modello per la tabella dei consigliati. */
    private final ObservableList<Libro> consigliatiData = FXCollections.observableArrayList();

    /**
     * Inizializza UI, recupera il libro da {@link SceneNavigator} e imposta listener.
     */
    @FXML private void initialize() {
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

    /** Doppio click sulla tabella per aprire il libro selezionato. */
    private void clickTabella(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            apriLibro();
        }
    }


    /** Vai alla ricerca libri (utente registrato). */
    @FXML private void onCercaLibri() { SceneNavigator.switchToCercaLibri(); }

    /** Logout. */
    @FXML private void onLogout() { SceneNavigator.logout(); }

    /** Torna all’area utente registrato. */
    @FXML private void onIndietro() { SceneNavigator.switchToUtenteRegistrato(); }

    /**
     * Apre nei dettagli il libro selezionato dalla tabella consigliati
     * e aggiorna la vista.
     */
    public void apriLibro() {
        Libro sel = tblViewConsigli.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        this.libro = sel;
        refreshUI();
    }

    /** Aggiorna tutte le label, textarea e i libri consigliati in base al libro corrente. */
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
            libro.getNoteOriginalità(),
            libro.getNoteEdizione()
        ));

        LinkedList<Libro> consigliati = libro.getLibriConsigliati();
        LinkedList<Libro> nuovaLista = new LinkedList();
        for(int i= 0; i<consigliati.size();i++) {
        	if(!nuovaLista.contains(consigliati.get(i)))
        		nuovaLista.add(consigliati.get(i));
        }
        consigliatiData.setAll(nuovaLista == null ? List.of() : nuovaLista);
    }

    /** Pulisce i campi quando non c’è un libro da mostrare. */
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
     * Concatena tutte le note fornite in un’unica stringa con separatori.
     * @param liste liste di note (per categoria)
     * @return testo unico con note concatenate
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
