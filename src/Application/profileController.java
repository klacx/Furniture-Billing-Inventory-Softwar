/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.shared;
    
import Application.adminFolder.workerScnController;
import Application.advancedController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 *
 * @author User
 */
public class profileController {
    

    private String username;
    private String storedPassword;
    private String storedRole;
    private String storedFirstName;
    private String storedLastName;
    private String storedEmail;
    private String storedGender;
    private String storedReligion;
    private String storedDob;
    private String storedContactNumber;
    private String storedAddress;
    private String selectedGender = "";
    private Boolean adminMode = false;
    
    private Runnable workersButtonAction;
    
    ArrayList<Boolean> save = new ArrayList<>();
    
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
    private Button Btn_advanced;
    
    @FXML
    private Button Btn_cancel;
    
    @FXML
    private Button Btn_back;
    
    private workerScnController parentController;
    
    public void initialize() {

    }
    
    public void setUsername(String username) {
        this.username = username;
        loadUserProfileInfo();  
        validationCheck();
        Btn_save.setDisable(true);
    }
    

    public void loadUserProfileInfo() {
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path 
        String filePath = currentWorkingDirectory + "/userCredentials.txt"; // Provide absolute path
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {  // Read the file
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("!");
                String storedUsername = userInfo[0];

                if (username.equals(storedUsername)) {
                    storedPassword = userInfo.length > 1 ? userInfo[1] : "";
                    storedRole = userInfo.length > 2 ? userInfo[2] : "";
                    storedFirstName = userInfo.length > 3 ? userInfo[3] : "";
                    storedLastName = userInfo.length > 4 ? userInfo[4] : "";
                    storedEmail = userInfo.length > 5 ? userInfo[5] : "";
                    storedGender = userInfo.length > 6 ? userInfo[6] : "";
                    storedReligion = userInfo.length > 7 ? userInfo[7] : "";
                    storedDob = userInfo.length > 8 ? userInfo[8] : "";
                    storedContactNumber = userInfo.length > 9 ? userInfo[9] : "";
                    storedAddress = userInfo.length > 10 ? userInfo[10] : "";

                    firstNameField.setText(storedFirstName.isEmpty() ? "" : storedFirstName);
                    lastNameField.setText(storedLastName.isEmpty() ? "" : storedLastName);
                    emailField.setText(storedEmail.isEmpty() ? "" : storedEmail);

                    if (!storedGender.isEmpty()) {
                        switch (storedGender) {
                            case "Male":
                                maleButton.setSelected(true);
                                femaleButton.setSelected(false);
                                othersButton.setSelected(false);
                                break;
                            case "Female":
                                femaleButton.setSelected(true);
                                maleButton.setSelected(false);
                                othersButton.setSelected(false);
                                break;
                            case "Others":
                                othersButton.setSelected(true);
                                maleButton.setSelected(false);
                                femaleButton.setSelected(false);
                                break;
                            default:
                                
                                break;
                        }
                    } else {
                        femaleButton.setSelected(false);
                        maleButton.setSelected(false);
                        othersButton.setSelected(false);
                    }

                    religionField.setText(storedReligion);
                    dobDatePicker.setValue(storedDob.isEmpty() ? null : LocalDate.parse(storedDob, DateTimeFormatter.ofPattern("MM/dd/yy")));
                    contactNumberField.setText(storedContactNumber);
                    addressField.setText(storedAddress);

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
        Btn_advanced.setVisible(false);
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
            Btn_advanced.setVisible(true);
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
        Btn_save.setDisable(true);
    } 
    
    private void saveUserProfileInfo() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/userCredentials.txt";
        try {
            File file = new File(filePath);

            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] storedInfo = line.split("!");
                String storedUsername = storedInfo[0];

                if (username.equals(storedUsername)) {
                    String[] userInfo = new String[11];
                    userInfo[0] = username;
                    userInfo[1] = storedInfo[1];  // Retain the existing password
                    userInfo[2] = storedInfo[2];
                    userInfo[3] = firstNameField.getText().length() > 0 ? firstNameField.getText() : "";
                    userInfo[4] = lastNameField.getText().length() > 0 ? lastNameField.getText() : "";
                    userInfo[5] = emailField.getText().length() > 0 ? emailField.getText() : "";
                    userInfo[6] = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : (othersButton.isSelected() ? "Others" : ""));
                    userInfo[7] = religionField.getText().length() > 0 ? religionField.getText() : "";
                    userInfo[8] = dobDatePicker.getValue() != null ? dobDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy")) : "";
                    userInfo[9] = contactNumberField.getText().length() > 0 ? contactNumberField.getText() : "";
                    userInfo[10] = addressField.getText().length() > 0 ? addressField.getText() : "";

                    // Construct the updated line
                    String updatedLine = String.join("!", userInfo);

                    // Replace the line at index i
                    lines.set(i, updatedLine);
                    break;
                }
            }

            Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void maleButtonSelected(){
        femaleButton.setSelected(false);
        othersButton.setSelected(false);
        selectedGender = "male";
        validationCheck();
    }
    
    @FXML
    private void femaleButtonSelected(){
        maleButton.setSelected(false);
        othersButton.setSelected(false);
        selectedGender = "female";
        validationCheck();
    }
    
    @FXML
    private void othersButtonSelected(){
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
        selectedGender = "others";
        validationCheck();
    }
  
    private void contactNumberValidation() {
        if (storedContactNumber.equals(contactNumberField.getText().trim())){
            contactNumberField.getStyleClass().remove("invalid-format");
            contactNumberField.getStyleClass().add("correct-format");
        } 
        else {
            if (contactNumberField.getText().trim().isEmpty()) {
                contactNumberField.getStyleClass().remove("invalid-format");
                contactNumberField.getStyleClass().add("correct-format");
                save.add(true);
            }
            else {
                String regex = "\\d{1,11}"; 

                if (!contactNumberField.getText().trim().matches(regex)) {
                    contactNumberField.getStyleClass().remove("correct-format");
                    contactNumberField.getStyleClass().add("invalid-format");
                    save.add(false);
                } else {
                    contactNumberField.getStyleClass().remove("invalid-format");
                    contactNumberField.getStyleClass().add("correct-format");
                    save.add(true);
                }
            }

        }
    }
    
    private void emailValidation() {    
        if (storedEmail.equals(emailField.getText().trim())){
            emailField.getStyleClass().remove("invalid-format");
            emailField.getStyleClass().add("correct-format");
        }
        else{ 
   
            if (emailField.getText().trim().isEmpty()) {
                emailField.getStyleClass().remove("invalid-format");
                emailField.getStyleClass().add("correct-format");
                save.add(true);
            } 
            else{
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                
                if (!emailField.getText().matches(emailRegex)) {
                    emailField.getStyleClass().remove("correct-format");
                    emailField.getStyleClass().add("invalid-format");
                    save.add(false);
                } 
                else {
                    emailField.getStyleClass().remove("invalid-format");
                    emailField.getStyleClass().add("correct-format");
                    save.add(true);
                }
            }
        }
    }  
    
    private void firstNameValidation() {       
        if (storedFirstName.equals(firstNameField.getText())){
            firstNameField.getStyleClass().remove("invalid-format");
            firstNameField.getStyleClass().add("correct-format");
        }
        else {
            if (firstNameField.getText().trim().isEmpty()) {
                firstNameField.getStyleClass().remove("invalid-format");
                firstNameField.getStyleClass().add("correct-format");
                save.add(true);
            }
            else{
                String regex = "^[a-zA-Z]+$";
                if (!firstNameField.getText().matches(regex)) {           
                    firstNameField.getStyleClass().remove("correct-format");
                    firstNameField.getStyleClass().add("invalid-format");
                    save.add(false);
                } 
                else {               
                    firstNameField.getStyleClass().remove("invalid-format");
                    firstNameField.getStyleClass().add("correct-format");
                    save.add(true);
                }
            }       
        }

    }
    
    private void lastNameValidation() {
        if(storedLastName.equals(emailField.getText())){
            lastNameField.getStyleClass().remove("invalid-format");
            lastNameField.getStyleClass().add("correct-format");
        }
        else{
            if (lastNameField.getText().trim().isEmpty()) {
                lastNameField.getStyleClass().remove("invalid-format");
                lastNameField.getStyleClass().add("correct-format");
                save.add(true);
            } 
            else {
                String regex = "^[a-zA-Z]+$";
                if (!lastNameField.getText().matches(regex)) {           
                    lastNameField.getStyleClass().remove("correct-format");
                    lastNameField.getStyleClass().add("invalid-format");
                    save.add(false);
                } else {               
                    lastNameField.getStyleClass().remove("invalid-format");
                    lastNameField.getStyleClass().add("correct-format");
                    save.add(true);
                }
            }
        }
    }
    
    private void religionValidation() {
        if (religionField.getText().trim().isEmpty()) {
            religionField.getStyleClass().remove("invalid-format");
            religionField.getStyleClass().add("correct-format");
            save.add(true);
        } 
        else {
            String regex = "^[a-zA-Z]+$";
            if (!religionField.getText().matches(regex)) {           
                religionField.getStyleClass().remove("correct-format");
                religionField.getStyleClass().add("invalid-format");
                save.add(false);
            } else {               
                religionField.getStyleClass().remove("invalid-format");
                religionField.getStyleClass().add("correct-format");
                save.add(true);
            }
        }
    }
    
    private void genderValidation(){
        if(!selectedGender.equals(storedGender)){
            save.add(true);
        }
    }
    
    private void dobValidation(){
        save.add(true);
    }
    
    private void addressChanged(){
        save.add(true);
    }
    
    @FXML
    private void validationCheck(){
        save.clear();
        firstNameValidation();
        lastNameValidation();
        emailValidation();
        religionValidation();
        contactNumberValidation();
        genderValidation();
        dobValidation();
        addressChanged();
        if(save.contains(false)){
            Btn_save.setDisable(true);
        }
        else{
            Btn_save.setDisable(false);
        }
    }
    
    @FXML
    private void advancedPressed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/advanced.fxml"));
            Parent sceneRoot = loader.load();
                 
            Scene scene = new Scene(sceneRoot);

            Stage stage = new Stage();

            advancedController advancedController = loader.getController();
            advancedController.setUp(username, storedPassword, storedRole, adminMode);
            advancedController.setParentController(this);
            
            Image icon = new Image("/resources/icon.png");
            stage.getIcons().add(icon);
            stage.setTitle("Change Password");
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();               
        } catch (IOException e) {
            System.err.println("Error loading scene FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void backBtnPressed(){
        parentController.callAdminFunction();
    }    
    
    public void setParentController(workerScnController parentController) {
        this.parentController = parentController;
    }
    
    public void adminMode(){
        Btn_back.setVisible(true);
        adminMode = true;
    }
}    

