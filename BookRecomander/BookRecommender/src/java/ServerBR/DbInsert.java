package ServerBR;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import ClassiCondivise.Libro;

public class DbInsert extends DataBase {


    //private Statement statement = dbconnect.connect();
    private String query;

    public DbInsert()
    {
        super();
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
            query = "insert into public.\"Librerie\" values (";
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
    
    public boolean loadConsigliPerLibroInDb(String titolo,LinkedList<Libro> suggeriti) {

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
