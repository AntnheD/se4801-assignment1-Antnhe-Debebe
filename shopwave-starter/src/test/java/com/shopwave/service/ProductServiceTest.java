package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_HappyPath() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest("Phone", "Smartphone", BigDecimal.valueOf(1000), 10, 1L);
        Category category = new Category();
        category.setId(1L);

        Product mappedProduct = new Product();
        Product savedProduct = new Product();
        ProductDTO productDTO = new ProductDTO();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request, category)).thenReturn(mappedProduct);
        when(productRepository.save(mappedProduct)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.createProduct(request);

        // Assert
        assertEquals(productDTO, result);
    }

    @Test
    void createProduct_CategoryNotFound() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest("Phone", "Smartphone", BigDecimal.valueOf(1000), 10, 1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.createProduct(request));
    }
}
