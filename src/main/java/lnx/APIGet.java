package lnx;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lnx.panache.QuackMeme;

@ApplicationScoped
public class APIGet {
    public Uni<QuackMeme> get(Long id){
        return QuackMeme.findById(id);
    }
}
