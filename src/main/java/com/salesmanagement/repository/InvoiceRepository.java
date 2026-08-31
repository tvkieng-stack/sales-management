package com.salesmanagement.repository;

import com.salesmanagement.model.Invoice;
import com.salesmanagement.model.InvoiceDetail;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceRepository {

    public Invoice saveInvoice(Connection conn, Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (invoice_code, employee_id, customer_id, subtotal, discount, total, payment_method, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, invoice.getInvoiceCode());
            ps.setInt(2, invoice.getEmployeeId());
            if (invoice.getCustomerId() != null) {
                ps.setInt(3, invoice.getCustomerId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setDouble(4, invoice.getSubtotal());
            ps.setDouble(5, invoice.getDiscount());
            ps.setDouble(6, invoice.getTotal());
            ps.setString(7, invoice.getPaymentMethod().name());
            ps.setString(8, invoice.getStatus().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    invoice.setId(keys.getInt(1));
                }
            }
        }
        return invoice;
    }

    public void saveInvoiceDetail(Connection conn, InvoiceDetail detail) throws SQLException {
        String sql = "INSERT INTO invoice_details (invoice_id, product_id, quantity, unit_price, discount, subtotal) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getInvoiceId());
            ps.setInt(2, detail.getProductId());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getUnitPrice());
            ps.setDouble(5, detail.getDiscount());
            ps.setDouble(6, detail.getSubtotal());
            ps.executeUpdate();
        }
    }

    public String generateInvoiceCode() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return "HD" + ts;
    }
}