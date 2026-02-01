/**
 * @author Matteo Sorrentino n: 753775
 */
package ServerBR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;

/**
 * Classe {@code ServerThread} che rappresenta un thread dedicato
 * alla gestione della comunicazione con un singolo client.
 *
 * <p>Per ogni client che si connette al server viene creata un'istanza
 * di {@code ServerThread}, la quale si occupa di ricevere le richieste,
 * elaborarle tramite la classe {@link DataBase} e inviare le risposte
 * al client.</p>
 *
 * <p>La comunicazione avviene tramite stream di oggetti
 * ({@link ObjectInputStream} e {@link ObjectOutputStream}).</p>
 */
public class ServerThread extends Thread {

    /** Riferimento al database utilizzato per l'elaborazione delle richieste */
    private DataBase db;

    /** Stream di input per la ricezione degli oggetti dal client */
    private ObjectInputStream in;

    /** Stream di output per l'invio degli oggetti al client */
    private ObjectOutputStream out;

    /** Socket associata alla connessione con il client */
    private Socket socket;

    /**
     * Costruttore della classe {@code ServerThread}.
     *
     * <p>Inizializza la socket di comunicazione, i flussi di input/output
     * e crea una nuova istanza della classe {@link DataBase}.</p>
     *
     * @param s socket relativa alla connessione con il client
     */
    public ServerThread(Socket s) {
        this.socket = s;
        this.db = new DataBase();
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            // Gestione semplificata dell'eccezione
        }
    }

    /**
     * Metodo principale del thread.
     *
     * <p>Il thread rimane in ascolto delle richieste inviate dal client
     * all'interno di un ciclo infinito. Ogni richiesta viene identificata
     * tramite una stringa di comando e gestita mediante uno {@code switch}.</p>
     *
     * <p>La comunicazione termina quando il client invia il comando
     * {@code "END"} o in caso di errore di comunicazione.</p>
     */
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            while (true) {
                String request = (String) in.readObject();

                Libro l = new Libro();
                Libreria libreria = new Libreria();
                UtenteRegistrato u;
                List<Libro> ris = new LinkedList<>();
                Libro corrente = new Libro();
                LinkedList<Libro> libroSuggeriti = new LinkedList<>();
                boolean esito;

                switch (request) {

                    // Termina la connessione con il client
                    case "END":
                        return;

                    // Ricerca di libri tramite titolo, autore o anno
                    case "CERCA LIBRO":
                        l = (Libro) in.readObject();
                        ris = db.cercaLibro(l);
                        out.writeObject(ris);
                        break;

                    // Registrazione di un nuovo utente
                    case "Registrazine":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.insertUtente(u);
                        out.writeObject(esito);
                        break;

                    // Login dell'utente
                    case "LOGIN":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.login(u);
                        out.writeObject(esito);
                        break;

                    // Inserimento valutazioni di un libro
                    case "INSETISCI VALUTAZIONE":
                        l = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.iserisciValutazioni(l, u);
                        out.writeObject(esito);
                        break;

                    // Registrazione di una nuova libreria
                    case "REGISTRA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.InserisciLibreria(u, libreria);
                        out.writeObject(esito);
                        break;

                    // Rinomina di una libreria esistente
                    case "RINOMINA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        String nomeVecchio = (String) in.readObject();
                        esito = db.RinominaNomeLibreria(u, libreria, nomeVecchio);
                        out.writeObject(esito);
                        break;

                    // Eliminazione di una libreria
                    case "ELIMINA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.EliminaLibreria(u, libreria);
                        out.writeObject(esito);
                        break;

                    // Inserimento suggerimenti di lettura
                    case "CONSIGLIA LIBRI":
                        u = (UtenteRegistrato) in.readObject();
                        corrente = (Libro) in.readObject();
                        esito = db.InserisciConsigli(u, corrente);
                        out.writeObject(esito);
                        break;

                    // Caricamento libri suggeriti
                    case "RIEMPI SUGGERITI":
                        corrente = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        libroSuggeriti = db.caricaSuggeriti(l, u);
                        out.writeObject(libroSuggeriti);
                        break;

                    // Controllo esistenza userId
                    case "CONTROLLA USERID":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.controllaUserId(u);
                        out.writeObject(esito);
                        break;

                    // Controllo esistenza email
                    case "CONTROLLA EMAIL":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.controllaEmail(u);
                        out.writeObject(esito);
                        break;

                    // Caricamento libri delle librerie dell'utente
                    case "CARICA LIBRI LIBRERIE CLIENT":
                        u = (UtenteRegistrato) in.readObject();
                        ris = db.caricaLibreriePerValutazione(u);
                        out.writeObject(ris);
                        break;

                    case "CARICA LIBRERIE":
                        u = (UtenteRegistrato) in.readObject();
                        LinkedList<Libreria> librerie = db.LibrerieUtente(u);
                        out.writeObject(librerie);
                        break;

                    case "CARICA LIBRI LIBRERIE CLIENT PER SUGGERITI":
                        u = (UtenteRegistrato) in.readObject();
                        ris = db.caricaLibreriePerSuggeriti(u);
                        out.writeObject(ris);
                        break;

                    case "CARICA NOTE":
                        l = (Libro) in.readObject();
                        out.writeObject(db.caricaNoteDalDb(l));
                        break;

                    case "CARICA LIBRI SUGGERITI PER VISUALIZZAZIONE":
                        l = (Libro) in.readObject();
                        out.writeObject(db.caricaSuggeritiDalDb(l));
                        break;

                    default:
                        // Comando non riconosciuto
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Gestione semplificata dell'errore
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignorata
            }
        }
    }
}
