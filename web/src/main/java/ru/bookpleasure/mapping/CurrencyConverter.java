package ru.bookpleasure.mapping;

import org.dozer.DozerConverter;
import ru.bookpleasure.db.entities.Payment;

/**
 * Created by Kirill on 06/11/16.
 */
public class CurrencyConverter extends DozerConverter<String, Payment.Currency> {

    public CurrencyConverter() {
        super(String.class, Payment.Currency.class);
    }

    @Override
    public Payment.Currency convertTo(String source, Payment.Currency destination) {
        return Payment.Currency.getFromCode(source);
    }

    @Override
    public String convertFrom(Payment.Currency source, String destination) {
        return source.getCode().toString();
    }
}
