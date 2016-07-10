package ru.bookpleasure.repos.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.db.entities.Product_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

/**
 * Created by Kirill on 10/07/16.
 */
public class ProductsSpecs {

    public static Specification<Product> avaliableToOrder(final int number) {
        return new Specification<Product>() {
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                return builder.and(
                        builder.isTrue(root.get(Product_.enabled)),
                        builder.isTrue(root.get(Product_.active)),
                        builder.greaterThanOrEqualTo(root.get(Product_.quantity), number)
                );
            }
        };
    }

    public static Specification<Product> hasId(final UUID id) {
        return new Specification<Product>() {
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                return builder.equal(root.get(Product_.id), id);
            }
        };
    }

}
