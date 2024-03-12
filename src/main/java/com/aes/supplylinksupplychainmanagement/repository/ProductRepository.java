package com.aes.supplylinksupplychainmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aes.supplylinksupplychainmanagement.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Finds a product by name or description.
     *
     * @param name        The name of the product.
     * @param description The description of the product.
     * @return An optional containing the product, if found.
     */
    Optional<Product> findByNameOrDescription(String name, String description);

    /**
     * Finds the list of products associated with a user ID.
     *
     * @param userId The ID of the user.
     * @return The list of products.
     */
    @Query("SELECT p FROM Supplier s JOIN s.products p JOIN s.user u WHERE u.userId = :userId ")
    List<Product> findProductListByUserId(int userId);

    /**
     * Finds a product by its slug.
     *
     * @param slug The slug of the product.
     * @return The product.
     */
    Product findBySlug(String slug);

    /**
     * Finds all products with their associated supplier's company name.
     *
     * @return The list of products.
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.supplier")
    List<Product> findAllWithSupplierCompanyName();

    /**
     * Finds products by a set of slugs.
     *
     * @param slugs The set of slugs.
     * @return The list of products.
     */
    @Query("SELECT p FROM Product p WHERE p.slug IN :slugs")
    List<Product> findBySlugs(Set<String> slugs);
}