/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

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

public class salesController implements Initializable {
    
    private String username;
    
    @FXML
    private Button Btn_profile;
    
    @FXML
    private Button Btn_order;
    
    @FXML
    private Button Btn_history;
    
    @FXML
    private Label lbl_username;
    
    @FXML
    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code, if any
    }

    protected void orderScn() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: #000000;");
        Btn_history.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/salesFolder/orderScn.fxml",2);
    }
    
    @FXML
    private void profileBtnPressed(MouseEvent event) {
        Btn_profile.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_history.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/profile.fxml",1);
    }
    
    @FXML
    private void orderBtnPressed(MouseEvent event) {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: #000000;");
        Btn_history.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/salesFolder/orderScn.fxml",2);
    }
    
    @FXML
    private void historyBtnPressed(MouseEvent event) {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_history.setStyle("-fx-background-color: #000000;");
        
        loadScene("/Application/salesFolder/historyScn.fxml",3);
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
                    orderTableController orderTableController = loader.getController();
                    orderTableController.setParentController(this);
                    orderTableController.setUsername(username);               
                }
                case 3 -> {
                    historyController historyController = loader.getController();
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
