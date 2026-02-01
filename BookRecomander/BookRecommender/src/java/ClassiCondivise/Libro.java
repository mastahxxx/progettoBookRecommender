 /**
 * @author Adrian Gabriel Soare 749483
 * @author Abdullah Waheed Malik 756789
 * @author Matteo Sorrentino n: 753775
 */
package ClassiCondivise;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Rappresenta un libro con informazioni principali come titolo, autore, anno di pubblicazione,
 * valutazioni su diversi aspetti (contenuto, stile, gradevolezza, originalità, edizione),
 * note associate alle valutazioni e libri consigliati.
 * <p>
 * Questa classe implementa {@link Serializable} per consentire la serializzazione degli oggetti Libro.
 * </p>
 */
public class Libro implements Serializable {

    private static final long SerialVersionUID = 1L;

    private String titolo;
    private String autore;
    private String annoPubblicazione;
    private int contenuto;
    private int stile;
    private int gradevolezza;
    private int originalita;
    private int edizione;

    // Note associate alle valutazioni
    private String noteContenuto;
    private String noteStile;
    private String noteGradevolezza;
    private String noteOriginalita;
    private String noteEdizione;

    // Liste di libri e note
    private LinkedList<Libro> libriConsigliati;
    private LinkedList<String> ListaNoteContenuto;
    private LinkedList<String> ListaNoteStile;
    private LinkedList<String> ListaNoteGradevolezza;
    private LinkedList<String> ListaNoteOriginalita;
    private LinkedList<String> ListaNoteEdizione;

    private boolean controllo;

    /**
     * Costruttore predefinito.
     * Inizializza le liste interne utilizzate per le note e i libri consigliati.
     */
    public Libro() {
        this.libriConsigliati = new LinkedList<>();
        ListaNoteContenuto = new LinkedList<>();
        ListaNoteStile = new LinkedList<>();
        ListaNoteGradevolezza = new LinkedList<>();
        ListaNoteOriginalita = new LinkedList<>();
        ListaNoteEdizione = new LinkedList<>();
    }

    // ====================
    // Getter
    // ====================

    /** @return il titolo del libro */
    public String getTitolo() { return this.titolo; }

    /** @return l'autore del libro */
    public String getAutore() { return this.autore; }

    /** @return l'anno di pubblicazione */
    public String getAnnoPubblicazione() { return this.annoPubblicazione; }

    /** @return valutazione del contenuto */
    public int getContenuto() { return this.contenuto; }

    /** @return valutazione dello stile */
    public int getStile() { return this.stile; }

    /** @return valutazione della gradevolezza */
    public int getGradevolezza() { return this.gradevolezza; }

    /** @return valutazione dell'originalità */
    public int getOriginalita() { return this.originalita; }

    /** @return valutazione dell'edizione */
    public int getEdizione() { return this.edizione; }

    /** @return note sul contenuto */
    public String getNoteContenuto() { return this.noteContenuto; }

    /** @return note sullo stile */
    public String getNoteStile() { return this.noteStile; }

    /** @return note sulla gradevolezza */
    public String getNoteGradevolezza() { return this.noteGradevolezza; }

    /** @return note sull'originalità */
    public String getNoteOriginalita() { return this.noteOriginalita; }

    /** @return note sull'edizione */
    public String getNoteEdizione() { return this.noteEdizione; }

    /** @return flag di controllo */
    public boolean getControllo() { return this.controllo; }

    /** @return lista di libri consigliati */
    public LinkedList<Libro> getLibriConsigliati() { return this.libriConsigliati; }

    // ====================
    // Setter
    // ====================

    /** @param titolo nuovo titolo del libro */
    public void setTitolo(String titolo) { this.titolo = titolo; }

    /** @param autore nuovo autore del libro */
    public void setAutore(String autore) { this.autore = autore; }

    /** @param annoPubblicazione nuovo anno di pubblicazione */
    public void setAnnoPubblicazione(String annoPubblicazione) { this.annoPubblicazione = annoPubblicazione; }

    /** @param valutazione nuova valutazione dello stile */
    public void setStile(int valutazione) { this.stile = valutazione; }

    /** @param valutazione nuova valutazione del contenuto */
    public void setContenuto(int valutazione) { this.contenuto = valutazione; }

    /** @param valutazione nuova valutazione della gradevolezza */
    public void setGradevolezza(int valutazione) { this.gradevolezza = valutazione; }

    /** @param valutazione nuova valutazione dell'originalità */
    public void setOriginalita(int valutazione) { this.originalita = valutazione; }

    /** @param valutazione nuova valutazione dell'edizione */
    public void setEdizione(int valutazione) { this.edizione = valutazione; }

    /** @param c nuovo valore del flag di controllo */
    public void setControllo(boolean c) { this.controllo = c; }

    // ====================
    // Note
    // ====================

    /**
     * Imposta la nota sullo stile.
     * @param noteSt testo della nota
     */
    public void setNoteStile(String noteSt) {
        if(noteSt.charAt(noteSt.length()-1)=='?' && noteSt.charAt(noteSt.length()-1)=='!') {
            noteStile = noteSt;
        } else {
            if(noteSt.charAt(noteSt.length()-1)!='.') noteSt = noteSt+".";
            noteStile = noteSt;
        }
        noteStile = noteSt;
    }

    /** Imposta la nota sul contenuto. */
    public void setNoteContenuto(String noteCo) {
        if(noteCo.charAt(noteCo.length()-1)=='?' && noteCo.charAt(noteCo.length()-1)=='!') {
            noteContenuto = noteCo;
        } else {
            if(noteCo.charAt(noteCo.length()-1)!='.') noteCo = noteCo+".";
            noteContenuto = noteCo;
        }
        noteContenuto = noteCo;
    }

    /** Imposta la nota sulla gradevolezza. */
    public void setNoteGradevolezza(String noteGr) {
        if(noteGr.charAt(noteGr.length()-1)=='?' && noteGr.charAt(noteGr.length()-1)=='!') {
            noteGradevolezza = noteGr;
        } else {
            if(noteGr.charAt(noteGr.length()-1)!='?') noteGr = noteGr+".";
            noteGradevolezza = noteGr;
        }
        noteGradevolezza = noteGr;
    }

    /** Imposta la nota sull'originalità. */
    public void setNoteOriginalita(String noteOr) {
        if(noteOr.charAt(noteOr.length()-1)=='?' && noteOr.charAt(noteOr.length()-1)=='!') {
            noteOriginalita = noteOr;
        } else {
            if(noteOr.charAt(noteOr.length()-1)!='.') noteOr = noteOr+".";
            noteOriginalita = noteOr;
        }
        noteOriginalita = noteOr;
    }

    /** Imposta la nota sull'edizione. */
    public void setNoteEdizione(String noteEd) {
        if(noteEd.charAt(noteEd.length()-1)=='?' && noteEd.charAt(noteEd.length()-1)=='!') {
            noteEdizione = noteEd;
        } else {
            if(noteEd.charAt(noteEd.length()-1)!='.') noteEd = noteEd+".";
            noteEdizione = noteEd;
        }
        noteEdizione = noteEd;
    }

    // ====================
    // Libri consigliati
    // ====================

    /**
     * Aggiunge un libro alla lista dei consigliati.
     * @param linkedList libro da aggiungere
     */
    public void setLibriConsigliati(Libro linkedList) {
        if (linkedList != null) {
            this.libriConsigliati.add(linkedList);
        }
    }

    /** Sostituisce l'intera lista di libri consigliati. */
    public void caricaContenutoSuggeritiPulito(LinkedList<Libro> l) { this.libriConsigliati = l; }

    /**
     * Restituisce una rappresentazione testuale del libro nel formato:
     * titolo — autore (anno)
     * @return descrizione del libro
     */
    @Override
    public String toString() {
        String t = (titolo != null) ? titolo : "(senza titolo)";
        String a = (autore != null) ? autore : "sconosciuto";
        String y = (annoPubblicazione != null) ? annoPubblicazione : "-";
        return t + " — " + a + " (" + y + ")";
    }

    // ====================
    // Liste note per utente
    // ====================

    public void setListNoteNoteStile (String u) {
        String nuovaNota = u+": "+this.getNoteStile();
        this.ListaNoteStile.add(nuovaNota);
    }

    public void setListNoteContenuto (String u) {
        String nuovaNota = u+": "+this.getNoteContenuto();
        this.ListaNoteContenuto.add(nuovaNota);
    }

    public void setListNoteGradevolezza (String u) {
        String nuovaNota = u+": "+this.getNoteGradevolezza();
        this.ListaNoteGradevolezza.add(nuovaNota);
    }

    public void setListNoteOriginalita (String u) {
        String nuovaNota = u+": "+this.getNoteOriginalita();
        this.ListaNoteOriginalita.add(nuovaNota);
    }

    public void setListNoteEdizione (String u) {
        String nuovaNota = u+": "+this.getNoteEdizione();
        this.ListaNoteEdizione.add(nuovaNota);
    }

    /** @return lista note stile */
    public LinkedList<String> getListaNoteStile() { return ListaNoteStile; }

    /** @return lista note contenuto */
    public LinkedList<String> getListaNoteContenuto() { return ListaNoteContenuto; }

    /** @return lista note gradevolezza */
    public LinkedList<String> getListaNoteGradevolezza() { return ListaNoteGradevolezza; }

    /** @return lista note originalità */
    public LinkedList<String> getListaNoteOriginalita() { return ListaNoteOriginalita; }

    /** @return lista note edizione */
    public LinkedList<String> getListaNoteEdizione() { return ListaNoteEdizione; }

    /**
     * Controlla se due libri sono uguali comparando titolo, autore e anno di pubblicazione.
     * @param o altro oggetto da confrontare
     * @return true se i libri sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(titolo, libro.titolo) &&
               Objects.equals(autore, libro.autore) &&
               Objects.equals(annoPubblicazione, libro.annoPubblicazione);
    }
}
