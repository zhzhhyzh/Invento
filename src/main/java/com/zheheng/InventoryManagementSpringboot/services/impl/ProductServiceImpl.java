package com.zheheng.InventoryManagementSpringboot.services.impl;

import com.zheheng.InventoryManagementSpringboot.dtos.ProductDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.exceptions.NotFoundException;
import com.zheheng.InventoryManagementSpringboot.models.Category;
import com.zheheng.InventoryManagementSpringboot.models.Product;
import com.zheheng.InventoryManagementSpringboot.repositories.CategoryRepository;
import com.zheheng.InventoryManagementSpringboot.repositories.ProductRepository;
import com.zheheng.InventoryManagementSpringboot.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;


    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images/";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile productImage) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        if (productRepository.existsBySku(productDTO.getSku())) {
            return Response.builder()
                    .status(400)
                    .message("Product with the SKU '" + productDTO.getSku() + "' already exists")
                    .build();
        }
        // map out dto to product entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (productImage != null && !productImage.isEmpty()) {
            log.info("Image File exits");
            String imagePath = saveImage(productImage);
            productToSave.setImageUrl(imagePath);
        }

        //Save the product entity
        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product created successfully")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile productImage) {

        //Check if product exist
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));


        // Check if image is associated with the product to update and upload
        if (productImage != null && !productImage.isEmpty()) {
            String imagePath = saveImage(productImage);
            existingProduct.setImageUrl(imagePath);
        }

        //Check if category is to be changed for the product
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }
        //Check if product fields is to be changed and update
        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        //update the product
        productRepository.save(existingProduct);
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();

    }

    @Override
    public Response getAllProducts() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
        }.getType());


        return Response.builder()
                .status(200)
                .message("Product fetched successfully")
                .products(productDTOList)
                .build();

    }

    @Override
    public Response getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Not Found"));

        return Response.builder()
                .status(200)
                .message("Product fetched successfully")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();

    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Not Found"));
        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();

    }

    @Override
    public Response searchProduct(String input) {
        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);
        if (products.isEmpty()) {
            throw new NotFoundException("Product Not Found");
        }

        List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
        }.getType());


        return Response.builder()
                .status(200)
                .message("Product fetched successfully")
                .products(productDTOList)
                .build();

    }

    private String saveImage(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Invalid image file format or image files under 1 Gb is allowed");
        }

        //create the directory if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            log.info("Image Directory Created");
        }

        //GENERATE UNIQUE FILE NAME FOR THE IMAGE
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        //get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile); //Writing the image file to the folder
        } catch (IOException e) {
            throw new IllegalArgumentException("Error saving image: " + e.getMessage());

        }

        return uniqueFileName;
    }
}
