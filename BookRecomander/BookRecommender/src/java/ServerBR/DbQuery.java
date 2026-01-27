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
    public List<Libro> libriLibro(String param) {
        String sql = "select * from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"NoteValutazioni\" as c\r\n"
        		+ "where titolo = ? or autore = ? and a.cod_libro = b.id_libro and a.cod_libro = c.id_libro";

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
        String sql = "select * from public.\"Libri\" where autore = ? and anno_pubblicazione = ?";

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
        String sql = "select * from public.\"Libri\" where autore = ? and anno_pubblicazione = ? and titolo = ?";

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
        String sql = "select * from public.\"Libri\" where anno_pubblicazione = ? and titolo = ?";

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

    // =============================
    //     LIBRERIE / LIBRI VARI
    // =============================
    public List<Libro> getLibroDaLibreria(String cf) {
        String sql =
            "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, " +
            "nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, b.id_codice_fiscale " +
            "from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"UtentiRegistrati\" as c, public.\"Librerie\" as d " +
            "where c.codice_fiscale = ? and c.codice_fiscale = d.id_codice_fiscale and d.id_libro = a.cod_libro " +
            "and a.cod_libro = b.id_libro";

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

    // =============================
    //     MAPPER RESULTSET -> OGGETTI
    // =============================
    public static List<Libro> resultSetToLibri(ResultSet result) throws SQLException {
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
