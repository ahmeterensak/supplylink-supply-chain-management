package com.aes.supplylinksupplychainmanagement.service;

import java.util.List;
import java.util.Set;

import com.aes.supplylinksupplychainmanagement.model.Product;

public interface IProductService {

    List<Product> getAllProducts = null;

    /**
     * Creates a product.
     *
     * @param product The product to create.
     * @return The created product, or null if already exists.
     */
    Product createProduct(Product product);

    /**
     * Updates a product.
     *
     * @param product The product to update.
     */
    void updateProduct(Product product);

    /**
     * Deletes a product.
     *
     * @param product The product to delete.
     */
    void deleteProduct(Product product);

    /**
     * Finds a list of products by user ID.
     *
     * @param userId The ID of the user.
     * @return The list of found products.
     */
    List<Product> findProductListByUserId(int userId);

    /**
     * Finds a product by its slug.
     *
     * @param slug The slug of the product.
     * @return The found product, or null if not found.
     */
    Product findBySlug(String slug);

    /**
     * Retrieves all products.
     *
     * @return The list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Finds products by their slugs.
     *
     * @param slugs The set of slugs.
     * @return The list of found products.
     */
    List<Product> findBySlugs(Set<String> slugs);

    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The found product, or null if not found.
     */
    Product findById(int productId);
}
