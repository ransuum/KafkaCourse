package org.course.kafka.util;

import lombok.RequiredArgsConstructor;
import org.course.kafka.models.dto.ProductDto;
import org.course.kafka.models.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper mapper;

    public ProductDto productToProductDto(Product product) {
        return mapper.map(product, ProductDto.class);
    }
}
