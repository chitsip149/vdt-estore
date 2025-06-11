package com.trang.estore.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @GetMapping
    @Operation(summary = "Get all products, can specify a category (optional)")
    public List<ProductDto> getProducts(@RequestParam (required = false, name = "categoryId") Byte categoryId) {
        if (categoryId == null) {
            return productRepository.findAll()
                    .stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
        }

        return productRepository.findByCategory(categoryId)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a specific product")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder) {
        var product = productMapper.toProduct(productDto);
        productRepository.save(product);

        var productDto2 = productMapper.toDto(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto2.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto2);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product information")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        var product = productRepository.findById(id).orElse(null);
        System.out.println(product);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productDto.setId(id);

        productMapper.update(productDto, product);
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @Operation(summary = "Delete a product from database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
