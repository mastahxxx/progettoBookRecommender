package ServerBR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import ClassiCondivise.Libro;

public class DbInsert extends DataBase {


    //private Statement statement = dbconnect.connect();
    private String query;
    protected String url = "jdbc:postgresql://localhost/bookReccomender";
    protected String user = "postgres";
    protected String password = "123";
    protected Statement statement;

    public DbInsert()
    {
    	try {

            Connection connection = DriverManager.getConnection(url, user, password);
            if(connection != null)
            {
                System.out.println("connessione eseguita con successo");
                statement = connection.createStatement();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }   
    }


    public boolean loadUtentiRegistrati(String nome, String cognome, String cf, String email, String uid, String password)
    {

        try {
            query = "insert into public.\"UtentiRegistrati\" values (";
            query = query + "'" + nome + "',";
            query = query + "'" + cognome + "',";
            query = query + "'" + cf + "',";
            query = query + "'" + email + "',";
            query = query + "'" + uid + "',";
            query = query + "'" + password + "')";
            statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void loadLibri(String titolo, String autore, int annoP, int codLibro) {

        try {
            query = "insert into public.\"Libri\" values (";
            query = query + "'" + titolo + "',";
            query = query + "'" + autore + "',";
            query = query + "'" + annoP + "',";
            query = query + "'" + codLibro + "')";
            statement.executeQuery(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadConsigli(int idlibro, String idcf) {

        try {
            query = "insert into public.\"Consigli\" values (";
            query = query + "'" + idlibro + "',";
            query = query + "'" + idcf + "')";
            statement.executeQuery(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // bisogna convertire userid a codice fiscale
    public boolean loadLibrerie(String idcf,String nomeLibreria, int idLibro) {

        try {
            query = "insert into public.\"Librerie\" values (";
            query = query + "'" + nomeLibreria + "',";
            query = query + "'" + idLibro + "',";
            query = query + "'" + idcf + "')";
            statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean loadValutazioniNote(int idLibro, String cf, String notaStile, String notaContenuto, String notaGradevolezza,String notaOriginalita, String notaEdizione ) {

        try {
            query = "insert into public.\"NoteValutazioni\" values (";
            query = query + "'" + notaStile + "',";
            query = query + "'" + notaContenuto + "',";
            query = query + "'" + notaGradevolezza + "',";
            query = query + "'" + notaOriginalita + "',";
            query = query + "'" + notaEdizione + "',";
            query = query + "'" + cf + "',";
            query = query + "'" + idLibro + "')";
            statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean loadConsigliPerLibroInDb(int idlibro ,String cf, int idlibroc) {

        try {
            query = "insert into public.\"Librerie\" values (";
            query = query + "'" + idlibro + "',";
            query = query + "'" + cf + "',";
            query = query + "'" + idlibroc + "')";
            statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadValutazioni(int idLibro, String idcf, int stile, int contenuto, int gradevolezza, int originalita, int edizione) {

        try {
            query = "insert into public.\"Valutazioni\" values (";
            query = query + "'" + idLibro + "',";
            query = query + "'" + idcf + "',";
            query = query + "'" + stile + "',";
            query = query + "'" + contenuto + "',";
            query = query + "'" + gradevolezza + "',";
            query = query + "'" + originalita + "',";
            query = query + "'" + edizione + "')";
            statement.executeQuery(query);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
