package ru.bookpleasure.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bookpleasure.db.entities.OrderProduct;

import java.util.UUID;

/**
 * Created by Kirill on 09/07/16.
 */
public interface OrderProductsRepo extends CrudRepository<OrderProduct, UUID> {
}
