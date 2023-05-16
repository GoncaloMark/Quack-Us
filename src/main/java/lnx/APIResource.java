package lnx;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lnx.actions.APIGet;
import lnx.actions.APIPost;
import lnx.panache.QuackMeme;

@Path("/api")
public class APIResource {

    @Inject
    APIGet getActions;

    @Inject
    APIPost postActions;

    @GET
    @Path("/get")
    public Uni<Response> get() {
        return getActions.get()
            .onItem()
            .transformToUni(result -> getActions.getImage(result.imageUrl))
            .onItem()
            .transform(imageBytes -> Response.ok(imageBytes, MediaType.APPLICATION_OCTET_STREAM)
                                    .header("Content-Disposition", "attachment; filename=\"image.jpg\"")
                                    .build())
            .onFailure().recoverWithItem(failure -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Response> create(@RestForm("form") FileUpload form){
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
            .onItem().transform(uri -> Response.ok().build())
            .onFailure().recoverWithItem(Response.serverError().build());
    }
}
