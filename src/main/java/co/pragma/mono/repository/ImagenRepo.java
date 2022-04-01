package co.pragma.mono.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.pragma.mono.model.Imagen;

@Repository
public interface ImagenRepo extends JpaRepository<Imagen, Integer>{

    Optional<Imagen> findByPersonid(int id);



}
