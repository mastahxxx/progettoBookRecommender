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

    private String noteContenuto;
    private String noteStile;
    private String noteGradevolezza;
    private String noteOriginalita;
    private String noteEdizione;

    private LinkedList<Libro> libriConsigliati;
    private boolean controllo;

    /**
     * Costruttore predefinito. Inizializza le liste interne.
     */
    public Libro() {
        this.libriConsigliati = new LinkedList();
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
    public String getNoteStile() { return this.noteStile; }

    /** @return note sul contenuto */
    public String getNoteContenuto() { return this.noteContenuto; }

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
    public void setNoteStile(String noteSt) {
        noteStile = "Note stile : "+ noteSt + ". ";
    }

    /** Aggiunge una nota sul contenuto. */
    public void setNoteContenuto(String noteCo) {
        noteContenuto = "Note contenuto : "  + noteCo + ". ";
    }

    /** Aggiunge una nota sulla gradevolezza. */
    public void setNoteGradevolezza(String noteGr) {
        noteGradevolezza = "Note gradevolezza : " + noteGr + ". ";
    }

    /** Aggiunge una nota sull'originalità. */
    public void setNoteOriginalita(String noteOr) {
        noteOriginalita = "Note originalità : "  + noteOr + ". ";
    }

    /** Aggiunge una nota sull'edizione. */
    public void setNoteEdizione(String noteEd) {
        noteEdizione = "Note edizione : " + noteEd + ". ";
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
