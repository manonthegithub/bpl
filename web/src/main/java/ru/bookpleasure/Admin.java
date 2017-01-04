package ru.bookpleasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.bookpleasure.beans.OrderBean;
import ru.bookpleasure.beans.ProductBean;
import ru.bookpleasure.models.OrderView;
import ru.bookpleasure.models.ProductView;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kirill on 30/05/16.
 */
@RestController
@RequestMapping("/adm")
public class Admin {

    @Autowired
    @Lazy
    ProductBean pb;

    @Autowired
    @Lazy
    OrderBean ob;

    @GetMapping(
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String hello() {
        return "Secure. It works!";
    }

    @GetMapping(
            value = "orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Collection<OrderView> listOrders() throws Exception {
        return ob.getListOfOrders();
    }

    @DeleteMapping(value = "/products/{id}")
    public void removeProduct(@PathVariable("id") UUID productID) {
        pb.removeProduct(productID);
    }

    @PutMapping(
            value = "/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ProductView saveProduct(@RequestBody ProductView productView) throws IOException {
        return pb.saveProduct(productView);
    }

    @GetMapping(
            value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ProductView> listProducts(
            @RequestParam("category") String category,
            Sort sort
    ) {
        if (category != null) {
            return pb.getProductByCategory(category, sort);
        } else {
            return pb.getAllProducts();
        }
    }
}
