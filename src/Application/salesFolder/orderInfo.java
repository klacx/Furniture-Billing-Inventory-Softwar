/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class orderInfo {
    private final StringProperty orderNumber;
    private final StringProperty amount;
    private final StringProperty date;
    private final StringProperty status;
    private final StringProperty customerName;
    private final StringProperty contactNumber;

    public orderInfo(String orderNumber, String amount, String date, String status, String customerName, String contactNumber) {
        this.orderNumber = new SimpleStringProperty(orderNumber);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.status = new SimpleStringProperty(status);
        this.customerName = new SimpleStringProperty(customerName);
        this.contactNumber = new SimpleStringProperty(contactNumber);
    }

    public StringProperty orderNumberProperty() {
        return orderNumber;
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getOrderNumber() {
        return orderNumber.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public String getContactNumber() {
        return contactNumber.get();
    }
}