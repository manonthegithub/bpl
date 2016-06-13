package ru.bookpleasure;

import net.iharder.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.bookpleasure.beans.FilesBean;
import ru.bookpleasure.beans.ProductBean;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.ProductView;

import javax.servlet.ServletContext;
import java.io.IOException;
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
    FilesBean fb;

    @Autowired
    @Lazy
    ServletContext context;

    @RequestMapping(
            method = RequestMethod.GET)
    @ResponseBody
    public String listOrders() throws Exception {

        return "ADMIN Ok";
    }

    @RequestMapping(
            value = "/boxes/{id}",
            method = RequestMethod.DELETE
    )
    public void removeProduct(@PathVariable("id") UUID productID) {
        pb.removeProduct(productID);
    }

    @RequestMapping(
            value = "/boxes",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ProductView saveProduct(@RequestBody ProductView productView) throws IOException {
        if (productView.getAvailableNumber() == null) {
            productView.setAvailableNumber(productView.getQuantity());
        }
        if (productView.getEnabled() == null) {
            productView.setEnabled(false);
        }
        if (productView.getId() == null) {
            productView.setId(UUID.randomUUID());
        }

        if (productView.getImageLink() != null && productView.getBase64ImageFile() != null) {
            String fileName = UUID.randomUUID().toString() +
                    productView.getImageLink().substring(productView.getImageLink().lastIndexOf("."));
            productView.setImageLink(fileName);

            fb.save(fileName,
                    Base64.decode(productView.getBase64ImageFile()),
                    context.getRealPath(FilesBean.IMAGE_FILES_PATH));
        }

        return pb.saveProduct(productView);
    }


    @RequestMapping(
            value = "/boxes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ProductView> listProducts() {
        return pb.getProductByCategory(Product.ProductCategory.BOOKBOX.toString());
    }
}
