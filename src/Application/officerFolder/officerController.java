/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.officerFolder;

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

public class officerController implements Initializable {

    private String username;
    
    @FXML
    private Button Btn_profile;
    
    @FXML
    private Button Btn_order;
    
    @FXML
    private Button Btn_invoice;
    
    @FXML
    private Button Btn_status;
    
    @FXML
    private Button Btn_report;
    
    @FXML
    private Label lbl_username;
    
    @FXML
    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code, if any
    }

    @FXML
    private void profileBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: #000000;");  // Dark Gray
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_invoice.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/profile.fxml", 1);
    }
    
    @FXML
    private void orderBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: #000000;");
        Btn_invoice.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/officerFolder/orderScn.fxml", 2);
    }
    
    @FXML
    private void invoiceBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: transparen;");
        Btn_invoice.setStyle("-fx-background-color: #000000;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: transparent;");

        loadScene("/Application/officerFolder/invoiceScn.fxml", 3);
    }
    
    @FXML
    protected void statusBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: transparent;");
        Btn_invoice.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: #000000;");
        Btn_report.setStyle("-fx-background-color: transparent;");
        
        loadScene("/Application/officerFolder/statusScn.fxml", 4);
    }
    
    @FXML
    private void reportBtnPressed() {
        Btn_profile.setStyle("-fx-background-color: transparent;");
        Btn_order.setStyle("-fx-background-color: transparen;");
        Btn_invoice.setStyle("-fx-background-color: transparent;");
        Btn_status.setStyle("-fx-background-color: transparent;");
        Btn_report.setStyle("-fx-background-color: #000000;");

        loadScene("/Application/officerFolder/reportScn.fxml", 5);
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
                }
                case 3 -> {
                }
                case 4 -> {
                    statusController statusController = loader.getController();
                    statusController.setParentController(this);
                }
                case 5 -> {
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
