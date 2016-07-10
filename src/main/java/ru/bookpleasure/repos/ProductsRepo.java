package ru.bookpleasure.repos;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.bookpleasure.db.entities.Product;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kirill on 13/06/16.
 */
public interface ProductsRepo extends PagingAndSortingRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategory(Product.ProductCategory category, Sort sort);

    List<Product> findByCategoryAndEnabledTrue(Product.ProductCategory category, Sort sort);

}
