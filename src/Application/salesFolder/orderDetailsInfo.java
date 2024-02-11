/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class orderDetailsInfo  {
    private final StringProperty productID;
    private final StringProperty unitPrice;
    private final StringProperty quantity;
    private final StringProperty amount;
    private final StringProperty description;

    public orderDetailsInfo (String productID, String unitPrice, String quantity, String amount, String description) {
        this.productID = new SimpleStringProperty(productID);
        this.unitPrice = new SimpleStringProperty(unitPrice);
        this.quantity = new SimpleStringProperty(quantity);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public StringProperty unitPriceProperty() {
        return unitPrice;
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }
    
    public String getProductID() {
        return productID.get();
    }

    public String getquantity() {
        return quantity.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getUnitPrice() {
        return unitPrice.get();
    }
    
    public String getDescription() {
        return description.get();
    }
}

