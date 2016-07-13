package ru.bookpleasure.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.bookpleasure.db.entities.Order;
import ru.bookpleasure.db.entities.OrderProduct;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.OrderView;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;

/**
 * Created by Kirill on 12/07/16.
 */
public class OrderServiceIT extends AbstractSpringTest {

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    EntityManager entityManager;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void orderTest() throws Exception {
        int quantity = 10;

        Product product = new Product();
        product.setActive(true);
        product.setCategory(Product.ProductCategory.BOOKBOX);
        product.setEnabled(true);
        product.setPrice(new BigDecimal(100));
        product.setName("Prod");
        product.setQuantity(100);
        entityManager.persist(product);
        entityManager.flush();

        String sample = "{" +
                "\"isBasket\": true," +
                "\"productInfos\": [" +
                "{" +
                "\"quantity\": " + quantity + "," +
                "\"id\": \"" + product.getId().toString() + "\"" +
                "}" +
                "]" +
                "}";


        MvcResult requestResult = this.mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sample))
                .andExpect(status().isOk())
                .andReturn();
        String response = requestResult.getResponse().getContentAsString();
        OrderView resultOrder = MAPPER.readValue(response, OrderView.class);

        Order order = entityManager.find(Order.class, Long.valueOf(resultOrder.getId()));
        assertFalse(order.getProducts().isEmpty());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(order.getCreatedAt(), order.getUpdatedAt());
        assertEquals(order.getStatus(), Order.Status.EDITING);
        assertEquals(order.getTotalAmount(), getTotalAmount(order.getProducts()));
        checkQuantity(order.getProducts(), Arrays.asList(product));
    }

    private void checkQuantity(Collection<OrderProduct> ops, List<Product> ps) {
        assertEquals(ops.size(), ps.size());
        for (OrderProduct op : ops) {
            if (op.getOrder().getStatus() == Order.Status.EDITING) {
                Integer expectedQuantity = ps.get(ps.indexOf(op.getProduct())).getQuantity() + op.getNumber();
                assertEquals(getSummQuantity(op), expectedQuantity);
            } else {
                assertEquals(getSummQuantity(op), ps.get(ps.indexOf(op.getProduct())).getQuantity());
            }
        }
    }

    private Integer getSummQuantity(OrderProduct product) {
        return product.getNumber() + product.getProduct().getQuantity();
    }

    private BigDecimal getTotalAmount(Collection<OrderProduct> products) {
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderProduct product : products) {
            totalAmount = totalAmount.add(product.getProduct().getPrice().multiply(new BigDecimal(product.getNumber())));
        }
        return totalAmount;
    }


}
