/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.salesFolder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class orderTableController {

    @FXML
    private TableColumn<orderInfo, String> orderNumberCol;

    @FXML
    private TableColumn<orderInfo, String> amountCol;

    @FXML
    private TableColumn<orderInfo, String> dateCol;

    @FXML
    private TableColumn<orderInfo, String> statusCol;

    @FXML
    private TableColumn<orderInfo, String> customerNameCol;

    @FXML
    private TableColumn<orderInfo, String> contactNumberCol;

    @FXML
    private TableColumn<orderInfo, Void> editCol;
    
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
        editCol.setCellFactory(getButtonCellFactory());
        // Load data from text file and populate table
        populateTable();
    }

    private void populateTable() {
        ObservableList<orderInfo> orders = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");
                
                // Create Order object from parsed values
                orderInfo order = new orderInfo(values[0], values[1], values[2], values[3], values[4], values[5]);

                // Add order to list
                orders.add(order);
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
                    private final ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit_black.png")));
                    private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/delete.png")));
                    private final HBox buttonsContainer = new HBox(10, editIcon, deleteIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        editIcon.setOnMouseClicked(event -> {
                            orderInfo rowData = getTableRow().getItem();
                            String orderNumber = rowData.getOrderNumber();
                            handleEditAction(orderNumber);
                        });

                        deleteIcon.setOnMouseClicked(event -> {
                            orderInfo rowData = getTableView().getItems().get(getIndex());
                            // Handle delete action
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
    
    private void handleEditAction(String orderNumber) {
        
        System.out.println("Edit action triggered for: " + orderNumber);
    }
}

