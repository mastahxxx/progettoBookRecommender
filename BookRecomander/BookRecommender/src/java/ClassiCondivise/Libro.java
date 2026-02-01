 /**
 * @author Adrian Gabriel Soare 749483
 * 
 * 
 * 
 * 
 */



package ClassiCondivise;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

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
    
    //Utilizzate per la valutazione da mandare la server
    private String noteContenuto;
    private String noteStile;
    private String noteGradevolezza;
    private String noteOriginalita;
    private String noteEdizione;

    private LinkedList<Libro> libriConsigliati;
    
    private LinkedList<String> ListaNoteContenuto;
    private LinkedList<String> ListaNoteStile;
    private LinkedList<String> ListaNoteGradevolezza;
    private LinkedList<String> ListaNoteOriginalita;
    private LinkedList<String> ListaNoteEdizione;
    private boolean controllo;

    /**
     * Costruttore predefinito. Inizializza le liste interne.
     */
    public Libro() {
        this.libriConsigliati = new LinkedList();
        ListaNoteContenuto = new LinkedList();
        ListaNoteStile = new LinkedList();
        ListaNoteGradevolezza = new LinkedList();
        ListaNoteOriginalita = new LinkedList();
        ListaNoteEdizione = new LinkedList();
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
    	if(noteSt.charAt(noteSt.length()-1)=='?'  && noteSt.charAt(noteSt.length()-1)=='!') {
    		noteStile = noteSt;
    	}else {
    		if(noteSt.charAt(noteSt.length()-1)!='.' )
    			noteSt = noteSt+".";
            noteStile = noteSt;
    	}
    	noteStile = noteSt;
    }

    /** Aggiunge una nota sul contenuto. */
    public void setNoteContenuto(String noteCo) {
    	if(noteCo.charAt(noteCo.length()-1)=='?' && noteCo.charAt(noteCo.length()-1)=='!') {
    		noteContenuto = noteCo;
    	}else {
    		if(noteCo.charAt(noteCo.length()-1)!='.')
    			noteCo = noteCo+".";
            noteContenuto = noteCo;
    	}
    	noteContenuto = noteCo;
    }

    /** Aggiunge una nota sulla gradevolezza. */
    public void setNoteGradevolezza(String noteGr) {
    	if(noteGr.charAt(noteGr.length()-1)=='?' && noteGr.charAt(noteGr.length()-1)=='!') {
    		noteGradevolezza = noteGr;
    	}else {
    		if(noteGr.charAt(noteGr.length()-1)=='?')
    			noteGr = noteGr+".";
            noteGradevolezza = noteGr;
    	}	
    	noteGradevolezza = noteGr;
    }

    /** Aggiunge una nota sull'originalità. */
    public void setNoteOriginalita(String noteOr) {
    	if(noteOr.charAt(noteOr.length()-1)=='?' && noteOr.charAt(noteOr.length()-1)=='!') {
    		noteOriginalita = noteOr;
    	}else {
    		if(noteOr.charAt(noteOr.length()-1)!='.')
    			noteOr = noteOr+".";
    		noteOriginalita = noteOr;
    	}
    	noteOriginalita = noteOr;	
    }

    /** Aggiunge una nota sull'edizione. */
    public void setNoteEdizione(String noteEd) {
    	if(noteEd.charAt(noteEd.length()-1)=='?' && noteEd.charAt(noteEd.length()-1)=='!') {
    		noteEdizione = noteEd;
    	}else {
    		if(noteEd.charAt(noteEd.length()-1)!='.')
    			noteEd = noteEd+".";
    		noteEdizione = noteEd;
    	}
    	noteEdizione = noteEd;
    }

    
    /**
     * Aggiunge un libro alla lista dei consigliati.
     * @param linkedList libro da aggiungere
     */
    public void setLibriConsigliati(Libro linkedList) {
        if (linkedList != null) {
            this.libriConsigliati.add(linkedList);
        }
      
    }
    
    public void caricaContenutoSuggeritiPulito(LinkedList<Libro> l) {
    	this.libriConsigliati = l;
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

    //getter note

    public LinkedList<String> getListaNoteStile() { return ListaNoteStile; }
    public LinkedList<String> getListaNoteContenuto() { return ListaNoteContenuto; }
    public LinkedList<String> getListaNoteGradevolezza() { return ListaNoteGradevolezza; }
    public LinkedList<String> getListaNoteOriginalita() { return ListaNoteOriginalita; }
    public LinkedList<String> getListaNoteEdizione() { return ListaNoteEdizione; }




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
