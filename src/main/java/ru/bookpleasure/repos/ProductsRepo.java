package ru.bookpleasure.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bookpleasure.db.entities.Product;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kirill on 13/06/16.
 */
public interface ProductsRepo extends CrudRepository<Product, UUID> {

    List<Product> findByCategory(Product.ProductCategory category);

    List<Product> findByCategoryAndEnabledTrue(Product.ProductCategory category);

}
