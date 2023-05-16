package lnx;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import jakarta.ws.rs.GET;

@Path("/")

public class FEResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public File get(){
        String filePath = "path";
        return new File(filePath);
    }   
}
