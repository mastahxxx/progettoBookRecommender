//<<<<<<< HEAD:BookRecommender/src/main/java/ServerBR/ServerThread.java
//	package ServerBR;
//=======

package ServerBR;
//>>>>>>> 17c85385528a759bc8ef0ed772bb2b9da97f2a28:BookRecommender/src/java/ServerBR/ServerThread.java

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import ClassiCondivise.UtenteRegistrato;

public class ServerThread extends Thread {
    private DataBase db;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    public ServerThread(Socket s, DataBase d) {
        this.db = d;
        this.socket = s;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e) {

        }
    }
    /*
     public ServerThread(Socket s) {
        this.socket = s;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e) {

        }
    }*/
    
    public void run() {
        try {
            while(true) {
                String request = "";
                request = (String) in.readObject();
                Libro l = new Libro();
                Libro l2 = new Libro();
                Libreria libreria = new Libreria();
                UtenteRegistrato u;
                List <Libro> ris = new LinkedList();
                Libro corrente = new Libro();
                LinkedList <Libro> libroSuggeriti = new LinkedList();
                boolean esito;
                switch(request) {
                    case "END":
                        break;
                    case "CERCA LIBRO":
                    	l = (Libro) in.readObject();
                        ris = db.cercaLibro(l);
                        out.writeObject(ris);
                        break;
                    case "Registrazine":
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.insertUtente(u);
                        out.writeObject(esito);
                        break;
                    case "LOGIN":
                    	u = (UtenteRegistrato) in.readObject();
                    	esito = db.login(u);
                        out.writeObject(esito);
                        break;
               
                    case "INSETISCI VALUTAZIONE":
                        l = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        esito = db.iserisciValutazioni(l, u);
                        out.writeObject(esito);
                        break;
                    case "REGISTRA LIBRERIA":
                    	u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.InserisciLibreria(u,libreria);
                        out.writeObject(esito);
                    case "RINOMINA LIBRERIA":
                    	u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        String nomeVecchio = (String) in.readObject();
                        esito = db.RinominaNomeLibreria(u,libreria, nomeVecchio);
                        out.writeObject(esito);
                    case "ELIMINA LIBRERIA":
                    	u = (UtenteRegistrato) in.readObject();
                        libreria = (Libreria) in.readObject();
                        esito = db.EliminaLibreria(u,libreria); //DA FAre
                        out.writeObject(esito);
                    case "CONSIGLIA LIBRI":
                    	//DA RIVEDERE
                        corrente = (Libro) in.readObject();
                        u = (UtenteRegistrato) in.readObject();
                        libroSuggeriti = (LinkedList <Libro>) in.readObject();
                        esito = db.InserisciConsigli(u, corrente, libroSuggeriti);
                        out.writeObject(esito);
                        break;
                    case "RIEMPI SUGGERITI":
                    	 corrente = (Libro) in.readObject();
                         u = (UtenteRegistrato) in.readObject();
                         libroSuggeriti = db.caricaSuggeriti(l,u);
                         out.writeObject(libroSuggeriti);
                    	break;
                    case "CONTROLLA USERID":
                    	u = (UtenteRegistrato) in.readObject();
                    	esito = db.controllaUserId(u); 
                        out.writeObject(esito);
                    	break;
                    case "CONTROLLA EMAIL":
                    	u = (UtenteRegistrato) in.readObject();
                    	esito = db.controllaEmail(u); 
                        out.writeObject(esito);
                    	break;
                    case "CARICA LIBRI LIBRERIE CLIENT":
                    	u = (UtenteRegistrato) in.readObject();
                    	ris = db.caricaLibrerie(u); 
                        out.writeObject(ris);
                    	break;
                    default:
                        break;
                }
            }

        }catch(IOException | ClassNotFoundException e) {}
        finally {
            try {
                socket.close();
            }catch(IOException e) {}

        }


    }



}

