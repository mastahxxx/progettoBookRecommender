
package ServerBR;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;

public class DbQuery extends DataBase {

    private Connection connection = null;

    public DbQuery(){

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
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
    
    public List<Libro> libriLibroAA(String autore, int anno)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo from public.\"Libri\" where autore = ? and anno_pubblicazione = ?;"; 

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, autore);
            pstm.setInt(2, anno);
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
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalità, nota_edizione, b.id_codice_fiscale \r\n"
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
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalità, nota_edizione, b.id_codice_fiscale \r\n"
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
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalità, nota_edizione, b.id_codice_fiscale \r\n"
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
    
    public List<Libro> getLibroDaLibreria(String nomeLibreria, String email)
    {
        ResultSet result;
        result = null;
        String query;
        List<Libro> metreturn = null;
        query = "select titolo, autore, anno_pubblicazione, stile, contenuto, gradevolezza, originalità, edizione, nota_stile, nota_contenuto, nota_gradevolezza, nota_originalità, nota_edizione, b.id_codice_fiscale \r\n"
        		+ "from public.\"Libri\" as a, public.\"Valutazioni\" as b, public.\"UtentiRegistrati\" as c, public.\"Librerie\" as d\r\n"
        		+ "where d.nome_libreria = ? and c.email = ? and c.codice_fiscale = d.id_codice_fiscale and d.id_libro = a.cod_libro \r\n"
        		+ "and a.cod_libro = b.id_libro";

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, nomeLibreria);
            pstm.setString(2, email);
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
            libro.setNoteStile(result.getString("nota_stile"), result.getString("id_codice_fiscale"));
            libro.setNoteContenuto(result.getString("nota_contenuto"), result.getString("id_codice_fiscale"));
            libro.setNoteGradevolezza(result.getString("nota_gradevolezza"), result.getString("id_codice_fiscale"));
            libro.setNoteOriginalita(result.getString("nota_originalità"), result.getString("id_codice_fiscale"));
            libro.setNoteEdizione(result.getString("nota_edizione"), result.getString("id_codice_fiscale"));
            libro.setControllo(true);

            libri.add(libro);                   // Aggiungi l’oggetto alla lista
        }
        return libri; // Ritorna la lista (vuota se non ci sono risultati)
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


    
    //Andre utilizza questo metodo per impostare i libri
    //matte se io ti passo una stringa poi lo richiami te nel server è inutile richiamarlo per ogni query che viene fatta incodizionalmente dal risultato
    /* 
    private Libro impostaParametriLibro(Libro l, String result) {
    	Libro libro;
    	String r = result.split(";");
    	if(!result.eqauls("")) {
    		//mancano i numeri della posizione del vettore perchè non so in che posizioone finiscono i vari valori
    		libro.setTitolo(r[]);
    		libro.setAutore(r[]);
    		libro.setAnnoPublicazione(r[]);
    		libro.setStile(r[])
    		libro.setContenuto(r[]);
    		libro.setGradevolezza(r[]);
    		libro.setsetOriginalita(r[]);
    		libro.setEdizione(r[]);
    		libro.setNoteStile(r[]);
    		libro.setNoteContenuto(r[]);
    		libro.setNoteGradevolezza(r[]);
    		libro.setNoteOriginalita(r[]);
    		libro.setNoteEdizione(r[]);
    		libro.setControllo(true);
    		//bisogna gestire i libri consigliuati per il libro ricercato dal client
    	}
    	else {
    		libro.setControllo(false);
    	}
		
		return libro;
    }

*/
}
