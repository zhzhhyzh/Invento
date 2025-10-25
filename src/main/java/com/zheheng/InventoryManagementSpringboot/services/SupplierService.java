package com.zheheng.InventoryManagementSpringboot.services;

import com.zheheng.InventoryManagementSpringboot.dtos.SupplierDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.dtos.SupplierDTO;

public interface SupplierService {
    Response createSupplier(SupplierDTO supplierDTO);

    Response getAllSuppliers();

    Response getSupplierById(long id);

    Response updateSupplier(long id, SupplierDTO supplierDTO);

    Response deleteSupplier(long id);
}
