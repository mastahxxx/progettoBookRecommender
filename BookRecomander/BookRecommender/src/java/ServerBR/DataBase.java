package ServerBR;

import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;

/**
 * La classe {@code DataBase} funge da intermediario tra il server e il database.
 * 
 * <p>Gestisce tutte le operazioni principali sui dati come:</p>
 * <ul>
 *     <li>ricerca di libri</li>
 *     <li>gestione utenti</li>
 *     <li>inserimento e gestione di librerie</li>
 *     <li>gestione valutazioni e consigli sui libri</li>
 * </ul>
 * 
 * <p>Ogni metodo è sincronizzato per garantire la coerenza dei dati quando 
 * viene utilizzata in contesti multithread.</p>
 */
public class DataBase {

    private final DbQuery dbq;
    private final DbInsert dbi;

    /**
     * Costruttore della classe {@code DataBase}.
     * 
     * <p>Inizializza una singola istanza di {@link DbQuery} e {@link DbInsert} 
     * da riutilizzare per tutte le operazioni sul database.</p>
     */
    public DataBase() {
        this.dbq = new DbQuery();
        this.dbi = new DbInsert();
    }

    /**
     * Cerca libri nel database in base ai parametri forniti.
     * 
     * <p>Il metodo normalizza eventuali valori {@code null} in stringhe vuote e 
     * seleziona la query più adatta in base ai campi presenti.</p>
     *
     * @param l il libro contenente i criteri di ricerca (titolo, autore, anno)
     * @return una lista di {@link Libro} che soddisfano i criteri
     */
    public synchronized List<Libro> cercaLibro(Libro l) {
        String titolo = l.getTitolo();
        String autore = l.getAutore();
        String anno = l.getAnnoPubblicazione();

        // normalizzo null -> ""
        titolo = (titolo == null) ? "" : titolo.trim();
        autore = (autore == null) ? "" : autore.trim();
        anno = (anno == null) ? "" : anno.trim();

        List<Libro> ris = new LinkedList<>();

        if (!titolo.isEmpty() && !autore.isEmpty() && !anno.isEmpty()) {
            ris = dbq.libriLibroTAA(titolo, autore, anno);
        } else if (!titolo.isEmpty() && !anno.isEmpty()) {
            ris = dbq.libriLibroTA(titolo, anno);
        } else if (!autore.isEmpty() && !anno.isEmpty()) {
            ris = dbq.libriLibroAA(autore, anno);
        } else if (!titolo.isEmpty()) {
            ris = dbq.libriLibro(titolo);
        } else if (!autore.isEmpty()) {
            ris = dbq.libriLibro(autore);
        }
        Libro libroPulito = new Libro();
        List<Libro> risPulito = new LinkedList<>();
        for (int i  = 0 ; i < ris.size(); i++) {
        	libroPulito.setTitolo(ris.get(i).getTitolo());
        	libroPulito.setAutore(ris.get(i).getAutore());
        	libroPulito.setAnnoPubblicazione(ris.get(i).getAnnoPubblicazione());
        	
        	libroPulito.setContenuto(ris.get(i).getContenuto());
        	libroPulito.setStile(ris.get(i).getStile());
        	libroPulito.setGradevolezza(ris.get(i).getGradevolezza());
        	libroPulito.setOriginalita(ris.get(i).getOriginalita());
        	libroPulito.setEdizione(ris.get(i).getEdizione());
        	
        	//libroPulito.caricaContenutoSuggeritiPulito(ris.get(i).getLibriConsigliati());
        	risPulito.add(libroPulito);
        }

        System.out.println(ris);
        System.out.println("XXXXXXXX" + risPulito);
        return risPulito;
    }

    /**
     * Carica tutti i libri presenti nelle librerie di un utente.
     * 
     * @param u l'utente registrato di cui caricare le librerie
     * @return una lista di {@link Libro} presenti nelle librerie dell'utente
     */
    public synchronized List<Libro> caricaLibrerie(UtenteRegistrato u) {
        List<Libro> ris = new LinkedList<>();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        if (cf == null || cf.isEmpty()) return ris;
        return dbq.getLibroDaLibreria(cf);
    }

    /**
     * Controlla se l'email di un utente è già registrata nel database.
     * 
     * @param u l'utente da verificare
     * @return {@code true} se l'email esiste, {@code false} altrimenti
     */
    public synchronized boolean controllaEmail(UtenteRegistrato u) {
        String mail = u.getMail();
        if (mail == null || mail.trim().isEmpty()) return false;
        return dbq.UtentiRegistratiE(mail.trim());
    }

    /**
     * Controlla se l'ID utente è già presente nel database.
     * 
     * @param u l'utente da verificare
     * @return {@code true} se l'ID esiste, {@code false} altrimenti
     */
    public synchronized boolean controllaUserId(UtenteRegistrato u) {
        String userId = u.getUserId();
        if (userId == null || userId.trim().isEmpty()) return false;
        return dbq.UtentiRegistratiUser(userId.trim());
    }

    /**
     * Inserisce un nuovo utente nel database.
     * 
     * <p>Divide il campo {@code nomeCognome} in nome e cognome e salva tutti i dati.</p>
     *
     * @param u l'utente da inserire
     * @return {@code true} se l'inserimento è riuscito, {@code false} altrimenti
     */
    public synchronized boolean insertUtente(UtenteRegistrato u) {
        String nomeCognome = u.getNomeCognome();
        String codiceFiscale = u.getCodiceFiscale();
        String mail = u.getMail();
        String userId = u.getUserId();
        String pass = u.getPassoword();

        if (nomeCognome == null) nomeCognome = "";
        String[] split = nomeCognome.trim().split("\\s+");

        String nome = split.length > 0 ? split[0] : "";
        String cognome = split.length > 1 ? split[1] : "";

        return dbi.loadUtentiRegistrati(nome, cognome, codiceFiscale, mail, userId, pass);
    }

    /**
     * Esegue il login di un utente utilizzando email o userId e password.
     * 
     * @param u l'utente che tenta il login
     * @return {@code true} se le credenziali sono corrette, {@code false} altrimenti
     */
    public synchronized boolean login(UtenteRegistrato u) {
        String mail = u.getMail();
        String userId = u.getUserId();
        String pass = u.getPassoword();

        boolean esito = false;

        if (mail != null && !mail.trim().isEmpty()) {
            esito = dbq.UtentiRegistratiEPB(mail.trim(), pass);
        }
        if (!esito && userId != null && !userId.trim().isEmpty()) {
            esito = dbq.UtentiRegistratiUPB(userId.trim(), pass);
        }
        return esito;
    }

    /**
     * Inserisce le valutazioni di un libro fatte da un utente.
     * 
     * @param l il libro da valutare
     * @param u l'utente che inserisce le valutazioni
     * @return {@code true} se l'inserimento è avvenuto con successo
     */
    public synchronized boolean iserisciValutazioni(Libro l, UtenteRegistrato u) {
        int idLibro = dbq.getCodiceLibro(l);
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);

        int contenuto = l.getContenuto();
        int stile = l.getStile();
        int gradevolezza = l.getGradevolezza();
        int originalita = l.getOriginalita();
        int edizione = l.getEdizione();

        boolean controlloValutazioni = dbi.loadValutazioni(idLibro, cf, contenuto, stile, gradevolezza, originalita, edizione);

        // TODO: se reinserisci le note, riattiva inserisciNoteLibro
        boolean controlloNote = true;

        return controlloValutazioni && controlloNote;
    }

    /**
     * Inserisce le note di valutazione di un libro (privato, non esposto all'esterno).
     * 
     * @param l il libro di cui inserire le note
     * @param u l'utente che inserisce le note
     * @return {@code true} se l'inserimento è riuscito
     */
    private synchronized boolean inserisciNoteLibro(Libro l, UtenteRegistrato u) {
        int idLibro = dbq.getCodiceLibro(l);
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);

        String noteContenuto = l.getNoteStile();
        String noteStile = l.getNoteContenuto();
        String noteGradevolezza = l.getNoteGradevolezza();
        String noteOriginalita = l.getNoteOriginalita();
        String noteEdizione = l.getNoteEdizione();

        return dbi.loadValutazioniNote(idLibro, cf, noteContenuto, noteStile, noteGradevolezza, noteOriginalita, noteEdizione);
    }

    /**
     * Inserisce una libreria associata a un utente.
     * 
     * @param u l'utente proprietario della libreria
     * @param libreria la libreria da inserire
     * @return {@code true} se l'inserimento è riuscito
     */
    public synchronized boolean InserisciLibreria(UtenteRegistrato u, Libreria libreria) {
        String nome = libreria.getNome();
        LinkedList<Libro> contenuto = libreria.getContenuto();
        String userId = u.getUserId();

        boolean controllo = false;

        for (int i = 0; i < contenuto.size(); i++) {
            Libro l = contenuto.get(i);
            int idLibro = dbq.getCodiceLibro(l);
            String cf = dbq.getCFU(userId);
            controllo = dbi.loadLibrerie(cf, nome, idLibro);
            System.out.println(i);
        }
        return controllo;
    }

    /**
     * Rinomina il nome di una libreria di un utente.
     * 
     * @param u l'utente proprietario della libreria
     * @param libreria la libreria con il nuovo nome
     * @param nomeVecchio il vecchio nome della libreria
     * @return {@code true} se il rinomino è avvenuto con successo
     */
    public synchronized boolean RinominaNomeLibreria(UtenteRegistrato u, Libreria libreria, String nomeVecchio) {
        String nomeNuovo = libreria.getNome();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        return dbq.aggiornaNomeLibreria(nomeVecchio, nomeNuovo, cf);
    }

    /**
     * Elimina una libreria di un utente.
     * 
     * @param u l'utente proprietario della libreria
     * @param libreria la libreria da eliminare
     * @return {@code true} se l'eliminazione è riuscita
     */
    public synchronized boolean EliminaLibreria(UtenteRegistrato u, Libreria libreria) {
        String nome = libreria.getNome();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        return dbq.eliminaLibreria(nome, cf);
    }

    /**
     * Inserisce consigli di altri libri correlati a un libro specifico.
     * 
     * @param ur l'utente che inserisce i consigli
     * @param corrente il libro corrente a cui associare i consigli
     * @param suggeriti lista dei libri suggeriti
     * @return {@code true} se l'inserimento è riuscito
     */
    public synchronized boolean InserisciConsigli(UtenteRegistrato ur, Libro corrente, LinkedList<Libro> suggeriti) {
        int idLibroCorrente = dbq.getCodiceLibro(corrente);
        String userId = ur.getUserId();
        String cf = dbq.getCFU(userId);

        boolean esito = false;

        for (int i = 0; i < suggeriti.size(); i++) {
            Libro l = suggeriti.get(i);
            int idLibroSuggerito = dbq.getCodiceLibro(l);
            esito = dbi.loadConsigliPerLibroInDb(idLibroCorrente, cf, idLibroSuggerito);
        }
        return esito;
    }

    /**
     * Carica i libri suggeriti per un libro specifico di un utente.
     * 
     * @param corrente il libro corrente
     * @param ur l'utente per cui caricare i suggerimenti
     * @return lista di {@link Libro} suggeriti
     */
    public synchronized LinkedList<Libro> caricaSuggeriti(Libro corrente, UtenteRegistrato ur) {
        int idlibro = dbq.getCodiceLibro(corrente);
        String userId = ur.getUserId();
        String cf = dbq.getCFU(userId);

        return dbq.caricaSuggeritiDaDB(idlibro, cf);
    }

	public Libro caricaNoteDalDb(Libro l) {
		int idlibro = dbq.getCodiceLibro(l);
		Libro libroConSoloNote = dbq.caricaNote(idlibro);
		return libroConSoloNote;
	}

//   	public LinkedList<Libreria> LibrerieUtente(UtenteRegistrato u) {
//		String userId = u.getUserId();
//        String cf = dbq.getCFU(userId);
//        LinkedList<Libreria> librerie =  dbq.caricaLibrerie(cf);
//        return librerie;    
//	} 
}








