/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.officerFolder;

import Application.salesFolder.orderInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author User
 */
public class statusController {
    
    @FXML
    private TableColumn<orderInfo, String> amountCol;

    @FXML
    private TableColumn<orderInfo, String> approvalCol;

    @FXML
    private TableColumn<orderInfo, String> contactNumberCol;

    @FXML
    private TableColumn<orderInfo, String> customerNameCol;

    @FXML
    private TableColumn<orderInfo, String> dateCol;

    @FXML
    private TableColumn<orderInfo, String> orderNumberCol;

    @FXML
    private TableColumn<orderInfo, String> statusCol;

    @FXML
    private TableView<orderInfo> table;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button Btn_search;
    
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private void initialize() {
        orderNumberCol.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        customerNameCol.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        approvalCol.setCellValueFactory(cellData -> cellData.getValue().approvalProperty());
        statusComboBox.getItems().addAll("All","Progressing", "Done");
        statusComboBox.getSelectionModel().select("All");
        populateTable();         
    }
    
    @FXML
    protected void populateTable() {
        ObservableList<orderInfo> orders = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");
                if (values.length >= 6){
                    String searchText = searchField.getText();
                    if (!searchText.isEmpty() && !values[0].contains(searchText)) {
                        continue; // Skip this worker if it doesn't match the search text
                    }
                    // Create Order object from parsed value
                    String statusFilter = statusComboBox.getValue();                
                    if(statusFilter.equals("All")){                    
                        orderInfo order = new orderInfo(values[0], values[1], values[2], values[3], values[4], values[5], values[7]);
                        orders.add(order);
                    }
                    else if(statusFilter.equals(values[3])){
                        orderInfo order = new orderInfo(values[0], values[1], values[2], values[3], values[4], values[5], values[7]);
                        orders.add(order);                   
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(orders);
    }
}
