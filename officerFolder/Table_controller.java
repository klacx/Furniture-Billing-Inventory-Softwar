/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Application.officerFolder;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Table_controller implements Initializable{
    
    @FXML
    private TableColumn<Officer, String> order;

    @FXML
    private TableColumn<Officer, String> customerName;
    
    @FXML
    private TableColumn<Officer, String> customerContact;

    @FXML
    private TableColumn<Officer, String> date;

    @FXML
    private TableColumn<Officer, String> totalAmount;

    @FXML
    private TableColumn<Officer, String> status;

    @FXML   
    private TableColumn<Officer, String> sales;
    
    @FXML
    private TextField searchTarget;
    
    @FXML
    private TableView<Officer> officer_table;
    
    @FXML
    private TextField customercontact;
    
    @FXML
    private TextField statusinput;
    
    @FXML
    private TextField customername;
    
    @FXML
    private TextField dateinput;
    
    @FXML
    private TextField orderid;
      
   @FXML 
   private TextField salesid;
    
   @FXML
    private TextField totalamount;
    
    
    
    public void initialData(){
         Officer officer = new Officer();
        try {
            String[] Formatted_Data;
            String[] Data;
            String[] Nothing = {};
            Officer OfficerData = new Officer();
            String StringData = officer.fetch_data();
            Formatted_Data = StringData.split("\n");
            for (String Format_Removed : Formatted_Data){
                Data = Format_Removed.split("!");
                if (Data.length == 7) {
                    OfficerData = new Officer(Data[0],Data[1],Data[2],Data[3],Data[4],Data[5],Data[6]);
                } else {
                    String[] filledData = new String[7];  // Assuming OfficerData has 7 elements
                    for (int i = 0; i < Data.length; i++) {
                        filledData[i] = Data[i];
                    }
                    for (int i = Data.length; i < filledData.length; i++) {
                        filledData[i] = "";
                    }
                    OfficerData = new Officer(filledData[0], filledData[1], filledData[2],filledData[3], filledData[4], filledData[5], filledData[6]);
                }
                if (officer_table.getItems() == null){
                    officer_table.setItems(FirstData(false, Nothing));
                }
                else {
                    officer_table.getItems().add(OfficerData);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Table_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public Officer GetRowData(){
        Officer SelectedRow = new Officer();
        if (officer_table.getSelectionModel().getSelectedItem() != null){
            SelectedRow = officer_table.getSelectionModel().getSelectedItem();
        }
        return SelectedRow;
    }
    
    public void modifyRow(MouseEvent event){
        Officer officer = new Officer();
        try {
            officer.modify_data(officer_table.getSelectionModel().getSelectedItem(),orderid.getText(),customername.getText(),customercontact.getText(),dateinput.getText(),totalamount.getText(), statusinput.getText(),salesid.getText());
            clearAllRow();
            initialData();
        } catch (IOException ex) {
            Logger.getLogger(Table_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ShowData(MouseEvent event){
        Officer Data = GetRowData();
        customercontact.setText(Data.getCustomerContact());
        orderid.setText(Data.getOrder());
        customername.setText(Data.getCustomerName());
        statusinput.setText(Data.getStatus());
        dateinput.setText(Data.getDate());
        salesid.setText(Data.getSales());
        totalamount.setText(Data.getTotalAmount());
    }
    
    public void clearAllRow(){
        for (int i = 0; i < officer_table.getItems().size(); i++){
            officer_table.getItems().clear();
        }
    }
    
    public void SearchData(MouseEvent event){
        clearAllRow();
        Officer SearchFunction = new Officer();
        String Target = searchTarget.getText();
        try {
            String Searching = SearchFunction.search_data(Target);
            String[] Line = Searching.split("\n");
            String[] Data;
            for (String Format_Removed : Line){
                if (Format_Removed != null){
                    Data = Format_Removed.split("!");
                    Officer officer_data = new Officer(Data[0],Data[1],Data[2],Data[3],Data[4],Data[5],Data[6]);
                    if (officer_table.getItems() == null){
                        officer_table.setItems(FirstData(true,Data));
                    } else {
                        officer_table.getItems().add(officer_data);
                }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Table_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ObservableList<Officer> FirstData(boolean Target,String[] TargetData){
        Officer OfficerData = new Officer();
        try{
            Officer officer = new Officer();
            String[] Line;
            String[] Data;
            if (Target == false){
                String LineDATA = officer.fetch_data();
                Line = LineDATA.split("\n");
                Data = Line[0].split("!");
                OfficerData = new Officer(Data[0],Data[1],Data[2],Data[3],Data[4],Data[5],Data[6]);
                return FXCollections.observableArrayList(OfficerData);
            } else if (Target == true && !TargetData.equals("")){
                OfficerData = new Officer(TargetData[0],TargetData[1],TargetData[2],TargetData[3],TargetData[4],TargetData[5],TargetData[6]);
                return FXCollections.observableArrayList(OfficerData);
            }
        } catch (IOException ex) {
            Logger.getLogger(Table_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FXCollections.observableArrayList(OfficerData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        order.setCellValueFactory(new PropertyValueFactory<Officer, String>("order"));
        customerName.setCellValueFactory(new PropertyValueFactory<Officer, String>("customerName"));
        date.setCellValueFactory(new PropertyValueFactory<Officer, String>("date"));
        sales.setCellValueFactory(new PropertyValueFactory<Officer, String>("sales"));
        status.setCellValueFactory(new PropertyValueFactory<Officer, String>("status"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<Officer, String>("totalAmount"));
        customerContact.setCellValueFactory(new PropertyValueFactory<Officer, String>("customerContact"));
        initialData();
    }
}
