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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Controller della schermata di sola visualizzazione dei libri presenti in una di una libreria.
 * <p>Mostra i libri</p>
 */
public class OnlyVisLibController {

    @FXML private Label lblNomeLibreria;
    @FXML private Label lblTotale;
    @FXML private Label lblErr;
    @FXML private Button btnIndietro;
    @FXML private Button btnLogout;;
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
        SceneNavigator.libreria = null;        
        libri.addAll(libreriaCorrente.getContenuto());
        tTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        tAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        tblLibri.setItems(libri);
        tblLibri.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblLibri.setPlaceholder(new Label("Nessun libro in questa libreria"));
        lblTotale.setText(String.valueOf(libri.size()));

        lblNomeLibreria.setText(libreriaCorrente.getNome() != null ? libreriaCorrente.getNome() : "(senza nome)");
    }

    /** Torna alla lista delle librerie. */
    @FXML private void onIndietro() { SceneNavigator.switchToUtenteRegistrato(); }

    /** Esegue il logout. */
    @FXML private void onLogout() { SceneNavigator.logout(); }

}
