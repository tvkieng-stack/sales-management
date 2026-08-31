package com.salesmanagement.service;

import com.salesmanagement.model.Supplier;
import com.salesmanagement.model.enums.Status;
import com.salesmanagement.repository.SupplierRepository;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {

    private final SupplierRepository supplierRepository = new SupplierRepository();

    public List<Supplier> getAll() throws SQLException {
        return supplierRepository.findAll();
    }

    public List<Supplier> getActive() throws SQLException {
        return supplierRepository.findActive();
    }

    public void create(String name, String phone, String email, String address) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống.");
        }
        supplierRepository.save(new Supplier(name.trim(), phone, email, address));
    }

    public void update(Supplier supplier) throws SQLException {
        if (supplier.getName() == null || supplier.getName().isBlank()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống.");
        }
        supplierRepository.update(supplier);
    }

    public void deactivate(Supplier supplier) throws SQLException {
        supplier.setStatus(Status.INACTIVE);
        supplierRepository.update(supplier);
    }
}