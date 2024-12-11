/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.officerFolder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Arrays;

/**
 *
 * @author User
 */
public class Officer {
    
    private String order;
    private String customerName;
    private String customerContact;
    private String date;
    private String totalAmount;
    private String status;
    private String sales;
    private String[][] Product;
    private int SalesAmount;

    public String[][] getProduct() {
        return Product;
    }

    public void setProduct(String[][] Product) {
        this.Product = Product;
    }

    public int getSalesAmount() {
        return SalesAmount;
    }

    public void setSalesAmount(int SalesAmount) {
        this.SalesAmount = SalesAmount;
    }
    
    private final StringProperty orderProperty = new SimpleStringProperty();
    private final StringProperty customerNameProperty = new SimpleStringProperty();
    private final StringProperty customerContactProperty = new SimpleStringProperty();
    private final StringProperty dateProperty = new SimpleStringProperty();
    private final StringProperty totalAmountProperty = new SimpleStringProperty();
    private final StringProperty statusProperty = new SimpleStringProperty();
    private final StringProperty salesProperty = new SimpleStringProperty();
     
        public StringProperty orderProperty() {
        return orderProperty;
    }

    public StringProperty customerNameProperty() {
        return customerNameProperty;
    }

    public StringProperty customerContactProperty() {
        return customerContactProperty;
    }

    public StringProperty dateProperty() {
        return dateProperty;
    }

    public StringProperty totalAmountProperty() {
        return totalAmountProperty;
    }

    public StringProperty statusProperty() {
        return statusProperty;
    }

    public StringProperty salesProperty() {
        return salesProperty;
    }
    
    public String getOrder() {
        return order;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public String getDate() {
        return date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getSales() {
        return sales;
    }

    public Officer(String orderID, String customer_name, String customer_contact, String date, String total_amount, String status, String salesID) {
        this.order = orderID;
        this.customerName = customer_name;
        this.customerContact = customer_contact;
        this.date = date;
        this.totalAmount = total_amount;
        this.status = status;
        this.sales = salesID;
        this.orderProperty.set(orderID);
        this.customerNameProperty.set(customer_name);
        this.customerContactProperty.set(customer_contact);
        this.dateProperty.set(date);
        this.totalAmountProperty.set(total_amount);
        this.statusProperty.set(status);
        this.salesProperty.set(salesID);
    }
    
    public Officer(){
        
    }
    
    public String fetch_data() throws FileNotFoundException, IOException {
        BufferedReader ReadFile = new BufferedReader (new FileReader("src/resources/order.txt"));
        StringBuilder RetrievedData = new StringBuilder();
        String Data;
        
        while ((Data=ReadFile.readLine())!= null){
            RetrievedData.append(Data + "\n");
        }
        
        return RetrievedData.toString();
    }
    
    public void modify_data(Officer selected_Row, String orderid, String customername,String customercontact,String date, String totalamount, String status, String salesid ) throws FileNotFoundException, IOException {
        StringBuilder Modified_Data = new StringBuilder();
        try {
            String StringData = fetch_data();
            String[] lines = StringData.split("\n");
            for (String Format_Removed : lines) {
                String[] line =  Format_Removed.split("!");
                if (line[1].equals(selected_Row.customerName)){
                    Modified_Data.append(orderid + "!" + customername + "!" + customercontact + "!" + date + "!" + totalamount + "!" + status + "!" + salesid + "\n");
                    selected_Row.orderProperty().set(orderid);
                    selected_Row.customerNameProperty().set(customername);
                    selected_Row.customerContactProperty().set(customercontact);
                    selected_Row.dateProperty().set(date);
                    selected_Row.totalAmountProperty().set(totalamount);
                    selected_Row.statusProperty().set(status);
                    selected_Row.salesProperty().set(salesid);
                } else {
                    Modified_Data.append(Format_Removed + "\n");
                }
            }
            BufferedWriter WriteFile = new BufferedWriter(new FileWriter("src/resources/order.txt"));
            WriteFile.write(Modified_Data.toString());
            WriteFile.close();
        } catch (IOException ex) {
            Logger.getLogger(Officer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void saveToFile(String data, String filePath) throws IOException {
        Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public String search_data(String target) throws FileNotFoundException, IOException {
        String Order_Details = this.fetch_data();
        StringBuilder FilteredData = new StringBuilder();
        String[] Format_Removal = Order_Details.split("\n");
        String[] Data;
        for (String Format_Removed : Format_Removal){
            Data = Format_Removed.split("!");
            if (Data[1].startsWith(target))
                FilteredData.append(Format_Removed + "\n");
        }
        return FilteredData.toString();
       
    } 
    
    //Sales invoice 
    
    public String sales_data() throws FileNotFoundException, IOException {
        BufferedReader ReadFile = new BufferedReader (new FileReader("src/resources/orderDetails.txt"));
        StringBuilder FetchData = new StringBuilder();
        String Data;
        
        while ((Data=ReadFile.readLine())!= null){
            FetchData.append(Data + "\n");
        }
        
        return FetchData.toString();
    }
        
    public void adjust_data(String orderdetailid, String orderid, String product, String quantity, String amount){

        StringBuffer Buffer = new StringBuffer();
        try {
            String fetch_data = sales_data();
            String[] collection = fetch_data.split("\n");
            String[] ProductName = {};
            int[] ProductQuantity = {};
            String[] ProductQuantityStr = {};
            String[][] ProductDetails = {};
            int total_amount1 = 0;
            boolean NameFound = false;
            int Turns = 0;

            for (String item : collection){
                 String[] items = item.split("!");
                 int amount_cal = Integer.parseInt(items[4]);
                 int quantity_cal = Integer.parseInt(items[3]);
                 
                total_amount1 = total_amount1 + amount_cal;
               
                if (ProductName.length != 0){
                    for (String Name : ProductName){
                        if (Name == items[2]){
                            NameFound = true;
                        }
                        if (!NameFound){
                            Turns++;
                        }
                    }
                } else {
                    ProductName[0] = items[2];
                }
                
                if (!NameFound){
                    ProductName[ProductName.length + 1] = items[2];
                } else;
                
                if (ProductQuantity.length != 0){
                    if (NameFound){
                        ProductQuantity[Turns] = ProductQuantity[Turns] + quantity_cal;
                    } else {
                        ProductQuantity[ProductQuantity.length + 1] = quantity_cal;
                    }
                } else {
                    ProductQuantity[0] = quantity_cal;
                }
            }
            for (int i = 0; i < ProductQuantity.length; i++){
                ProductQuantityStr[i] = Double.toString(ProductQuantity[i]);
            }
            ProductDetails[0] = ProductName;
            ProductDetails[1] = ProductQuantityStr;
            setProduct(ProductDetails);
        }
        catch(IOException e){                              
            System.out.println(e);
        };
          
    }
    
}

//product quantity + order_id (add up amount)


