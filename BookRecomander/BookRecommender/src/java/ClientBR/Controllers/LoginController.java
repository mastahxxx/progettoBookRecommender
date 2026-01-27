package ClientBR.Controllers;

import ClientBR.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ClassiCondivise.UtenteRegistrato;
import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Controller della schermata di login.
 * <p>Valida le credenziali e, se corrette, naviga all'area utente.</p>
 */
public class LoginController {

    /** Campo per username/userID o email. */
    @FXML private TextField fUserID;
    @FXML private PasswordField pfPassword;
    @FXML private Button btnAccedi;
    @FXML private Button btnRegistrati;
    @FXML private Button btnHome;
    @FXML private Label lblError;

    /** Inizializza la view e pulisce messaggi d'errore. */
    @FXML
    private void initialize() {
        Helpers.clearError(lblError);
    }

    /** Naviga alla schermata di registrazione. */
    @FXML
    private void onRegistrati() {
        SceneNavigator.switchToRegister();
        Helpers.clearError(lblError);
    }

    /** Torna alla Home. */
    @FXML
    private void onHome() {
        SceneNavigator.switchToHome();
        Helpers.clearError(lblError);
    }

    /**
     * Tenta l'accesso: valida input, invia richiesta al server e gestisce la navigazione.
     * <p>Mostra errori su {@code lblError} in caso di credenziali non valide.</p>
     */
    @FXML
    private void onAccedi() {
        String userId = fUserID.getText().trim();
        String pswd   = pfPassword.getText();

        if (!Helpers.validPswd(pswd)) {
            Helpers.showError("La password deve essere di almeno 8 caratteri", lblError);
            return;
        }

        boolean ok = false;
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in   = new ObjectInputStream(socket.getInputStream());

            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setMail(userId);
            ur.setUserId(userId);
            ur.setPassoword(pswd);

            out.writeObject("LOGIN");
            out.writeObject(ur);

            ok = (boolean) in.readObject();

            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            // errore silenziato
        }

        if (ok) {
            SceneNavigator.setUserID(userId);
            SceneNavigator.switchToUtenteRegistrato();
        } else {
            Helpers.showError("credenziali sbagliate");
        }
    }
}
