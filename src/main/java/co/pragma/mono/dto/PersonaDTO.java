package co.pragma.mono.dto;

import java.io.Serializable;

import co.pragma.mono.model.Imagen;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonaDTO implements Serializable{

    int id;
    String nombre;
    String apellido;
    String ciudad;
    int edad;
    String tipoid;
    int numid;
    Imagen img;
    
    public PersonaDTO(String nombre, String apellido, String ciudad, int edad, String tipoid, int numid) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.edad = edad;
        this.tipoid = tipoid;
        this.numid = numid;
    }
}
