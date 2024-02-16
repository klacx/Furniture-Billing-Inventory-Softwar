/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.officerFolder;

import Application.salesFolder.orderDetailsInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class invoiceController {
    @FXML
    private TableColumn<orderDetailsInfo, String> amountCol;

    @FXML
    private Text customerNameField;

    @FXML
    private Text customerNumberField;

    @FXML
    private Text dateField;
    
     @FXML
    private TableView<orderDetailsInfo> table;

    @FXML
    private TableColumn<orderDetailsInfo, String> descriptionCol;

    @FXML
    private TableColumn<orderDetailsInfo, String> idCol;

    @FXML
    private Text orderNumberField;

    @FXML
    private TableColumn<orderDetailsInfo, String> quantityCol;

    @FXML
    private Text salesField;

    @FXML
    private Text totalAmountField;

    @FXML
    private TableColumn<orderDetailsInfo, String> unitPriceCol;
    
    public String orderNumber;
    
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());        
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());      
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }
    
    public void populateTable(String orderNumber) {       
        table.getItems().clear();
        ObservableList<orderDetailsInfo> orderDetailsInfo = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/orderDetails.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                // Check if the array has the expected length
                if (values.length >= 7) {
                    // Create orderDetails object from parsed values
                    String formattedUnitPrice = String.format("%.2f", Double.parseDouble(values[3]));
                    String formattedAmount = String.format("%.2f", Double.parseDouble(values[5]));
                    orderDetailsInfo orderDetails = new orderDetailsInfo(values[2], formattedUnitPrice , values[4], formattedAmount , values[6]);
                    if (!values[1].equals(orderNumber)) {
                        continue;
                    }
                    // Add orderDetails to list
                    orderDetailsInfo.add(orderDetails);
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(orderDetailsInfo);
        readCustomerDetails();
    }
    
    protected void readCustomerDetails() {
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                if (!values[0].equals(orderNumber)) {
                    continue; // Skip if it doesn't match the search text
                }
                orderNumberField.setText(values[0]);
                totalAmountField.setText(values[1]);
                dateField.setText(values[2]);
                customerNameField.setText(values[4]);
                customerNumberField.setText(values[5]);
                salesField.setText(values[6]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        populateTable(this.orderNumber);
    }
}
