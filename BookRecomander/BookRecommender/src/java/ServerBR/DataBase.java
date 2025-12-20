

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
    	ris = dbq.CaricaLibriDalleLibrerie(userId); //metodo che restituisce una lista contenente tutti i libri presenti in tutte le librerie del utente
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
    	boolean esito = dbq.loginMail(mail, password);;//metodo che controlla il login tramite mail; Se il login va a buon fine restituisce true
    	if(!esito) {
    		String userId = u.getUserId();
    		esito = dbq.loginUserId(userId, password);///metodo che controlla il login tramite mail; Se il login va a buon fine restituisce true
    	}
    	return esito;
    }
    //uguale per prima, guarda cosa chiede il metodo in in
    public synchronized boolean iserisciValutazioni(Libro l) {
    	String titolo = l.getTitolo();
    	int contenuto = l.getContenuto();
    	int stile = l.getStile();
    	int gadevolezza = l.getGradevolezza();
    	int originalita = l.getOriginalita();
    	int edizione = l.getEdizione();
    	LinkedList noteContenuto = l.getNoteStile();
    	LinkedList noteStile = l.getNoteContenuto();
    	LinkedList noteGradevolezza = l.getNoteGradevolezza();
    	LinkedList noteOriginalita = l.getNoteOriginalità();
    	LinkedList noteEdizione = l.getNoteEdizione();
    	boolean controllo = dbi.loadValutazioni(titolo, contenuto, stile, gadevolezza, originalita, edizione, noteContenuto, noteStile, noteGradevolezza, noteOriginalita, noteEdizione);
    	//metodo che inserisci le valutazione di un utente nel db e restituisce true in caso di esito posito altrimenti false
    	return controllo;
    	
    	//Rifare tabelle separando note, metterle null le righe
    }
    
    //questo metodo non ha senso lato db. non posso fare un sql che inserisce una lista bisogno. vorrebbe dire fare un metodo che prende libro linked list
    //lo trasforma in string per il suo id e poi in seguito lo passi in insert per caricarlo. anche perchè l'utente immagino inserisca un libro alla volta
    //quindi non ha senso passare una lista
    
    public synchronized boolean InserisciLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	LinkedList<Libro> contenuto = libreria.getContenuto();
    	String userId = u.getUserId();
    	//MODIFICA METODO
    	//Ora il metodo InserisciLibreriaDb inserisce nella tabella librerie lo userId dell'utente che lha creata il nome che gli è stata assegnata 
    	//e i libri inseriti dentro e restituisce true in caso di sucesso altriment false
    	for(int i=0; i<contenuto.size();i++)
    	{
    		Libro l = contenuto.get(i);
    		int IdLibro = dbi.getCodiceLibro(l);
    		boolean controllo = dbi.loadLibrerie(userId, nome, IdLibro); 
    		//metodo che restituisce codice libro
        	return controllo;
    	}
    	
    }
    
    //da creare andrea
    
    public synchronized boolean RinominaNomeLibreria(UtenteRegistrato u, Libreria libreria, String nomeVecchio) {
    	String nomeNuovo = libreria.getNome(); //nome che l'utente ha rinominato
    	//nomeVecchio è il nome che la libreira aveva prima della modifica
    	String userId = u.getUserId();
    	//metodo che aggiorna il nome della libreria e restituisce true in caso di successo, false altrimenti
    	boolean controllo = dbu.rinominaLibreriaInDb(userId, nomeNuovo, nomeVecchio); 
    	return controllo;
    }
    
    //da creare andrea
    
    public synchronized boolean EliminaLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	String userId = u.getUserId();
    	//metodo che elimina la libreria dal Db e restituisce true in caso di successo altrimenti false
    	boolean controllo = dbd.EliminaLibreriaInDb(userId, nome); 
    	return controllo;
    }
    
    //DA RIGUARDARE 
    // anche questo metodo non è chiaro:
    //come faccio a fare un associazione per titolo, mi state dicendo quindi che non possono esistere due libri con lo stesso titolo?
    //va passata la pk che è cod libro
    public synchronized boolean InserisciConsigli(UtenteRegistrato ur, Libro corrente, LinkedList<Libro> suggeriti) {
    	String titolo = corrente.getTitolo();
    	String userId = ur.getUserId();
    	//InserisciConsigliInDb() metodo che dati titolo del libro corrente e libri suggeriti, aggiunge alla collonna dei libri suggeriti quelli selezionati dall'utente
    	boolean esito = dbq.InserisciConsigliPerLibroInDb(titolo, suggeriti);
    	//InserisciConsigliPerUtente() metodo che dati titolo del libro corrente e libri suggeriti, aggiunge alla tabella suggeriti i libri consigliati dall'utente
    	esito = dbq.InserisciConsigliPerUtente(titolo, titolo, suggeriti);
    	return esito;
    } 
    
    public synchronized LinkedList caricaSuggeriti(Libro corrente, UtenteRegistrato ur) {
    	String titolo = corrente.getTitolo();
    	String userId = ur.getUserId();
    	//caricaSuggeritiDaDB() metodo che dati il titolo del libro corrente(ovvero il libro su cui l'utente 
    	//sta effettuando i suggeritmenti) e lo userId dell'utente restituisce i libri consigliati da lui stesso per il libro corrente
    	//Andre se non capisci scrivimi che ci colleghiamo su discord e te lo spiego meglio
    	
    	//ho capito cosa intendi ma anche qua non mi puoi passare il titolo per lo stesso principio di prima
    	LinkedList<Libro> libriSuggeriti = dbq.caricaSuggeritiDaDB(titolo, userId);
    	return libriSuggeriti;	
    }
    
}
