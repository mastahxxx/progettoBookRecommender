package ServerBR;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe {@code Server} che avvia il server per la gestione dei client.
 * 
 * <p>Il server ascolta sulla porta {@link #NUM_PORT} e, per ogni connessione in arrivo,
 * crea un nuovo {@link ServerThread} per gestire la comunicazione con quel client.</p>
 * 
 * <p>In questo modo, ogni client viene gestito in un thread separato, permettendo al server
 * di gestire più client contemporaneamente.</p>
 */
public class Server {
    
    /** Porta su cui il server ascolta le connessioni in arrivo */
    final static int NUM_PORT = 8999;

    /**
     * Metodo principale per l'avvio del server.
     * 
     * <p>Il server crea un {@link ServerSocket} sulla porta definita e rimane in ascolto
     * in un loop infinito. Ad ogni nuova connessione viene creato un {@link ServerThread}
     * dedicato al client, che viene immediatamente avviato.</p>
     * 
     * <p>Il server chiude la socket principale solo in caso di terminazione del programma
     * o di eccezione.</p>
     * 
     * @param args parametri da riga di comando (non utilizzati)
     * @throws IOException in caso di errori nella creazione del {@link ServerSocket}
     */
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(NUM_PORT);

        try {
            while (true) {
                Socket socket = server.accept();
                new ServerThread(socket).start();
            }

        } finally {
            server.close();
        }
    }
}




