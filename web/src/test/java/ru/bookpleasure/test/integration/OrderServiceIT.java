package ru.bookpleasure.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.bookpleasure.db.entities.Order;
import ru.bookpleasure.db.entities.OrderProduct;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.OrderView;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;
import static ru.bookpleasure.Utils.sha1HashFromParams;

/**
 * Created by Kirill on 12/07/16.
 */
public class OrderServiceIT extends AbstractSpringTest {

    private final ObjectMapper MAPPER = new ObjectMapper();

    private final static Integer TOTAL_QUANTITY = 20;

    private final static Integer DEFAULT_PRICE = 100;

    private final static String CURRENCY = "643";

    @Value("${notificationSecret}")
    private String secret;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    EntityManager entityManager;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Тестируем создание корзины с одним продуктом - боксом
     */
    @Test
    public void newBasketWithOneProduct() throws Exception {
        int quantity = TOTAL_QUANTITY - 10;

        Product product = persistBookBoxProduct();

        String sample = "{" +
                "\"isBasket\": true," +
                "\"productInfos\": [" +
                "{" +
                "\"quantity\": " + quantity + "," +
                "\"id\": \"" + product.getId().toString() + "\"" +
                "}" +
                "]" +
                "}";

        OrderView resultOrder = sendOrderRequestAndGetResponse(sample);
        Order order = entityManager.find(Order.class, Long.valueOf(resultOrder.getId()));

        assertEquals(order.getCreatedAt(), order.getUpdatedAt());
        assertEquals(order.getStatus(), Order.Status.EDITING);
        checkQuantity(order.getProducts());
        checkCorrectnessOfOrderEntity(order);
    }

    /**
     * Заказ бокса, клик на "Заказать"
     */
    @Test
    public void newBookBoxOrderProduct() throws Exception {
        Order order = newBookBoxOrder();
        assertEquals(order.getCreatedAt(), order.getUpdatedAt());
        assertEquals(order.getStatus(), Order.Status.AWAITING_PAYMENT);
        checkQuantity(order.getProducts());
        checkCorrectnessOfOrderEntity(order);
    }

    /**
     * Оплата бокса
     */
    @Test
    public void paymentNotificationTest() throws Exception {
        Order order = newBookBoxOrder();
        assertNull(order.getPayment());

        String resp = payAndGetResponse(order);
        assertEquals(resp, "Ok");
        assertNotNull(order.getPayment());
        assertEquals(order.getPayment().getAmount(), order.getTotalAmount());
    }


    /**
     * Редактирование деталей заказа, изменение статуса
     */
    @Test(dataProvider = "statuses")
    public void editOrderTest(Order.Status status) throws Exception {
        Order order = newBookBoxOrder();
        String resp = payAndGetResponse(order);
        assertEquals(resp, "Ok");
        assertNotEquals(order.getStatus(), status);

        String sample = "{" +
                "\"id\":\"" + order.getNumberForCustomer() + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"isBasket\": false" +
                "}";
        OrderView orderView = sendOrderRequestAndGetResponse(sample);

        assertEquals(orderView.getStatus(), status.readableValue());
        assertEquals(order.getStatus(), status);
    }

    @DataProvider(name = "statuses")
    public Object[][] provider() {
        return new Object[][]{
                {Order.Status.CANCELLED},
                {Order.Status.SENT},
                {Order.Status.FINISHED}
        };
    }

    private String payAndGetResponse(Order order) {
        String notificationType = "card-incoming";
        String operationId = "441361714955017004";
        String amount = order.getTotalAmount().toPlainString();
        String datetime = "2013-12-26T08:28:34Z";
        String codepro = "false";
        String sender = "";
        String label = order.getNumberForCustomer().toString();


        String sha1 = sha1HashFromParams(
                Arrays.asList(
                        notificationType,
                        operationId,
                        amount,
                        CURRENCY,
                        datetime,
                        sender,
                        codepro,
                        secret,
                        label)
        );

        String body = "operation_id=" + operationId + "&" +
                "notification_type=" + notificationType + "&" +
                "datetime=" + datetime + "&" +
                "sha1_hash=" + sha1 + "&" +
                "sender=" + sender + "&" +
                "codepro=" + codepro + "&" +
                "currency=" + CURRENCY + "&" +
                "amount=" + amount + "&" +
                "withdraw_amount=100.00&" +
                "label=" + label + "&" +
                "lastname=Иванов&" +
                "firstname=Петр&" +
                "fathersname=Сидорович&" +
                "zip=195123&" +
                "city=Санкт-Петербург&" +
                "street=Денежная&" +
                "building=12&" +
                "suite=12&" +
                "flat=12&" +
                "phone=&" +
                "email=";
        try {
            return sendPaymentRequestAndGetResponse(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Order newBookBoxOrder() throws Exception {
        int quantity = TOTAL_QUANTITY - 10;

        Product product = persistBookBoxProduct();

        String sample = "{" +
                "\"isBasket\": false," +
                "\"productInfos\": [" +
                "{" +
                "\"quantity\": " + quantity + "," +
                "\"id\": \"" + product.getId().toString() + "\"" +
                "}" +
                "]" +
                "}";

        OrderView resultOrder = sendOrderRequestAndGetResponse(sample);
        return entityManager.find(Order.class, Long.valueOf(resultOrder.getId()));
    }


    private void checkCorrectnessOfOrderEntity(Order order) {
        assertFalse(order.getProducts().isEmpty());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(order.getTotalAmount(), getTotalAmount(order.getProducts()));
    }


    private OrderView sendOrderRequestAndGetResponse(String body) throws Exception {
        return sendPostRequestAndGetResponse("/order", body, MediaType.APPLICATION_JSON, OrderView.class);
    }

    private String sendPaymentRequestAndGetResponse(String body) throws Exception {
        return sendPostRequestAndGetStringResponse("/order/payment", body, MediaType.APPLICATION_FORM_URLENCODED);
    }

    private String sendPostRequestAndGetStringResponse(String path, String body, MediaType type) throws Exception {
        MvcResult requestResult = this.mockMvc.perform(post(path)
                .contentType(type)
                .content(body))
                .andExpect(status().isOk())
                .andReturn();
        return requestResult.getResponse().getContentAsString();
    }


    private <T> T sendPostRequestAndGetResponse(String path, String body, MediaType type, Class<T> clazz) throws Exception {
        return MAPPER.readValue(sendPostRequestAndGetStringResponse(path, body, type), clazz);
    }

    private Product persistBookBoxProduct() {
        Product product = new Product();
        product.setActive(true);
        product.setCategory(Product.ProductCategory.BOOKBOX);
        product.setEnabled(true);
        product.setPrice(new BigDecimal(DEFAULT_PRICE));
        product.setName(UUID.randomUUID().toString());
        product.setQuantity(TOTAL_QUANTITY);
        entityManager.persist(product);
        entityManager.flush();
        return product;
    }

    private void checkQuantity(Collection<OrderProduct> ops) {
        for (OrderProduct op : ops) {
            if (op.getOrder().getStatus() == Order.Status.EDITING) {
                Integer expectedQuantity = TOTAL_QUANTITY + op.getNumber();
                assertEquals(getSummQuantity(op), expectedQuantity);
            } else {
                assertEquals(getSummQuantity(op), TOTAL_QUANTITY);
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
