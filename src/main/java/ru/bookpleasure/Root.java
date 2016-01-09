package ru.bookpleasure;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Root {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "GOOOOOOOOOOOOOT iT!";
    }


    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/contact")
    public String hello2(
            @FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("message") String message) {
        String subj = "Сообщение от " + name + " - " + email;
        MailAgent.sendMailFromRobot("info@bookpleasure.ru", subj, message);

        return "Ok";
    }

}
