package Reclamation.Controller;

import Reclamation.API.JokeAPI;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import Reclamation.Utils.AnimationUtil;
import Reclamation.Utils.ClipboardUtil;
public class JokeController {

    @FXML private AnchorPane mainPane;
    @FXML private TextArea jokeArea;
    @FXML private ImageView emoji1, emoji2;

    private Timeline emojiAnimation;
    private Runnable onCloseCallback; // Callback pour la fermeture de la fenêtre

    @FXML
    public void initialize() {
        startEmojiAnimation();
        loadJoke();
        setupEnterAnimation();
    }

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    private void loadJoke() {
        String joke = JokeAPI.getRandomJoke();
        animateTextDisplay(joke);
    }

    private void animateTextDisplay(String text) {
        jokeArea.setText("");
        AnimationUtil.typeText(jokeArea, text, 50);
    }

    private void startEmojiAnimation() {
        emojiAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(emoji1.rotateProperty(), -15)),
                new KeyFrame(Duration.seconds(1.0),
                        new KeyValue(emoji1.rotateProperty(), 15),
                        new KeyValue(emoji2.rotateProperty(), -15)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(emoji1.rotateProperty(), 0),
                        new KeyValue(emoji2.rotateProperty(), 0))
        );
        emojiAnimation.setCycleCount(Animation.INDEFINITE);
        emojiAnimation.play();
    }

    private void setupEnterAnimation() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), mainPane);
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), mainPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();
    }

    @FXML
    private void copyJoke() {
        if (!jokeArea.getText().isEmpty()) {
            ClipboardUtil.copyToClipboard(jokeArea.getText());
            AnimationUtil.bounceEffect(emoji1);
            AnimationUtil.bounceEffect(emoji2);
        }
    }

    @FXML
    private void closePopup() {
        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.3), mainPane);
        scaleOut.setToX(0.8);
        scaleOut.setToY(0.8);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), mainPane);
        fadeOut.setToValue(0.0);

        ParallelTransition exitTransition = new ParallelTransition(scaleOut, fadeOut);
        exitTransition.setOnFinished(e -> {
            ((Stage) mainPane.getScene().getWindow()).close();
            if (onCloseCallback != null) {
                onCloseCallback.run(); // Exécuter le callback de fermeture
            }
        });
        exitTransition.play();
    }
}