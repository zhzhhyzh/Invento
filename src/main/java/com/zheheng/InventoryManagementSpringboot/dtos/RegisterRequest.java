package com.zheheng.InventoryManagementSpringboot.dtos;

import com.zheheng.InventoryManagementSpringboot.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "email is required")
    private String email;

    private UserRole role;

    @NotBlank(message = "password is required")
    private String password;
}
