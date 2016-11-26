package ru.bookpleasure.test;

import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.bookpleasure.db.entities.Payment;
import ru.bookpleasure.models.PaymentInfo;
import ru.bookpleasure.test.integration.AbstractSpringTest;

import javax.xml.bind.DatatypeConverter;
import java.sql.Time;
import java.sql.Timestamp;

import static org.testng.Assert.assertEquals;

/**
 * Created by Kirill on 06/11/16.
 */
public class MappingTests extends AbstractSpringTest {

    @Autowired
    Mapper mapper;

    @Test
    public void paymentToPaymentInfoTest() {

        String amount = "100.00";
        Payment.Currency currency = Payment.Currency.RUR;
        String operationId = "441361714955017004";
        Payment.PaymentSource paymentSource = Payment.PaymentSource.YANDEX;
        String dateTime = "2013-12-26T08:28:34Z";


        PaymentInfo info = new PaymentInfo();
        info
                .withAmount(amount)
                .withCurrency(currency.getCode().toString())
                .withDatetime(dateTime)
                .withOperationId(operationId)
                .withPaymentSource(paymentSource.toString())
                .withWithdrawAmount(amount);

        Payment payment = mapper.map(info, Payment.class);


        assertEquals(payment.getCurrency(), currency);
        assertEquals(payment.getAmount().toPlainString(), amount);
        assertEquals(payment.getWithdrawAmount().toPlainString(), amount);
        assertEquals(payment.getOperationId(), operationId);
        assertEquals(payment.getPaymentSource(), paymentSource);
        assertEquals(payment.getMadeAt(), new Timestamp(
                DatatypeConverter.parseDateTime(dateTime)
                        .getTime()
                        .getTime()));
    }


}
