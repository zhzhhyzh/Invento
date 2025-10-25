package com.zheheng.InventoryManagementSpringboot.services.impl;

import com.zheheng.InventoryManagementSpringboot.dtos.SupplierDTO;
import com.zheheng.InventoryManagementSpringboot.dtos.Response;
import com.zheheng.InventoryManagementSpringboot.models.Supplier;
import com.zheheng.InventoryManagementSpringboot.repositories.SupplierRepository;
import com.zheheng.InventoryManagementSpringboot.repositories.SupplierRepository;
import com.zheheng.InventoryManagementSpringboot.services.SupplierService;
import com.zheheng.InventoryManagementSpringboot.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierService;
    private final ModelMapper modelMapper;

    @Override
    public Response createSupplier(SupplierDTO supplierDTO) {
        if (supplierService.existsByName(supplierDTO.getName())) {
            return Response.builder()
                    .status(400)
                    .message("Supplier with the name '" + supplierDTO.getName() + "' already exists")
                    .build();
        }
        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);
        supplierService.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier created successfully")
                .build();
    }

    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<SupplierDTO> supplierDTOList = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("Successfully retrieved suppliers")
                .suppliers(supplierDTOList)
                .build();

    }

    @Override
    public Response getSupplierById(long id) {
        Supplier supplier = supplierService.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("Successfully retrieved supplier")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response updateSupplier(long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierService.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        if (supplierDTO.getId() != null) {
            existingSupplier.setName(supplierDTO.getName());
        }
        if (supplierDTO.getContactInfo() != null) {
            existingSupplier.setContactInfo(supplierDTO.getContactInfo());
        }
        if (supplierDTO.getAddress() != null) {
            existingSupplier.setAddress(supplierDTO.getAddress());
        }
        supplierService.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier updated successfully")
                .build();
    }

    @Override
    public Response deleteSupplier(long id) {
        supplierService.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierService.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier deleted successfully")
                .build();
    }
}
