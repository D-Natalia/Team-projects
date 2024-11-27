package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repositories.UserRepository;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> handleBackToLogin());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Username and password cannot be empty.");
            return;
        }

        UserRepository userRepository = new UserRepository();
        if (userRepository.isUserExists(username)) {
            showAlert("Registration failed", "Username already exists. Please choose another.");
            return;
        }

        boolean registrationSuccess = userRepository.registerUser(username, password);
        if (registrationSuccess) {
            showAlert("Registration successful!", "Welcome, " + username);
            usernameField.clear();
            passwordField.clear();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Registration failed", "There was an error during registration. Please try again.");
        }
    }

    private void handleBackToLogin() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
