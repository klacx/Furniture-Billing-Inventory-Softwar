/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.shared;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author User
 */
public abstract class sceneController {
    
    public String username;
    
    @FXML
    public Button Btn_profile;
    
    @FXML
    public Label lbl_username;
    
    @FXML
    public Pane pane;
    
    public void setUsername(String username) {
        this.username = username;
        lbl_username.setText("ID:" + username);
        profileBtnPressed();
    }
    
    @FXML
    private void profileBtnPressed() {
    }
    
    private void loadScene() {
    }
}
