package ru.bookpleasure.test.integration;


import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import ru.bookpleasure.beans.OrderBean;

/**
 * Created by Kirill on 11/07/16.
 */
public class OrderBeanIT extends AbstractSpringTest {

    @Autowired
    OrderBean orderBean;


    @Test
    public void test() {
        OrderBean orderBea = orderBean;
    }

}
