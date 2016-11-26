package ru.bookpleasure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.bookpleasure.beans.OrderBean;
import ru.bookpleasure.beans.ProductBean;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.*;

import java.util.*;

import static ru.bookpleasure.Utils.sha1HashFromParams;

@RestController
@RequestMapping("/")
public class Root {

    @Lazy
    @Autowired
    ProductBean pb;

    @Lazy
    @Autowired
    OrderBean ob;

    @Value("${notificationSecret}")
    private String secret;

    @GetMapping(
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String hello() {
        return "It works!";
    }


    /**
     * Contact us form
     */
    @PostMapping(
            value = "contact",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public void emailMessageToAdmin(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message) {
        String subj = "Сообщение от " + name + " - " + email;
        MailAgent.sendMailFromRobot("info@bookpleasure.ru", subj, message);
    }


    /**
     * List of boxes
     */
    @GetMapping(
            value = "/boxes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ProductView> listBoxes() {
        List<ProductView> result = pb.getEnabledProductByCategory(
                Product.ProductCategory.BOOKBOX.toString(),
                Optional.<Sort>empty()
        );
        return result;
    }

    /**
     * get order by Id and email
     */
    @GetMapping(
            value = "/order/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public OrderView trackOrderDetails(
            @PathVariable("id") String orderId,
            @RequestParam("email") String email
    ) {
        return ob.getOrderByNumberAndEmail(orderId, email);
    }

    @PostMapping(
            value = "/order",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public OrderView createOrUpdateOrder(@RequestBody OrderView orderView) {
        return ob.createOrUpdateOrder(orderView);
    }

    @PostMapping(
            value = "/order/payment",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String paymentInfoFromYandex(@RequestBody MultiValueMap<String, String> paymentForm) {

        checkSha1Hash(paymentForm);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo
                .withDatetime(paymentForm.getFirst("datetime"))
                .withAmount(paymentForm.getFirst("amount"))
                .withOperationId(paymentForm.getFirst("operation_id"))
                .withAmount(paymentForm.getFirst("amount"))
                .withWithdrawAmount(paymentForm.getFirst("withdraw_amount"));

        AddressInfo addressInfo = new AddressInfo();
        addressInfo
                .withZip(paymentForm.getFirst("zip"))
                .withSuite(paymentForm.getFirst("suite"))
                .withStreet(paymentForm.getFirst("street"))
                .withFlat(paymentForm.getFirst("flat"))
                .withCity(paymentForm.getFirst("city"))
                .withBuilding(paymentForm.getFirst("building"));

        CustomerDetailsInfo customerDetailsInfo = new CustomerDetailsInfo();
        customerDetailsInfo
                .withPhone(paymentForm.getFirst("phone"))
                .withMiddlename(paymentForm.getFirst("fathersname"))
                .withLastname(paymentForm.getFirst("lastname"))
                .withFirstname(paymentForm.getFirst("firstname"))
                .withEmail(paymentForm.getFirst("email"));

        OrderView orderView = new OrderView();
        orderView
                .withIsBasket(false)
                .withId(paymentForm.getFirst("label"))
                .withCustomerDetailsInfo(customerDetailsInfo)
                .withPaymentInfo(paymentInfo)
                .withAddressInfo(addressInfo);

        createOrUpdateOrder(orderView);
        return "Ok";
    }


    private void checkSha1Hash(MultiValueMap<String, String> paymentForm) {
        List<String> params = Arrays.asList(
                "notification_type",
                "operation_id",
                "amount",
                "currency",
                "datetime",
                "sender",
                "codepro",
                secret,
                "label"
        );
        List<String> paramValues = new ArrayList<String>();
        for (String param : params) {
            if (param.equals(secret)) {
                paramValues.add(param);
            } else {
                String value = paymentForm.getFirst(param);
                if (value != null) {
                    paramValues.add(value);
                }
            }
        }

        String expectedHash = sha1HashFromParams(paramValues);
        if (!paymentForm.getFirst("sha1_hash").equals(expectedHash)) {
            throw new RuntimeException("Illegal hash");
        }

    }

}
