package ru.bookpleasure.test.integration;


import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.bookpleasure.beans.OrderService;

/**
 * Created by Kirill on 11/07/16.
 */
public class OrderServiceIT extends AbstractSpringIT {

    @Autowired
    OrderService orderService;


    @Test
    public void test() {
        OrderService ob = orderService;
    }

}
