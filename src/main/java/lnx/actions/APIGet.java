package lnx.actions;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lnx.panache.QuackMeme;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class APIGet {

    public Uni<QuackMeme> get(){
        
        return QuackMeme
                .count()
                .onItem()
                .transformToUni(count -> {
                    long randomId = (long) (Math.random() * count) + 1;
                    return QuackMeme.findById(randomId);
        });
    }

    public Uni<byte[]> getImage(String imageName) {
        String imagePath = "images/" + imageName;
        File imageFile = new File(imagePath);
        String absolutePath = imageFile.getAbsolutePath();
        
        return Uni.createFrom()
            .item(() -> {
                byte[] bytes = null;
                try {
                    bytes = Files.readAllBytes(Paths.get(absolutePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bytes;
        });
    }
}
