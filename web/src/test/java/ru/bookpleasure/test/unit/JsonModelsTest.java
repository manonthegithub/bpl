package ru.bookpleasure.test.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import ru.bookpleasure.models.OrderView;

import java.io.IOException;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

/**
 * Created by Kirill on 12/07/16.
 */
public class JsonModelsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testSample() throws IOException {
        String sample = "{" +
                "\"isBasket\": true," +
                "\"productInfos\": [" +
                "{" +
                "\"quantity\": 10," +
                "\"id\": \"" + UUID.randomUUID().toString() + "\"" +
                "}" +
                "]" +
                "}";

        OrderView orderView = OBJECT_MAPPER.readValue(sample, OrderView.class);
        assertEquals(orderView.getProductInfos().isEmpty(), false);
    }

}
