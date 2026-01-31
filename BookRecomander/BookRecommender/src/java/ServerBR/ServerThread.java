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
 * La classe {@code ServerThread} rappresenta un thread dedicato per la gestione
 * della connessione con un singolo client.
 * 
 * <p>Ogni volta che un client si connette al server, viene creato un {@code ServerThread}
 * che si occupa di ricevere richieste, elaborarle tramite {@link DataBase} e restituire
 * i risultati al client.</p>
 */
public class ServerThread extends Thread {
    private DataBase db;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    /**
     * Costruttore della classe {@code ServerThread}.
     * 
     * <p>Inizializza i flussi di input/output per la comunicazione con il client
     * e crea una nuova istanza del database.</p>
     * 
     * @param s la socket della connessione con il client
     */
    public ServerThread(Socket s) {
        this.socket = s;
        this.db = new DataBase();
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            // Gestione semplificata, da migliorare eventualmente con log
        }
    }

    /**
     * Metodo principale del thread.
     * 
     * <p>Il thread si mette in ascolto delle richieste del client in un loop infinito
     * fino a quando il client invia il comando "END" o si verifica un'eccezione.</p>
     * 
     * <p>Gestisce richieste come:</p>
     * <ul>
     *     <li>ricerca libri</li>
     *     <li>registrazione e login utenti</li>
     *     <li>inserimento librerie e valutazioni</li>
     *     <li>inserimento e caricamento suggerimenti</li>
     *     <li>verifica email e userId</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
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

                    /**
                     * Comando "END"
                     * Termina la connessione con il client e chiude il thread.
                     */
                    case "END":
                        return;

                    /**
                     * Comando "CERCA LIBRO"
                     * Legge un oggetto {@link Libro} dal client e restituisce una lista di libri
                     * corrispondenti ai criteri di ricerca (titolo, autore, anno) tramite {@link DataBase#cercaLibro}.
                     */
                    case "CERCA LIBRO":
                        l = (Libro) in.readObject();
                        ris = db.cercaLibro(l);
                        out.writeObject(ris);
                        break;

                    /**
                     * Comando "Registrazine"
                     * Registra un nuovo utente nel database leggendo un oggetto {@link UtenteRegistrato} dal client.
                     * Restituisce true se la registrazione è avvenuta con successo.
                     */
                    case "Registrazine":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.insertUtente(u);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "LOGIN"
                     * Effettua il login dell'utente. Riceve {@link UtenteRegistrato} e verifica
                     * le credenziali tramite {@link DataBase#login}.s
                     * Restituisce true se le credenziali sono corrette.
                     */
                    case "LOGIN":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.login(u);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "INSETISCI VALUTAZIONE"
                     * Inserisce le valutazioni di un libro fatte da un utente.
                     * Legge {@link Libro} e {@link UtenteRegistrato} dal client.
                     * Restituisce true se l'inserimento è avvenuto con successo.
                     */
                    case "INSETISCI VALUTAZIONE":
                        l = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.iserisciValutazioni(l, u);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "REGISTRA LIBRERIA"
                     * Inserisce una nuova libreria per un utente.
                     * Riceve {@link UtenteRegistrato} e {@link Libreria} dal client.
                     * Restituisce true se l'inserimento è riuscito.
                     */
                    case "REGISTRA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.InserisciLibreria(u, libreria);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "RINOMINA LIBRERIA"
                     * Rinomina una libreria esistente di un utente.
                     * Riceve {@link UtenteRegistrato}, {@link Libreria} e il vecchio nome della libreria.
                     * Restituisce true se il rinomino è avvenuto con successo.
                     */
                    case "RINOMINA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        String nomeVecchio = (String) in.readObject();
                        esito = db.RinominaNomeLibreria(u, libreria, nomeVecchio);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "ELIMINA LIBRERIA"
                     * Elimina una libreria di un utente.
                     * Riceve {@link UtenteRegistrato} e {@link Libreria}.
                     * Restituisce true se l'eliminazione è riuscita.
                     */
                    case "ELIMINA LIBRERIA":
                        u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.EliminaLibreria(u, libreria);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "CONSIGLIA LIBRI"
                     * Inserisce suggerimenti di libri correlati a un libro specifico.
                     * Riceve {@link UtenteRegistrato}, il libro corrente e la lista dei libri suggeriti.
                     * Restituisce true se l'inserimento è riuscito.
                     */
                    case "CONSIGLIA LIBRI":
                         u = (UtenteRegistrato) in.readObject();
                        corrente = (Libro) in.readObject();
                        esito = db.InserisciConsigli(u, corrente);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "RIEMPI SUGGERITI"
                     * Carica i libri suggeriti per un libro specifico di un utente.
                     * Riceve {@link Libro} e {@link UtenteRegistrato}.
                     * Restituisce la lista dei libri suggeriti.
                     */
                    case "RIEMPI SUGGERITI":
                        corrente = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        libroSuggeriti = db.caricaSuggeriti(l, u);
                        out.writeObject(libroSuggeriti);
                        break;

                    /**
                     * Comando "CONTROLLA USERID"
                     * Controlla se l'userId di un utente è già presente nel database.
                     * Riceve {@link UtenteRegistrato}.
                     * Restituisce true se l'userId esiste.
                     */
                    case "CONTROLLA USERID":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.controllaUserId(u);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "CONTROLLA EMAIL"
                     * Controlla se l'email di un utente è già presente nel database.
                     * Riceve {@link UtenteRegistrato}.
                     * Restituisce true se l'email esiste.
                     */
                    case "CONTROLLA EMAIL":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.controllaEmail(u);
                        out.writeObject(esito);
                        break;

                    /**
                     * Comando "CARICA LIBRI LIBRERIE CLIENT"
                     * Carica tutti i libri presenti nelle librerie di un utente.
                     * Riceve {@link UtenteRegistrato}.
                     * Restituisce la lista di libri.
                     */
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
                    	Libro libroConSoloNote = db.caricaNoteDalDb(l);
                        out.writeObject(libroConSoloNote);
                        break;
                    
                    case "CARICA LIBRI SUGGERITI PER VISUALIZAZZIONE":
                    	l = (Libro) in.readObject();
                    	Libro libroConSoloSuggeriti = db.caricaSuggeritiDalDb(l);
                        out.writeObject(libroConSoloSuggeriti);
                        break;
                    default:
                        // richiesta non riconosciuta, ignorata
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            // Eccezioni ignorate, migliorabile con logging
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // Ignorata
            }
        }
    }
}










