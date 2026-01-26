
package ServerBR;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;

public class DbQuery extends DataBase {

    private Connection connection = null;

    public DbQuery(){

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
        	System.err.println("--- ERRORE DATABASE ---");
            System.err.println("Messaggio: " + e.getMessage());
            System.err.println("Codice SQLState: " + e.getSQLState());
            System.err.println("Codice Errore Nativo: " + e.getErrorCode());
            ;
        }
    }

    public void queryProva()
    {
        try {
            ResultSet result = statement.executeQuery("SELECT nome FROM public.\"UtentiRegistrati\"");
            result.next();
            System.out.println(result.getString(1));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public List<Libro> libriLibro(String param)
    {
        ResultSet result;
        result = null;
        String query;
        List <Libro> metreturn = null;
        query = "select * from public.\"Libri\" where titolo = ? or autore = ?;"; 

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, param);
            pstm.setString(2, param);
            result = statement.executeQuery(query);
            result.next();
            System.out.println(result.getString(1));
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;
    }
    
    public List<Libro> libriLibroAA(String autore, String anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select * from public.\"Libri\" where autore = ? and anno_pubblicazione = ?;"; 

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, autore);
            pstm.setString(2, anno);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> libriLibroTAA(String titolo, String autore, String anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select * from public.\"Libri\" where autore = ? and anno_pubblicazione = ? and titolo = ?;"; 

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, autore);
            pstm.setString(2, anno);
            pstm.setString(3, titolo);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> libriLibroTA(String titolo, String anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select * from public.\"Libri\" where anno_pubblicazione = ? and titolo = ?;"; 

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, anno);
            pstm.setString(2, titolo);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public String UtentiRegistratiEP(String email, String pass)
    {
        ResultSet result;
        result = null;
        String query;
        String metreturn = null;
        query = "select codice_fiscale from public.\"UtentiRegistrati\" where email = ? and password = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, email);
            pstm.setString(2, pass);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToString(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public boolean UtentiRegistratiEPB(String email, String pass)
    {
        ResultSet result;
        result = null;
        String query;
        boolean metreturn = false;
        query = "select * from public.\"UtentiRegistrati\" where email = ? and password = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, email);
            pstm.setString(2, pass);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = result.next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public boolean UtentiRegistratiUPB(String user, String pass)
    {
        ResultSet result;
        result = null;
        String query;
        boolean metreturn = false;
        query = "select * from public.\"UtentiRegistrati\" where userId = ? and password = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, user);
            pstm.setString(2, pass);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = result.next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public boolean UtentiRegistratiE(String email)
    {
        ResultSet result;
        result = null;
        String query;
        boolean metreturn = false;
        query = "select email from public.\"UtentiRegistrati\" where email = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, email);
            result = statement.executeQuery(query);
            metreturn = result.next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public boolean UtentiRegistratiUser(String userID)
    {
        ResultSet result;
        result = null;
        String query;
        boolean metreturn = false;
        query = "select email from public.\"UtentiRegistrati\" where userId = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, userID);
            result = statement.executeQuery(query);
            metreturn = result.next();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public String MediaValutazioni(String idLibro)
    {
        ResultSet result;
        result = null;
        String query;
        String metreturn = null;
        query = "select avg(stile), avg(contenuto), avg(gradevolezza), avg(originalità), avg(edizione) from public.\"Valutazioni\" where id_libro = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, idLibro);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToString(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> getLibroTOA(String titoloAutore)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, b.id_codice_fiscale \r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b\r\n"
        		+ "where titolo = ? or autore = ? and cod_libro = id_libro";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, titoloAutore);
            pstm.setString(2, titoloAutore);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> getLibroAEA(String autore, String anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, b.id_codice_fiscale \r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b\r\n"
        		+ "where autore = ? and anno_pubblicazione = ? and cod_libro = id_libro";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, autore);
            pstm.setString(2, anno);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> getLibroTAA(String titolo, String autore, String anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, b.id_codice_fiscale \r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b\r\n"
        		+ "where titolo = ? and autore = ? and anno_pubblicazione = ? and cod_libro = id_libro";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, titolo);
            pstm.setString(2, autore);
            pstm.setString(3, anno);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libreria> getLibreriaNome(String nomeLibreria)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libreria> metreturn = null;
        query = "select nome_libreria from public.\"Librerie\" where nome_libreria = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, nomeLibreria);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibreria(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libreria> getLibreriaUtente(String email)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libreria> metreturn = null;
        query = "select nome_libreria from public.\"Librerie\", public.UtentiRegistrati where email = ? and codice_fiscale = id_codice_fiscale";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, email);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibreria(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public List<Libro> getLibroDaLibreria(String cf)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione, b.id_codice_fiscale \r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"UtentiRegistrati\" as c, public.\"Librerie\" as d\r\n"
        		+ "where c.codice_fiscale = ? and c.codice_fiscale = d.id_codice_fiscale and d.id_libro = a.cod_libro \r\n"
        		+ "and a.cod_libro = b.id_libro";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, cf);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibri(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public boolean aggiornaNomeLibreria(String nomeLibreria, String nomeAggiornato, String cf)
    {
    	ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "UPDATE public.\"Librerie\"\r\n"
        		+ "SET nome_libreria = ?\r\n"
        		+ "WHERE nome_libreria = ? and id_codice_fiscale = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, nomeLibreria);
            pstm.setString(2, nomeAggiornato);
            pstm.setString(3, cf);
            result = statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
          
    }
    
    public boolean eliminaLibreria(String nomeLibreria, String cf)
    {
    	ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "DELETE FROM public.\"Librerie\"\r\n"
        		+ "WHERE nome_libreria = ? and id_codice_fiscale = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, nomeLibreria);
            pstm.setString(2, cf);
            result = statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
          
    }
    
    public String getCF(String email)
    {
    	ResultSet result;
        result = null;
        String query;
        String metreturn = null;
        query = "select codice_fiscale from public.\"UtentiRegistrati\" where email = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, email);
            result = statement.executeQuery(query);
            result.next();
            metreturn = result.getString("codice_fiscale");
            return metreturn;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return metreturn;
          
    }
    
    public String getCFU(String userid)
    {
    	ResultSet result;
        result = null;
        String query;
        String metreturn = null;
        query = "select codice_fiscale from public.\"UtentiRegistrati\" where userId = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, userid);
            result = statement.executeQuery(query);
            result.next();
            metreturn = result.getString("codice_fiscale");
            return metreturn;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return metreturn;
          
    }
    
    
    
    
    public static String resultSetToString(ResultSet result) throws SQLException {
        if (result == null) {
        	String res ="";
            return res;
        }

        StringBuilder sb = new StringBuilder();
        boolean hasRows = false;

        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (result.next()) {
            hasRows = true;
            for (int i = 1; i <= columnCount; i++) {
                Object value = result.getObject(i);
                if (value != null) {
                    sb.append(value.toString());
                }
                if (i < columnCount) {
                    sb.append(";");
                }
            }
            sb.append(";"); // separatore tra righe
        }

        if (!hasRows) {
            return null;
        }

        // Rimuove eventuale ultimo ";" in eccesso
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
    
    //da scrivere query per estrarre idlibro
    public int getCodiceLibro(Libro libro)
    {
        ResultSet result;
        result = null;
        String query;
        int metreturn = 0;
        String titolo = libro.getTitolo();
        String autore = libro.getAutore();
        String anno = libro.getAnnoPubblicazione();
        
        		query = "SELECT cod_libro FROM public.\"Libri\"\r\n"
        		+ "where titolo = ? and autore = ? and anno_pubblicazione = ?";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, titolo);
            pstm.setString(2, autore);
            pstm.setString(3, anno);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            result.next();
            metreturn = result.getInt(4);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;  
    }
    
    public LinkedList<Libro> caricaSuggeritiDaDB(int idlibro, String cf)
    {
        ResultSet result;
        result = null;
        String query;
        LinkedList<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalita, nota_edizione\r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"NoteValutazioni\" as c\r\n"
        		+ "where a.cod_libro = ? and a.cod_libro = b.id_libro and b.id_libro = c.id_libro and c.cf = ? and c.cf = b.id_codice_fiscale";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, idlibro);
            pstm.setString(2, cf);
            result = statement.executeQuery(query);
            DbQuery classe = new DbQuery();
            metreturn = classe.resultSetToLibriL(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return metreturn;
    }
    
    public static List<Libro> resultSetToLibri(ResultSet result) throws SQLException {
        List<Libro> libri = new ArrayList<>();   // Lista che conterrà i risultati

        if (result == null) return libri;           // Se il ResultSet è nullo, restituisci lista vuota

        while (result.next()) {
            Libro libro = new Libro();          // Crea un nuovo oggetto Libro per ogni riga

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

            libri.add(libro);                   // Aggiungi l’oggetto alla lista
        }
        return libri; // Ritorna la lista (vuota se non ci sono risultati)
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
        List<Libreria> librerie = new ArrayList<>();   // Lista che conterrà i risultati

        if (result == null) return librerie;           // Se il ResultSet è nullo, restituisci lista vuota

        while (result.next()) {
            Libreria libreria = new Libreria();          // Crea un nuovo oggetto Libro per ogni riga

            libreria.setNome(result.getString("nome_libreria"));
            libreria.setControllo(true);

            librerie.add(libreria);                   // Aggiungi l’oggetto alla lista
        }
        return librerie; // Ritorna la lista (vuota se non ci sono risultati)
    }
   
}
