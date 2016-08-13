package ru.bookpleasure.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bookpleasure.db.entities.*;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.*;
import ru.bookpleasure.repos.OrdersRepo;
import ru.bookpleasure.repos.ProductsRepo;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.springframework.data.jpa.domain.Specifications.where;
import static ru.bookpleasure.repos.specs.ProductsSpecs.avaliableToOrder;
import static ru.bookpleasure.repos.specs.ProductsSpecs.hasId;

/**
 * Created by Kirill on 16/06/16.
 */
@Component
@Lazy
public class OrderBean {

    @Autowired
    OrdersRepo ordersRepo;

    @Autowired
    ProductsRepo productsRepo;


    @Transactional(rollbackFor = Throwable.class)
    public OrderView createOrUpdateOrder(OrderView orderView) {
        Order order = orderViewToOrder(orderView);
        order = ordersRepo.saveAndFlush(order);
        return orderToOrderView(order);
    }

    private Order orderViewToOrder(OrderView orderView) {
        Order order;
        if (orderView.getId() != null) {
            //существующий заказ
            order = ordersRepo.findByNumberForCustomer(Long.valueOf(orderView.getId()));

            order.setTrackingNumber(orderView.getTrackingNumber());

            if (orderView.getIsBasket() && order.getStatus() == Order.Status.EDITING) {
                saveProducts(order, orderView.getProductInfos());
            } else if (orderView.getIsBasket()) {
                throw new IllegalArgumentException("Статус заказа не соответствует запросу");
            } else if (order.getStatus() == Order.Status.EDITING) {
                order.setStatus(Order.Status.AWAITING_PAYMENT);
            }

        } else {
            //новый заказ
            order = new Order();

            if (orderView.getTrackingNumber() != null) {
                throw new IllegalArgumentException("Созданный заказ не может иметь трекномера");
            }

            if (orderView.getIsBasket()) {
                order.setStatus(Order.Status.EDITING);
            } else {
                order.setStatus(Order.Status.AWAITING_PAYMENT);
            }

            if (orderView.getProductInfos() != null && !orderView.getProductInfos().isEmpty()) {
                saveProducts(order, orderView.getProductInfos());
            } else {
                throw new IllegalArgumentException("Нельзя создать заказ без товаров");
            }

        }

        if (orderView.getAddressInfo() != null) {
            order.setAddress(addressFromAddressInfo(orderView.getAddressInfo()));
        }
        if (orderView.getCustomerDetailsInfo() != null) {
            order.setCustomerDetails(
                    customerDetailsFromCustomerDetailsInfo(orderView.getCustomerDetailsInfo()));
        }

        if (orderView.getPaymentInfo() != null) {
            savePayment(order, orderView.getPaymentInfo());
        }

        if (orderView.getStatus() != null) {
            order.setStatus(Order.Status.valueOf(orderView.getStatus()));
        }

        return order;
    }

    private OrderView orderToOrderView(Order order) {
        OrderView result = new OrderView();
        result.withId(order.getNumberForCustomer().toString())
                .withTotalAmount(order.getTotalAmount().toPlainString())
                .withIsBasket(order.getStatus() == Order.Status.EDITING)
                .withStatus(order.getStatus().readableValue())
                .withTrackingNumber(order.getTrackingNumber());

        if (order.getAddress() != null) {
            AddressInfo address = new AddressInfo();
            address.withBuilding(order.getAddress().getBuilding())
                    .withCity(order.getAddress().getCity())
                    .withFlat(order.getAddress().getFlat())
                    .withStreet(order.getAddress().getStreet())
                    .withSuite(order.getAddress().getSuite())
                    .withZip(order.getAddress().getSuite());
            result.withAddressInfo(address);
        }

        if (order.getCustomerDetails() != null) {
            CustomerDetailsInfo customerDetails = new CustomerDetailsInfo();
            customerDetails.withFirstname(order.getCustomerDetails().getFirstname())
                    .withLastname(order.getCustomerDetails().getLastname())
                    .withMiddlename(order.getCustomerDetails().getMiddlename())
                    .withEmail(order.getCustomerDetails().getEmail())
                    .withPhone(order.getCustomerDetails().getPhone());
            result.withCustomerDetailsInfo(customerDetails);
        }

        if (order.getProducts() != null && !order.getProducts().isEmpty()) {
            List<ProductInfo> products = new ArrayList<ProductInfo>();
            for (OrderProduct p : order.getProducts()) {
                ProductInfo product = new ProductInfo();
                product.withQuantity(p.getNumber())
                        .withId(p.getProduct().getId())
                        .withName(p.getProduct().getName())
                        .withPrice(p.getProduct().getPrice().toString());
                products.add(product);
            }
            result.withProductInfos(new HashSet<ProductInfo>(products));
        }

        if (order.getPayment() != null) {
            PaymentInfo payment = new PaymentInfo();
            payment.withAmount(order.getPayment().getAmount().toPlainString())
                    .withDatetime(order.getPayment().getMadeAt().toString());
            result.withPaymentInfo(payment);
        }

        return result;
    }

    private Address addressFromAddressInfo(AddressInfo info) {
        Address address = new Address();
        address.setBuilding(info.getBuilding());
        address.setCity(info.getCity());
        address.setFlat(info.getFlat());
        address.setStreet(info.getStreet());
        address.setSuite(info.getSuite());
        address.setZip(info.getZip());
        return address;
    }

    private CustomerDetails customerDetailsFromCustomerDetailsInfo(CustomerDetailsInfo info) {
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setEmail(info.getEmail());
        customerDetails.setPhone(info.getPhone());
        customerDetails.setFirstname(info.getFirstname());
        customerDetails.setLastname(info.getLastname());
        customerDetails.setMiddlename(info.getMiddlename());
        return customerDetails;
    }

    private Order savePayment(Order order, PaymentInfo info) {
        if (!order.getStatus().paymentNeeded()) {
            throw new IllegalArgumentException("Невозможно добавить платёж на этапе: "
                    + order.getStatus().readableValue());
        }
        Payment payment = new Payment();
        payment.setOperationId(info.getOperationId());
        payment.setAmount(new BigDecimal(info.getAmount()));

        if (info.getDatetime() != null) {
            Date dt = DatatypeConverter.parseDateTime(info.getDatetime())
                    .getTime();
            Timestamp ts = new Timestamp(dt.getTime());
            payment.setMadeAt(ts);
        }

        if (info.getWithdrawAmount() != null) {
            payment.setWithdrawAmount(new BigDecimal(info.getWithdrawAmount()));
        } else {
            payment.setWithdrawAmount(new BigDecimal(info.getAmount()));
        }

        if (info.getCurrency() != null) {
            payment.setCurrency(Payment.Currency.getFromCode(info.getCurrency()));
        } else {
            payment.setCurrency(Payment.Currency.RUR);
        }

        if (info.getPaymentSource() != null) {
            payment.setPaymentSource(Payment.PaymentSource.valueOf(info.getPaymentSource()));
        } else {
            payment.setPaymentSource(Payment.PaymentSource.YANDEX);
        }

        if (payment.getAmount().compareTo(order.getTotalAmount()) >= 0) {
            order.setStatus(Order.Status.PAID);
        } else {
            throw new IllegalArgumentException("Платёж не может быть меньше суммы заказа");
        }

        order.setPayment(payment);

        return order;
    }

    private Order saveProducts(Order order, Collection<ProductInfo> productInfos) {
        if (!productInfos.isEmpty()) {
            BigDecimal totalAmount = new BigDecimal(0);
            List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
            for (ProductInfo info : productInfos) {
                Product product = productsRepo.findOne(
                        where(avaliableToOrder(info.getQuantity()))
                                .and(hasId(info.getId())));

                if (product == null) {
                    throw new IllegalArgumentException("Товар " + info.getId() + " недоступен для заказа");
                }

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setProduct(product);
                orderProduct.setNumber(info.getQuantity());
                orderProduct.setOrder(order);
                orderProducts.add(orderProduct);

                //если заказ отправлен, то бронируем товары
                if (order.getStatus() != Order.Status.EDITING) {
                    product.setQuantity(product.getQuantity() - info.getQuantity());
                }

                totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(orderProduct.getNumber())));
            }

            order.setTotalAmount(totalAmount);
            order.setProducts(orderProducts);
        }
        return order;
    }


}
