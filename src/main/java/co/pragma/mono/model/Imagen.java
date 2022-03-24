package co.pragma.mono.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="imagen")
@Data
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    int id;
    @Column(name = "personid", nullable = false) 
    int personid;
    @Column(name = "img", nullable = false)
    String img;
    
    public Imagen(int personid, String img) {
        this.personid = personid;
        this.img = img;
    }

}
