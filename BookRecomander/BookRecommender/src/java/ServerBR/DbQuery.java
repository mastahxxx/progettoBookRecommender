package ServerBR;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;

public class DbQuery {

    // ==== CONFIG (cambia solo qui se serve) ====
    private static final String URL = "jdbc:postgresql://localhost:5432/bookRecommender";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private final Connection connection;

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
   /* public List<Libro> libriLibro(String param) {
        String sql = "SELECT *\r\n"
        + "FROM \r\n"
        + "    public.\"Libri\" AS a\r\n"
        + "LEFT JOIN \r\n"
        + "    public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro\r\n"
        + "LEFT JOIN \r\n"
        + "    public.\"NoteValutazioni\" AS c ON b.id_libro = c.id_libro \r\n"
        + "    AND b.id_codice_fiscale = c.cf\r\n"
        + "WHERE a.titolo =? or autore =?";

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
    } */
    
    public List<Libro> libriLibro(String param) {
        // FIX: Uso SELECT esplicita invece di *. 
        // Questo permette al ResultSet di trovare sicuramente le colonne "stile", "nota_stile", ecc.
       /* String sql = "SELECT "
                   + "  a.titolo, a.autore, a.anno_pubblicazione, "
                   + "  b.stile, b.contenuto, b.gradevolezza, b.\"originalità\", b.edizione, "
                   + "  c.nota_stile, c.nota_contenuto, c.nota_gradevolezza, c.nota_originalita, c.nota_edizione "
                   + "FROM public.\"Libri\" AS a "
                   + "LEFT JOIN public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro "
                   + "LEFT JOIN public.\"NoteValutazioni\" AS c ON b.id_libro = c.id_libro AND b.id_codice_fiscale = c.cf "
                   + "WHERE a.titolo = ? OR a.autore = ?"; */
    	
    	String sql = "SELECT "
                + "  a.titolo, a.autore, a.anno_pubblicazione, "
                + "  0 AS stile, 0 AS contenuto, 0 AS gradevolezza, 0 AS \"originalità\", 0 AS edizione, "
                + "  NULL AS nota_stile, NULL AS nota_contenuto, NULL AS nota_gradevolezza, "
                + "  NULL AS nota_originalita, NULL AS nota_edizione "
                + "FROM public.\"Libri\" AS a "
                + "WHERE a.titolo = ? OR a.autore = ?";

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
    
    public List<Libro> libriLibroAA(String autore, String anno) {
        String sql = "SELECT *\r\n"
        		+ "FROM \r\n"
        		+ "    public.\"Libri\" AS a\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"NoteValutazioni\" AS c ON b.id_libro = c.id_libro \r\n"
        		+ "    AND b.id_codice_fiscale = c.cf\r\n"
        		+ "WHERE a.autore =? and a.anno_pubblicazione =?";

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

    public List<Libro> libriLibroTAA(String titolo, String autore, String anno) {
        String sql = "SELECT *\r\n"
        		+ "FROM \r\n"
        		+ "    public.\"Libri\" AS a\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"NoteValutazioni\" AS c ON b.id_libro = c.id_libro \r\n"
        		+ "    AND b.id_codice_fiscale = c.cf\r\n"
        		+ "WHERE a.autore =? and a.anno_pubblicazione =? and a.titolo = ?";

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

    public List<Libro> libriLibroTA(String titolo, String anno) {
        String sql = "SELECT *\r\n"
        		+ "FROM \r\n"
        		+ "    public.\"Libri\" AS a\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"Valutazioni\" AS b ON a.cod_libro = b.id_libro\r\n"
        		+ "LEFT JOIN \r\n"
        		+ "    public.\"NoteValutazioni\" AS c ON b.id_libro = c.id_libro \r\n"
        		+ "    AND b.id_codice_fiscale = c.cf\r\n"
        		+ "WHERE a.anno_pubblicazione =? and a.titolo = ?";

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
    //          UTENTI
    // =============================
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
     * Metodo rinominato per convertire il ResultSet in una lista di Librerie
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
            // (utile se ci sono librerie vuote o join nulli)
            if (rs.getString("titolo") != null) {
                Libro libro = new Libro();
                
        
                libro.setTitolo(rs.getString("titolo"));
                libro.setAutore(rs.getString("autore"));
                libro.setAnnoPubblicazione(rs.getString("anno_pubblicazione"));

              
                libro.setStile(rs.getInt("stile"));
                libro.setContenuto(rs.getInt("contenuto"));
                libro.setGradevolezza(rs.getInt("gradevolezza"));
                libro.setOriginalita(rs.getInt("originalità")); // Nome colonna con accento nel DB
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

                    //  NOTE STILE 
                    String notaStile = rs.getString("nota_stile");
                    if (notaStile != null && !notaStile.isEmpty()) {
                        libro.setNoteStile(notaStile);     
                        libro.setListNoteNoteStile(userIdAutore); 
                    }

                    //  NOTE CONTENUTO 
                    String notaContenuto = rs.getString("nota_contenuto");
                    if (notaContenuto != null && !notaContenuto.isEmpty()) {
                        libro.setNoteContenuto(notaContenuto);
                        libro.setListNoteContenuto(userIdAutore);
                    }

                    //  NOTE GRADEVOLEZZA 
                    String notaGradevolezza = rs.getString("nota_gradevolezza");
                    if (notaGradevolezza != null && !notaGradevolezza.isEmpty()) {
                        libro.setNoteGradevolezza(notaGradevolezza);
                        libro.setListNoteGradevolezza(userIdAutore);
                    }

                    //  NOTE ORIGINALITÀ 
                    String notaOriginalita = rs.getString("nota_originalita");
                    if (notaOriginalita != null && !notaOriginalita.isEmpty()) {
                        libro.setNoteOriginalita(notaOriginalita);
                        libro.setListNoteOriginalita(userIdAutore);
                    }

                    //  NOTE EDIZIONE
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
    

    // =============================
    //     MAPPER RESULTSET -> OGGETTI
    // =============================
  /*  public static List<Libro> resultSetToLibri(ResultSet result) throws SQLException {
        List<Libro> libri = new ArrayList<>();
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
    } */
    
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
            // getInt restituisce 0 se il campo è NULL, il che non rompe il codice
            libro.setStile(result.getInt("stile"));
            libro.setContenuto(result.getInt("contenuto"));
            libro.setGradevolezza(result.getInt("gradevolezza"));
            libro.setOriginalita(result.getInt("originalità")); // OK l'accento se nel DB è così
            libro.setEdizione(result.getInt("edizione"));

            // --- Note (Stringhe) - FIX PER EVITARE "null" ---
            
            String notaStile = result.getString("nota_stile");
            if (notaStile != null) {
                libro.setNoteStile(notaStile);
            }

            String notaContenuto = result.getString("nota_contenuto");
            if (notaContenuto != null) {
                libro.setNoteContenuto(notaContenuto);
            }

            String notaGradevolezza = result.getString("nota_gradevolezza");
            if (notaGradevolezza != null) {
                libro.setNoteGradevolezza(notaGradevolezza);
            }

            String notaOriginalita = result.getString("nota_originalita");
            if (notaOriginalita != null) {
                libro.setNoteOriginalita(notaOriginalita);
            }

            String notaEdizione = result.getString("nota_edizione");
            if (notaEdizione != null) {
                libro.setNoteEdizione(notaEdizione);
            }

            libro.setControllo(true);
            libri.add(libro);
        }
        return libri;
    }
    
    
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
