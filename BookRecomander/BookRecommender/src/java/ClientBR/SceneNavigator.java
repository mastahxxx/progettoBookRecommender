package ClientBR;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ClassiCondivise.Libreria;
import ClassiCondivise.Libro;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ClientBR.Controllers.Helpers;

/**
 * Gestore centralizzato della navigazione tra scene JavaFX in un unico stage.
 * <p>Si occupa anche di passare oggetti (Libro/Libreria) tra controller e di mantenere lo userID di sessione.</p>
 */
public class SceneNavigator {

    /** Oggetto libro da passare tra scene. */
    public static Libro libro;
    /** Collezione di libri da salvare nella libreria */
    public static List<Libro> listaLibri = new ArrayList<>();
    /** Oggetto libreria da passare tra scene. */
    public static Libreria libreria;
    /** UserID della sessione corrente (null se non loggato). */
    public static String userID;
    /** Stage principale dell'app. */
    private static Stage stage;
    /** Percorso base dei file FXML. */
    private static final String BASE_PATH = "/views/";

    /**
     * Imposta lo {@link Stage} principale (da chiamare all'avvio).
     * @param s stage dell'applicazione
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Sostituisce la scena corrente con quella indicata dall'FXML.
     * <p>Mostra un alert in caso di errori (FXML mancante o non caricabile).</p>
     * @param fxml nome del file FXML (relativo a {@link #BASE_PATH})
     */
    public static void switchTo(String fxml) {
        if (stage == null) {
            Helpers.showError("Stage non iniziallizzato.");
            System.exit(1);
        }

        try {
            URL url = SceneNavigator.class.getResource(BASE_PATH + fxml);
            if (url == null) {
                Helpers.showError("View non trovata: \n" + BASE_PATH + fxml);
                System.exit(1);
            }

            Parent root = FXMLLoader.load(url);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Helpers.showError("Impossibile aprire la schermata");
            System.exit(1);
        }
    }


    /** Vai a Home. */                        public static void switchToHome() { switchTo("home.fxml"); }
    /** Vai a Login. */                       public static void switchToLogin() { switchTo("login.fxml"); }
    /** Vai a Registrazione. */               public static void switchToRegister() { switchTo("register.fxml"); }
    /** Vai a Cerca Libri (ospite). */        public static void switchToCercaLibri() { switchTo("cercaLibro.fxml"); }
    /** Vai a Dashboard utente registrato. */ public static void switchToUtenteRegistrato() { switchTo("utenteRegistrato.fxml"); }
    /** Vai a dettaglio libro (ospite). */    public static void switchToVisualizzaLibro() { switchTo("visualizzaLibro.fxml"); }
    /** Vai a elenco librerie. */             public static void switchToLibrerie() { switchTo("librerie.fxml"); }
    /** Vai a Valuta Libro. */                public static void switchToValutaLibro() { switchTo("valutaLibro.fxml"); }
    /** Vai a Suggerimenti. */                public static void switchToSuggerimenti() { switchTo("suggerimenti.fxml"); }
    /** Vai a Cerca Libri (registrato). */    public static void switchToCercaLibroRegistrato() { switchTo("cercaLibroRegistrato.fxml"); }
    /** Vai a dettaglio libro (registrato). */public static void switchToVisualizzaLibroRegistrato() { switchTo("visualizzaLibroUtenteRegistrato.fxml"); }
    /** Vai al dettaglio di una libreria. */  public static void switchToVisualizzaLibreria() { switchTo("visualizzaLibreria.fxml"); }
    /** Vai al SalvaLibro */                  public static void switchToSalvaLibro() { switchTo("salvaLibro.fxml");}
    /** Vai a OnlyVisLib */                   public static void switchToOnlyVisLib() { switchTo("onlyVisLib.fxml");}


    /**
     * Effettua il logout: azzera lo userID e torna alla Home.
     */
    public static void logout() {
        setUserIDNull();
        switchToHome();
    }

    /**
     * Chiude l'applicazione.
     */
    public static void Esci() {
        System.exit(0);
    }
    /**
     * Imposta il libro da condividere con la prossima scena.
     * @param lib libro selezionato
     */
    public static void setLibro(Libro lib) { libro = lib; }

    /**
     * Restituisce il libro condiviso.
     * @return libro corrente (può essere null)
     */
    public static Libro getLibro() { return libro; }

    /**
     * Imposta la libreria da condividere con la prossima scena.
     * @param l libreria selezionata
     */
    public static void setLibreria(Libreria l) { libreria = l; }

    /**
     * Restituisce la libreria condivisa.
     * @return libreria corrente (può essere null)
     */
    public static Libreria getLibreria() { return libreria; }

    // ---- Gestione userID di sessione ----

    /**
     * Imposta lo userID (dopo login valido su DB).
     * @param uID identificativo utente
     */
    public static void setUserID(String uID) { userID = uID; }

    /**
     * Restituisce lo userID corrente.
     * @return userID o null se non loggato
     */
    public static String getUserID() { return userID; }

    /**
     * Azzera lo userID (logout).
     */
    public static void setUserIDNull() { userID = null; }
}
