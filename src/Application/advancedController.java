/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
/**
 *
 * @author User
 */
public class advancedController {
    
    private String workerID;
    private String storedPassword;
    private String storedRole;
    private ArrayList<Boolean> save = new ArrayList<>();
    private profileController parentController;
    
    @FXML
    private Button Btn_cancel;

    @FXML
    private Button Btn_save;
    
    @FXML
    private PasswordField confirmPassword;

    @FXML
    private PasswordField newPassword;
    
    @FXML
    private ComboBox<String> rolesComboBox;

    @FXML
    private void cancelBtnPressed(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveBtnPressed(MouseEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to save the changes?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ButtonType userResponse = confirmationAlert.showAndWait().orElse(ButtonType.NO);
        
        if(userResponse == ButtonType.YES){
            updateInfo();
            parentController.loadUserProfileInfo();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }        
    }
    
    public void setUp(String passedWorkerID, String password, String role, Boolean adminMode){       
        rolesComboBox.getItems().addAll("sales","officer", "admin");
        workerID = passedWorkerID;
        storedPassword = password;
        storedRole = role;
        if(adminMode)rolesComboBox.setDisable(false);
        rolesComboBox.getSelectionModel().select(storedRole);
    }
    
    public void enableRolesComboBox(){
        rolesComboBox.setDisable(false);
    }      
    
    @FXML
    private void validationCheck(){
        save.clear();
        if(newPassword.getText().length()>5 & !newPassword.getText().equals(storedPassword)){
            if(confirmPassword.getText().equals(newPassword.getText())){
                save.add(true);
            }
            else{
                save.add(false);
            }
        }
        else{
            save.add(false);
        }
        if(!rolesComboBox.getValue().equals(storedRole)){
            save.add(true);
        }
        else{
            save.add(false);
        }
        if(save.contains(true)){
            Btn_save.setDisable(false);
        }
        else{
            Btn_save.setDisable(true);
        }
    }
    
    private void updateInfo(){
        String usernameToFind = workerID; // Username to search for
        String newPassword = confirmPassword.getText(); // New password
        String newRoles = rolesComboBox.getValue(); // New roles
        
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path 
        String filePath = currentWorkingDirectory + "/userCredentials.txt"; // Provide absolute path
        String tmpFilePath = currentWorkingDirectory + "userCredentials.txt.tmp"; // Temporary file path
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("!");
                if (userInfo.length >= 1 && userInfo[0].equals(usernameToFind)) {
                    // Found the username, modify the line
                    if (newPassword != null) {
                        // Replace password
                        userInfo[1] = newPassword;
                    }
                    else{
                        userInfo[1] = storedPassword;
                    }
                    if (newRoles != null) {
                        // Replace roles
                        userInfo[2] = newRoles;
                    }
                    line = String.join("!", userInfo);
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Replace the original file with the temporary file
        try {
            Files.move(Paths.get(tmpFilePath), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setParentController(profileController parentController) {
        this.parentController = parentController;
    }
}

