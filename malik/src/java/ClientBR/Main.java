/**
 * @author Adrian Gabriel Soare 749483
 * 
 * 
 * 
 * 
 */





package ClientBR;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

/**
 * Entry point JavaFX dell'applicazione Book Recommender.
 * <p>Inizializza lo {@link Stage} principale e carica la schermata Home tramite {@link SceneNavigator}.</p>
 */
public class Main extends Application {

    /**
     * Avvio dell'applicazione JavaFX.
     * @param primaryStage stage principale fornito dal runtime
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // passa lo Stage al navigator
            SceneNavigator.setStage(primaryStage);
            primaryStage.setTitle("Book Recommender");
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);

            // mostra la scena iniziale
            SceneNavigator.switchToHome();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Avvia l'applicazione.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
