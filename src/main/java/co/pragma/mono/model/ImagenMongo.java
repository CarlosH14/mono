package co.pragma.mono.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class ImagenMongo {
    @Id
	private String id ;
	@Field
	private Imagen imagen;
    @Field
	private Binary photo;

    public ImagenMongo(Imagen imagen) {
        this.imagen = imagen;
    }
    public ImagenMongo(String id, Imagen imagen) {
        this.id = id;
        this.imagen = imagen;
    }
}
