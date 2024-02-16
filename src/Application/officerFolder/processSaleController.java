/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.officerFolder;

import Application.salesFolder.cartController;
import Application.salesFolder.orderInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.print.PrinterJob.JobStatus;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Callback;

public class processSaleController {
    
    @FXML
    private Button Btn_search;
    
    @FXML
    private Button Btn_approve;

    @FXML
    private Button Btn_done;

    @FXML
    private Button Btn_print;

    @FXML
    private Button Btn_edit;

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
    private Pane pane;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<orderInfo, String> statusCol;
    
    @FXML
    private TableColumn<orderInfo, Void> actionCol;

    @FXML
    private TableView<orderInfo> table;
    
    private String rowOrderNumber;
    
    private String status;
    
    private String approval;
    
    private officerController parentController;
    
    @FXML
    private void initialize() {
        orderNumberCol.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        customerNameCol.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        approvalCol.setCellValueFactory(cellData -> cellData.getValue().approvalProperty());
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
                    orderInfo order = new orderInfo(values[0], values[1], values[2], values[3], values[4], values[5], values[7]);
                    orders.add(order);                
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(orders);
        
        table.setRowFactory(tv -> {
            TableRow<orderInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    orderInfo rowData = row.getItem(); // Corrected to access the item from the row
                    rowOrderNumber = rowData.getOrderNumber();
                    status = rowData.getStatus();
                    approval = rowData.getApproval();
                    // Handle double-click event here
                    handleRowDoubleClick(rowOrderNumber, status, approval);
                } else if (event.getClickCount() == 1 && !row.isEmpty()) {
                    orderInfo rowData = row.getItem(); 
                    rowOrderNumber = rowData.getOrderNumber();
                    status = rowData.getStatus();
                    approval = rowData.getApproval();
                    // Handle single-click event here
                    handleRowSelect(rowOrderNumber);
                }
            });
            return row;
        });
    }
    
    private Callback<TableColumn<orderInfo, Void>, TableCell<orderInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<orderInfo, Void> call(final TableColumn<orderInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit_black.png")));
                    private final ImageView printIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/print_black.png")));
                    private final HBox buttonsContainer = new HBox(10, editIcon, printIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        editIcon.setOnMouseClicked(event -> {
                            orderInfo rowData = getTableRow().getItem();
                            rowOrderNumber = rowData.getOrderNumber();
                            handleEditAction(rowOrderNumber);
                        });
                        
                        printIcon.setOnMouseClicked(event -> {
                            orderInfo rowData = getTableRow().getItem();
                            rowOrderNumber = rowData.getOrderNumber();
                            handlePrintAction(rowOrderNumber);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            orderInfo rowData = getTableRow().getItem();
                            if (rowData != null && rowData.getApproval().equals("approved")) {
                                if(rowData.getStatus().equals("Done")){
                                    setGraphic(buttonsContainer);
                                    editIcon.setDisable(true);
                                    printIcon.setDisable(false);
                                    editIcon.setOpacity(0.5);
                                }
                                else{
                                    setGraphic(buttonsContainer);
                                    editIcon.setDisable(false);
                                    printIcon.setDisable(false);
                                }
                            } else {
                                setGraphic(buttonsContainer);
                                editIcon.setDisable(true);
                                printIcon.setDisable(true);
                                editIcon.setOpacity(0.5);
                                printIcon.setOpacity(0.5); 
                            }
                        }
}
                };
            }
        };
    }
    
    public void setParentController(officerController parentController){
        this.parentController = parentController;
    }
    
    public void callParentFunction() {
        parentController.processSaleBtnPressed();
    }
    
    private void handleEditAction(String orderNumber) {
        callCart(rowOrderNumber, "edit");
    }
    
    private void handlePrintAction(String orderNumber) {
        previewAndPrintInvoice(orderNumber);
    }
    
    private void handleRowDoubleClick(String orderNumber, String status, String approval) {
        callCart(orderNumber, "view");
    }
    
    private void handleRowSelect(String orderNumber) {
        updateBtn();
        rowOrderNumber = orderNumber;
    }
    
    @FXML
    private void printBtnPressed() {
        previewAndPrintInvoice(rowOrderNumber);
    }
    
    
    @FXML
    private void previewAndPrintInvoice(String orderNumber) {
        try {
            // Load the invoice scene from invoiceScn.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("invoiceScn.fxml"));
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
    
    private void updateBtn(){
        if(approval.equals("unapproved")){
            Btn_approve.setVisible(true);
            Btn_approve.setDisable(false);
            Btn_edit.setVisible(false);
            Btn_done.setDisable(true);
            Btn_done.setVisible(true);
            Btn_print.setVisible(false);
        }
        else{
            Btn_approve.setVisible(false);
            Btn_approve.setDisable(true);
            if(status.equals("Done")){
                Btn_done.setVisible(false);
                Btn_done.setDisable(true);
                Btn_edit.setVisible(false);
                Btn_edit.setDisable(true);
                Btn_print.setVisible(true);
                Btn_print.setDisable(false);
            }
            else{
                Btn_done.setVisible(true);
                Btn_done.setDisable(false); 
                Btn_edit.setVisible(true);
                Btn_edit.setDisable(false);
                Btn_print.setVisible(false);
                Btn_print.setDisable(true);
            }
        }
    }
    
    private void originalBtn(){
        Btn_approve.setVisible(false);
        Btn_approve.setDisable(true);
        Btn_done.setVisible(false);
        Btn_done.setDisable(true);
        Btn_print.setVisible(false);
        Btn_print.setDisable(true);
        Btn_edit.setVisible(false);
        Btn_edit.setDisable(true);
    }
    
    @FXML 
    private void approveBtnPressed(){
        makeApproval(rowOrderNumber, "approval");
        populateTable(); 
        originalBtn();        
    }
    
    @FXML 
    private void doneBtnPressed(){
        makeApproval(rowOrderNumber, "status");
        populateTable(); 
        originalBtn();
    }
    

    private void makeApproval(String orderNumber, String choice) {            
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
                    String updatedLine="";
                    if(choice.equals("approval")){
                        updatedLine = String.join("!", orderInfo[0], orderInfo[1], orderInfo[2], orderInfo[3], orderInfo[4] , orderInfo[5], orderInfo[6], "approved");
                    }
                    else if(choice.equals("status")){
                        updatedLine = String.join("!", orderInfo[0], orderInfo[1], orderInfo[2], "Done" , orderInfo[4] , orderInfo[5], orderInfo[6], orderInfo[7]);
                    }
                    // Construct the updated line

                    // Replace the line at index i
                    lines.set(i, updatedLine);
                    Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                    return; // No need to continue searching
                }
            }  
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    private void callCart (String orderNumber,String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/salesFolder/cart.fxml"));
            Parent sceneRoot = loader.load();

            cartController cartController = loader.getController();
            cartController.approval = approval;
            cartController.status = status;
            cartController.setProcessSaleAsParentController(this);
            cartController.setRoles("officer");
            cartController.setMode(mode, orderNumber);            
            
                    
            Node node = pane;
            Pane parent = (Pane) node.getParent();
            parent.getChildren().clear();
            parent.getChildren().add(sceneRoot);
        } catch (IOException e) {
            System.err.println("Error loading scene FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void editBtnPressed(){
        callCart(rowOrderNumber, "edit");
    }
}


