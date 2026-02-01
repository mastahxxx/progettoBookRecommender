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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    @FXML private Button btnSalva;

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


        if(SceneNavigator.libro != null) { //Se il libro non è nulla lo salvo nella lista e lo aggiungo alla schermata
            Libro lib = SceneNavigator.libro;
            SceneNavigator.listaLibri.add(lib);
            SceneNavigator.libro = null;
        }
        
        libri.clear();
        libri.addAll(SceneNavigator.listaLibri);
        tTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        tAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        tblLibri.setItems(libri);
        tblLibri.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblLibri.setPlaceholder(new Label("Nessun libro in questa libreria"));

        // abilita/disabilita Rimuovi in base alla selezione
        tblLibri.getSelectionModel().selectedItemProperty().addListener(this::onSelezioneLibroCambiata);

        lblNomeLibreria.setText(libreriaCorrente.getNome() != null ? libreriaCorrente.getNome() : "(senza nome)");
        refreshTotale();
        refreshUI();
    }

    /** Aggiorna UI. */
    private void onSelezioneLibroCambiata(ObservableValue<? extends Libro> obs, Libro oldV, Libro newV) {
        refreshUI();
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
        SceneNavigator.switchToSalvaLibro();
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

        SceneNavigator.listaLibri.remove(sel);
        libreriaCorrente.getContenuto().remove(sel);
        libri.remove(sel);

        Helpers.showInfo("Libro rimosso.", lblErr);
        refreshTotale();
        refreshUI();
    }

    //** Salva le librerie nel db */
    @FXML
    private void onSalva() {  
        String nomelibreria = SceneNavigator.libreria.getNome();    
        LinkedList<Libro> linkedList = new LinkedList<>(SceneNavigator.listaLibri);
        Libreria l = (Libreria) SceneNavigator.libreria;
        l.setContenuto(linkedList);
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato u = new UtenteRegistrato();
            u.setUserId(SceneNavigator.userID);
            l.setContenuto(linkedList);
            out.writeObject("REGISTRA LIBRERIA");
            out.writeObject(u);
            out.writeObject(l);
            boolean ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            
        } 
        SceneNavigator.listaLibri.clear();
        SceneNavigator.libreria = null;
        SceneNavigator.switchToLibrerie();
    }

    /** Torna alla lista delle librerie. */
    @FXML private void onIndietro() { SceneNavigator.switchToUtenteRegistrato(); }

    /** Esegue il logout. */
    @FXML private void onLogout() { SceneNavigator.logout(); }

}
