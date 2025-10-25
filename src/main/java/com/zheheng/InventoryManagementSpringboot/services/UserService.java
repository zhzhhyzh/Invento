package com.zheheng.InventoryManagementSpringboot.services;

import com.zheheng.InventoryManagementSpringboot.dtos.LoginRequest;
import com.zheheng.InventoryManagementSpringboot.dtos.RegisterRequest;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.dtos.UserDTO;
import com.zheheng.InventoryManagementSpringboot.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(long id);


}
