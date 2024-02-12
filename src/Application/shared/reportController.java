/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.shared;

import Application.adminFolder.additionalReportController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class reportController {
    
    @FXML
    private ProgressBar approveProgressBar;

    @FXML
    private Text approveText;

    @FXML
    private Pane pane;

    @FXML
    private ProgressBar statusProgressBar;

    @FXML
    private Text statusText;
    
    @FXML
    private PieChart categoryIncomeChart;

    @FXML
    private BarChart<String, Number> nameIncomeChart;
    
    @FXML
    private Button Btn_more;
    
    @FXML
    private void initialize() {
        calculatePercentages();
        updateChart();
    }
    
    public void setMoreBtn(){
        Btn_more.setVisible(true);
    }
    
    public void calculatePercentages() {
        int totalOrders = 0;
        int approvedOrders = 0;
        int doneOrders = 0;

        // Read data from the "order.txt" file
        String currentWorkingDirectory = System.getProperty("user.dir"); // Read directory path
        String filePath = currentWorkingDirectory + "/order.txt"; // Provide absolute path
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("!");
                if (values.length >= 8) {
                    totalOrders++;
                    if (values[7].equals("approved")) {
                        approvedOrders++;
                    }
                    if (values[3].equals("Done")) {
                        doneOrders++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate percentages
        double approvedPercentage = (double) approvedOrders / totalOrders;
        double donePercentage = (double) doneOrders / totalOrders;

        // Update progress bar and text area
        approveProgressBar.setProgress(approvedPercentage);
        approveText.setText(String.valueOf((int) (approvedPercentage * 100)) + "%");
        statusProgressBar.setProgress(donePercentage);
        statusText.setText(String.valueOf((int) (donePercentage * 100)) + "%");
    }
    
    private void updateChart() {
        // Read order details from orderDetails.txt
        Map<String, Double> categoryIncomeMap = new HashMap<>();
        Map<String, Double> nameIncomeMap = new HashMap<>();
        try (BufferedReader orderDetailsReader = new BufferedReader(new FileReader("orderDetails.txt"))) {
            String orderDetailLine;
            while ((orderDetailLine = orderDetailsReader.readLine()) != null) {
                String[] orderDetail = orderDetailLine.split("!");
                String productId = orderDetail[2];
                double totalAmount = Double.parseDouble(orderDetail[5]);

                // Read product details from product.txt
                try (BufferedReader productReader = new BufferedReader(new FileReader("product.txt"))) {
                    String productLine;
                    while ((productLine = productReader.readLine()) != null) {
                        String[] productDetail = productLine.split("!");
                        String productCategoryId = productDetail[2];
                        String productName = productDetail[1];

                        // If product ID matches, update category-wise income
                        if (productId.equals(productDetail[0])) {
                            double income = totalAmount;
                            categoryIncomeMap.put(productCategoryId, categoryIncomeMap.getOrDefault(productCategoryId, 0.0) + income);
                            nameIncomeMap.put(productName, nameIncomeMap.getOrDefault(productName, 0.0) + income);
                            break; // No need to continue searching for the product ID
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update PieChart data
        ObservableList<PieChart.Data> categoryPieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categoryIncomeMap.entrySet()) {
            categoryPieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        categoryIncomeChart.setData(categoryPieChartData);
        updatePieChartWithGradientColors(categoryIncomeChart);

        // Update BarChart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : nameIncomeMap.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
        }
        nameIncomeChart.getData().clear();
        nameIncomeChart.getData().add(series);

        // Customize bar colors
        for (int i = 0; i < series.getData().size(); i++) {
            Node bar = series.getData().get(i).getNode();
            bar.setStyle("-fx-bar-fill: " + getRandomColor(i / (double) series.getData().size()) + ";");
        }
    }

    private String getRandomColor(double gradientPosition) {
        // Calculate gradient color
        int r = (int) (255 + gradientPosition * (255 - 255));
        int g = (int) (0 + gradientPosition * (191 - 0));
        int b = (int) (0 + gradientPosition * (0 - 0));
        return String.format("#%02x%02x%02x", r, g, b); // Format to hexadecimal
    }
    
    private void displayLabelForData(XYChart.Data<String, Number> data) {
        Node node = data.getNode();
        Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener((observable, oldParent, newParent) -> {
            Group parentGroup = (Group) newParent;
            parentGroup.getChildren().add(dataText);
        });
        node.boundsInParentProperty().addListener((observable, oldBounds, newBounds) -> {
            dataText.setLayoutX(Math.round(newBounds.getMinX() + newBounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
            dataText.setLayoutY(Math.round(newBounds.getMinY() - dataText.prefHeight(-1) * 0.5));
        });
    }
    
    private void updatePieChartWithGradientColors(PieChart pieChart) {
        ObservableList<PieChart.Data> pieChartData = pieChart.getData();
        for (int i = 0; i < pieChartData.size(); i++) {
            PieChart.Data slice = pieChartData.get(i);
            Node sliceNode = slice.getNode();
            double position = i / (double) pieChartData.size();
            sliceNode.setStyle("-fx-pie-color: " + getRandomGradientColor(position) + ";");
        }
    }

    // Generate a random gradient color
    private String getRandomGradientColor(double position) {
        // Start color
        Color startColor = Color.web("#0000FF");

        // End color
        Color endColor = Color.web("#7EC8E3");

        // Calculate the intermediate color based on the position
        double r = startColor.getRed() * (1 - position) + endColor.getRed() * position;
        double g = startColor.getGreen() * (1 - position) + endColor.getGreen() * position;
        double b = startColor.getBlue() * (1 - position) + endColor.getBlue() * position;

        // Convert to hexadecimal string
        return String.format("#%02X%02X%02X", (int) (r * 255), (int) (g * 255), (int) (b * 255));
    }
    
    @FXML
    private void loadAdditionalReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/adminFolder/additionalReport.fxml"));
            Parent sceneRoot = loader.load();

            additionalReportController additionalReportController = loader.getController();
            
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

