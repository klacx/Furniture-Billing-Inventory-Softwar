/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application;

/**
 *
 * @author User
 */
import Application.adminFolder.adminController;
import Application.officerFolder.officerController;
import Application.salesFolder.salesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;


public class App extends Application {
    private Scene scene;

    private String role;
    private String enteredUsername;
    private String enteredPassword;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginFolder/LogInScn.fxml")));
            scene = new Scene(root, 311, 449);

            // Set the login button action
            Button loginButton = (Button) scene.lookup("#LogInBtn");
            loginButton.setOnAction(e -> handleLogin());
            
            Image icon = new Image("/resources/icon.png");
            primaryStage.getIcons().add(icon);

            primaryStage.setTitle("Log In");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleLogin() {
        // Get references to the text fields
        TextField usernameField = (TextField) scene.lookup("#emailTextBox");
        PasswordField passwordField = (PasswordField) scene.lookup("#passwordTextBox");

        // Get the entered username and password
        enteredUsername = usernameField.getText();
        enteredPassword = passwordField.getText();

        // Check credentials
        if (checkCredentials(enteredUsername, enteredPassword)) {
            closeScene();
            loadScene(role+"Folder/"+role+".fxml");

        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }



    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/resources/userCredentials.txt"))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split("!");

                // Check if the array has at least two elements before accessing them
                if (credentials.length >= 3) {
                    String storedUsername = credentials[0];
                    String storedPassword = credentials[1];
                    role = credentials[2];

                    // Check if entered credentials match stored credentials
                    if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                        return true;
                    }
                } 
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to read user credentials file.");
            e.printStackTrace();
        }
        return false;
    }

    private void closeScene() {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

private void loadScene(String sceneName) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
        Parent sceneRoot = loader.load();

        // Access the controller
        if("admin".equals(role)){
            adminController controller = loader.getController();
            controller.setUsername(enteredUsername);
        }
        else if("officer".equals(role)){
            officerController controller = loader.getController();
            controller.setUsername(enteredUsername);
        }
        else if("sales".equals(role)){
            salesController controller = loader.getController();
            controller.setUsername(enteredUsername);
    }
        
        Scene roleScene = new Scene(sceneRoot);
        // Create a new stage for the specific scene
        Stage roleStage = new Stage();
        
        Image icon = new Image("/resources/icon.png");
        roleStage.getIcons().add(icon);
        roleStage.setTitle("Yoyo Furniture");
        roleStage.setScene(roleScene);
        roleStage.setResizable(false);
        roleStage.show();
    } catch (IOException e) {
        System.err.println("Error loading scene FXML: " + e.getMessage());
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}