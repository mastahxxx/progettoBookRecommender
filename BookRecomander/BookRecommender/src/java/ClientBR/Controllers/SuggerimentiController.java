// da visitare
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
    private static final String USERID = SceneNavigator.getUserID();;

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
        if (USERID == null) {
            SceneNavigator.logout();
            return;
        }

        caricaLibri(USERID);
        cbLibro.setItems(mieiLibri);

        lvDisponibili.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvSelezionati.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        
        cbLibro.valueProperty().addListener(this::onLibroChanged);
        

        lvDisponibili.setItems(disponibili);
        lvSelezionati.setItems(selezionati);

        ricalcolaDisponibli();
        refreshUI();
    }
    
    private boolean controlloLibroCorrente() {
    	Libro lib = cbLibro.getValue();
    	UtenteRegistrato ur = new UtenteRegistrato();
        ur.setUserId(USERID);
        boolean ok = false;
    	try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            out.writeObject("");
            out.writeObject(lib);
            ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(1);
        }
    	return ok;
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
        Libro lib = cbLibro.getValue();
        if (lib == null) {
            Helpers.showError("seleziona un libro dalla libreria", lblErr);
            return;
        }
        
        if (selezionati.isEmpty()) {
            Helpers.showError("seleziona almeno un suggerimento", lblErr);
            return;
        }

        boolean ok = false;
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
            List<Libro> normalList = new LinkedList<Libro>(selezionati);
            out.writeObject(normalList);
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
    }

    /**
     * Carica i libri dell’utente e resetta le liste locali.
     * @param userId identificativo dell’utente
     */
    private void caricaLibri(String userId) {
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
        mieiLibri.clear();
        disponibili.clear();
        selezionati.clear();
        ultimoLibro = null;
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
            caricaSuggeriti(lib);
            
            
            
            if (lib != null && lib.getLibriConsigliati() != null) {
                for (Libro l : lib.getLibriConsigliati()) {
                	try {
                        InetAddress addr = InetAddress.getByName(null);
                        Socket socket = new Socket(addr, 8999);
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
                        UtenteRegistrato ur = new UtenteRegistrato();
                        ur.setUserId(USERID);
                        out.writeObject("RIEMPI SUGGERITI");
                        out.writeObject(ur);
                        out.writeObject(l);
                        List<Libro> normalList = new LinkedList<Libro>(selezionati);
                        normalList = (List<Libro>) in.readObject();
                        selezionati = FXCollections.observableArrayList(normalList);
                        out.close();
                        in.close();
                        socket.close();
                    } catch (Exception e) {
                        System.out.println(1);
                    }
                    if (selezionati.size() >= LIMITE) {
                    	Helpers.showError("Hai già effettuato i suggerimenti per il libro");
                    	break;
                    }
                    if (!selezionati.contains(l)) selezionati.add(l);
                }
            }
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
    private void caricaSuggeriti(Libro l) {
    	try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setUserId(USERID);
            out.writeObject("RIEMPI SUGGERITI");
            out.writeObject(ur);
            out.writeObject(l);
            List<Libro> normalList = new LinkedList<Libro>(selezionati);
            normalList = (List<Libro>) in.readObject();
            selezionati = FXCollections.observableArrayList(normalList);
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(1);
        }
    }

    /** Aggiorna stato pulsante Salva (testo e abilitazione). */
    private void refreshUI() {
        btnSalva.setText("Salva (" + selezionati.size() + "/" + LIMITE + ")");
        btnSalva.setDisable(cbLibro.getValue() == null || selezionati.isEmpty());
    }
}
