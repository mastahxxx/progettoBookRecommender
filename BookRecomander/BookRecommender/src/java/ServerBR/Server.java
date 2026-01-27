
/**
 * Classe main per l'avvio del server
 */
package ServerBR;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final static int NUM_PORT = 8999;

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        ServerSocket server = new ServerSocket(NUM_PORT);
       // DataBase database = new DataBase();

        try {
            while(true) {
                Socket socket = server.accept();
                new ServerThread(socket).start();
                //new ServerThread(socket).start();
            }

        }finally {
            server.close();
        }

    }
}
