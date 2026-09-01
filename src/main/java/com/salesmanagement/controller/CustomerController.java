package com.salesmanagement.controller;

import com.salesmanagement.model.Customer;
import com.salesmanagement.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private Label messageLabel;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, Integer> pointsColumn;

    private final CustomerService customerService = new CustomerService();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("loyaltyPoints"));

        customerTable.setItems(customerList);

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillForm(newVal);
        });

        loadData();
    }

    private void loadData() {
        try {
            customerList.setAll(customerService.getAll());
            messageLabel.setText("");
        } catch (SQLException e) {
            messageLabel.setText("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void fillForm(Customer c) {
        nameField.setText(c.getName());
        phoneField.setText(c.getPhone());
        emailField.setText(c.getEmail());
        addressField.setText(c.getAddress());
    }

    @FXML
    private void handleAdd() {
        try {
            customerService.create(nameField.getText(), phoneField.getText(), emailField.getText(), addressField.getText());
            clearForm();
            loadData();
        } catch (IllegalArgumentException | SQLException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Vui lòng chọn 1 khách hàng để cập nhật.");
            return;
        }
        try {
            selected.setName(nameField.getText());
            selected.setPhone(phoneField.getText());
            selected.setEmail(emailField.getText());
            selected.setAddress(addressField.getText());
            customerService.update(selected);
            clearForm();
            loadData();
        } catch (IllegalArgumentException | SQLException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        clearForm();
        loadData();
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        customerTable.getSelectionModel().clearSelection();
    }
}