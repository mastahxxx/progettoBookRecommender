package ServerBR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DbInsert {

    // ==== CONFIG (coerente con DbQuery) ====
    private static final String URL = "jdbc:postgresql://localhost:5432/bookRecommender";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private final Connection connection;

    public DbInsert() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connessione DB OK (DbInsert)");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Connessione DB fallita (DbInsert)", e);
        }
    }

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

    public boolean loadLibrerie(String idcf, String nomeLibreria, int idLibro) {
        String sql = "INSERT INTO public.\"Librerie\" VALUES (?, ?, ?)";

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
