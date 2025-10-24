package com.zheheng.InventoryManagementSpringboot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheheng.InventoryManagementSpringboot.enums.TransactionStatus;
import com.zheheng.InventoryManagementSpringboot.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {
    private Long id;

    private Integer totalProducts;
    private BigDecimal totalPrice;

    private TransactionType transactionType; //purchase, sale, return

    private TransactionStatus status; //Pending, completed, processing

    private String description;
    private String note;
    private  LocalDateTime createdAt ;
    private LocalDateTime updateAt;

    private ProductDTO product;

    private UserDTO user;

    private SupplierDTO supplier;
}
