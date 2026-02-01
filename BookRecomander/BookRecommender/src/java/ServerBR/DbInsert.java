package ServerBR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Gestisce le operazioni di inserimento (scrittura) nel database PostgreSQL.
 * <p>
 * Questa classe si occupa esclusivamente di eseguire query di tipo 
 * {@code INSERT} per salvare nuovi utenti, libri, valutazioni, 
 * note e relazioni tra le entità.
 * </p>
 */
public class DbInsert {

    // ==== CONFIG (coerente con DbQuery) ====
    private static final String URL = "jdbc:postgresql://localhost:5432/bookRecommender";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private final Connection connection;

    /**
     * Costruttore della classe.
     * <p>
     * Stabilisce la connessione con il database utilizzando i parametri di configurazione
     * (URL, User, Password). Se la connessione fallisce, viene lanciata una 
     * {@link RuntimeException}.
     * </p>
     */
    public DbInsert() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connessione DB OK (DbInsert)");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Connessione DB fallita (DbInsert)", e);
        }
    }

    /**
     * Inserisce un nuovo utente nella tabella {@code UtentiRegistrati}.
     *
     * @param nome     Il nome dell'utente.
     * @param cognome  Il cognome dell'utente.
     * @param cf       Il codice fiscale (chiave primaria).
     * @param email    L'indirizzo email.
     * @param uid      Lo UserID (nickname).
     * @param password La password dell'account.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadUtentiRegistrati(String nome, String cognome, String cf, String email, String uid, String password) {
        // Se la tabella ha colonne in ordine diverso, specifica le colonne esplicitamente!
        String sql = "INSERT INTO public.\"UtentiRegistrati\" VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, cf);
            ps.setString(4, uid);
            ps.setString(5, email);
            ps.setString(6, password);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce un nuovo libro nella tabella {@code Libri}.
     *
     * @param titolo   Il titolo del libro.
     * @param autore   L'autore del libro.
     * @param annoP    L'anno di pubblicazione.
     * @param codLibro Il codice identificativo del libro.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadLibri(String titolo, String autore, int annoP, int codLibro) {
        String sql = "INSERT INTO public.\"Libri\" VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titolo);
            ps.setString(2, autore);
            ps.setInt(3, annoP);
            ps.setInt(4, codLibro);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce un record base nella tabella {@code Consigli}.
     *
     * @param idlibro L'ID del libro di partenza.
     * @param idcf    Il codice fiscale dell'utente.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadConsigli(int idlibro, String idcf) {
        String sql = "INSERT INTO public.\"Consigli\" VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idlibro);
            ps.setString(2, idcf);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiunge un libro a una specifica libreria di un utente nella tabella {@code Librerie}.
     *
     * @param idcf         Il codice fiscale dell'utente proprietario della libreria.
     * @param nomeLibreria Il nome della libreria in cui inserire il libro.
     * @param idLibro      L'ID del libro da aggiungere.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadLibrerie(String idcf, String nomeLibreria, int idLibro) {
        String sql = "INSERT INTO public.\"Librerie\" (nome_libreria, id_libro, id_codice_fiscale) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeLibreria);
            ps.setInt(2, idLibro);
            ps.setString(3, idcf);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce le note testuali relative alla valutazione di un libro nella tabella {@code NoteValutazioni}.
     *
     * @param idLibro         L'ID del libro valutato.
     * @param cf              Il codice fiscale dell'utente che lascia la nota.
     * @param notaStile       Commento sullo stile.
     * @param notaContenuto   Commento sul contenuto.
     * @param notaGradevolezza Commento sulla gradevolezza.
     * @param notaOriginalita Commento sull'originalità.
     * @param notaEdizione    Commento sull'edizione.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadValutazioniNote(int idLibro, String cf,
                                      String notaStile, String notaContenuto, String notaGradevolezza,
                                      String notaOriginalita, String notaEdizione) {
        String sql = "INSERT INTO public.\"NoteValutazioni\" VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, notaStile);
            ps.setString(2, notaContenuto);
            ps.setString(3, notaGradevolezza);
            ps.setString(4, notaOriginalita);
            ps.setString(5, notaEdizione);
            ps.setString(6, cf);
            ps.setInt(7, idLibro);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce un consiglio completo che collega un libro letto a un libro suggerito nella tabella {@code Consigli}.
     *
     * @param idLibroCorrente  L'ID del libro di base (letto).
     * @param cf               Il codice fiscale dell'utente che dà il consiglio.
     * @param idLibroSuggerito L'ID del libro suggerito.
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadConsigliPerLibroInDb(int idLibroCorrente, String cf, int idLibroSuggerito) {
        // QUI nel tuo codice originale stavi inserendo nella tabella "Librerie" (quasi sicuramente è un errore).
        // Io metto "Consigli" come tabella logica dei suggerimenti.
        // Se la tua tabella corretta è un'altra, cambia SOLO il nome tabella.
        String sql = "INSERT INTO public.\"Consigli\" VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idLibroCorrente);
            ps.setString(2, cf);
            ps.setInt(3, idLibroSuggerito);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce le valutazioni numeriche di un libro nella tabella {@code Valutazioni}.
     *
     * @param idLibro      L'ID del libro valutato.
     * @param idcf         Il codice fiscale dell'utente che valuta.
     * @param stile        Punteggio stile (int).
     * @param contenuto    Punteggio contenuto (int).
     * @param gradevolezza Punteggio gradevolezza (int).
     * @param originalita  Punteggio originalità (int).
     * @param edizione     Punteggio edizione (int).
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} in caso di errore.
     */
    public boolean loadValutazioni(int idLibro, String idcf,
                                   int stile, int contenuto, int gradevolezza, int originalita, int edizione) {
        String sql = "INSERT INTO public.\"Valutazioni\" VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ps.setString(2, idcf);
            ps.setInt(3, stile);
            ps.setInt(4, contenuto);
            ps.setInt(5, gradevolezza);
            ps.setInt(6, originalita);
            ps.setInt(7, edizione);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}