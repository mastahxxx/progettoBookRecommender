/**
 * @author Andrea Boaretto n: 754274
 */
package ServerBR;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;

/**
 * Gestisce le operazioni di lettura (query) sul database PostgreSQL.
 * <p>
 * Questa classe contiene metodi per ricercare libri, autenticare utenti,
 * recuperare librerie, caricare consigli e mappare i {@link ResultSet}
 * negli oggetti del dominio (es. {@link Libro}, {@link Libreria}).
 * </p>
 */
public class DbQuery {

    // ==== CONFIG (cambia solo qui se serve) ====
    private static final String URL = "jdbc:postgresql://localhost:5432/bookRecommender";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private final Connection connection;

    /**
     * Costruttore della classe.
     * <p>
     * Stabilisce la connessione con il database utilizzando i parametri di configurazione.
     * </p>
     * * @throws RuntimeException se la connessione al database fallisce.
     */
    public DbQuery() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connessione DB OK");
        } catch (SQLException e) {
            System.err.println("--- ERRORE DATABASE ---");
            System.err.println("Messaggio: " + e.getMessage());
            System.err.println("Codice SQLState: " + e.getSQLState());
            System.err.println("Codice Errore Nativo: " + e.getErrorCode());
            throw new RuntimeException("Connessione DB fallita", e);
        }
    }

    // =============================
    //        ESEMPI/UTILITY
    // =============================

    /**
     * Metodo di prova per verificare la connessione e recuperare un nome dalla tabella utenti.
     * <p>Stampa il risultato su console.</p>
     */
    public void queryProva() {
        String sql = "SELECT nome FROM public.\"UtentiRegistrati\"";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println(rs.getString(1));
            } else {
                System.out.println("Nessun risultato");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================
    //           QUERY LIBRI
    // =============================

    /**
     * Cerca libri per Titolo o Autore.
     * <p>
     * Esegue una ricerca OR sui campi titolo e autore. Calcola la media delle valutazioni
     * ricevute da ogni libro trovato.
     * </p>
     *
     * @param param La stringa da cercare (usata sia per titolo che per autore).
     * @return Una lista di {@link Libro} che corrispondono alla ricerca.
     */
    public List<Libro> libriLibro(String param) {
        String sql = "SELECT "
                + "    a.titolo, a.autore, a.anno_pubblicazione, "
                + "    CAST(ROUND(AVG(b.stile)) AS INTEGER) AS stile, "
                + "    CAST(ROUND(AVG(b.contenuto)) AS INTEGER) AS contenuto, "
                + "    CAST(ROUND(AVG(b.gradevolezza)) AS INTEGER) AS gradevolezza, "
                + "    CAST(ROUND(AVG(b.\"originalità\")) AS INTEGER) AS \"originalità\", "
                + "    CAST(ROUND(AVG(b.edizione)) AS INTEGER) AS edizione, "
                + "    NULL AS nota_stile, "
                + "    NULL AS nota_contenuto, "
                + "    NULL AS nota_gradevolezza, "
                + "    NULL AS nota_originalita, "
                + "    NULL AS nota_edizione "
                + "FROM public.\"Libri\" AS a "
                + "LEFT JOIN public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro "
                + "WHERE a.titolo = ? OR a.autore = ? "
                + "GROUP BY a.cod_libro, a.titolo, a.autore, a.anno_pubblicazione";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            ps.setString(2, param);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibri(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Cerca libri per Autore E Anno di pubblicazione.
     *
     * @param autore L'autore del libro.
     * @param anno   L'anno di pubblicazione.
     * @return Una lista di {@link Libro} che corrispondono ai criteri.
     */
    public List<Libro> libriLibroAA(String autore, String anno) {
        String sql = "SELECT "
                   + "    a.titolo, a.autore, a.anno_pubblicazione, "
                   + "    CAST(ROUND(AVG(b.stile)) AS INTEGER) AS stile, "
                   + "    CAST(ROUND(AVG(b.contenuto)) AS INTEGER) AS contenuto, "
                   + "    CAST(ROUND(AVG(b.gradevolezza)) AS INTEGER) AS gradevolezza, "
                   + "    CAST(ROUND(AVG(b.\"originalità\")) AS INTEGER) AS \"originalità\", "
                   + "    CAST(ROUND(AVG(b.edizione)) AS INTEGER) AS edizione, "
                   + "    NULL AS nota_stile, "
                   + "    NULL AS nota_contenuto, "
                   + "    NULL AS nota_gradevolezza, "
                   + "    NULL AS nota_originalita, "
                   + "    NULL AS nota_edizione "
                   + "FROM public.\"Libri\" AS a "
                   + "LEFT JOIN public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro "
                   + "WHERE a.autore = ? AND a.anno_pubblicazione = ? "
                   + "GROUP BY a.cod_libro, a.titolo, a.autore, a.anno_pubblicazione";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, autore);
            ps.setString(2, anno);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibri(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Cerca libri per Titolo, Autore E Anno.
     *
     * @param titolo Il titolo del libro.
     * @param autore L'autore del libro.
     * @param anno   L'anno di pubblicazione.
     * @return Una lista di {@link Libro} che corrispondono esattamente ai tre criteri.
     */
    public List<Libro> libriLibroTAA(String titolo, String autore, String anno) {
        String sql = "SELECT "
                + "    a.titolo, a.autore, a.anno_pubblicazione, "
                + "    CAST(ROUND(AVG(b.stile)) AS INTEGER) AS stile, "
                + "    CAST(ROUND(AVG(b.contenuto)) AS INTEGER) AS contenuto, "
                + "    CAST(ROUND(AVG(b.gradevolezza)) AS INTEGER) AS gradevolezza, "
                + "    CAST(ROUND(AVG(b.\"originalità\")) AS INTEGER) AS \"originalità\", "
                + "    CAST(ROUND(AVG(b.edizione)) AS INTEGER) AS edizione, "
                + "    NULL AS nota_stile, "
                + "    NULL AS nota_contenuto, "
                + "    NULL AS nota_gradevolezza, "
                + "    NULL AS nota_originalita, "
                + "    NULL AS nota_edizione "
                + "FROM public.\"Libri\" AS a "
                + "LEFT JOIN public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro "
                + "WHERE a.autore = ? AND a.anno_pubblicazione = ? AND a.titolo = ? "
                + "GROUP BY a.cod_libro, a.titolo, a.autore, a.anno_pubblicazione";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, autore);
            ps.setString(2, anno);
            ps.setString(3, titolo);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibri(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Cerca libri per Titolo E Anno.
     *
     * @param titolo Il titolo del libro.
     * @param anno   L'anno di pubblicazione.
     * @return Una lista di {@link Libro} che corrispondono ai criteri.
     */
    public List<Libro> libriLibroTA(String titolo, String anno) {
        String sql = "SELECT "
                + "    a.titolo, a.autore, a.anno_pubblicazione, "
                + "    CAST(ROUND(AVG(b.stile)) AS INTEGER) AS stile, "
                + "    CAST(ROUND(AVG(b.contenuto)) AS INTEGER) AS contenuto, "
                + "    CAST(ROUND(AVG(b.gradevolezza)) AS INTEGER) AS gradevolezza, "
                + "    CAST(ROUND(AVG(b.\"originalità\")) AS INTEGER) AS \"originalità\", "
                + "    CAST(ROUND(AVG(b.edizione)) AS INTEGER) AS edizione, "
                + "    NULL AS nota_stile, "
                + "    NULL AS nota_contenuto, "
                + "    NULL AS nota_gradevolezza, "
                + "    NULL AS nota_originalita, "
                + "    NULL AS nota_edizione "
                + "FROM public.\"Libri\" AS a "
                + "LEFT JOIN public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro "
                + "WHERE a.anno_pubblicazione = ? AND a.titolo = ? "
                + "GROUP BY a.cod_libro, a.titolo, a.autore, a.anno_pubblicazione";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, anno);
            ps.setString(2, titolo);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibri(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // =============================
    //         UTENTI & AUTH
    // =============================

    /**
     * Verifica le credenziali di un utente tramite Email e Password.
     *
     * @param email L'email dell'utente.
     * @param pass  La password dell'utente.
     * @return {@code true} se le credenziali sono corrette, {@code false} altrimenti.
     */
    public boolean UtentiRegistratiEPB(String email, String pass) {
        String sql = "select 1 from public.\"UtentiRegistrati\" where email = ? and password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Verifica se un utente specifico ha già valutato un determinato libro.
     *
     * @param idlibro L'ID del libro.
     * @param cf      Il codice fiscale dell'utente.
     * @return {@code true} se esiste già una valutazione, {@code false} altrimenti.
     */
    public boolean valutazionePresentechk(int idlibro, String cf) {
        String sql = "SELECT * FROM public.\"Valutazioni\"\r\n"
        		+ "where id_libro = ? and id_codice_fiscale = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idlibro );
            ps.setString(2, cf);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica le credenziali di un utente tramite UserID e Password.
     *
     * @param userId Lo UserID dell'utente.
     * @param pass   La password dell'utente.
     * @return {@code true} se le credenziali sono corrette, {@code false} altrimenti.
     */
    public boolean UtentiRegistratiUPB(String userId, String pass) {
        String sql = "select 1 from public.\"UtentiRegistrati\" where \"userId\" = ? and password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se un'email è già presente nel database.
     *
     * @param email L'email da controllare.
     * @return {@code true} se l'email esiste, {@code false} altrimenti.
     */
    public boolean UtentiRegistratiE(String email) {
        String sql = "select 1 from public.\"UtentiRegistrati\" where email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se uno UserID è già presente nel database.
     *
     * @param userID Lo UserID da controllare.
     * @return {@code true} se lo UserID esiste, {@code false} altrimenti.
     */
    public boolean UtentiRegistratiUser(String userID) {
        String sql = "select 1 from public.\"UtentiRegistrati\" where \"userId\" = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera il Codice Fiscale associato a un'email.
     *
     * @param email L'email dell'utente.
     * @return Il codice fiscale se trovato, {@code null} altrimenti.
     */
    public String getCF(String email) {
        String sql = "select codice_fiscale from public.\"UtentiRegistrati\" where email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("codice_fiscale");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recupera il Codice Fiscale associato a uno UserID.
     *
     * @param userid Lo UserID dell'utente.
     * @return Il codice fiscale se trovato, {@code null} altrimenti.
     */
    public String getCFU(String userid) {
        String sql = "select codice_fiscale from public.\"UtentiRegistrati\" where \"userId\" = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("codice_fiscale");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // =============================
    //      GESTIONE LIBRERIE
    // =============================

    /**
     * Recupera tutti i libri presenti nelle librerie di un utente specifico.
     * <p>Include le valutazioni e le note personali dell'utente.</p>
     *
     * @param cf Il codice fiscale dell'utente.
     * @return Una lista di {@link Libro} appartenenti alle librerie dell'utente.
     */
    public List<Libro> getLibroDaLibreria(String cf) {
    	String sql = "SELECT "
    	 		   + "    a.titolo, a.autore, a.anno_pubblicazione, "
    	 		   + "    b.stile, b.contenuto, b.gradevolezza, b.\"originalità\", b.edizione, "
    	 		   + "    e.nota_stile, e.nota_contenuto, e.nota_gradevolezza, e.nota_originalita, e.nota_edizione, "
    	 		   + "    d.id_codice_fiscale "
    	 		   + "FROM public.\"Librerie\" AS d "
    	 		   + "JOIN public.\"Libri\" AS a ON d.id_libro = a.cod_libro "
    	 		   + "LEFT JOIN public.\"Valutazioni\" AS b "
    	 		   + "    ON d.id_libro = b.id_libro AND d.id_codice_fiscale = b.id_codice_fiscale "
    	 		   + "LEFT JOIN public.\"NoteValutazioni\" AS e "
    	 		   + "    ON d.id_libro = e.id_libro AND d.id_codice_fiscale = e.cf "
    	 		   + "WHERE d.id_codice_fiscale = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibri(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Aggiorna il nome di una libreria esistente.
     *
     * @param nomeVecchio Il nome attuale della libreria.
     * @param nomeNuovo   Il nuovo nome da assegnare.
     * @param cf          Il codice fiscale del proprietario.
     * @return {@code true} se l'aggiornamento ha avuto successo, {@code false} altrimenti.
     */
    public boolean aggiornaNomeLibreria(String nomeVecchio, String nomeNuovo, String cf) {
        String sql =
            "UPDATE public.\"Librerie\" " +
            "SET nome_libreria = ? " +
            "WHERE nome_libreria = ? and id_codice_fiscale = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeNuovo);
            ps.setString(2, nomeVecchio);
            ps.setString(3, cf);

            int righe = ps.executeUpdate();
            return righe > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una libreria di un utente.
     *
     * @param nomeLibreria Il nome della libreria da eliminare.
     * @param cf           Il codice fiscale del proprietario.
     * @return {@code true} se l'eliminazione ha avuto successo, {@code false} altrimenti.
     */
    public boolean eliminaLibreria(String nomeLibreria, String cf) {
        String sql =
            "DELETE FROM public.\"Librerie\" " +
            "WHERE nome_libreria = ? and id_codice_fiscale = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeLibreria);
            ps.setString(2, cf);

            int righe = ps.executeUpdate();
            return righe > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera il codice identificativo (ID) di un libro in base ai suoi dati.
     *
     * @param libro L'oggetto libro contenente Titolo, Autore e Anno.
     * @return L'ID del libro se trovato, 0 altrimenti.
     */
    public int getCodiceLibro(Libro libro) {
        String sql =
            "SELECT cod_libro FROM public.\"Libri\" " +
            "where titolo = ? and autore = ? and anno_pubblicazione = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, libro.getTitolo());
            ps.setString(2, libro.getAutore());
            ps.setString(3, libro.getAnnoPubblicazione());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cod_libro");
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Recupera i dettagli di un libro specifico, incluse le note dell'utente.
     * <p>Utilizzato per caricare i dati completi di un libro suggerito salvato.</p>
     *
     * @param idlibro L'ID del libro.
     * @param cf      Il codice fiscale dell'utente.
     * @return Una LinkedList contenente il libro trovato.
     */
    public LinkedList<Libro> caricaSuggeritiDaDB(int idlibro, String cf) {
        String sql =
            "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, " +
            "nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione " +
            "from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"NoteValutazioni\" as c " +
            "where a.cod_libro = ? and a.cod_libro = b.id_libro and b.id_libro = c.id_libro and c.cf = ? and c.cf = b.id_codice_fiscale";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idlibro);
            ps.setString(2, cf);

            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToLibriL(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    
    /**
     * Carica tutte le librerie di un utente, organizzando i libri al loro interno.
     * <p>
     * Utilizza una query complessa con LEFT JOIN per recuperare librerie, libri, 
     * valutazioni e note in un'unica chiamata.
     * </p>
     *
     * @param cf Il codice fiscale dell'utente.
     * @return Una LinkedList di {@link Libreria}, ciascuna contenente i propri libri.
     */
    public LinkedList<Libreria> caricaLibrerie(String cf) {
        String sql = "SELECT a.nome_libreria, b.*, c.stile, c.contenuto, c.gradevolezza, c.\"originalità\", c.edizione, "
                + "d.nota_stile, d.nota_contenuto, d.nota_gradevolezza, d.nota_originalita, d.nota_edizione "
                + "FROM public.\"Librerie\" AS a "
                + "LEFT JOIN public.\"Libri\" AS b ON a.id_libro = b.cod_libro "
                + "LEFT JOIN public.\"Valutazioni\" AS c ON a.id_libro = c.id_libro AND a.id_codice_fiscale = c.id_codice_fiscale "
                + "LEFT JOIN public.\"NoteValutazioni\" AS d ON a.id_libro = d.id_libro AND a.id_codice_fiscale = d.cf "
                + "WHERE a.id_codice_fiscale = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);

            try (ResultSet rs = ps.executeQuery()) {
                return mappaResultSetInLibrerie(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    
    /**
     * Verifica se ci sono almeno 3 consigli per un determinato libro da parte di un utente.
     *
     * @param cf      Il codice fiscale dell'utente.
     * @param idLibro L'ID del libro.
     * @return {@code true} se ci sono >= 3 consigli, {@code false} altrimenti.
     */
    public boolean verificaSogliaConsigli(String cf, int idLibro) {
        String sql = "SELECT COUNT(*) FROM public.\"Consigli\" "
                   + "WHERE id_codice_fiscale = ? AND id_libro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);
            ps.setInt(2, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int numeroConsigli = rs.getInt(1);
                    return numeroConsigli >= 3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Verifica se esiste almeno un consiglio per un determinato libro.
     *
     * @param cf      Il codice fiscale dell'utente.
     * @param idLibro L'ID del libro.
     * @return {@code true} se esiste almeno 1 consiglio, {@code false} altrimenti.
     */
    public boolean verificaPresenzaConsigli(String cf, int idLibro) {
        String sql = "SELECT COUNT(*) FROM public.\"Consigli\" "
                   + "WHERE id_codice_fiscale = ? AND id_libro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);
            ps.setInt(2, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int numeroConsigli = rs.getInt(1);
                    return numeroConsigli >= 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Restituisce il numero esatto di consigli presenti per un libro.
     *
     * @param cf      Il codice fiscale dell'utente.
     * @param idLibro L'ID del libro.
     * @return Il numero di consigli trovati. In caso di errore restituisce default (3).
     */
    public int verificaNumConsigli(String cf, int idLibro) {
        String sql = "SELECT COUNT(*) FROM public.\"Consigli\" "
                   + "WHERE id_codice_fiscale = ? AND id_libro = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);
            ps.setInt(2, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 3;
    }
   
    /**
     * Scarica la lista dei libri consigliati collegati a un libro specifico.
     * <p>Calcola la media delle valutazioni per ogni libro suggerito trovato.</p>
     *
     * @param idlibro L'ID del libro di partenza.
     * @return Una LinkedList di {@link Libro} suggeriti.
     */
    public LinkedList<Libro> scaricaSuggeriti(int idlibro) {
        String sql = "SELECT "
                   + "    l.titolo, l.autore, l.anno_pubblicazione, "
                   + "    CAST(ROUND(AVG(v.stile)) AS INTEGER) AS stile, "
                   + "    CAST(ROUND(AVG(v.contenuto)) AS INTEGER) AS contenuto, "
                   + "    CAST(ROUND(AVG(v.gradevolezza)) AS INTEGER) AS gradevolezza, "
                   + "    CAST(ROUND(AVG(v.\"originalità\")) AS INTEGER) AS \"originalità\", "
                   + "    CAST(ROUND(AVG(v.edizione)) AS INTEGER) AS edizione, "
                   + "    NULL AS nota_stile, "
                   + "    NULL AS nota_contenuto, "
                   + "    NULL AS nota_gradevolezza, "
                   + "    NULL AS nota_originalita, "
                   + "    NULL AS nota_edizione "
                   + "FROM public.\"Consigli\" AS c "
                   + "JOIN public.\"Libri\" AS l ON c.id_libro_consigliato = l.cod_libro "
                   + "LEFT JOIN public.\"Valutazioni\" AS v ON l.cod_libro = v.id_libro "
                   + "WHERE c.id_libro = ? "
                   + "GROUP BY l.cod_libro, l.titolo, l.autore, l.anno_pubblicazione";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idlibro);

            try (ResultSet rs = ps.executeQuery()) {
                return new LinkedList<>(resultSetToLibri(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    
    // =============================
    //         MAPPERS
    // =============================
    
    /**
     * Metodo helper per convertire il ResultSet in una lista strutturata di Librerie.
     * <p>Gestisce il raggruppamento dei libri sotto la stessa libreria in base al nome.</p>
     * * @param rs Il ResultSet della query.
     * @return Una lista di oggetti {@link Libreria} popolati.
     * @throws SQLException se si verifica un errore SQL.
     */
    private LinkedList<Libreria> mappaResultSetInLibrerie(ResultSet rs) throws SQLException {
        LinkedList<Libreria> listaLibrerie = new LinkedList<>();

        while (rs.next()) {
            String nomeLibreria = rs.getString("nome_libreria");
            
            // 1. Cerco se la libreria esiste già nella mia lista (Raggruppamento)
            Libreria libreriaCorrente = null;
            for (Libreria lib : listaLibrerie) {
                if (lib.getNome() != null && lib.getNome().equals(nomeLibreria)) {
                    libreriaCorrente = lib;
                    break;
                }
            }

            // 2. Se non esiste, la creo e la aggiungo alla lista
            if (libreriaCorrente == null) {
                libreriaCorrente = new Libreria(nomeLibreria);
                listaLibrerie.add(libreriaCorrente);
            }

            // 3. Controllo se c'è effettivamente un libro nella riga corrente
            if (rs.getString("titolo") != null) {
                Libro libro = new Libro();
                
                libro.setTitolo(rs.getString("titolo"));
                libro.setAutore(rs.getString("autore"));
                libro.setAnnoPubblicazione(rs.getString("anno_pubblicazione"));

                libro.setStile(rs.getInt("stile"));
                libro.setContenuto(rs.getInt("contenuto"));
                libro.setGradevolezza(rs.getInt("gradevolezza"));
                libro.setOriginalita(rs.getInt("originalità")); 
                libro.setEdizione(rs.getInt("edizione"));
              
                if (rs.getString("nota_stile") != null) 
                    libro.setNoteStile(rs.getString("nota_stile"));
                
                if (rs.getString("nota_contenuto") != null) 
                    libro.setNoteContenuto(rs.getString("nota_contenuto"));
                
                if (rs.getString("nota_gradevolezza") != null) 
                    libro.setNoteGradevolezza(rs.getString("nota_gradevolezza"));
                
                if (rs.getString("nota_originalita") != null) 
                    libro.setNoteOriginalita(rs.getString("nota_originalita"));
                
                if (rs.getString("nota_edizione") != null) 
                    libro.setNoteEdizione(rs.getString("nota_edizione"));

                // 4. Aggiungo il libro alla lista interna della libreria trovata
                libreriaCorrente.getContenuto().add(libro);
            }
        }

        return listaLibrerie;
    }
    
    /**
     * Recupera le note (testuali) di un libro e le associa agli utenti che le hanno scritte.
     * * @param idlibro L'ID del libro.
     * @return Un oggetto {@link Libro} popolato con le liste di note.
     */
    public Libro caricaNote(int idlibro) {
        String sql = "SELECT a.*, b.\"userId\" "
                   + "FROM public.\"NoteValutazioni\" AS a "
                   + "JOIN public.\"UtentiRegistrati\" AS b ON a.cf = b.codice_fiscale "
                   + "WHERE a.id_libro = ?";

        Libro libro = new Libro();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idlibro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String userIdAutore = rs.getString("userId");

                    String notaStile = rs.getString("nota_stile");
                    if (notaStile != null && !notaStile.isEmpty()) {
                        libro.setNoteStile(notaStile);     
                        libro.setListNoteNoteStile(userIdAutore); 
                    }

                    String notaContenuto = rs.getString("nota_contenuto");
                    if (notaContenuto != null && !notaContenuto.isEmpty()) {
                        libro.setNoteContenuto(notaContenuto);
                        libro.setListNoteContenuto(userIdAutore);
                    }

                    String notaGradevolezza = rs.getString("nota_gradevolezza");
                    if (notaGradevolezza != null && !notaGradevolezza.isEmpty()) {
                        libro.setNoteGradevolezza(notaGradevolezza);
                        libro.setListNoteGradevolezza(userIdAutore);
                    }

                    String notaOriginalita = rs.getString("nota_originalita");
                    if (notaOriginalita != null && !notaOriginalita.isEmpty()) {
                        libro.setNoteOriginalita(notaOriginalita);
                        libro.setListNoteOriginalita(userIdAutore);
                    }

                    String notaEdizione = rs.getString("nota_edizione");
                    if (notaEdizione != null && !notaEdizione.isEmpty()) {
                        libro.setNoteEdizione(notaEdizione);
                        libro.setListNoteEdizione(userIdAutore);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Libro();
        }

        return libro;
    }
    
    /**
     * Converte un ResultSet in una Lista (ArrayList) di Libri.
     * <p>Metodo statico di utilità usato dalle query di ricerca.</p>
     *
     * @param result Il ResultSet da convertire.
     * @return Una lista di oggetti {@link Libro}.
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet.
     */
    public static List<Libro> resultSetToLibri(ResultSet result) throws SQLException {
        List<Libro> libri = new ArrayList<>();
        if (result == null) return libri;

        while (result.next()) {
            Libro libro = new Libro();
            
            // --- Dati base del libro ---
            libro.setTitolo(result.getString("titolo"));
            libro.setAutore(result.getString("autore"));
            libro.setAnnoPubblicazione(result.getString("anno_pubblicazione"));

            // --- Valutazioni (Numeriche) ---
            libro.setStile(result.getInt("stile"));
            libro.setContenuto(result.getInt("contenuto"));
            libro.setGradevolezza(result.getInt("gradevolezza"));
            libro.setOriginalita(result.getInt("originalità")); 
            libro.setEdizione(result.getInt("edizione"));

            // --- Note (Stringhe) - FIX PER EVITARE "null" ---
            String notaStile = result.getString("nota_stile");
            if (notaStile != null) libro.setNoteStile(notaStile);

            String notaContenuto = result.getString("nota_contenuto");
            if (notaContenuto != null) libro.setNoteContenuto(notaContenuto);

            String notaGradevolezza = result.getString("nota_gradevolezza");
            if (notaGradevolezza != null) libro.setNoteGradevolezza(notaGradevolezza);

            String notaOriginalita = result.getString("nota_originalita");
            if (notaOriginalita != null) libro.setNoteOriginalita(notaOriginalita);

            String notaEdizione = result.getString("nota_edizione");
            if (notaEdizione != null) libro.setNoteEdizione(notaEdizione);

            libro.setControllo(true);
            libri.add(libro);
        }
        return libri;
    }
    
    /**
     * Converte un ResultSet in una LinkedList di Libri.
     * <p>Variante che restituisce specificamente una LinkedList.</p>
     *
     * @param result Il ResultSet da convertire.
     * @return Una LinkedList di oggetti {@link Libro}.
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet.
     */
    public static LinkedList<Libro> resultSetToLibriL(ResultSet result) throws SQLException {
        LinkedList<Libro> libri = new LinkedList<>();
        if (result == null) return libri;

        while (result.next()) {
            Libro libro = new Libro();
            libro.setTitolo(result.getString("titolo"));
            libro.setAutore(result.getString("autore"));
            libro.setAnnoPubblicazione(result.getString("anno_pubblicazione"));
            libro.setStile(result.getInt("stile"));
            libro.setContenuto(result.getInt("contenuto"));
            libro.setGradevolezza(result.getInt("gradevolezza"));
            libro.setOriginalita(result.getInt("originalità"));
            libro.setEdizione(result.getInt("edizione"));
            libro.setNoteStile(result.getString("nota_stile"));
            libro.setNoteContenuto(result.getString("nota_contenuto"));
            libro.setNoteGradevolezza(result.getString("nota_gradevolezza"));
            libro.setNoteOriginalita(result.getString("nota_originalita"));
            libro.setNoteEdizione(result.getString("nota_edizione"));
            libro.setControllo(true);
            libri.add(libro);
        }
        return libri;
    }

    /**
     * Converte un ResultSet in una Lista di Librerie vuote.
     * * @param result Il ResultSet da convertire.
     * @return Una lista di oggetti {@link Libreria}.
     * @throws SQLException se si verifica un errore durante la lettura del ResultSet.
     */
    public static List<Libreria> resultSetToLibreria(ResultSet result) throws SQLException {
        List<Libreria> librerie = new ArrayList<>();
        if (result == null) return librerie;

        while (result.next()) {
            Libreria libreria = new Libreria();
            libreria.setNome(result.getString("nome_libreria"));
            libreria.setControllo(true);
            librerie.add(libreria);
        }
        return librerie;
    }
}