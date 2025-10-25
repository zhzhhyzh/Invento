package com.zheheng.InventoryManagementSpringboot.services;

import com.zheheng.InventoryManagementSpringboot.dtos.CategoryDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(long id);

    Response updateCategory(long id, CategoryDTO categoryDTO);

    Response deleteCategory(long id);
}
