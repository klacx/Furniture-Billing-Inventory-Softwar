/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

/**
 *
 * @author User
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class cartController {
    
     @FXML
    private Button Btn_add;

    @FXML
    private Button Btn_cancel;

    @FXML
    private Button Btn_save;

    @FXML
    private TableColumn<orderDetailsInfo, String> amountCol;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerNumberField;

    @FXML
    private TableColumn<orderDetailsInfo, Void> deleteCol;

    @FXML
    private TableColumn<orderDetailsInfo, String> descriptionCol;

    @FXML
    private TableColumn<orderDetailsInfo, String> idCol;

    @FXML
    private TextField orderNumberField;

    @FXML
    private Pane pane;

    @FXML
    private TableColumn<orderDetailsInfo, String> quantityCol;

    @FXML
    private TableView<orderDetailsInfo> table;

    @FXML
    private TableColumn<orderDetailsInfo, String> unitPriceCol;
    
    private orderTableController parentController;
    
    private String username;
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setParentController(orderTableController parentController) {
        this.parentController = parentController;
    }
    
    @FXML
    private void cancelBtnPressed(){
        if (parentController != null) {
            parentController.callParentFunction(); 
        }
    }
    
    @FXML
    private void addBtnPressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/salesFolder/addProduct.fxml"));
            Parent sceneRoot = loader.load();
                 
            Scene scene = new Scene(sceneRoot);

            Stage stage = new Stage();

            addProductController addProductController = loader.getController();
            addProductController.setParentController(this);
            addProductController.setUsername(username);
            
            Image icon = new Image("/resources/icon.png");
            stage.getIcons().add(icon);
            stage.setTitle("Add Product");
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
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());        
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());      
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        deleteCol.setCellFactory(getButtonCellFactory());
        orderNumberField.setText(String.valueOf(getNextOrderId()));
    }
    
    protected void populateTable() {
        table.getItems().clear();
        ObservableList<orderDetailsInfo> orderDetailss = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/cart.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                // Check if the array has the expected length
                if (values.length >= 6) {
                    // Create orderDetails object from parsed values
                    orderDetailsInfo orderDetails = new orderDetailsInfo(values[1], values[2] + ".00", values[3], values[4] + ".00", values[5]);

                    if (!values[0].contains(username)) {
                        continue;
                    }
                    // Add orderDetails to list
                    orderDetailss.add(orderDetails);
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(orderDetailss);
    }
    
    private Callback<TableColumn<orderDetailsInfo, Void>, TableCell<orderDetailsInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<orderDetailsInfo, Void> call(final TableColumn<orderDetailsInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/delete.png")));
                    private final HBox buttonsContainer = new HBox(deleteIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        deleteIcon.setOnMouseClicked(event -> {
                            orderDetailsInfo rowData = getTableRow().getItem();
                            String productID = rowData.getProductID();
                            handleDeleteAction(username, productID);
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
    
    private void handleDeleteAction(String salesID, String productID) {
        deleteLine(salesID, productID);
        populateTable();
    }
    
    private void deleteLine(String salesID, String productID) {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/cart.txt";
        try {
            File file = new File(filePath);
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            // Create a new list to store the updated lines
            List<String> updatedLines = new ArrayList<>();

            // Iterate over each line
            for (String line : lines) {
                String[] cartInfo = line.split("!");
                // Check if the line matches the specified productID and salesID
                if (!(cartInfo[0].equals(salesID) && cartInfo[1].equals(productID))) {
                    // If it doesn't match, add it to the updated lines
                    updatedLines.add(line);
                }
            }

            // Write the updated lines back to the file
            Files.write(file.toPath(), updatedLines, StandardCharsets.UTF_8);

            System.out.println("Lines removed successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting line: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int getNextOrderId() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/order.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String lastLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (!line.trim().isEmpty()) {
                    lastLine = line;
                }
            }

            if (lastLine != null) {
                String[] orderInfo = lastLine.split("!");
                int lastOrderId = Integer.parseInt(orderInfo[0]);
                return lastOrderId + 1;
            } else {
                // If the file is empty, start from order ID 1
                return 1;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
