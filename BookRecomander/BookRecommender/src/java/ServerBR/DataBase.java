package ServerBR;

import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;

public class DataBase {

    private final DbQuery dbq;
    private final DbInsert dbi;

    public DataBase() {
        // Crea UNA sola istanza e riusala
        this.dbq = new DbQuery();
        this.dbi = new DbInsert();
    }

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

        return ris;
    }

    public synchronized List<Libro> caricaLibrerie(UtenteRegistrato u) {
        List<Libro> ris = new LinkedList<>();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        if (cf == null || cf.isEmpty()) return ris;
        return dbq.getLibroDaLibreria(cf);
    }

    public synchronized boolean controllaEmail(UtenteRegistrato u) {
        String mail = u.getMail();
        if (mail == null || mail.trim().isEmpty()) return false;
        // Nota: il tuo commento dice "true se la mail non è presente",
        // ma il metodo DbQuery.UtentiRegistratiE() (come lo hai scritto) ritorna true se ESISTE.
        // Qui lascio il comportamento "così com'è" (ritorna true se esiste).
        return dbq.UtentiRegistratiE(mail.trim());
    }

    public synchronized boolean controllaUserId(UtenteRegistrato u) {
        String userId = u.getUserId();
        if (userId == null || userId.trim().isEmpty()) return false;
        // stesso discorso: ritorna true se esiste
        return dbq.UtentiRegistratiUser(userId.trim());
    }

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
        }
        return controllo;
    }

    public synchronized boolean RinominaNomeLibreria(UtenteRegistrato u, Libreria libreria, String nomeVecchio) {
        String nomeNuovo = libreria.getNome();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        return dbq.aggiornaNomeLibreria(nomeVecchio, nomeNuovo, cf);
    }

    public synchronized boolean EliminaLibreria(UtenteRegistrato u, Libreria libreria) {
        String nome = libreria.getNome();
        String userId = u.getUserId();
        String cf = dbq.getCFU(userId);
        return dbq.eliminaLibreria(nome, cf);
    }

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

    public synchronized LinkedList<Libro> caricaSuggeriti(Libro corrente, UtenteRegistrato ur) {
        int idlibro = dbq.getCodiceLibro(corrente);
        String userId = ur.getUserId();
        String cf = dbq.getCFU(userId);

        return dbq.caricaSuggeritiDaDB(idlibro, cf);
    }
}
