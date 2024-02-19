/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.officerFolder;

import Application.shared.profileController;
import Application.shared.sceneController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;


public class officerController extends sceneController implements Initializable {

    @FXML
    private Button Btn_processSale;
    
    @FXML
    private Button Btn_status;
    
    @FXML
    private Button Btn_report;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code, if any
    }

    @FXML
    private void profileBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_processSale.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/profile.fxml", 1);
    }
    
    @FXML
    public void processSaleBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_processSale.setStyle("-fx-background-color: #000000;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/officerFolder/submitScn.fxml", 2);
    }
    
    
    @FXML
    protected void statusBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_processSale.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: #000000;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/officerFolder/statusScn.fxml", 3);
    }
    
    @FXML
    private void reportBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_processSale.setStyle("-fx-background-color: transparen;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: #000000;");

        loadScene("/Application/shared/reportScn.fxml", 4);
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
                    processSaleController processSaleController = loader.getController();
                    processSaleController.setParentController(this);                  
                }
                case 3 -> {
                    statusController statusController = loader.getController();
                    statusController.setParentController(this);
                }   
                case 4 -> {

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
