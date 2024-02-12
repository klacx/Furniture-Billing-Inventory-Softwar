/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.adminFolder;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class additionalReportController {

    @FXML
    private Pane pane;

    @FXML
    private TableView<SalesPerson> table;

    @FXML
    private TableColumn<SalesPerson, String> idCol;

    @FXML
    private TableColumn<SalesPerson, String> nameCol;

    @FXML
    private TableColumn<SalesPerson, Integer> ordersMadeCol;

    @FXML
    private BarChart<String, Integer> salesChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    
    public void initialize() {
        // Initialize TableView columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ordersMadeCol.setCellValueFactory(new PropertyValueFactory<>("ordersMade"));

        // Read order details from order.txt and count the number of orders made by each salesperson
        Map<String, Integer> ordersBySales = new HashMap<>();
        try (BufferedReader orderReader = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            while ((line = orderReader.readLine()) != null) {
                String[] orderData = line.split("!");
                String salesId = orderData[6];
                ordersBySales.put(salesId, ordersBySales.getOrDefault(salesId, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate TableView with salesperson data and orders made
        try (BufferedReader userReader = new BufferedReader(new FileReader("userCredentials.txt"))) {
            String line;
            while ((line = userReader.readLine()) != null) {
                String[] userData = line.split("!");
                if(userData.length >=5){
                    String id = userData[0];
                    String firstName = userData[3];
                    String lastName = userData[4];
                    String fullName = firstName + " " + lastName;
                    Integer ordersMade = ordersBySales.getOrDefault(id, 0);
                    table.getItems().add(new SalesPerson(id, fullName, ordersMade));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate BarChart with orders made by each salesperson
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (SalesPerson salesPerson : table.getItems()) {
            series.getData().add(new XYChart.Data<>(salesPerson.getId(), salesPerson.getOrdersMade()));
        }
        salesChart.getData().add(series);
    }
    
    public static class SalesPerson {
        private final String id;
        private final String name;
        private final int ordersMade;

        public SalesPerson(String id, String name, int ordersMade) {
            this.id = id;
            this.name = name;
            this.ordersMade = ordersMade;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getOrdersMade() {
            return ordersMade;
        }
    }
}
