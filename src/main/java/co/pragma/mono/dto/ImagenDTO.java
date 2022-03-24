package co.pragma.mono.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImagenDTO implements Serializable{
    
    int id;
    int personid;
    String img;
    public ImagenDTO(int personid, String img) {
        this.personid = personid;
        this.img = img;
    }

}
