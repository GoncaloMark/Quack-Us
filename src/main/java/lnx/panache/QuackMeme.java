package lnx.panache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity
public class QuackMeme extends PanacheEntity {

    @Column(length = 200, unique = true)
    public String imageUrl;
}

