package ru.bookpleasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.bookpleasure.beans.ProductBean;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.ProductView;
import java.util.List;

@RestController
@RequestMapping("/")
public class Root {

    @Autowired
    @Lazy
    ProductBean pb;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String hello() {
        return "It works!";
    }

    @RequestMapping(
            value = "contact",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public void emailMessageToAdmin(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message) {
        String subj = "Сообщение от " + name + " - " + email;
        MailAgent.sendMailFromRobot("info@bookpleasure.ru", subj, message);
    }


    @RequestMapping(
            value = "/boxes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ProductView> listEnabledProducts(){

        List<ProductView> result = pb.getEnabledProductByCategory(Product.ProductCategory.BOOKBOX.toString());
        return result;
    }


}
