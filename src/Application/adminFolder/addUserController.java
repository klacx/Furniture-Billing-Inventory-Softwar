/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.adminFolder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class addUserController implements Initializable {
    
    private workerScnController parentController;
    
    @FXML
    private Button Btn_cancel;

    @FXML
    private Button Btn_save;

    @FXML
    private TextField passwordField;  

    @FXML
    private TextField usernameField;
    
    @FXML
    private ComboBox<String> rolesComboBox;

    @FXML
    private void cancelBtnPressed(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveBtnPressed(MouseEvent event) {   
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path 
        String filePath = currentWorkingDirectory + "/userCredentials.txt"; // Provide absolute path
        String newRow = usernameField.getText() + "!" + passwordField.getText() + "!" + rolesComboBox.getValue() + "!!!!!!!";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Append new data to the file
            writer.write(newRow);
            writer.newLine(); // Add a newline after each piece of data if needed
        } catch (IOException e) {
            System.err.println("Error writing data to the file: " + e.getMessage());
        }    
        parentController.populateTable();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rolesComboBox.getItems().addAll("sales","officer", "admin");
    }

    public void setParentController(workerScnController parentController) {
        this.parentController = parentController;
    }
    
    @FXML
    private void addValidation(){
        if(usernameField.getText().length()>0 & passwordField.getText().length()>0 & rolesComboBox.getValue()!= null){
            Btn_save.setDisable(false);
        }
        else{
            Btn_save.setDisable(true);
        }
    }
}
