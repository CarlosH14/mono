package co.pragma.mono.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.pragma.mono.model.Persona;

@Repository
public interface PersonaRepo extends JpaRepository<Persona, Integer>{
    
        //ArrayList<Persona> findByEdad(int edad);
        //SpringDataQuery - Spring Data Derived Queries

        //@Query(value ="SELECT * FROM persona WHERE persona.tipoid = LOWER(?1)", nativeQuery = true)
        //ArrayList<Persona> findByTipoid(String tipoid);
        ArrayList<Persona> findByTipoid(String tipoid);

        //@Query(value ="SELECT * FROM persona WHERE persona.numid = ?1", nativeQuery = true)
        //ArrayList<Persona> findByNumid(int numeroid);
        ArrayList<Persona> findByNumid(int numid);

        //@Query(value ="SELECT * FROM persona WHERE persona.tipoid = ?1 AND persona.numid = ?2", nativeQuery = true)
        //ArrayList<Persona> findByFullid(String tipoid, int numeroid);
        ArrayList<Persona> findByTipoidAndNumid(String tipoid, int numid);


        //@Query(value ="SELECT * FROM persona WHERE persona.edad >= ?1", nativeQuery = true)
        //public abstract ArrayList<Persona> findByEdad(int age);
        ArrayList<Persona> findByEdadGreaterThanEqual(int age);

}
