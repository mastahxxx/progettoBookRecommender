/**
 * @author Adrian Gabriel Soare n: 749483
 * @author Matteo Sorrentino n: 753775
 * 
 * 
 * 
 */
package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;
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
 * Controller per la visualizzazione dei dettagli di un libro e dei consigliati correlati.
 * <p>Mostra voti, note aggregate e consente di aprire un altro libro consigliato.</p>
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
        //Mostro i voti
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


    //** Carica le note dal db */
    private Libro caricaNote() {
    	try {
    		InetAddress addr = InetAddress.getByName(null);
    		Socket socket = new Socket(addr, 8999);
    		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
    		out.writeObject("CARICA NOTE");
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
    
    //** Carica suggerimenti dal db */
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

      /**
    * Formatta le note di un {@link Libro} raggruppandole per sezione.
    *
    * @param l libro di cui formattare le note
    * @return note formattate, oppure "Nessuna nota per questo libro." se assenti
    */
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

/**
 * Aggiunge una sezione di note con titolo e punti elenco, se la lista non è vuota.
 *
 * @param sb builder su cui scrivere
 * @param titolo titolo della sezione
 * @param lista note da aggiungere
 */
private void aggiungiSezioneNote(StringBuilder sb, String titolo, LinkedList<String> lista) {
    if (lista == null || lista.isEmpty()) return;

    sb.append("=== NOTE ").append(titolo).append(" ===\n");
    for (String nota : lista) {
        sb.append("• ").append(nota).append("\n");
    }
    sb.append("\n");
}


}
