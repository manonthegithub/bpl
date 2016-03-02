package ru.bookpleasure;

import net.iharder.Base64;
import ru.bookpleasure.beans.ProductBean;
import ru.bookpleasure.db.entities.Product;
import ru.bookpleasure.models.ProductView;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.*;
import java.util.List;

@Path("/")
public class Root {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "It works!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
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

        return "ADMIN Ok";
    }

    @GET
    @Path("/boxes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductView> listEnabledProducts(){
        ProductBean pb = new ProductBean();
        List<ProductView> result = pb.getEnabledProductByCategory(Product.ProductCategory.BOOKBOX.toString());
        return result;
    }

    @GET
    @Path("/adm/boxes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductView> listProducts(){
        ProductBean pb = new ProductBean();
        List<ProductView> result = pb.getProductByCategory(Product.ProductCategory.BOOKBOX.toString());
        return result;
    }

    @POST
    @Path("/adm/boxes/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveProduct(@Context ServletContext context, ProductView productView) throws IOException{
        if(productView.getAvailableNumber() == null){
            productView.setAvailableNumber(productView.getQuantity());
        }
        OutputStream file = null;
        File imagePath = Paths.get(context.getRealPath("/img/products")).toFile();
        imagePath.mkdirs();
        File image = new File(imagePath,productView.getImageLink());
        if(!image.createNewFile()) return Response.status(400).entity("Файл с таким именем уже существует").build();
        try {
            file = new BufferedOutputStream(new FileOutputStream(image));
            file.write(Base64.decode(productView.getBase64ImageFile()));
            file.flush();
        }finally {
            if (file != null) file.close();
        }
        ProductBean pb = new ProductBean();
        pb.saveProduct(productView);
        return Response.ok().build();
    }

}
