

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
    private DbUpdate dbu;
    private DbDelete dbd;


    public DataBase(){
        dbq = new DbQuery();
        dbi = new DbInsert();
        dbu = new DbUpdate();
        dbd = new DbDelete();
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
    
    public synchronized List<Libro> cercaLibroPerAutore(Libro l){
    	String titolo = l.getTitolo();
    	String autore = l.getAutore();
    	String anno = l.getAnnoPubblicazione();
    	List <Libro> ris = new LinkedList();
    	if(titolo != "" && autore != "" && anno != "") {
    		ris = dbq.ceracaTitoloAutoreAnno(titolo, autore, anno);
    	}
    	else {
    		if(titolo != "" && anno != "") {
        		ris = dbq.cercaTitoloAnno(titolo, anno);	
        	}
    		else {
    			if(autore != "" && anno != "") {
    				ris = dbq.cercaAuotreAnno(autore, anno);
    			}
    			else {
    				if(titolo != "") {
    					ris = dbq.cercaTitolo(titolo);
    				}
    				else {
    					if(titolo != "") {
        					ris = dbq.cercaAuotre(autore);
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
    	boolean esito = dbq.controllaEmailInDB(mail); //metodo che restituisce true se la mail non è presente nel db
    	return esito;	
    }
    
    public synchronized boolean controllaUserId(UtenteRegistrato u){
    	String userId = u.getUserId();
    	boolean esito = dbq.controllaUserIDInDB(userId); //metodo che restituisce true se lo userId non è presente nel db
    	return esito;	
    }
        
    public synchronized boolean insertUtente(UtenteRegistrato u) {
    	String nomeCognome = u.getNomeCognome();
    	String codiceFiscale = u.getCodiceFiscale();
    	String mail = u.getmail();
    	String user = u.getUserId();
    	String password = u.getPassoword();
    	boolean esito = dbi.inserisciUtente(nomeCognome, codiceFiscale, mail, user, password); l'utente viene inserito all'interno del db
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
    	boolean controllo = dbi.inserisciValutazioneDb(titolo, contenuto, stile, gadevolezza, originalita, edizione, noteContenuto, noteStile, noteGradevolezza, noteOriginalita, noteEdizione);
    	//metodo che inserisci le valutazione di un utente nel db e restituisce true in caso di esito posito altrimenti false
    	return controllo;
    }
    
    public synchronized boolean InserisciLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	LinkedList<Libro> contenuto = libreria.getContenuto();
    	String userId = u.getUserId();
    	//MODIFICA METODO
    	//Ora il metodo InserisciLibreriaDb inserisce nella tabella librerie lo userId dell'utente che lha creata il nome che gli è stata assegnata 
    	//e i libri inseriti dentro e restituisce true in caso di sucesso altriment false
    	boolean controllo = dbi.InserisciLibreriaDb(userId, nome, contenuto); 
    	return controllo;
    }
    
    public synchronized boolean RinominaNomeLibreria(UtenteRegistrato u, Libreria libreria, String nomeVecchio) {
    	String nomeNuovo = libreria.getNome(); //nome che l'utente ha rinominato
    	//nomeVecchio è il nome che la libreira aveva prima della modifica
    	String userId = u.getUserId();
    	//metodo che aggiorna il nome della libreria e restituisce true in caso di successo, false altrimenti
    	boolean controllo = dbu.rinominaLibreriaInDb(userId, nomeNuovo, nomeVecchio); 
    	return controllo;
    }
    
    public synchronized boolean EliminaLibreria(UtenteRegistrato u, Libreria libreria) {
    	String nome = libreria.getNome();
    	String userId = u.getUserId();
    	//metodo che elimina la libreria dal Db e restituisce true in caso di successo altrimenti false
    	boolean controllo = dbd.EliminaLibreriaInDb(userId, nome); 
    	return controllo;
    }
    
    //DA RIGUARDARE
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
    	LinkedList<Libro> libriSuggeriti = dbq.caricaSuggeritiDaDB(titolo, userId);
    	return libriSuggeriti;	
    }
    
}
