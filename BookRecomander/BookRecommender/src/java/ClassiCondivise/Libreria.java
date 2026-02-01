/**
 * @author Adrian Gabriel Soare 749483
 * 
 * 
 * 
 * 
 */




package ClassiCondivise;

import java.util.LinkedList;
import java.io.Serializable;

/**
 * Rappresenta una libreria che contiene una collezione di libri.
 * <p>
 * Ogni libreria ha un nome, un contenuto rappresentato come lista di libri
 *
 */
public class Libreria implements Serializable {
    
    private static final long SerialVersionUID = 1L;
    private String nome;
    private LinkedList<Libro> contenuto;
    private boolean controllo;

    /**
     * Crea una libreria vuota senza nome e inizializza la lista dei libri.
     */
    public Libreria() {
        this.contenuto = new LinkedList<>();
    }

    /**
     * Costruttore con nome.
     *
     * @param nome il nome della libreria
     */
    public Libreria(String nome) {
        this.nome = nome;
        this.contenuto = new LinkedList<>();
    }

    /**
     * Restituisce il nome della libreria.
     *
     * @return il nome della libreria
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Imposta il nome della libreria.
     *
     * @param nome il nuovo nome della libreria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce i libri della libreria.
     *
     * @return la lista di libri contenuti
     */
    public LinkedList<Libro> getContenuto() {
        return this.contenuto;
    }

    /**
     * Restituisce il valore del flag di controllo.
     *
     * @return {@code true} se il controllo è attivo, {@code false} altrimenti
     */
    public boolean getControllo() {
        return this.controllo;
    }

    /**
     * Imposta il valore del flag di controllo.
     *
     * @param c nuovo valore per il flag di controllo
     */
    public void setControllo(boolean c) {
        this.controllo = c;
    }
    
    public void setContenuto(LinkedList<Libro> c) {
    	this.contenuto = c;
    }
}
