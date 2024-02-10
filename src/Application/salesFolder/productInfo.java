/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.salesFolder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class productInfo {
    private final StringProperty productID;
    private final StringProperty productName;
    private final StringProperty category;
    private final StringProperty unitPrice;
    private final StringProperty description;

    public productInfo (String productID, String productName, String category, String unitPrice, String description) {
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.category = new SimpleStringProperty(category);
        this.unitPrice = new SimpleStringProperty(unitPrice);
        this.description = new SimpleStringProperty(description);
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public StringProperty categoryProperty() {
        return category;
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

    public String getProductName() {
        return productName.get();
    }

    public String getCategory() {
        return category.get();
    }

    public String getUnitPrice() {
        return unitPrice.get();
    }
    
    public String getDescription() {
        return description.get();
    }
}
