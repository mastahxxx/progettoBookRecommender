/**
 /**
 * @author Adrian Gabriel Soare n: 749483
 * @author Matteo Sorrentino n: 753775
 * 
 * 
 * 
 */

package ClientBR.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.LinkedList;
import java.util.List;
 
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;
import ClientBR.SceneNavigator;

import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controller per gestione dei suggerimenti.
 * <p>Permette di scegliere fino a {@value #LIMITE} libri consigliati per un libro dell'utente.</p>
 */
public class SuggerimentiController {

    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnAnnulla;
    @FXML private Button btnSalva;
    @FXML private Button btnLogout;
    @FXML private ComboBox<Libro> cbLibro;
    @FXML private ListView<Libro> lvSelezionati;
    @FXML private ListView<Libro> lvDisponibili;
    @FXML private Label lblErr;
    //** UserId corrente */
    private static final String USERID = SceneNavigator.getUserID();
    /** Numero massimo di suggerimenti consentiti per libro. */
    private static final int LIMITE = 3;
    /** Tutti i libri dell’utente (da DB). */
    private ObservableList<Libro> mieiLibri = FXCollections.observableArrayList();
    /** Libri disponibili da aggiungere come suggerimento. */
    private ObservableList<Libro> disponibili = FXCollections.observableArrayList();
    /** Libri attualmente selezionati come suggerimenti. */
    private ObservableList<Libro> selezionati = FXCollections.observableArrayList();

    /** Ultimo libro base selezionato nella combo (per ricalcoli). */
    private Libro ultimoLibro;

    /**
     * Inizializza componenti, carica i libri dell’utente e imposta i listener.
     */
    @FXML
    private void initialize() {
        LinkedList<Libro> libri = new LinkedList<>();
        if (USERID == null) {
            SceneNavigator.logout();
            return;
        }

        libri = caricaLibri(USERID);
        mieiLibri.setAll(libri);
        cbLibro.setItems(mieiLibri);


        lvDisponibili.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSelezionati.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        
        cbLibro.valueProperty().addListener(this::onLibroChanged);
        

        lvDisponibili.setItems(disponibili);
        lvSelezionati.setItems(selezionati);

        refreshUI();
    }
    

    /**
     * Ricalola liste in base al libro selezionato dal utente nella ComboBox.
     */
    private void onLibroChanged(javafx.beans.value.ObservableValue<? extends Libro> obs, Libro oldV, Libro newV) {
        ricalcolaDisponibli();
    }

    /** Esegue il logout. */
    @FXML private void onLogout() { SceneNavigator.logout(); }

    /** Torna all’area utente. */
    @FXML private void onAnnulla() { SceneNavigator.switchToUtenteRegistrato(); }

    /**
     * Aggiunge ai selezionati i libri scelti dalla lista disponibili
     * (fino al limite massimo).
     */
    @FXML
    private void onAdd() {
        if (cbLibro.getValue() == null) {
            Helpers.showError("Scegli prima il libro a cui vuoi aggiungere suggerimenti", lblErr);
            return;
        }
        List<Libro> scelti = List.copyOf(lvDisponibili.getSelectionModel().getSelectedItems());
        if (scelti.isEmpty()) return;

        for (Libro l : scelti) {
            if (selezionati.size() >= LIMITE)break;
            if (!selezionati.contains(l)) selezionati.add(l);
        }

        ricalcolaDisponibli();
        refreshUI();
        lvDisponibili.getSelectionModel().clearSelection();
    }

    /**
     * Rimuove dai selezionati i libri attualmente evidenziati.
     */
    @FXML
    private void onRemove() {
        List<Libro> scelti = List.copyOf(lvSelezionati.getSelectionModel().getSelectedItems());
        selezionati.removeAll(scelti);
        ricalcolaDisponibli();
        refreshUI();
        lvSelezionati.getSelectionModel().clearSelection();
    }

    /**
     * Salva i suggerimenti selezionati sul libro corrente, inviando l’aggiornamento al server.
     */
    @FXML
    private void onSalva() {
        Libro lib = cbLibro.getValue(); //libro corrente
        if (lib == null) {
            Helpers.showError("seleziona un libro dalla libreria", lblErr);
            return;
        }
        
        if (selezionati.isEmpty()) {
            Helpers.showError("seleziona almeno un suggerimento", lblErr);
            return;
        }

        boolean ok = false;
        LinkedList<Libro> lista = new LinkedList<>();
        lista.addAll(selezionati);
        lib.caricaContenutoSuggeritiPulito(lista);
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setUserId(USERID);
            out.writeObject("CONSIGLIA LIBRI");
            out.writeObject(ur);
            out.writeObject(lib);
            ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(1);
        }
        if (ok) {
            Helpers.showInfo("suggerimenti salvati con successo", lblErr);
        }
        refreshUI();
    }

    /**
     * Carica i libri dell’utente.
     * @param userId identificativo dell’utente
     */
    private LinkedList<Libro> caricaLibri(String userId) {
        LinkedList<Libro> prova = new LinkedList<>();
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setUserId(SceneNavigator.getUserID());
            out.writeObject("CARICA LIBRI LIBRERIE CLIENT PER SUGGERITI");
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
       // mieiLibri.clear();
       // disponibili.clear();
       // selezionati.clear();
       // ultimoLibro = null;
    }

    /**
     * Ricostruisce liste “disponibili” e “selezionati” in base al libro corrente.
     * <p>Rispetta il limite massimo di suggerimenti.</p>
     */
    private void ricalcolaDisponibli() {
        Libro lib = cbLibro.getValue();
        
        boolean cambiato = (lib != null && lib != ultimoLibro) || (lib == null && ultimoLibro != null);

        if (cambiato) {
            lblErr.setText("");
            lblErr.setStyle("");

            selezionati.clear();
            ultimoLibro = lib;
        }  
           
        disponibili.clear();
        for (Libro l : mieiLibri) {
            if (lib != null && l == lib) continue;    // esclude il libro base
            if (selezionati.contains(l)) continue;    // esclude già selezionati
            disponibili.add(l);
        }

        refreshUI();
    }

    /** Aggiorna stato pulsante Salva (testo e abilitazione). */
    private void refreshUI() {
        btnSalva.setText("Salva (" + selezionati.size() + "/" + LIMITE + ")");
        btnSalva.setDisable(cbLibro.getValue() == null || selezionati.isEmpty());
    }
}
