package ClassiCondivise;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Rappresenta un libro con titolo, autore, anno di pubblicazione,
 * valutazioni e note.
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

    private LinkedList<String> noteContenuto;
    private LinkedList<String> noteStile;
    private LinkedList<String> noteGradevolezza;
    private LinkedList<String> noteOriginalita;
    private LinkedList<String> noteEdizione;

    private LinkedList<Libro> libriConsigliati;
    private boolean controllo;

    /**
     * Costruttore predefinito. Inizializza le liste interne.
     */
    public Libro() {
        this.libriConsigliati = new LinkedList<>();
        this.noteContenuto = new LinkedList<>();
        this.noteStile = new LinkedList<>();
        this.noteGradevolezza = new LinkedList<>();
        this.noteOriginalita = new LinkedList<>();
        this.noteEdizione = new LinkedList<>();
    }

    //Getter

    /** @return il titolo del libro */
    public String getTitolo() { return this.titolo; }

    /** @return l'autore del libro */
    public String getAutore() { return this.autore; }

    /** @return l'anno di pubblicazione */
    public String getAnnoPubblicazione() { return this.annoPubblicazione; }

    /** @return valutazione dello stile */
    public int getStile() { return this.stile; }

    /** @return valutazione del contenuto */
    public int getContenuto() { return this.contenuto; }

    /** @return valutazione della gradevolezza */
    public int getGradevolezza() { return this.gradevolezza; }

    /** @return valutazione dell'originalità */
    public int getOriginalita() { return this.originalita; }

    /** @return valutazione dell'edizione */
    public int getEdizione() { return this.edizione; }

    /** @return note sullo stile */
    public LinkedList<String> getNoteStile() { return this.noteStile; }

    /** @return note sul contenuto */
    public LinkedList<String> getNoteContenuto() { return this.noteContenuto; }

    /** @return note sulla gradevolezza */
    public LinkedList<String> getNoteGradevolezza() { return this.noteGradevolezza; }

    /** @return note sull'originalità */
    public LinkedList<String> getNoteOriginalità() { return this.noteOriginalita; }

    /** @return note sull'edizione */
    public LinkedList<String> getNoteEdizione() { return this.noteEdizione; }

    /** @return flag di controllo */
    public boolean getControllo() { return this.controllo; }

    /** @return lista di libri consigliati */
    public LinkedList<Libro> getLibriConsigliati() { return this.libriConsigliati; }

    //Setter

    /** @param titolo nuovo titolo */
    public void setTitolo(String titolo) { this.titolo = titolo; }

    /** @param autore nuovo autore */
    public void setAutore(String autore) { this.autore = autore; }

    /** @param annoPubblicazione nuovo anno */
    public void setAnnoPubblicazione(String annoPubblicazione) { this.annoPubblicazione = annoPubblicazione; }

    /** @param valutazione nuovo valore */
    public void setStile(int valutazione) { this.stile = valutazione; }

    /** @param valutazione nuovo valore */
    public void setContenuto(int valutazione) { this.contenuto = valutazione; }

    /** @param valutazione nuovo valore */
    public void setGradevolezza(int valutazione) { this.gradevolezza = valutazione; }

    /** @param valutazione nuovo valore */
    public void setOriginalita(int valutazione) { this.originalita = valutazione; }

    /** @param valutazione nuovo valore */
    public void setEdizione(int valutazione) { this.edizione = valutazione; }

    /** @param c nuovo valore di controllo */
    public void setControllo(boolean c) { this.controllo = c; }

    //Note

    /** Aggiunge una nota sullo stile. */
    public void setNoteStile(String noteSt, String autore) {
        noteStile.add("Note stile : " + autore + ": " + noteSt + ". ");
    }

    /** Aggiunge una nota sul contenuto. */
    public void setNoteContenuto(String noteCo, String autore) {
        noteContenuto.add("Note contenuto : " + autore + ": " + noteCo + ". ");
    }

    /** Aggiunge una nota sulla gradevolezza. */
    public void setNoteGradevolezza(String noteGr, String autore) {
        noteGradevolezza.add("Note gradevolezza : " + autore + ": " + noteGr + ". ");
    }

    /** Aggiunge una nota sull'originalità. */
    public void setNoteOriginalita(String noteOr, String autore) {
        noteOriginalita.add("Note originalità : " + autore + ": " + noteOr + ". ");
    }

    /** Aggiunge una nota sull'edizione. */
    public void setNoteEdizione(String noteEd, String autore) {
        noteEdizione.add("Note edizione : " + autore + ": " + noteEd + ". ");
    }


    /**
     * Aggiunge un libro alla lista dei consigliati.
     * @param libro libro da aggiungere
     */
    public void setLibriConsigliati(Libro libro) {
        if (libro != null) {
            this.libriConsigliati.add(libro);
        }
    }

    /**
     * Restituisce una stringa descrittiva del libro.
     * @return titolo — autore (anno)
     */
    @Override
    public String toString() {
        String t = (titolo != null) ? titolo : "(senza titolo)";
        String a = (autore != null) ? autore : "sconosciuto";
        String y = (annoPubblicazione != null) ? annoPubblicazione : "-";
        return t + " — " + a + " (" + y + ")";
    }
}
