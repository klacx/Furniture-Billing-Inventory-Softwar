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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class processSaleController {
    
    @FXML
    private Button Btn_search;

    @FXML
    private TableColumn<orderInfo, Void> actionCol;

    @FXML
    private TableColumn<orderInfo, String> amountCol;

    @FXML
    private TableColumn<orderInfo, String> contactNumberCol;

    @FXML
    private TableColumn<orderInfo, String> customerNameCol;

    @FXML
    private TableColumn<orderInfo, String> dateCol;

    @FXML
    private TableColumn<orderInfo, String> orderNumberCol;

    @FXML
    private Pane pane;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<orderInfo, String> statusCol;

    @FXML
    private ComboBox<orderInfo> statusComboBox;

    @FXML
    private TableView<orderInfo> table;
    
    @FXML
    private void initialize() {
        orderNumberCol.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        customerNameCol.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        actionCol.setCellFactory(getButtonCellFactory());
        // Load data from text file and populate table
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
                if (values.length >= 7){
                    // Create Order object from parsed values
                    

                    String searchText = searchField.getText();
                    if (!searchText.isEmpty() && !values[0].contains(searchText)&& !values[4].contains(searchText) && !values[5].contains(searchText) && !values[2].contains(searchText)) {
                        continue; // Skip this worker if it doesn't match the search text
                    }
                    // Add order to list
                    if(values[7].equals("unapproved")){
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
    
    private Callback<TableColumn<orderInfo, Void>, TableCell<orderInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<orderInfo, Void> call(final TableColumn<orderInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView statusIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/approval.png")));
                    private final HBox buttonsContainer = new HBox(statusIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        statusIcon.setOnMouseClicked(event -> {
                            orderInfo rowData = getTableRow().getItem();
                            String orderNumber = rowData.getOrderNumber();
                            handleApprovalAction(orderNumber);
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
    
    private void handleApprovalAction(String orderNumber) {
        populateTable();
    }
}
