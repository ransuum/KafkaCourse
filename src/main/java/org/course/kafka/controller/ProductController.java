package org.course.kafka.controller;

import org.course.kafka.models.dto.ProductDto;
import org.course.kafka.models.entity.Product;
import org.course.kafka.service.ProductService;
import org.course.kafka.util.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final Mapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, Mapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> create(@RequestBody Product product) {
        return new ResponseEntity<>(modelMapper.productToProductDto(productService.createProduct(product)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
        return new ResponseEntity<>(modelMapper.productToProductDto(productService.getProduct(id)), HttpStatus.FOUND);
    }
}
