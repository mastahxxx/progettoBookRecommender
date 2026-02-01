/**
 * @author Adrian Gabriel Soare n: 749483
 * @author Matteo Sorrentino n: 753775
 * 
 * 
 * 
 */
package ClientBR.Controllers;
import ClientBR.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller della schermata principale dell'utente registrato.
 * <p>Gestisce la navigazione verso librerie, valutazioni e suggerimenti.</p>
 */
public class UtenteRegistratoController {

    // ---- UI ----
    @FXML private Button btnLibrerie;
    @FXML private Button btnValutaLibro;
    @FXML private Button btnSuggerimenti;
    @FXML private Button btnLogout;
    @FXML private Label lblBenvenuto;
    @FXML private Button btnCercaLibriUtenteRegistrato;

    /** Inizializza la schermata mostrando il messaggio di benvenuto. */
    @FXML
    private void initialize() {
        String benvenuto = "Benvenuto " + SceneNavigator.getUserID();
        lblBenvenuto.setText(benvenuto);
    }

    /** Vai alla gestione librerie. */
    @FXML private void onLibrerie() {
        SceneNavigator.switchToLibrerie();
    }

    /** Vai alla schermata di valutazione libri. */
    @FXML private void onValutaLibro() {
        SceneNavigator.switchToValutaLibro();
    }

    /** Vai alla gestione suggerimenti. */
    @FXML private void onSuggerimenti() {
        SceneNavigator.switchToSuggerimenti();
    }

    /** Esegue il logout. */
    @FXML private void onLogout() {
        SceneNavigator.logout();
    }

    /** Vai alla ricerca libri (modalità utente registrato). */
    @FXML private void onCercaLibriRegistrato() {
        SceneNavigator.switchToCercaLibroRegistrato();
    }
}
