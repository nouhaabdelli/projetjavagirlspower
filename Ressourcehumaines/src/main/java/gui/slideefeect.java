package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class slideefeect {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button btnPrev, btnNext;

    private List<Pane> slides = new ArrayList<>();
    private int currentIndex = 0;
    private Timeline autoSlide;

    @FXML
    public void initialize() {
        initializeSlides();
    }

    private void initializeSlides() {
        slides.add(createSlide("Événements à venir", "Participez à nos conférences , \n ateliers et rencontres", "Voir les événements", "/images/2.jpg"));
        slides.add(createSlide("Formations spécialisées", "Développez vos compétences\n avec nos parcours adaptés", "Découvrir les formations", "/images/3.jpg"));
        slides.add(createSlide("Annonces importantes", "Restez informé des dernières\n nouvelles et mises à jour", "Voir les annonces", "/images/4.jpg"));

        if (!slides.isEmpty()) {
            contentPane.getChildren().setAll(slides.get(0));
        }

        // Carrousel automatique
        autoSlide = new Timeline(new KeyFrame(Duration.seconds(5), e -> showNext()));
        autoSlide.setCycleCount(Timeline.INDEFINITE);
        autoSlide.play();
    }

    private Pane createSlide(String titleText, String subtitleText, String buttonText, String imagePath) {
        BorderPane slide = new BorderPane();
        slide.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");

        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: rgba(150, 150, 150, 0.85); " + // Gris semi-transparent
                "-fx-padding: 30; " +
                "-fx-alignment: center-left; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 2);");
        box.setPrefWidth(400); // Largeur fixe pour éviter qu'elle s'étende sur toute la photo

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 28px; -fx-text-fill: black; -fx-font-family: 'Poppins', sans-serif; -fx-font-weight: bold;");
        Label subtitle = new Label(subtitleText);
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-font-family: 'Poppins', sans-serif;");
        Button button = new Button(buttonText);
        button.setStyle("-fx-background-color: #E30613; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Poppins', sans-serif; -fx-background-radius: 5;");

        box.getChildren().addAll(title, subtitle, button);

        // Placer la boîte dans la région center et centrer horizontalement
        slide.setRight(box);
        BorderPane.setMargin(box, new Insets(0, 200, 0, 0)); // Ajuste les marges si besoin

        return slide;
    }

    @FXML
    public void handleNext() {
        showNext();
    }

    @FXML
    public void handlePrevious() {
        showPrevious();
    }

    private void showNext() {
        int nextIndex = (currentIndex + 1) % slides.size();
        Pane nextSlide = slides.get(nextIndex);
        showSlideWithTransition(nextSlide, true);
        currentIndex = nextIndex;
    }

    private void showPrevious() {
        int prevIndex = (currentIndex - 1 + slides.size()) % slides.size();
        Pane prevSlide = slides.get(prevIndex);
        showSlideWithTransition(prevSlide, false);
        currentIndex = prevIndex;
    }

    private void showSlideWithTransition(Pane newSlide, boolean toLeft) {
        Pane oldSlide = slides.get(currentIndex);
        TranslateTransition out = new TranslateTransition(Duration.millis(800), oldSlide);
        out.setFromX(0);
        out.setToX(toLeft ? -contentPane.getWidth() : contentPane.getWidth());

        TranslateTransition in = new TranslateTransition(Duration.millis(1000), newSlide);
        in.setFromX(toLeft ? contentPane.getWidth() : -contentPane.getWidth());
        in.setToX(0);

        contentPane.getChildren().setAll(oldSlide, newSlide);
        out.play();
        in.play();

        out.setOnFinished(e -> contentPane.getChildren().setAll(newSlide));
    }
}
