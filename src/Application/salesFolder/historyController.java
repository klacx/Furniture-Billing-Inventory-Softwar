/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

import Application.shared.viewOrderController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author User
 */
public class historyController {
    
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
      
    private String salesID;
    
    private salesController parentController;
    
    @FXML
    private Pane pane;
    
    @FXML
    private void initialize() {
                
    }
    
    public void setParentController(salesController parentController) {
        this.parentController = parentController;
    }
    
    public void callSalesFunction(){
        parentController.historyBtnPressed();
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
                    if (!searchText.isEmpty() && !values[0].contains(searchText) && !values[2].contains(searchText) && !values[4].contains(searchText) && !values[5].contains(searchText)) {
                        continue; // Skip this order if it doesn't match the search text
                    }
                    // Create Order object from parsed value
                    if(values[6].equals(salesID)){                    
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

        // Add double-click event handler for table rows
        table.setRowFactory(tv -> {
            TableRow<orderInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    orderInfo rowData = table.getSelectionModel().getSelectedItem();
                    if (rowData != null) {
                        String orderNumber = rowData.getOrderNumber();
                        // Handle double-click event here
                        handleRowDoubleClick(orderNumber);
                    }
                }
            });
            return row;
        });
    }
    
    protected void setSalesID(String username){
        salesID = username;
        orderNumberCol.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        customerNameCol.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        approvalCol.setCellValueFactory(cellData -> cellData.getValue().approvalProperty());
        populateTable();
    }

    private void handleRowDoubleClick(String orderNumber) {
        loadOrder(orderNumber);
    }
    
    private void loadOrder(String orderNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/shared/viewOrder.fxml"));
            Parent sceneRoot = loader.load();

            viewOrderController viewOrderController = loader.getController();
            viewOrderController.setHistoryAsParentController(this);
            viewOrderController.setOrderNumber(orderNumber);
            viewOrderController.populateTable();
            
            // Access UI elements after the FXML file is loaded
            Node node = pane;
            Pane parent = (Pane) node.getParent();
            parent.getChildren().clear();
            parent.getChildren().add(sceneRoot);
        } catch (IOException e) {
            System.err.println("Error loading scene FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
