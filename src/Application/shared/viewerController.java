/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.shared;

import Application.officerFolder.statusController;
import Application.salesFolder.historyController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 *
 * @author User
 */
public class viewerController {
    
    public void handleRowDoubleClick(String orderNumber, statusController controller, Pane pane) {
        loadOrder(orderNumber, controller, pane);
    }
    
    public void handleRowDoubleClick(String orderNumber, historyController controller, Pane pane) {
        loadOrder(orderNumber, controller, pane);
    }
    
    private void loadOrder(String orderNumber, statusController controller, Pane pane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/shared/viewOrder.fxml"));
            Parent sceneRoot = loader.load();

            viewOrderController viewOrderController = loader.getController();
            viewOrderController.setStatusAsParentController(controller);
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
    
    private void loadOrder(String orderNumber, historyController controller, Pane pane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/shared/viewOrder.fxml"));
            Parent sceneRoot = loader.load();

            viewOrderController viewOrderController = loader.getController();
            viewOrderController.setHistoryAsParentController(controller);
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
