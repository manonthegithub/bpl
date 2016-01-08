package ru.bookpleasure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Root {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "GOOOOOOOOOOOOOT iT!";
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/bpl")
    public String hello2() {
        return "GOOOOOOOOOOOOOT iT!";
    }

}
