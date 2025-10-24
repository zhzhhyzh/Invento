package com.zheheng.InventoryManagementSpringboot.repositories;

import com.zheheng.InventoryManagementSpringboot.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}
