package ServerBR;

import java.sql.ResultSet;
import java.sql.Statement;

public class DbInsert extends DataBase {


    //private Statement statement = dbconnect.connect();
    private String query;

    public DbInsert()
    {
        super();
    }


    public void loadUtentiRegistrati(String nome, String cognome, String cf, String email, String uid, String password)
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
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadLibri(String titolo, String autore, int annoP, String editore, String categoria, int codLibro) {

        try {
            query = "insert into public.\"Libri\" values (";
            query = query + "'" + titolo + "',";
            query = query + "'" + autore + "',";
            query = query + "'" + annoP + "',";
            query = query + "'" + editore + "',";
            query = query + "'" + categoria + "',";
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

    public void loadLibrerie(int codLibreria, String nomeLibreria, int idLibro, String idcf) {

        try {
            query = "insert into public.\"Librerie\" values (";
            query = query + "'" + codLibreria + "',";
            query = query + "'" + nomeLibreria + "',";
            query = query + "'" + idLibro + "',";
            query = query + "'" + idcf + "')";
            statement.executeQuery(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadValutazioni(int idLibro, String idcf, int stile, int contenuto, int gradevolezza, int originalita, int edizione, int votoF, String notaStile, String notaContenuto, String notaGradevolezza, String notaOriginalita, String notaEdizione) {

        try {
            query = "insert into public.\"Valutazioni\" values (";
            query = query + "'" + idLibro + "',";
            query = query + "'" + idcf + "',";
            query = query + "'" + stile + "',";
            query = query + "'" + contenuto + "',";
            query = query + "'" + gradevolezza + "',";
            query = query + "'" + originalita + "',";
            query = query + "'" + edizione + "',";
            query = query + "'" + votoF + "',";
            query = query + "'" + notaStile + "',";
            query = query + "'" + notaContenuto + "',";
            query = query + "'" + notaGradevolezza + "',";
            query = query + "'" + notaOriginalita + "',";
            query = query + "'" + notaEdizione + "')";
            statement.executeQuery(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
