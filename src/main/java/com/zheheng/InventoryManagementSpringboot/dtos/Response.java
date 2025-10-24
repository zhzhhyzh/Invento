package com.zheheng.InventoryManagementSpringboot.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheheng.InventoryManagementSpringboot.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    //Generic
    private int status;
    private String message;

    //for login
    private String token;
    private String expirationTime;
    private UserRole role;

    //For pagination
    private Integer totalPages;
    private Long totalElements;

    //data output optionals
    private UserDTO user;
    private List<UserDTO> users;

    private SupplierDTO supplier;
    private List<SupplierDTO> suppliers;
    private CategoryDTO category;
    private List<CategoryDTO> categories;
    private ProductDTO product;
    private List<ProductDTO> products;
    private TransactionDTO transaction;
    private List<TransactionDTO> transactions;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
