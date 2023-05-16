package lnx;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lnx.panache.QuackMeme;

@Path("/api")
public class APIResource {

    @Inject
    APIGet getActions;

    @Inject
    APIPost postActions;

    @GET
    @Path("/get/{id}")
    public Uni<QuackMeme> get(@PathParam("id") Long id) {
        return getActions.get(id);
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Response> create(@RestForm("image") FileUpload form){
        String fileName = form.fileName();

        if (!postActions.isAllowedFileType(fileName)) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Only JPEG and PNG files are allowed")
                    .build());
        }

        QuackMeme meme = new QuackMeme();
        meme.imageUrl = fileName;

        return postActions.create(meme)
            .flatMap(uri -> postActions.saveFile(form, fileName))
            .onItem().transform(uri -> Response.created(uri).build())
            .onFailure().recoverWithItem(Response.serverError().build());
    }
}
