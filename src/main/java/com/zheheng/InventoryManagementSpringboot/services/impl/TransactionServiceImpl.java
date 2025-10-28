package com.zheheng.InventoryManagementSpringboot.services.impl;

import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.dtos.TransactionDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.TransactionRequest;
import com.zheheng.InventoryManagementSpringboot.enums.TransactionStatus;
import com.zheheng.InventoryManagementSpringboot.enums.TransactionType;
import com.zheheng.InventoryManagementSpringboot.exceptions.NotFoundException;
import com.zheheng.InventoryManagementSpringboot.models.Product;
import com.zheheng.InventoryManagementSpringboot.models.Supplier;
import com.zheheng.InventoryManagementSpringboot.models.Transaction;
import com.zheheng.InventoryManagementSpringboot.models.User;
import com.zheheng.InventoryManagementSpringboot.repositories.ProductRepository;
import com.zheheng.InventoryManagementSpringboot.repositories.SupplierRepository;
import com.zheheng.InventoryManagementSpringboot.repositories.TransactionRepository;
import com.zheheng.InventoryManagementSpringboot.repositories.UserRepository;
import com.zheheng.InventoryManagementSpringboot.services.TransactionService;
import com.zheheng.InventoryManagementSpringboot.services.UserService;
import com.zheheng.InventoryManagementSpringboot.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new IllegalArgumentException("supplierId is Required");

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new NotFoundException("Supplier Not Found"));
        User user = userService.getCurrentLoggedInUser();

        //Save the product qty
        product.setStockQuantity(product.getStockQuantity() + quantity);

        productRepository.save(product);

        //Create a transaction
        Transaction newTransaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(newTransaction);

        return Response.builder()
                .status(200)
                .message("Purchasing Transaction created successfully")
                .build();

    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        User user = userService.getCurrentLoggedInUser();

        //Update the stock qty and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);

        productRepository.save(product);

        //Create a transaction
        Transaction newTransaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .note(transactionRequest.getNote())
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(newTransaction);

        return Response.builder()
                .status(200)
                .message("Sale Transaction created successfully")
                .build();

    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new IllegalArgumentException("supplierId is Required");

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product Not Found"));
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new NotFoundException("Supplier Not Found"));
        User user = userService.getCurrentLoggedInUser();

        //Save the product qty
        product.setStockQuantity(product.getStockQuantity() - quantity);

        productRepository.save(product);

        //Create a transaction
        Transaction newTransaction = Transaction.builder()
                .transactionType(TransactionType.RETURN)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(newTransaction);

        return Response.builder()
                .status(200)
                .message("Return Transaction created successfully")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        //User the Transaction Spec.
        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Transaction fetched successfully")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();

    }

    @Override
    public Response getAllTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction Not Found"));
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.getUser().setTransactions(null);

        return Response.builder()
                .status(200)
                .message("Transaction fetched successfully")
                .transaction(transactionDTO)
                .build();

    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDTO>>() {
        }.getType());
        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);

        });
        return Response.builder()
                .status(200)
                .message("Transaction fetched successfully")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepository.findById(transactionId).orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdateAt(LocalDateTime.now());
        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status updated successfully")
                .build();
    }
}
