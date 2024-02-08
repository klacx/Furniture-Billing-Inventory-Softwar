/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application.adminFolder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class workerInfo {
    private final StringProperty workerID;
    private final StringProperty roles;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty contactNumber;

    public workerInfo(String workerID, String roles, String name, String email, String contactNumber) {
        this.workerID = new SimpleStringProperty(workerID);
        this.roles = new SimpleStringProperty(roles);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.contactNumber = new SimpleStringProperty(contactNumber);
    }

    public StringProperty workerIDProperty() {
        return workerID;
    }

    public StringProperty rolesProperty() {
        return roles;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getWorkerID() {
        return workerID.get();
    }

    public String getRoles() {
        return roles.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getContactNumber() {
        return contactNumber.get();
    }
}
