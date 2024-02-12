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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class addProductController {
    
    @FXML
    private TableColumn<productInfo, String> categoryCol;

    @FXML
    private TableColumn<productInfo, String> idCol;

    @FXML
    private TableColumn<productInfo, String> nameCol;

    @FXML
    private TableColumn<productInfo, String> unitPriceCol;
    
    @FXML
    private TableColumn<productInfo, String> descriptionCol;

    @FXML
    private TableColumn<productInfo, Void> addCol;

    @FXML
    private TableView<productInfo> table;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button Btn_search;

    private cartController parentController;
    
    private String username;
    
    private String orderNumber;
            
    protected Boolean edit_mode = false;
    
    private int count = 0;
    
    protected void setEditMode(){
        edit_mode=true;
    }
    
    protected void setOrderNumber(String orderNumber){
        this.orderNumber = orderNumber;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setParentController(cartController parentController) {
        this.parentController = parentController;
    }
    
    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        addCol.setCellFactory(getButtonCellFactory());
        populateTable();
    }
    
    @FXML
    protected void populateTable() {
        ObservableList<productInfo> products = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/product.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");
                
                // Create product object from parsed values
                productInfo product = new productInfo(values[0], values[1], values[2], values[3], values[8]);
                
                String searchText = searchField.getText();
                if (!searchText.isEmpty() && !values[0].contains(searchText) && !values[1].contains(searchText)) {
                    continue; // Skip this worker if it doesn't match the search text
                }
                // Add product to list
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(products);
    }
    
    private Callback<TableColumn<productInfo, Void>, TableCell<productInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<productInfo, Void> call(final TableColumn<productInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView addIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/add.png")));
                    private final HBox buttonsContainer = new HBox(addIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        addIcon.setOnMouseClicked(event -> {
                            productInfo rowData = getTableRow().getItem();
                            String productID = rowData.getProductID();
                            String unitPrice = rowData.getUnitPrice();
                            String description = rowData.getDescription();
                            handleAddAction(productID,unitPrice,description);
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
    
    private void handleAddAction(String productID, String unitPrice, String description) {
        if(!edit_mode){
            addIntoCart(productID,unitPrice,description);
            parentController.populateCartTable();
        }
        else{
            addIntoTmpCart(productID,unitPrice,description);
            parentController.populateOrderDetailsTable();
        }
    }
    
    
    private void addIntoCart(String productID, String unitPrice, String description) {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/cart.txt";
        try {
            File file = new File(filePath);

            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] cartInfo = line.split("!");                
                // Check if the salesid and product id match with the new variables
                if (cartInfo[0].equals(username) && cartInfo[1].equals(productID)) {
                    // Update the quantity and amount
                    int quantity = Integer.parseInt(cartInfo[3]) + 1;
                    double price = Double.parseDouble(cartInfo[2]);
                    double amount = quantity * price;
                    // Construct the updated line
                    String updatedLine = String.join("!", cartInfo[0], cartInfo[1], cartInfo[2], Integer.toString(quantity), Double.toString(amount), cartInfo[5]);
                    // Replace the line at index i
                    lines.set(i, updatedLine);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    return; // No need to continue searching
                }
            }

            // If no similar value was found, add the new line
            String newLine = String.join("!", username, productID, unitPrice, "1" , unitPrice ,description);
            lines.add(newLine);
            Files.write(file.toPath(), Collections.singletonList(newLine), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addIntoTmpCart(String productID, String unitPrice, String description) {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/tmp_cart.txt";

        File file = new File(filePath);

        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] cartInfo = line.split("!");                
                // Check if the salesid and product id match with the new variables
                if (cartInfo[2].equals(productID)) {
                    // Update the quantity and amount
                    int quantity = Integer.parseInt(cartInfo[4]) + 1;
                    double price = Double.parseDouble(cartInfo[3]);
                    double amount = quantity * price;
                    // Construct the updated line
                    String updatedLine = String.join("!", cartInfo[0], orderNumber, cartInfo[2], cartInfo[3], Integer.toString(quantity), Double.toString(amount), cartInfo[6]);
                    // Replace the line at index i
                    lines.set(i, updatedLine);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    return; // No need to continue searching
                }
            }

            // If no similar value was found, add the new line
            String newLine = String.join("!", String.valueOf(getOrderDetailsId()), orderNumber, productID, unitPrice, "1" , unitPrice ,description);
            lines.add(newLine);           
            Files.write(file.toPath(), Collections.singletonList(newLine), StandardCharsets.UTF_8, StandardOpenOption.APPEND);            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getOrderDetailsId() {
        if(count < 1){
            String currentWorkingDirectory = System.getProperty("user.dir");
            String filePath = currentWorkingDirectory + "/orderDetails.txt";

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
                    String[] orderDetailsInfo = lastLine.split("!");
                    int lastOrderId = Integer.parseInt(orderDetailsInfo[0]);
                    count += 1;
                    return lastOrderId + 1;
                } else {
                    // If the file is empty, start from order ID 1    
                    count += 1;
                    return 1;                  
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        } 
        else{
            String currentWorkingDirectory = System.getProperty("user.dir");
            String filePath = currentWorkingDirectory + "/tmp_cart.txt";

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
                    String[] orderDetailsInfo = lastLine.split("!");
                    int lastOrderId = Integer.parseInt(orderDetailsInfo[0]);
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
}
