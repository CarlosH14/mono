package co.pragma.mono.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    int id;
    @Column(name = "nombre", nullable = false)
    String nombre;
    @Column(name = "apellido", nullable = false)
    String apellido;
    @Column(name = "ciudad", nullable = false)
    String ciudad;
    @Column(name = "edad", nullable = false)
    int edad;
    @Column(name = "tipoid", nullable = false)
    String tipoid;
    @Column(name = "numid", nullable = false)
    Long numid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "img")
    private Imagen img;
    
    public Persona(String nombre, String apellido, String ciudad, int edad, String tipoid, Long numid) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.edad = edad;
        this.tipoid = tipoid;
        this.numid = numid;
    }
    
}
