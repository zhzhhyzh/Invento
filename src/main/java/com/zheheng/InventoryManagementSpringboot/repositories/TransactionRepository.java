package com.zheheng.InventoryManagementSpringboot.repositories;

import com.zheheng.InventoryManagementSpringboot.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

}
