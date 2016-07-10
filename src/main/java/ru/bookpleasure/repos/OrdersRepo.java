package ru.bookpleasure.repos;

import org.springframework.data.repository.CrudRepository;
import ru.bookpleasure.db.entities.Order;

import java.util.UUID;

/**
 * Created by Kirill on 13/06/16.
 */
public interface OrdersRepo extends CrudRepository<Order, UUID> {

    Order findByNumberForCustomer(Long orderNumber);

}
