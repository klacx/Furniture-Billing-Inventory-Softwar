/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.shared;

import Application.officerFolder.statusController;
import Application.salesFolder.historyController;
import Application.salesFolder.orderDetailsInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class viewOrderController {
    
    @FXML
    private TableColumn<orderDetailsInfo, String> amountCol;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerNumberField;

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
    private ImageView approved;

    @FXML
    private Text totalAmountText;
    
    private historyController historyParentController;
    
    private statusController statusParentController;

    @FXML
    private TableColumn<orderDetailsInfo, String> unitPriceCol;
    
    public void setHistoryAsParentController(historyController parentController) {
        this.historyParentController = parentController;
    }
    
    public void setStatusAsParentController(statusController parentController) {
        this.statusParentController = parentController;
    }
    
    public void setOrderNumber(String orderNumber){
        orderNumberField.setText(orderNumber);
    }
    
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());        
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());      
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        populateTable();
    }
    
    public void populateTable() {       
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
                    if (!values[1].contains(orderNumberField.getText())) {
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
        calculateTotalAmount();
    }
    
    protected void readCustomerDetails() {
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                if (!values[0].contains(orderNumberField.getText())) {
                    continue; // Skip if it doesn't match the search text
                }
                // Add product to list
                customerNameField.setText(values[4]);
                customerNumberField.setText(values[5]);
                if(values[7].equals("approved")){
                    approved.setVisible(true);
                }
                else{
                    approved.setVisible(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void calculateTotalAmount() {       //calculate the total amount in the cart
        double totalAmount = 0.0;

        // Iterate over each item in the table
        for (orderDetailsInfo item : table.getItems()) {
            // Extract the amount value from the item
            String amountStr = amountCol.getCellObservableValue(item).getValue();
            
            // Convert the amount value to a double and add it to the total
            totalAmount += Double.parseDouble(amountStr);
        }

        String formattedAmount = String.format("RM%.2f", totalAmount);
        totalAmountText.setText(formattedAmount);
    }
    
    @FXML
    private void backBtnPressed(){
        if(historyParentController!=null){
            historyParentController.callSalesFunction();
        }
        if(statusParentController!=null){
            statusParentController.callOfficerFunction();
        }
    } 
}
