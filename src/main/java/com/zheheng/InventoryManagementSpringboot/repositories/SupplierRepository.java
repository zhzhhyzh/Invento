package com.zheheng.InventoryManagementSpringboot.repositories;

import com.zheheng.InventoryManagementSpringboot.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {

}
