package Reclamation.Utils;  // Déclaration du package

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class AnimationUtil {

    /**
     * Affiche un texte caractère par caractère (effet machine à écrire).
     *
     * @param area  La zone de texte où afficher le texte.
     * @param text  Le texte à afficher.
     * @param speed La vitesse d'affichage en millisecondes.
     */
    public static void typeText(TextArea area, String text, int speed) {
        Timeline timeline = new Timeline();
        final IntegerProperty i = new SimpleIntegerProperty(0);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speed), event -> {
            if (i.get() < text.length()) {
                area.appendText(text.charAt(i.get()) + "");
                i.set(i.get() + 1);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(text.length());
        timeline.play();
    }

    /**
     * Applique un effet de rebond à un nœud (par exemple, un bouton ou une image).
     *
     * @param node Le nœud à animer.
     */
    public static void bounceEffect(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), node);
        tt.setFromY(0);
        tt.setToY(-20);
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.play();
    }
}