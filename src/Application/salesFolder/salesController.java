/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class salesController extends sceneController implements Initializable {
    
    @FXML
    private Button Btn_order;
    
    @FXML
    private Button Btn_history;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code, if any
    }
    
    @FXML
    private void profileBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_history.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/profile.fxml",1);
    }
    
    @FXML
    protected void orderBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: #000000;");
        Btn_history.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/salesFolder/orderScn.fxml",2);
    }
    
    @FXML
    protected void historyBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_history.setStyle("-fx-background-color: #000000;");
        
        loadScene("/Application/salesFolder/historyScn.fxml",3);
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
                    orderTableController orderTableController = loader.getController();
                    orderTableController.setParentController(this);
                    orderTableController.setUsername(username);               
                }
                case 3 -> {
                    historyController historyController = loader.getController();
                    historyController.setParentController(this);
                    historyController.setSalesID(username);                   
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
