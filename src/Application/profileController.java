/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
/**
 *
 * @author User
 */
public class profileController {
    

    private String username;
    private String storedPassword;
    private String storedRole;
    
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private TextField religionField;
    
    @FXML
    private TextField contactNumberField;
    
    @FXML
    private TextArea addressField;
    
    @FXML
    private RadioButton maleButton;
    
    @FXML
    private RadioButton femaleButton;

    @FXML
    private RadioButton othersButton;
    
    @FXML
    private DatePicker dobDatePicker;
    
    @FXML
    private Button Btn_edit;
    
    @FXML
    private Button Btn_save;
    
    @FXML
    private Button Btn_cancel;
    
    public void initialize() {
        
    }
    
    public void setUsername(String username) {
        this.username = username;
        loadUserProfileInfo();
    }
    

    private void loadUserProfileInfo() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/resources/userCredentials.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("!");
                String storedUsername = userInfo[0];
                
                if (username.equals(storedUsername)) {
                    storedPassword = userInfo[1];
                    storedRole = userInfo[2];
                    String firstName = userInfo.length > 3 ? userInfo[3] : "";
                    String lastName = userInfo.length > 4 ? userInfo[4] : "";
                    String email = userInfo.length > 5 ? userInfo[5] : "";
                    String gender = userInfo.length > 6 ? userInfo[6] : "";
                    String religion = userInfo.length > 7 ? userInfo[7] : "";
                    String dob = userInfo.length > 8 ? userInfo[8] : "";
                    String contactNumber = userInfo.length > 9 ? userInfo[9] : "";
                    String address = userInfo.length > 10 ? userInfo[10] : "";

                    firstNameField.setText(firstName.isEmpty() ? "" : firstName);
                    lastNameField.setText(lastName.isEmpty() ? "" : lastName);
                    emailField.setText(email.isEmpty() ? "" : email);

                    if (!gender.isEmpty()) {
                        if ("Male".equals(gender)) {
                            maleButton.setSelected(true);
                            femaleButton.setSelected(false);
                            othersButton.setSelected(false);
                        } else if ("Female".equals(gender)) {
                            femaleButton.setSelected(true);
                            maleButton.setSelected(false);
                            othersButton.setSelected(false);
                        } else if ("Others".equals(gender)) {
                            femaleButton.setSelected(false);
                            maleButton.setSelected(false);
                            othersButton.setSelected(true);
                        }
                    }
                    else{
                        femaleButton.setSelected(false);
                        maleButton.setSelected(false);
                        othersButton.setSelected(false);
                    }

                    religionField.setText(religion.isEmpty() ? "" : religion);
                    dobDatePicker.setValue(dob.isEmpty() ? null : LocalDate.parse(dob, DateTimeFormatter.ofPattern("MM/dd/yy")));
                    contactNumberField.setText(contactNumber.isEmpty() ? "" : contactNumber);
                    addressField.setText(address.isEmpty() ? "" : address);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void editBtnPressed(MouseEvent event){
        Btn_edit.setVisible(false);
        Btn_save.setVisible(true);
        Btn_cancel.setVisible(true);
        firstNameField.setDisable(false);
        lastNameField.setDisable(false);
        emailField.setDisable(false);
        religionField.setDisable(false);
        maleButton.setDisable(false);
        femaleButton.setDisable(false);
        othersButton.setDisable(false);
        dobDatePicker.setDisable(false);
        contactNumberField.setDisable(false);
        addressField.setDisable(false);      
    } 
    
    @FXML
    private void cancelBtnPressed(MouseEvent event){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to cancel?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ButtonType userResponse = confirmationAlert.showAndWait().orElse(ButtonType.NO);
        
        if (userResponse == ButtonType.YES) {
            Btn_edit.setVisible(true);
            Btn_save.setVisible(false);
            Btn_cancel.setVisible(false);
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            emailField.setDisable(true);
            religionField.setDisable(true);
            maleButton.setDisable(true);
            femaleButton.setDisable(true);
            othersButton.setDisable(true);
            dobDatePicker.setDisable(true);
            contactNumberField.setDisable(true);
            addressField.setDisable(true);      
            loadUserProfileInfo();
        }
    } 
    
    @FXML
    private void saveBtnPressed(MouseEvent event){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to save changes?");
        confirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ButtonType userResponse = confirmationAlert.showAndWait().orElse(ButtonType.NO);
        
        if (userResponse == ButtonType.YES) {
            Btn_edit.setVisible(true);
            Btn_save.setVisible(false);
            Btn_cancel.setVisible(false);
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            emailField.setDisable(true);
            religionField.setDisable(true);
            maleButton.setDisable(true);
            femaleButton.setDisable(true);
            othersButton.setDisable(true);
            dobDatePicker.setDisable(true);
            contactNumberField.setDisable(true);
            addressField.setDisable(true);
            saveUserProfileInfo();
        }
    } 
    
    private void saveUserProfileInfo() {
        try {
            Path filePath = Paths.get(getClass().getResource("/resources/userCredentials.txt").toURI());
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] storedInfo = line.split("!");
                String storedUsername = storedInfo[0];
                
                                
                if (username.equals(storedUsername)) {
                    String[] userInfo = new String[11];
                    userInfo[0] = username;
                    userInfo[1] = storedPassword;
                    userInfo[2] = storedRole ;
                    userInfo[3] = firstNameField.getText().length() > 0 ? firstNameField.getText() : "";
                    userInfo[4] = lastNameField.getText().length() > 0 ? lastNameField.getText() : "";
                    userInfo[5] = emailField.getText().length() > 0 ? emailField.getText() : "";
                    userInfo[6] = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : (othersButton.isSelected() ?"Others" : ""));
                    userInfo[7] = religionField.getText().length() > 0 ? religionField.getText() : "";
                    userInfo[8] = dobDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                    userInfo[9] = contactNumberField.getText().length() > 0 ? contactNumberField.getText() : "";
                    userInfo[10] = addressField.getText().length() > 0 ? addressField.getText() : "";

                        // Construct the updated line
                    String updatedLine = String.join("!", userInfo);

                        // Replace the line at index i
                    lines.set(i, updatedLine);
                    break;  
                }
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } 
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void maleButtonSelected(){
        femaleButton.setSelected(false);
        othersButton.setSelected(false);
    }
    
    @FXML
    private void femaleButtonSelected(){
        maleButton.setSelected(false);
        othersButton.setSelected(false);
    }
    
    @FXML
    private void othersButtonSelected(){
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
    }
}    

