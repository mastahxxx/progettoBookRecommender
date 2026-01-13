

package ServerBR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;

//import BookRecommender.src.main.java.ClassiCondivise.String;

import java.sql.ResultSet;

public class DataBase {

    protected String url = "jdbc:postgresql://localhost/BookReccomenderDB";
    protected String user = "entry";
    protected String password = "pass";
    protected Statement statement;
    private DbQuery dbq;
    private DbInsert dbi;
    


    public DataBase(){
        dbq = new DbQuery();
        dbi = new DbInsert();
  
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
    
    public synchronized List<Libro> cercaLibro(Libro l){
    	String titolo = l.getTitolo();
    	String autore = l.getAutore();
    	String anno = l.getAnnoPubblicazione();
    	List <Libro> ris = new LinkedList();
    	if(titolo != "" && autore != "" && anno != "") {
    		ris = dbq.libriLibroTAA(titolo, autore, anno); //io devo fare il metodo e bisogna fare un casting ad int(anno)
    	}
    	else {
    		if(titolo != "" && anno != "") {
        		ris = dbq.libriLibroTA(titolo, anno);	//io devo fare il metodo e bisogna fare un casting ad int(anno)
        	}
    		else {
    			if(autore != "" && anno != "") {
    				ris = dbq.libriLibroAA(autore, anno);//bisogna fare un casting ad int(anno)
    			}
    			else {
    				if(titolo != "") {
    					ris = dbq.libriLibro(titolo);
    				}
    				else {
    					if(titolo != "") {
        					ris = dbq.libriLibro(autore);
        				}
    				}
    			}
    		} 		
    	}
    	return ris;	
    }
    
    public synchronized List<Libro> caricaLibrerie(UtenteRegistrato u){
    	List <Libro> ris = new LinkedList();
    	String userId = u.getUserId();
    	String cf = dbq.getCFU(userId);
    	ris = dbq.getLibroDaLibreria(cf); //metodo che restituisce una lista contenente tutti i libri presenti in tutte le librerie del utente
    	return ris;
    }
    
    public synchronized boolean controllaEmail(UtenteRegistrato u){
    	String mail = u.getMail();
    	boolean esito = dbq.UtentiRegistratiE(mail); //metodo che restituisce true se la mail non è presente nel db
    	return esito;	
    }
    
    public synchronized boolean controllaUserId(UtenteRegistrato u){
    	String userId = u.getUserId();
    	boolean esito = dbq.UtentiRegistratiUser(userId); //metodo che restituisce true se lo userId non è presente nel db
    	return esito;	
    }
    
        
    public synchronized boolean insertUtente(UtenteRegistrato u) {
    	String nomeCognome = u.getNomeCognome();
    	String codiceFiscale = u.getCodiceFiscale();
    	String mail = u.getMail();
    	String user = u.getUserId();
    	String password = u.getPassoword();
    	String[] split = nomeCognome.split(" ");
    	boolean esito = dbi.loadUtentiRegistrati(split[0],split[1], codiceFiscale, mail, user, password); //l'utente viene inserito all'interno del db
    	return esito;
    }
    
    public synchronized boolean login(UtenteRegistrato u) {
    	String mail = u.getMail();
    	String password = u.getPassoword();
    	boolean esito = dbq.UtentiRegistratiEPB(mail, password);;//metodo che controlla il login tramite mail; Se il login va a buon fine restituisce true
    	if(!esito) {
    		String userId = u.getUserId();
    		esito = dbq.UtentiRegistratiUPB(userId, password);///metodo che controlla il login tramite mail; Se il login va a buon fine restituisce true
    	}
    	return esito;
    }
    //uguale per prima, guarda cosa chiede il metodo in in
    public synchronized boolean iserisciValutazioni(Libro l,UtenteRegistrato u) {
    	int idLibro = dbq.getCodiceLibro(l);
    	String userId = u.getUserId();
    	String cf = dbq.getCFU(userId);
    	int contenuto = l.getContenuto();
    	int stile = l.getStile();
    	int gadevolezza = l.getGradevolezza();
    	int originalita = l.getOriginalita();
    	int edizione = l.getEdizione();
    	boolean controlloValutazioni = dbi.loadValutazioni(idLibro, cf, contenuto, stile, gadevolezza, originalita, edizione);
    	//metodo che inserisci le valutazione di un utente nel db e restituisce true in caso di esito posito altrimenti false
    //	boolean controlloNote = this.inserisciNoteLibro(l);
    	boolean controlloNote = true; //temporaneo
    	if(controlloValutazioni && controlloNote)
    		return true;
    	return false;
    	//Rifare tabelle separando note, metterle null le righe
    }
    
    private synchronized boolean inserisciNoteLibro(Libro l, UtenteRegistrato u) {
    	int idLibro = dbq.getCodiceLibro(l);
    	String userId = u.getUserId();
    	String cf = dbq.getCFU(userId);
    	String noteContenuto = l.getNoteStile();
    	String noteStile = l.getNoteContenuto();
    	String noteGradevolezza = l.getNoteGradevolezza();
    	String noteOriginalita = l.getNoteOriginalita();
    	String noteEdizione = l.getNoteEdizione();
    	boolean controllo = dbi.loadValutazioniNote(idLibro, cf, noteContenuto, noteStile, noteGradevolezza, noteOriginalita, noteEdizione);
    	return controllo;
    }
    

    //matte devi verderlo tu perchè non ne conosco la logica
    public synchronized boolean InserisciLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	LinkedList<Libro> contenuto = libreria.getContenuto();
    	String userId = u.getUserId();
    	boolean controllo = false;
    	//MODIFICA METODO
    	//Ora il metodo InserisciLibreriaDb inserisce nella tabella librerie lo userId dell'utente che lha creata il nome che gli è stata assegnata 
    	//e i libri inseriti dentro e restituisce true in caso di sucesso altriment false
    	for(int i=0; i<contenuto.size();i++)
    	{
    		Libro l = contenuto.get(i);
    		int IdLibro = dbq.getCodiceLibro(l);
    		String cf = dbq.getCFU(userId);
    		controllo = dbi.loadLibrerie(cf, nome, IdLibro); 
    		//metodo che restituisce codice libro
        	
    	}
    	return controllo;
    }
    
    //da creare andrea
    
    public synchronized boolean RinominaNomeLibreria(UtenteRegistrato u, Libreria libreria, String nomeVecchio) {
    	String nomeNuovo = libreria.getNome(); //nome che l'utente ha rinominato
    	//nomeVecchio è il nome che la libreira aveva prima della modifica
    	String userId = u.getUserId();
    	//metodo che aggiorna il nome della libreria e restituisce true in caso di successo, false altrimenti
    	String cf = dbq.getCFU(userId);
    	boolean controllo = dbq.aggiornaNomeLibreria(nomeVecchio, nomeNuovo, cf); 
    	return controllo;
    }
    
    //da creare andrea
    
    public synchronized boolean EliminaLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	String userId = u.getUserId();
    	//metodo che elimina la libreria dal Db e restituisce true in caso di successo altrimenti false
    	String cf=dbq.getCFU(userId);
    	boolean controllo = dbq.eliminaLibreria(nome, cf); 
    	return controllo;
    }
    
    //DA RIGUARDARE 
    // anche questo metodo non è chiaro:
 
    public synchronized boolean InserisciConsigli(UtenteRegistrato ur, Libro corrente, LinkedList<Libro> suggeriti) {
    	int idLibroCorrente = dbq.getCodiceLibro(corrente);
    	String userId = ur.getUserId();
    	String cf = dbq.getCFU(userId);
    	int idLibroSuggerito;
    	boolean esito = false;
    	Libro l;
    	//InserisciConsigliInDb() metodo che dati titolo del libro corrente e libri suggeriti, aggiunge alla collonna dei libri suggeriti quelli selezionati dall'utente
    	for(int i=0; i<suggeriti.size(); i++)
    	{
    		l = suggeriti.get(i);
    		idLibroSuggerito = dbq.getCodiceLibro(l);
    		esito = dbi.loadConsigliPerLibroInDb(idLibroCorrente, cf, idLibroSuggerito );
    	}
    	//InserisciConsigliPerUtente() metodo che dati titolo del libro corrente e libri suggeriti, aggiunge alla tabella suggeriti i libri consigliati dall'utente
    	return esito;
    } 
    
    public synchronized LinkedList caricaSuggeriti(Libro corrente, UtenteRegistrato ur) {
    	int idlibro = dbq.getCodiceLibro(corrente);
    	String userId = ur.getUserId();
    	String cf = dbq.getCFU(userId);

    //	LinkedList<Libro> libriSuggeriti = dbq.caricaSuggeritiDaDB(idlibro, cf);
    	LinkedList<Libro> libriSuggeriti = new LinkedList<Libro>(); //da modificare
    	return libriSuggeriti;	
    }
    
}
