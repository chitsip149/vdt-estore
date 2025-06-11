package com.trang.estore.products;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
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
