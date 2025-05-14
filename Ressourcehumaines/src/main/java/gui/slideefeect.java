package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        slides.add(createSlide("Événements à venir", "           Notre Application vous aide  \n              à gérer vos Evénements  ", "Gestion des événements", "/images/evenement.jpg" ));
        slides.add(createSlide("Formations spécialisées", "           Notre Application vous aide\n                à gérer vos Formations ", "Gestion des formations", "/images/FORMATION.jpg"));
        slides.add(createSlide("Employés Satisfaits", "          Notre Application vous aide\n              à gérer vos Employés", "Gestion des employés", "/images/personnel.jpg"));

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
        box.setStyle("-fx-background-color: rgba(173, 216, 230, 0.75); " +
                "-fx-padding: 30; " +
                "-fx-alignment: center-left; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 2);");


        box.setPrefWidth(400);// Largeur fixe pour éviter qu'elle s'étende sur toute la photo
        box.setAlignment(Pos.CENTER);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 28px; -fx-text-fill: #273b60; -fx-font-family: 'Poppins', sans-serif; -fx-font-weight: bold; -fx-underline: true;");
        Label subtitle = new Label(subtitleText);
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #273b60; -fx-font-family: 'Poppins' , sans-serif; -fx-font-weight: bold;");
        Button button = new Button(buttonText);
        button.setStyle("-fx-background-color: #6fccdc; -fx-text-fill: #273b60; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: 'Poppins', sans-serif; -fx-background-radius: 5;");
        button.setTranslateX(80);
        button.setTranslateY(10); // déplace verticalement vers le bas
        title.setTranslateX(40);
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/NN.jpeg")));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        logo.setTranslateX(100);
        logo.setFitWidth(150); // agrandit le logo
        logo.setPreserveRatio(true); // garde la forme
        logo.setTranslateY(20); // déplace verticalement vers le bas

        box.getChildren().addAll(title, subtitle, button,logo);



        // Placer la boîte dans la région center et centrer horizontalement
        slide.setRight(box);
        BorderPane.setMargin(box, new Insets(0, 0, 0, 0)); // Ajuste les marges si besoin

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
