/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.adminFolder;

/**
 *
 * @author User
 */

import Application.profileController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class workerScnController{
    
    @FXML
    private Pane pane;
    
    @FXML
    private Button Btn_new;

    @FXML
    private TableColumn<workerInfo, String> contactNumberCol;

    @FXML
    private TableColumn<workerInfo, String> emailCol;

    @FXML
    private TableColumn<workerInfo, String> idCol;

    @FXML
    private TableColumn<workerInfo, String> nameCol;

    @FXML
    private TableColumn<workerInfo, String> rolesCol;
    
    @FXML
    private TableColumn<workerInfo, Void> editCol;

    @FXML
    private TableView<workerInfo> table;
    
    @FXML
    private TextField searchField;
    
    private adminController parentController;

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().workerIDProperty());
        rolesCol.setCellValueFactory(cellData -> cellData.getValue().rolesProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        editCol.setCellFactory(getButtonCellFactory());
        // Load data from text file and populate table
        populateTable();
    }

    protected void populateTable() {
        ObservableList<workerInfo> workers = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/userCredentials.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                String workerID = "";
                String password = "";
                String roles = "";
                String firstName = "";
                String lastName = "";
                String email = "";
                String gender = "";
                String religion = "";
                String dob = "";
                String contactNumber = "";

                if (values.length > 0) workerID = values[0];
                if (values.length > 1) password = values[1];
                if (values.length > 2) roles = values[2];
                if (values.length > 3) firstName = values[3];
                if (values.length > 4) lastName = values[4];
                if (values.length > 5) email = values[5];
                if (values.length > 6) gender = values[6];
                if (values.length > 7) religion = values[7];
                if (values.length > 8) dob = values[8];
                if (values.length > 9) contactNumber = values[9];

                // Check if the worker ID contains the search text
                String searchText = searchField.getText();
                if (!searchText.isEmpty() && !workerID.contains(searchText)) {
                    continue; // Skip this worker if it doesn't match the search text
                }

                if (!roles.equals("admin")){                
                    workerInfo worker = new workerInfo(workerID, roles, firstName + " " + lastName, email, contactNumber);
                    workers.add(worker);
                }
                // Add worker to list           
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(workers);
    }
    
    private Callback<TableColumn<workerInfo, Void>, TableCell<workerInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<workerInfo, Void> call(final TableColumn<workerInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit_black.png")));
                    private final HBox buttonsContainer = new HBox(editIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        editIcon.setOnMouseClicked(event -> {
                            workerInfo rowData = getTableRow().getItem();
                            String workerID = rowData.getWorkerID();
                            handleEditAction(workerID);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        };
    }
    
    private void handleEditAction(String workerID) {
        loadScene(workerID);
    }
    
    private void loadScene(String workerID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/profile.fxml"));
            Parent sceneRoot = loader.load();
            
            profileController profileController = loader.getController();
            profileController.setUsername(workerID);
            profileController.setParentController(this);
            profileController.adminMode();
            
            Node node = pane;
            Pane parent = (Pane) node.getParent();
            parent.getChildren().clear();
            parent.getChildren().add(sceneRoot);
        } catch (IOException e) {
            System.err.println("Error loading scene FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setParentController(adminController parentController) {
        this.parentController = parentController;
    }
    
    public void callAdminFunction() {
        if (parentController != null) {
            parentController.workersBtnPressed(); // Call the function from adminController
        }
    } 
    
    @FXML
    private void newBtnPressed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/adminFolder/addUser.fxml"));
            Parent sceneRoot = loader.load();
                 
            Scene scene = new Scene(sceneRoot);

            Stage stage = new Stage();

            addUserController addUserController = loader.getController();
            addUserController.setParentController(this);
            
            Image icon = new Image("/resources/icon.png");
            stage.getIcons().add(icon);
            stage.setTitle("Add New User");
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
    private void search(){
        populateTable();
    }
}
