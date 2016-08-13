package ru.bookpleasure.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bookpleasure.db.entities.Payment;

import java.util.UUID;


/**
 * Created by Kirill on 17/07/16.
 */
public interface PaymentsRepo extends JpaRepository<Payment, UUID> {
}
