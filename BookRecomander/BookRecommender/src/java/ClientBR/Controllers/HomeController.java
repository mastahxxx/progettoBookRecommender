/**
 * @author Adrian Gabriel Soare 749483
 * 
 * 
 * 
 * 
 */

package ClientBR.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ClientBR.SceneNavigator;

/**
 * Controller della schermata Home.
 */
public class HomeController {

    @FXML private Button btnCercaLibri;
    @FXML private Button btnAccedi;
    @FXML private Button btnRegistrati;
    @FXML private Button btnEsci;

    /** Naviga alla schermata di ricerca libri (non registrat). */
    @FXML
    private void onCercaLibri() {
        SceneNavigator.switchToCercaLibri();
    }

    /** Naviga alla schermata di login. */
    @FXML
    private void onAccedi() {
        SceneNavigator.switchToLogin();
    }

    /** Naviga alla schermata di registrazione. */
    @FXML
    private void onRegistrati() {
        SceneNavigator.switchToRegister();
    }

    /** Chiude l'applicazione. */
    @FXML
    private void onEsci() {
        SceneNavigator.Esci();
    }
}
