package com.aes.supplylinksupplychainmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.aes.supplylinksupplychainmanagement.model.Product;
import com.aes.supplylinksupplychainmanagement.repository.ProductRepository;
import com.aes.supplylinksupplychainmanagement.service.IProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private ProductRepository productRepository;

    /**
     * Creates a product.
     *
     * @param product The product to create.
     * @return The created product, or null if already exists.
     */
    @Override
    public Product createProduct(Product product) {
        Optional<Product> optionalProduct = productRepository.findByNameOrDescription(product.getName(),
                product.getDescription());
        if (optionalProduct.isPresent()) {
            return null;
        }
        Product instance = productRepository.save(product);
        return instance;
    }

    /**
     * Updates a product.
     *
     * @param product The product to update.
     */
    @Override
    public void updateProduct(Product product) {
        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        if (!optionalProduct.isPresent()) {
            return;
        }
        productRepository.save(product);
    }

    /**
     * Deletes a product.
     *
     * @param product The product to delete.
     */
    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    /**
     * Finds a list of products by user ID.
     *
     * @param userId The ID of the user.
     * @return The list of found products.
     */
    @Override
    public List<Product> findProductListByUserId(int userId) {
        return productRepository.findProductListByUserId(userId);
    }

    /**
     * Retrieves all products.
     *
     * @return The list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllWithSupplierCompanyName();
    }

    /**
     * Finds a product by its slug.
     *
     * @param slug The slug of the product.
     * @return The found product, or null if not found.
     */
    @Override
    public Product findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    /**
     * Finds products by their slugs.
     *
     * @param slugs The set of slugs.
     * @return The list of found products.
     */
    @Override
    public List<Product> findBySlugs(Set<String> slugs) {
        return productRepository.findBySlugs(slugs);
    }

    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The found product, or null if not found.
     */
    @Override
    public Product findById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }
}