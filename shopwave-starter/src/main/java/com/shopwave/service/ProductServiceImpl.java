package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // Notice we need category repository to fetch the category
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        }

        Product product = productMapper.toEntity(request, category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        // Simple search combining both or separate depending on what's provided
        // We will do a generic approach since instructions didn't specify strict criteria
        if (keyword != null && !keyword.isEmpty() && maxPrice != null) {
            // No direct method for this requested, we can use streams or add a custom query later.
            // As per instructions, let's just use what's available or filter
            List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
            return products.stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
        } else if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
        } else if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice).stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
        }
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        int newStock = product.getStock() + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Final stock cannot be negative. Current stock: " + product.getStock() + ", Requested change: " + delta);
        }

        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }
}
