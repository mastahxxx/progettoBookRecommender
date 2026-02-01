package ClassiCondivise;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Classe {@code Libreria} che rappresenta una libreria personale di un utente.
 *
 * <p>Una libreria è identificata da un nome e contiene una collezione di
 * {@link Libro}. La classe viene utilizzata sia lato client che lato server
 * ed è serializzabile per consentire la trasmissione degli oggetti
 * tramite stream.</p>
 */
public class Libreria implements Serializable {

    /** Identificativo per la serializzazione della classe */
    private static final long serialVersionUID = 1L;

    /** Nome della libreria */
    private String nome;

    /** Lista dei libri contenuti nella libreria */
    private LinkedList<Libro> contenuto;

    /**
     * Flag di controllo utilizzato per indicare lo stato della libreria
     * (ad esempio selezione o validità in contesti applicativi).
     */
    private boolean controllo;

    /**
     * Costruttore di default.
     *
     * <p>Crea una libreria senza nome e inizializza la lista dei libri
     * come vuota.</p>
     */
    public Libreria() {
        this.contenuto = new LinkedList<>();
    }

    /**
     * Costruttore con nome.
     *
     * <p>Crea una libreria con il nome specificato e una lista
     * di libri inizialmente vuota.</p>
     *
     * @param nome nome della libreria
     */
    public Libreria(String nome) {
        this.nome = nome;
        this.contenuto = new LinkedList<>();
    }

    /**
     * Restituisce il nome della libreria.
     *
     * @return nome della libreria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della libreria.
     *
     * @param nome nuovo nome della libreria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la lista dei libri contenuti nella libreria.
     *
     * @return lista di {@link Libro}
     */
    public LinkedList<Libro> getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il contenuto della libreria.
     *
     * @param c lista di {@link Libro} da associare alla libreria
     */
    public void setContenuto(LinkedList<Libro> c) {
        this.contenuto = c;
    }

    /**
     * Restituisce il valore del flag di controllo.
     *
     * @return {@code true} se il flag è attivo, {@code false} altrimenti
     */
    public boolean getControllo() {
        return controllo;
    }

    /**
     * Imposta il valore del flag di controllo.
     *
     * @param c nuovo valore del flag di controllo
     */
    public void setControllo(boolean c) {
        this.controllo = c;
    }
}
