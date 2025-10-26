package com.zheheng.InventoryManagementSpringboot.controllers;

import com.zheheng.InventoryManagementSpringboot.dtos.CategoryDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.ProductDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.models.Product;
import com.zheheng.InventoryManagementSpringboot.services.CategoryService;
import com.zheheng.InventoryManagementSpringboot.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> saveProduct(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("categoryId") Long categoryId,


            @RequestParam(value = "description", required = false) String description
    ) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setSku(sku);
        productDTO.setPrice(price);
        productDTO.setCategoryId(categoryId);
        productDTO.setStockQuantity(stockQuantity);
        productDTO.setDescription(description);

        return ResponseEntity.ok(productService.saveProduct(productDTO, imageFile));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,


            @RequestParam(value = "description", required = false) String description
    ) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setSku(sku);
        productDTO.setPrice(price);
        productDTO.setCategoryId(categoryId);
        productDTO.setStockQuantity(stockQuantity);
        productDTO.setProductId(productId);
        productDTO.setDescription(description);

        return ResponseEntity.ok(productService.saveProduct(productDTO, imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> searchProduct(@PathVariable String input) {
        return ResponseEntity.ok(productService.searchProduct(input));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }


}
