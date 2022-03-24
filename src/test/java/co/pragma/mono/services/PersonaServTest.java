package co.pragma.mono.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.pragma.mono.dto.PersonaDTO;
import co.pragma.mono.dto.PersonaMapper;
import co.pragma.mono.model.Persona;
import co.pragma.mono.repository.ImagenRepo;
import co.pragma.mono.repository.PersonaRepo;
import co.pragma.mono.repository.toMongo;

public class PersonaServTest {
    
    @Mock
    private PersonaRepo personaRepo;
    @Mock
    private ImagenRepo imagenRepo;
    @Mock
    private toMongo mongoRepo;

    @InjectMocks
    private PersonaServ personaServ;
    
    private Persona persona;
    private PersonaMapper personaMapper = Mappers.getMapper(PersonaMapper.class);
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        persona = new Persona(777,"NameTest","LastNameTest","CityTest",77,"TypeTest",77777,null);

        ArrayList<Persona> pArray = new ArrayList<Persona>();
        pArray.add(persona);
    
    }
    
    @Test
    void testDeletePersona() {

    }

    @Test
    void testFindByFullid() {

    }

    @Test
    void testFindByNumeroid() {

    }

    @Test
    void testFindByTipoid() {

    }

    @Test
    void testFindbyEdad() {

    }

    @Test
    void testGetAllPersona() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findAll()).thenReturn(pArrayList);
        assertNotNull(personaServ.getAllPersona());
    }

    @Test
    void testGetbyId() throws Exception {
        when(personaRepo.findById(anyInt())).thenReturn(any(Optional.class));
        assert (personaServ.getbyId(anyInt())) != null;
    }

    @Test
    void testSavePersona() {
        
    }

    @Test
    void testUpdatePersona() {

    }
}
