/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.salesFolder;

import Application.profileController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private Button Btn_create;
    
    @FXML
    private Button Btn_search;
    
    @FXML
    private Pane pane;
    
    @FXML
    private TextField searchField;
    
    private salesController parentController;
    
    private String username;
    
    public void setUsername(String username) {
        this.username = username;
    }
    
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
                    // Create Order object from parsed values
                    orderInfo order = new orderInfo(values[0], values[1], values[2], values[3], values[4], values[5], null);

                    String searchText = searchField.getText();
                    if (!searchText.isEmpty() && !values[0].contains(searchText)) {
                        continue; // Skip this worker if it doesn't match the search text
                    }
                    // Add order to list
                    orders.add(order);
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
    
    @FXML
    private void createButtonPressed(ActionEvent event){
        loadCart();
    }
    
    protected void setParentController(salesController parentController) {
        this.parentController = parentController;
    }
    
    protected void callParentFunction(){
        if (parentController != null) {
            parentController.orderScn(); // Call the function from adminController
        }
    }
    
    private void loadCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/salesFolder/cart.fxml"));
            Parent sceneRoot = loader.load();

            cartController cartController = loader.getController();
            cartController.setParentController(this);
            cartController.setUsername(username);
            cartController.populateTable();

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

