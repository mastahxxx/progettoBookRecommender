/**
 * @author Matteo Sorrentino n: 753775
 */
package ServerBR;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe {@code Server} responsabile dell'avvio e della gestione del server.
 *
 * <p>Il server rimane in ascolto sulla porta {@link #NUM_PORT} e accetta
 * le connessioni in ingresso da parte dei client. Per ogni nuova connessione
 * viene creato un {@link ServerThread}, incaricato di gestire la comunicazione
 * con il singolo client.</p>
 *
 * <p>L'utilizzo di thread separati consente al server di gestire più client
 * contemporaneamente.</p>
 */
public class Server {

    /** Porta su cui il server ascolta le connessioni in ingresso */
    final static int NUM_PORT = 8999;

    /**
     * Metodo principale per l'avvio del server.
     *
     * <p>Il metodo crea un {@link ServerSocket} sulla porta specificata e
     * rimane in attesa di connessioni all'interno di un ciclo infinito.
     * Ad ogni connessione accettata viene istanziato e avviato un
     * {@link ServerThread} dedicato alla gestione del client.</p>
     *
     * <p>La socket del server viene chiusa automaticamente in caso di
     * terminazione del programma o di errore.</p>
     *
     * @param args argomenti da riga di comando (non utilizzati)
     * @throws IOException se si verifica un errore nella creazione del
     *         {@link ServerSocket} o nell'accettazione delle connessioni
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
