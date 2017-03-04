package ru.fgv.testmvc;

import com.oracle.ozark.core.Models;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author prusakovan
 */
@Path("name")
public class MvcResource {
    @Context
    private UriInfo context;

    @Inject Models m;

    /**
     * Creates a new instance of MvcResource
     */
    public MvcResource() {
    }

    /**
     * Retrieves representation of an instance of ru.fgv.testmvc.MvcResource
     *
     * @param n
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET  
    @Controller
    public String getName(@QueryParam("name") String n) {
        String message = "Hello " + n;
        m.put("message", message);
        return "hello.xhtml";
    }

}
