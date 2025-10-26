package com.zheheng.InventoryManagementSpringboot.services;

import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.dtos.TransactionRequest;
import com.zheheng.InventoryManagementSpringboot.enums.TransactionStatus;

public interface TransactionService {

    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);
}
