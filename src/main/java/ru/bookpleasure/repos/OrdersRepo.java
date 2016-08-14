package ru.bookpleasure.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.bookpleasure.db.entities.Order;

import java.util.UUID;

/**
 * Created by Kirill on 13/06/16.
 */
public interface OrdersRepo extends PagingAndSortingRepository<Order, UUID>, JpaRepository<Order, UUID> {

    Order findByNumberForCustomer(Long orderNumber);

    Order findByNumberForCustomerAndEmail(Long orderNumber, String email);

}
