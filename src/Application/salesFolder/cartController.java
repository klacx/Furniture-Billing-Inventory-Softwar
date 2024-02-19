/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

/**
 *
 * @author User
 */

import Application.officerFolder.invoiceController;
import Application.officerFolder.processSaleController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
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
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class cartController {
    
    @FXML
    private Button Btn_add;

    @FXML
    private Button Btn_approve;

    @FXML
    private Button Btn_cancel;

    @FXML
    private Button Btn_done;

    @FXML
    private Button Btn_edit;

    @FXML
    private Button Btn_print;

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
    
    @FXML
    private Text totalAmountText;
    
    private orderTableController orderParentController;
    
    private processSaleController processSaleParentController;
    
    private String username="";
    
    public String status="";
    
    public String approval="";
    
    protected String mode ="add";
    
    private String rolesMode="sales";
    
    public void setMode(String mode, String orderNumber){
        this.mode = mode;       
        updateBtn();
        orderNumberField.setText(orderNumber);
        updateTable();
        
    }
    
    public void setRoles(String roles){
        this.rolesMode = roles;
    }
    
    private void updateTable(){
        if(mode.equals("add")){
            populateCartTable();
        }
        if(mode.equals("edit")){
            copyOrderDetailsToTmpCart(orderNumberField.getText());
            populateOrderDetailsTable();
        }
        else if(mode.equals("view")){
            copyOrderDetailsToTmpCart(orderNumberField.getText());
            populateOrderDetailsTable();
        }
    }
    
    @FXML
    private void editBtnPressed(){
        setMode("edit", orderNumberField.getText());
    }
 
    private void updateBtn(){
        if(mode.equals("add")){
            Btn_save.setVisible(true);
            Btn_add.setVisible(true);
            Btn_cancel.setVisible(true);
            Btn_print.setVisible(false);
            Btn_approve.setVisible(false);
            Btn_done.setVisible(false);
            Btn_edit.setVisible(false);
        }
        else if(mode.equals("edit")){
            Btn_save.setVisible(true);
            Btn_add.setVisible(true);
            Btn_cancel.setVisible(true);
            Btn_print.setVisible(false);
            Btn_approve.setVisible(false);
            Btn_done.setVisible(false);
            Btn_edit.setVisible(false);
        }
        else if(mode.equals("view")){
            Btn_save.setVisible(false);
            Btn_add.setVisible(false);
            Btn_cancel.setVisible(false);
            if(approval.equals("unapproved")){
                Btn_print.setVisible(true);
                Btn_print.setDisable(true);
                Btn_approve.setVisible(true);
                Btn_edit.setVisible(false);
                Btn_done.setVisible(false); 
            }
            else{
                Btn_print.setVisible(true);
                Btn_print.setDisable(false);
                Btn_approve.setVisible(false);
                if(status.equals("Done")){
                    Btn_edit.setVisible(false);
                    Btn_done.setVisible(false); 
                }
                else{
                    Btn_edit.setVisible(true);
                    Btn_done.setVisible(true); 
                }
            }          
        }
    }
            
    public void setUsername(String username) {
        this.username = username;
        updateTable();
    }
    
    public void setOrderAsParentController(orderTableController orderParentController) {
        this.orderParentController = orderParentController;
    }
    
    public void setProcessSaleAsParentController(processSaleController processSaleParentController) {
        this.processSaleParentController = processSaleParentController;
    }
    
    @FXML
    private void updateSaveButton() {
        boolean disableButton = customerNameField.getText().isEmpty() || customerNumberField.getText().isEmpty() || table.getItems().isEmpty();
        Btn_save.setDisable(disableButton);
    }
    
    @FXML
    private void cancelBtnPressed(){
        if (orderParentController != null) {
            orderParentController.callParentFunction(); 
        }
        else{
            processSaleParentController.callParentFunction();
        }
    }
    
    @FXML
    private void saveBtnPressed(){
        addOrderDetails();
        makeOrder();
        if(rolesMode.equals("sales")){
            table.getItems().clear();    
            customerNameField.clear();
            customerNumberField.clear();
            orderNumberField.setText(String.valueOf(getOrderId()));
            updateSaveButton();
        }
        else if(rolesMode.equals("officer")){
            setMode("view", orderNumberField.getText());
            updateBtn();
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
            
            if(mode.equals("edit")){
                addProductController.setEditMode();
                addProductController.setOrderNumber(orderNumberField.getText());
            }
            
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
    private void backBtnPressed(){
        if (orderParentController != null) {
            orderParentController.callParentFunction(); 
        }
        else{
            processSaleParentController.callParentFunction();
        }
    }
    
    @FXML
    private void approveBtnPressed(){
        makeApprovalOrStatus(orderNumberField.getText(), "approval");
        approval = "approved";
        updateBtn();
    }
    
    @FXML
    private void doneBtnPressed(){
        makeApprovalOrStatus(orderNumberField.getText(), "status");
        status = "Done";
        updateBtn();
    }
    
    
    
    private void makeApprovalOrStatus(String orderNumber, String choice) {            
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/order.txt";
        try {
            File file = new File(filePath);

            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] orderInfo = line.split("!");                
                
                if (orderInfo[0].equals(orderNumber)) {
                    // Update the total amount
                    String updatedLine ="";
                    // Construct the updated line
                    if(choice.equals("approval")){
                        updatedLine = String.join("!", orderInfo[0], orderInfo[1], orderInfo[2], orderInfo[3], orderInfo[4] , orderInfo[5], orderInfo[6], "approved");
                    }
                    else if(choice.equals("status")){
                        updatedLine = String.join("!", orderInfo[0], orderInfo[1], orderInfo[2], "Done" , orderInfo[4] , orderInfo[5], orderInfo[6], orderInfo[7]);
                    }

                    lines.set(i, updatedLine);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    return; // No need to continue searching
                }
            }  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void printBtnPressed() {
        previewAndPrintInvoice(orderNumberField.getText());
    }
    
    
    @FXML
    private void previewAndPrintInvoice(String orderNumber) {
        try {
            // Load the invoice scene from invoiceScn.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/officerFolder/invoiceScn.fxml"));
            Parent root = loader.load();
            
            invoiceController invoiceController = loader.getController();
            invoiceController.setOrderNumber(orderNumber);
            // Create a new stage for preview
            
            Scene invoiceScene = new Scene(root, 816, 1056);
            Stage previewStage = new Stage();
            previewStage.setTitle("Invoice Preview");
            previewStage.setScene(invoiceScene);
            previewStage.show();

            // Print the scene
            printScene(invoiceScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void printScene(Scene scene) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null && job.showPrintDialog(scene.getWindow())) {
            double scaleX = pageLayout.getPrintableWidth() / scene.getRoot().getLayoutBounds().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / scene.getRoot().getLayoutBounds().getHeight();

            // Use the smaller scale factor to maintain aspect ratio
            double scale = Math.min(scaleX, scaleY);
            Scale scaleTransform = new Scale(scale, scale);
            scene.getRoot().getTransforms().add(scaleTransform);

            boolean success = job.printPage(pageLayout, scene.getRoot());
            if (success) {
                job.endJob();
            }

            // Remove the scale transform after printing
            scene.getRoot().getTransforms().remove(scaleTransform);
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
        orderNumberField.setText(String.valueOf(getOrderId()));
        allowAlphabetsAndSpaces(customerNameField);
        allowNumericInput(customerNumberField);
    }
    
    protected void populateCartTable() {       //populate the table with cart text file
        table.getItems().clear();
        ObservableList<orderDetailsInfo> orderDetailsInfo = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/cart.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                // Check if the array has the expected length
                if (values.length >= 6) {
                    // Create orderDetails object from parsed values
                    String formattedUnitPrice = String.format("%.2f", Double.parseDouble(values[2]));
                    String formattedAmount = String.format("%.2f", Double.parseDouble(values[4]));
                    orderDetailsInfo orderDetails = new orderDetailsInfo(values[1], formattedUnitPrice , values[3], formattedAmount , values[5]);

                    if (!values[0].equals(username)) {
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
        updateSaveButton();
        calculateTotalAmount();
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
    
    private void handleDeleteAction(String salesID,String productID) {
        if(mode.equals("add") ){
            deleteCartItem(salesID, productID);
            populateCartTable();
        }
        else if(mode.equals("edit")){
            deleteTmpCartItem(orderNumberField.getText(), productID);
            populateOrderDetailsTable();
        }
    }
    
    private void deleteCartItem(String salesID, String productID) {   //delete specific item from the cart
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

        } catch (IOException e) {
            System.err.println("Error deleting line: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteTmpCartItem(String orderNumber, String productID) {   //delete specific item from the cart
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/tmp_cart.txt";
        try {
            File file = new File(filePath);
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            // Create a new list to store the updated lines
            List<String> updatedLines = new ArrayList<>();

            // Iterate over each line
            for (String line : lines) {
                String[] cartInfo = line.split("!");
                // Check if the line matches the specified productID and salesID
                if (!(cartInfo[1].equals(orderNumber) && cartInfo[2].equals(productID))) {
                    // If it doesn't match, add it to the updated lines
                    updatedLines.add(line);
                }
            }

            // Write the updated lines back to the file
            Files.write(file.toPath(), updatedLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Error deleting line: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int getOrderId() {   //get last order id
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
    
    private void makeOrder() {              // make a new order or update the order
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/order.txt";
        try {
            File file = new File(filePath);

            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] orderInfo = line.split("!");                
                
                if (orderInfo[0].equals(orderNumberField.getText())) {
                    // Update the total amount
                    String updatedLine="";
                    // Construct the updated line
                    if(rolesMode.equals("sales")){
                        updatedLine = String.join("!", orderInfo[0], totalAmountText.getText() , orderInfo[2], "Progressing", customerNameField.getText() , customerNumberField.getText(), orderInfo[6], "unapproved");
                    }
                    else if(rolesMode.equals("officer")){
                        updatedLine = String.join("!", orderInfo[0], totalAmountText.getText() , orderInfo[2], "Progressing", customerNameField.getText() , customerNumberField.getText(), orderInfo[6], "approved");
                    }
                    
                    // Replace the line at index i
                    lines.set(i, updatedLine);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    return; // No need to continue searching
                }
            }

            // If no similar value was found, add the new line
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String newLine = String.join("!", orderNumberField.getText(), totalAmountText.getText(),date, "Progressing", customerNameField.getText() , customerNumberField.getText() ,username, "unapproved");
            lines.add(newLine);
            Files.write(file.toPath(), Collections.singletonList(newLine), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            if(mode.equals("add")){
                deleteCart(username);     
            }     
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
    
    private void deleteCart(String salesID) {                //delete the emtire cart (use after make order)
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
                if (!(cartInfo[0].equals(salesID))) {
                    // If it doesn't match, add it to the updated lines
                    updatedLines.add(line);
                }
            }

            // Write the updated lines back to the file
            Files.write(file.toPath(), updatedLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Error deleting line: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addOrderDetails() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/orderDetails.txt";
        try {
            System.out.println(table.getItems().size());
            for (orderDetailsInfo item : table.getItems()) {
                // Get data from the current row
                String orderId = orderNumberField.getText();
                String productId = item.getProductID();
                String price = item.getUnitPrice();
                String quantity = item.getquantity();
                String amount = item.getAmount();
                String description = item.getDescription();

                File file = new File(filePath);

                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                if (mode.equals("edit")) {
                    // Remove all lines with matching order ID
                    lines.removeIf(line -> {
                        String[] orderDetailsInfo = line.split("!");
                        return orderDetailsInfo.length >= 2 && orderDetailsInfo[1].equals(orderId);
                    });

                    // Append lines from tmp_cart.txt
                    String tmpCartFilePath = currentWorkingDirectory + "/tmp_cart.txt";
                    List<String> tmpCartLines = Files.readAllLines(Paths.get(tmpCartFilePath));
                    lines.addAll(tmpCartLines);
                } else {
                    // If no similar value was found, add the new line
                    String newLine = String.join("!", String.valueOf(getOrderDetailsId()), orderId, productId, price, quantity, amount, description);
                    lines.add(newLine);
                }

                Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int getOrderDetailsId() {
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
   
    protected void populateOrderDetailsTable() {       //populate the table with cart text file
        table.getItems().clear();
        ObservableList<orderDetailsInfo> orderDetailsInfo = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/tmp_cart.txt"; // Provide absolute path

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
                    if (!values[1].equals(orderNumberField.getText())) {
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
        updateSaveButton();
        calculateTotalAmount();
    }
    
    protected void readCustomerDetails() {
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                if (!values[0].equals(orderNumberField.getText())) {
                    continue; // Skip if it doesn't match the search text
                }
                // Add product to list

                customerNameField.setText(values[4]);
                customerNumberField.setText(values[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected static void copyOrderDetailsToTmpCart(String orderId) {
        deleteAndRecreateTmpFile();
        String currentWorkingDirectory = System.getProperty("user.dir");
        String orderDetailsFilePath = currentWorkingDirectory + "/orderDetails.txt";
        String tmpCartFilePath = currentWorkingDirectory + "/tmp_cart.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(orderDetailsFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmpCartFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderDetails = line.split("!");

                if (orderDetails.length >= 2 && orderDetails[1].equals(orderId)) {
                    writer.write(line);  
                    writer.newLine();    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void deleteAndRecreateTmpFile() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        File tmpFile = new File(currentWorkingDirectory, "tmp_cart.txt");

        if (tmpFile.exists()) {
            if (!tmpFile.delete()) {
                System.err.println("Failed to delete the existing temporary file.");
                return;
            }
        }

        try {
            if (!tmpFile.createNewFile()) {
                System.err.println("Failed to create a new temporary file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    private void allowAlphabetsAndSpaces(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z\\s]*$")) {
                textField.setText(oldValue);
            }
        });
    }
    
    private void allowNumericInput(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,11}")) {
                textField.setText(oldValue);
            }
        });
    }   
}
