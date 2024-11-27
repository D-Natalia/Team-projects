package application.controllers;

import database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.JournalEntry;
import models.User;
import models.exceptions.EntryNotFoundException;
import repositories.JournalEntryRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

public class MyJournalController {
    @FXML
    private ImageView banner;
    @FXML
    private VBox entryContainer;
    @FXML
    private Button backButton;
    @FXML
    private Button removeEntryButton;

    private JournalEntryRepository journalEntryRepository;
    private User user;
    public MyJournalController() {}

    public void setUser(User user) {
        if(user != null) {
            this.user = user;
        }
    }

    public void setConnection() {
        Connection conn = DatabaseManager.getInstance().getConnection().getConnection();
        if (conn != null) {
            System.out.println("Database connection established.");
            this.journalEntryRepository = new JournalEntryRepository(conn);
        } else {
            System.out.println("Failed to establish database connection.");
        }
    }

    @FXML
    public void initialize() {
        setConnection();
        URL imageUrl = getClass().getResource("/images/My.jpg");
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toString());
            banner.setImage(image);
        }
        backButton.setOnAction(event -> handleBack());
        removeEntryButton.setOnAction(event -> removeEntry());
    }

    public void loadJournalEntries() {
        if (journalEntryRepository == null) {
            System.out.println("JournalEntryRepository is not initialized.");
            return;
        }

        List<JournalEntry> entries = journalEntryRepository.getEntriesByUserId(user.getId());

        System.out.println("Loaded entries: " + entries.size());

        entryContainer.getChildren().clear();

        for (JournalEntry entry : entries) {
            VBox entryBox = new VBox();
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSpacing(10);
            entryBox.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-border-width: 1;");

            Label titleLabel = new Label(entry.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-alignment: center;");

            TextFlow descriptionTextFlow = new TextFlow();
            Text descriptionText = new Text(entry.getContent());
            descriptionText.setWrappingWidth(400);
            descriptionText.setStyle("-fx-font-size: 14px;");
            descriptionTextFlow.getChildren().add(descriptionText);

            Text costText = new Text("Cost: " + String.valueOf(entry.getCost()));
            costText.setStyle("-fx-font-size: 14px;");

            HBox costBox = new HBox();
            costBox.getChildren().add(costText);
            costBox.setAlignment(Pos.CENTER_LEFT);

            Text isPublicText = new Text("Public: " + String.valueOf(entry.isPublic() ? "Yes" : "No"));
            isPublicText.setStyle("-fx-font-size: 14px;");
            HBox isPublicBox = new HBox();
            isPublicBox.getChildren().add(isPublicText);
            isPublicBox.setAlignment(Pos.CENTER_LEFT);

            ImageView imageView = new ImageView();
            String imagePath = entry.getImagePath();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            entryBox.getChildren().addAll(titleLabel, imageView, descriptionTextFlow, costBox,isPublicBox);
            entryContainer.getChildren().add(entryBox);
        }
    }
    private void removeEntry() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Entry");
        dialog.setHeaderText("Remove Journal Entry");
        dialog.setContentText("Enter the title of the entry you want to delete:");

        dialog.showAndWait().ifPresent(title -> {
            try {
                JournalEntry entry = journalEntryRepository.findEntryByTitle(title);
                if (entry.getUserId() != user.getId()) {
                    showError("Permission Denied", "You cannot delete this entry.");
                    return;
                }
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Deletion");
                confirmDialog.setHeaderText("Are you sure you want to delete this entry?");
                confirmDialog.setContentText("Entry: " + entry.getTitle());

                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        journalEntryRepository.removeEntry(entry);
                        loadJournalEntries();
                    }
                });
            } catch (EntryNotFoundException e) {
                showError("Entry Not Found", "No entry found with title: " + title);
            }
        });
    }


    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
            Parent mainRoot = loader.load();

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            MainController controller = loader.getController();
            controller.setUser(user);
            currentStage.setScene(new Scene(mainRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
