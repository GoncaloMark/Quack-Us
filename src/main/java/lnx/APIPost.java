package lnx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lnx.panache.QuackMeme;

@ApplicationScoped
public class APIPost {
    public Uni<URI> create(QuackMeme meme) {
        return Panache.<QuackMeme>withTransaction(meme::persist)
                .map(Item -> URI.create("/get/" + Item.id));
    }

    public boolean isAllowedFileType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return fileExtension.equals("jpeg") || fileExtension.equals("jpg") || fileExtension.equals("png");
    }

    public Uni<URI> saveFile(FileUpload form, String fileName) {
        String targetDirectory = "quackus/src/main/resources/META-INF/resources/images";
        File outputFile = new File(targetDirectory, fileName);

    try (
        InputStream fileInputStream = Files.newInputStream(form.uploadedFile());
        FileOutputStream outputStream = new FileOutputStream(outputFile)
        ) {
        
        byte[] buffer = new byte[1024*4];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return Uni.createFrom().item(outputFile.toURI());
    } catch (Exception e) {
        e.printStackTrace();
        return Uni.createFrom().failure(e);
    }
    }
}
