package ru.bookpleasure;

import ru.bookpleasure.db.PersistenceManager;
import ru.bookpleasure.db.entities.Order;
import ru.bookpleasure.db.entities.Product;

import javax.naming.InitialContext;
import javax.naming.Context;

import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Root {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "It works!";
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/contact")
    public void emailMessageToAdmin(
            @FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("message") String message) {
        String subj = "Сообщение от " + name + " - " + email;
        MailAgent.sendMailFromRobot("info@bookpleasure.ru", subj, message);
    }

    @GET
    @Path("/adm")
    public String listOrders() throws Exception {
        Context c = new InitialContext();
        Context envCtx = (Context) c.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/data");
        return "ADMIN Ok";
    }

    @GET
    @Path("/adm/products")
    public String listProducts() throws Exception {
        Order o = new Order();
        o.setId((int) Math.random());
        o.setName("sdcsc");
        Product p = new Product();
        p.name = "scsdc";
        p.enabled = true;
        p.category = Product.ProductCategory.BOOKBOX;
        PersistenceManager.saveEntity(o);
        PersistenceManager.saveEntity(p);
        return "ADMIN Okkkk";
    }

}
