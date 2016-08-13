package ru.bookpleasure.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bookpleasure.db.entities.PaymentRequestDetails;

import java.util.UUID;

/**
 * Created by Kirill on 17/07/16.
 */
public interface PaymentRequestDetailsRepo extends JpaRepository<PaymentRequestDetails, UUID> {
}
