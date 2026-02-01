/**
 * @author Adrian Gabriel Soare n: 749483
 * @author Matteo Sorrentino n: 753775
 * 
 * 
 * 
 */

package ClientBR.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ClassiCondivise.UtenteRegistrato;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe di utilità per la gestione di messaggi e controlli lato client.
 */
public class Helpers {

    /**
     * Cancella il contenuto di un label di errore.
     * @param lblError label da resettare
     */
    public static void clearError(Label lblError) {
        lblError.setText("");
    }

    /**
     * Mostra un messaggio di errore in rosso.
     * @param err messaggio da mostrare
     * @param lblError label di riferimento
     */
    public static void showError(String err, Label lblError) {
        lblError.setText(err);
        lblError.setStyle("-fx-text-fill: red;");
    }

    /**
     * Mostra un messaggio informativo in verde.
     * @param info messaggio da mostrare
     * @param lblError label di riferimento
     */
    public static void showInfo(String info, Label lblError) {
        lblError.setText(info);
        lblError.setStyle("-fx-text-fill: green;");
    }

    /**
     * Pulisce i campi di input passati come argomento.
     * @param controls campi di testo da pulire
     */
    public static void clearFields(TextInputControl... controls) {
        if (controls == null) return;
        for (TextInputControl c : controls) {
            if (c != null) c.clear();
        }
    }

    /**
     * Valida un codice fiscale.
     * Deve contenere 16 caratteri alfanumerici (A–Z, 0–9).
     * @param CF codice fiscale
     * @return true se valido
     */
    public static boolean validCF(String CF) {
        return CF.matches("[A-Z0-9]{16}$");
    }

    /**
     * Valida una password.
     * Deve avere almeno 8 caratteri.
     * @param psw password
     * @return true se valida
     */
    public static boolean validPswd(String psw) {
        return psw.matches("^.{8,}$");
    }

    /**
     * Valida un indirizzo e-mail basilare (es: a@b.c).
     * @param mail email da validare
     * @return true se valida
     */
    public static boolean validEmail(String mail) {
        return mail.matches(".+@.+\\..+");
    }

    /**
     * Mostra un popup di errore (alert).
     * @param messaggio testo da mostrare
     */
    public static void showError(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERRORE");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Controlla se una email risulta già usata nel sistema (via socket al server).
     * @param email email da verificare
     * @return true se già usata
     */
    public static boolean emailAlreadyUsed(String email) {
        boolean ok = false;
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setMail(email);
            out.writeObject("CONTROLLA EMAIL");
            out.writeObject(ur);
            ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            // ignorato
        }
        return ok;
    }

    /**
     * Controlla se uno userID risulta già usato nel sistema (via socket al server).
     * @param UserID user ID da verificare
     * @return true se già usato
     */
    public static boolean userIDAlreadyUsed(String UserID) {
        boolean ok = false;
        try {
            InetAddress addr = InetAddress.getByName(null);
            Socket socket = new Socket(addr, 8999);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            UtenteRegistrato ur = new UtenteRegistrato();
            ur.setUserId(UserID);
            out.writeObject("CONTROLLA USERID");
            out.writeObject(ur);
            ok = (boolean) in.readObject();
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            // ignorato
        }
        return ok;
    }
}
