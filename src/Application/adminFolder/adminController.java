/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.adminFolder;

import Application.profileController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class adminController implements Initializable {
    
    private String username;

    @FXML
    private Button Btn_profile;
    
    @FXML
    private Button Btn_workers;
    
    @FXML
    private Button Btn_report;
    
    @FXML
    private Label lbl_username;
    
    @FXML
    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void profileBtnPressed(MouseEvent event) {
        Btn_profile.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_workers.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/profile.fxml",1);
    }
    
    @FXML
    private void workersBtnPressed(MouseEvent event) {
        Btn_workers.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/adminFolder/workersScn.fxml", 2);
    }
    
    @FXML
    private void reportBtnPressed(MouseEvent event) {
        Btn_report.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_workers.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/adminFolder/reportScn.fxml", 3);
    }
    
    public void setUsername(String username) {
        this.username = username;
        lbl_username.setText("ID:" + username);
    }
    
    private void loadScene(String sceneName, int choice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
            Parent sceneRoot = loader.load();
            
            switch (choice) {
                case 1 -> {
                    profileController profileController = loader.getController();
                    profileController.setUsername(username);
                }
                case 2 -> {
                    workerScnController workerScnController = loader.getController();
                }
                case 3 -> {
                }
                default -> {
                }
            }
            
            pane.getChildren().clear();
            pane.getChildren().setAll(sceneRoot);
        } catch (IOException e) {
            System.err.println("Error loading scene FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}