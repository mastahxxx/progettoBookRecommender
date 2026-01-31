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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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
        SceneNavigator.libro = null;
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

        Libro consigliati = caricaSuggeriti();
        LinkedList<Libro> cons = consigliati.getLibriConsigliati();
        LinkedList<Libro> nuovaLista = new LinkedList();
        
        for(int i= 0; i<cons.size();i++) {
        	if(!nuovaLista.contains(cons.get(i)))
        		nuovaLista.add(cons.get(i));
        }
        consigliatiData.setAll(nuovaLista == null ? List.of() : nuovaLista);
        Libro libroConNote = caricaNote();
        note.setText(formattaNote(libroConNote));

        
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


    private Libro caricaNote() {
    try {
        InetAddress addr = InetAddress.getByName(null);
        Socket socket = new Socket(addr, 8999);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        out.writeObject("CARICA NOTE");
        out.writeObject(this.libro); // mando il libro corrente

        Libro libroConNote = (Libro) in.readObject();

        out.close();
        in.close();
        socket.close();

        return libroConNote;

    } catch (Exception e) {
        e.printStackTrace();
        return new Libro();
    }
}
    private Libro caricaSuggeriti() {
    	try {
    		InetAddress addr = InetAddress.getByName(null);
    		Socket socket = new Socket(addr, 8999);
    		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    		out.writeObject("CARICA LIBRI SUGGERITI PER VISUALIZAZZIONE");
    		out.writeObject(this.libro);
    		Libro libroConNote = (Libro) in.readObject();
    		out.close();
    		in.close();
        	socket.close();
        	return libroConNote;

    	} catch (Exception e) {
    		e.printStackTrace();
    		return new Libro();
    	}
    }

    private String formattaNote(Libro l) {
        StringBuilder sb = new StringBuilder();

        aggiungiSezioneNote(sb, "STILE", l.getListaNoteStile());
        aggiungiSezioneNote(sb, "CONTENUTO", l.getListaNoteContenuto());
        aggiungiSezioneNote(sb, "GRADEVOLEZZA", l.getListaNoteGradevolezza());
        aggiungiSezioneNote(sb, "ORIGINALITÀ", l.getListaNoteOriginalita());
        aggiungiSezioneNote(sb, "EDIZIONE", l.getListaNoteEdizione());

        if (sb.length() == 0) return "Nessuna nota per questo libro.";
            return sb.toString();
    }

    private void aggiungiSezioneNote(StringBuilder sb, String titolo, LinkedList<String> lista) {
        if (lista == null || lista.isEmpty()) return;

        sb.append("=== NOTE ").append(titolo).append(" ===\n");
            for (String nota : lista) {
                sb.append("• ").append(nota).append("\n");
            }
                sb.append("\n");
}


}
