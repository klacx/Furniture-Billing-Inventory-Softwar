/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Application.adminFolder;

/**
 *
 * @author User
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class workerScnController{

    @FXML
    private Button Btn_new;

    @FXML
    private TableColumn<workerInfo, String> contactNumberCol;

    @FXML
    private TableColumn<workerInfo, String> editCol;

    @FXML
    private TableColumn<workerInfo, String> emailCol;

    @FXML
    private TableColumn<workerInfo, String> idCol;

    @FXML
    private TableColumn<workerInfo, String> nameCol;

    @FXML
    private TableColumn<workerInfo, String> rolesCol;

    @FXML
    private TableView<workerInfo> table;

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().workerIDProperty());
        rolesCol.setCellValueFactory(cellData -> cellData.getValue().rolesProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        contactNumberCol.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());

        // Load data from text file and populate table
        populateTable();
    }

    private void populateTable() {
        ObservableList<workerInfo> workers = FXCollections.observableArrayList();
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/userCredentials.txt"; // Provide absolute path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");

                // Create Order object from parsed values
                workerInfo worker = new workerInfo(values[0], values[2], values[3]+values[4], values[5], values[9]);

                // Add worker to list
                workers.add(worker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set table items
        table.setItems(workers);
    }
    
    private Callback<TableColumn<workerInfo, Void>, TableCell<workerInfo, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<workerInfo, Void> call(final TableColumn<workerInfo, Void> param) {
                return new TableCell<>() {
                    private final ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit_black.png")));
                    private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/resources/delete.png")));
                    private final HBox buttonsContainer = new HBox(10, editIcon, deleteIcon); 

                    {
                        buttonsContainer.setAlignment(Pos.CENTER); // Align icons in the center

                        editIcon.setOnMouseClicked(event -> {
                            workerInfo rowData = getTableView().getItems().get(getIndex());
                            // Handle edit action
                            handleEditAction(rowData);
                        });

                        deleteIcon.setOnMouseClicked(event -> {
                            workerInfo rowData = getTableView().getItems().get(getIndex());
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
    
    private void handleEditAction(workerInfo rowData) {
        // Implement the logic for handling edit action
        // For example:
        // System.out.println("Edit action triggered for: " + rowData);
    }
}
