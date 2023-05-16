package lnx.actions;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lnx.panache.QuackMeme;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        return Uni.createFrom()
            .item(() -> {
                byte[] bytes = null;
                try {
                    Path imagePath = Paths.get("C:\\Users\\gmgon\\Desktop\\Desktop\\Quack-Us\\quackus\\src\\main\\resources\\META-INF\\resources\\images\\" + imageName);
                    bytes = Files.readAllBytes(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bytes;
        });
    }
}
