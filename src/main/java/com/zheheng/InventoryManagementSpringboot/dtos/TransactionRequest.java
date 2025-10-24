package com.zheheng.InventoryManagementSpringboot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zheheng.InventoryManagementSpringboot.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @Positive(message = "product id is required")
    private Long productId;

    @Positive(message = "quantity id is required")
    private Integer quantity;

    @Positive(message = "supplier id is required")
    private Long supplierid;

    private String decription;
    private String note;


}
