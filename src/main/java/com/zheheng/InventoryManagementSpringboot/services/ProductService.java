package com.zheheng.InventoryManagementSpringboot.services;


import com.zheheng.InventoryManagementSpringboot.dtos.ProductDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile productImage);

    Response updateProduct(ProductDTO productDTO, MultipartFile productImage);

    Response getAllProducts();

    Response getProductById(Long id);

    Response deleteProduct(Long id);

    Response searchProduct(String input);


}
