package application.controllers;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.JournalEntry;
import models.Location;
import models.User;
import repositories.JournalEntryRepository;
import repositories.LocationRepository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainController {

    @FXML
    private Button toggleMenuButton;
    @FXML
    private Button addExperienceButton;
    @FXML
    private Button logoutButton;
    @FXML
    private ComboBox<String> experienceComboBox;
    @FXML
    private VBox dropdownMenu;

    @FXML
    private Label box1Label;
    @FXML
    private Label box2Label;
    @FXML
    private Label box3Label;
    @FXML
    private Label box4Label;

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private Text text1;

    @FXML
    private Rectangle box1Rectangle;
    @FXML
    private Rectangle box2Rectangle;
    @FXML
    private Rectangle box3Rectangle;
    @FXML
    private Rectangle box4Rectangle;
    @FXML
    private Button myJournalButton;
    @FXML
    private ImageView scrollBackgroundImageView;
    private User user;

    @FXML
    private VBox scrollableContainer;
    @FXML
    private Text experienceDetailsText;

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public User getUser() {
        return user;
    }

    @FXML
    private void initialize() {
        loadScrollBackgroundImage();
        populateExperienceComboBox();
        setupButtonActions();
        loadImages();
        setupLabels();

        scrollableContainer.setVisible(false);
        scrollableContainer.setManaged(false);
    }

    private void setupLabels() {
        box1Label.setOnMouseClicked(event -> handleCountrySelection(box1Label.getText()));
        box2Label.setOnMouseClicked(event -> handleCountrySelection(box2Label.getText()));
        box3Label.setOnMouseClicked(event -> handleCountrySelection(box3Label.getText()));
        box4Label.setOnMouseClicked(event -> handleCountrySelection(box4Label.getText()));
    }

    private void handleCountrySelection(String countryName) {
        clearExperienceBoxes();
        showAlert("Selected Country", "You selected: " + countryName);

        if (countryName.startsWith("Visiting ")) {
            countryName = countryName.replace("Visiting ", "");
        }

        scrollableContainer.getChildren().clear();

        JournalEntryRepository journalEntryRepository = new JournalEntryRepository(DatabaseConnection.getConnection());
        List<JournalEntry> experiences = journalEntryRepository.getExperiencesByCountry(countryName);

        if (experiences.isEmpty()) {
            experienceDetailsText.setText("No experiences found for " + countryName);
        } else {

            VBox experiencesVBox = new VBox(20);

            for (JournalEntry entry : experiences) {

                VBox entryBox = new VBox(10);
                entryBox.setStyle("-fx-background-color: #502a2d; -fx-padding: 10; -fx-border-color: lightgrey; -fx-border-width: 1;");


                Label titleLabel = new Label(entry.getTitle());
                titleLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

                ImageView imageView = new ImageView();
                String imagePath = entry.getImagePath();
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                imageView.setImage(image);
                imageView.setFitWidth(400);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                Label contentLabel = new Label(entry.getContent());
                contentLabel.setWrapText(true);
                contentLabel.setStyle("-fx-text-fill: white;");

                Label costLabel = new Label("Cost: " + entry.getCost());
                costLabel.setStyle("-fx-text-fill: white;");

                entryBox.getChildren().addAll(titleLabel, imageView, contentLabel, costLabel);
                experiencesVBox.getChildren().add(entryBox);
            }

            scrollableContainer.getChildren().add(experiencesVBox);
            experienceDetailsText.setText("Experiences in " + countryName);
            scrollableContainer.setVisible(true);
            scrollableContainer.setManaged(true);
        }
    }

    private void clearExperienceBoxes() {
        box1Label.setVisible(false);
        box2Label.setVisible(false);
        box3Label.setVisible(false);
        box4Label.setVisible(false);

        imageView1.setVisible(false);
        imageView2.setVisible(false);
        imageView3.setVisible(false);
        imageView4.setVisible(false);

        box1Rectangle.setVisible(false);
        box2Rectangle.setVisible(false);
        box3Rectangle.setVisible(false);
        box4Rectangle.setVisible(false);
        text1.setVisible(false);

        scrollableContainer.setVisible(false);
        scrollableContainer.setManaged(false);
    }

    private void loadScrollBackgroundImage() {
        try {
            Image scrollBackgroundImage = new Image(getResourcePath("/images/Background.jpg"));
            scrollBackgroundImageView.setImage(scrollBackgroundImage);
        } catch (Exception e) {
            showAlert("Error", "The background image for the ScrollPane could not be loaded.");
        }
    }

    private void populateExperienceComboBox() {
        JournalEntryRepository journalEntryRepository = new JournalEntryRepository(DatabaseConnection.getConnection());
        List<String> countries = journalEntryRepository.getCountries();
        experienceComboBox.setItems(FXCollections.observableArrayList(countries));

        experienceComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                handleCountrySelection(newValue);
            }
        });
    }

    private void setupButtonActions() {
        toggleMenuButton.setOnAction(e -> toggleDropdownMenu());
        addExperienceButton.setOnAction(e -> handleAddExperience());
        logoutButton.setOnAction(e -> handleLogout());
        myJournalButton.setOnAction(e -> openMyJournal());
    }

    private void toggleDropdownMenu() {
        boolean isVisible = dropdownMenu.isVisible();
        dropdownMenu.setVisible(!isVisible);
        dropdownMenu.setManaged(!isVisible);
    }

    private void handleAddExperience() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Experience");
        dialog.setHeaderText("Enter the details of the experience.");

        TextField titleField = new TextField();
        TextArea contentArea = new TextArea();
        TextField countryField = new TextField();
        TextField cityField = new TextField();
        TextField costField = new TextField();
        CheckBox isPublicCheckBox = new CheckBox("Is Public");
        TextField imagePathField = new TextField();

        VBox vbox = new VBox(10,
                new Label("Title:"), titleField,
                new Label("Content:"), contentArea,
                new Label("Country:"), countryField,
                new Label("City:"), cityField,
                new Label("Cost:"), costField,
                isPublicCheckBox,
                new Label("Image Path:"), imagePathField
        );

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String title = titleField.getText();
                String content = contentArea.getText();
                String country = countryField.getText();
                String city = cityField.getText();
                double cost = Double.parseDouble(costField.getText());
                boolean isPublic = isPublicCheckBox.isSelected();
                String imagePath = imagePathField.getText();

                LocationRepository locationRepository = new LocationRepository(DatabaseConnection.getConnection());
                int locationId = locationRepository.getLocationId(country, city);

                Location location = new Location(locationId, country, city, 0);
                JournalEntry entry = new JournalEntry(0, title, content, location, isPublic, cost, imagePath, user.getId());

                JournalEntryRepository repository = new JournalEntryRepository(DatabaseConnection.getConnection());
                repository.addEntry(entry);
                showAlert("Success", "Experience added successfully!");

            } catch (Exception e) {
                showAlert("Error", "Could not add experience. Make sure all fields are correct.");
                e.printStackTrace();
            }
        }
    }

    private void handleLogout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openMyJournal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/my-journal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 750));
            stage.setTitle("My Journal");
            MyJournalController controller = loader.getController();
            controller.setUser(user);
            controller.loadJournalEntries();
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not open My Journal.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadImages() {
        try {
            imageView1.setImage(loadImageOrFallback("/images/Italy.jpg", "/path/to/backup/Italy.jpg"));
            imageView2.setImage(loadImageOrFallback("/images/Turkey.jpg", "/path/to/backup/Turkey.jpg"));
            imageView3.setImage(loadImageOrFallback("/images/Japan.jpg", "/path/to/backup/Japan.jpg"));
            imageView4.setImage(loadImageOrFallback("/images/Mexico.jpg", "/path/to/backup/Mexico.jpg"));
        } catch (Exception e) {
            showAlert("Error", "One or more images could not be loaded.");
        }
    }

    private Image loadImageOrFallback(String resourcePath, String fallbackPath) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm());
        } catch (Exception e) {
            System.out.println("Failed to load " + resourcePath + ", using fallback.");
            return new Image("file:" + fallbackPath);
        }
    }

    private String getResourcePath(String resourcePath) {
        return Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm();
    }
}