package co.pragma.mono.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
    
    // ----------------------------------------------------------------
    // --------------------- Tests GE Methods -------------------------
    // ----------------------------------------------------------------
    @Test
    void testDeletePersona() throws Exception {
        doNothing().when(personaRepo).deleteById(anyInt());
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        assertNotNull(personaServ.deletePersona(persona.getId()));
    }

    @Test
    void testFindByFullid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByTipoidAndNumid(persona.getTipoid(),persona.getNumid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByFullid(persona.getTipoid(),persona.getNumid()));

    }

    @Test
    void testFindByNumeroid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByNumid(persona.getNumid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByNumeroid(persona.getNumid()));

    }

    @Test
    void testFindByTipoid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByTipoid(persona.getTipoid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByTipoid(persona.getTipoid()));
    }

    @Test
    void testFindbyEdad() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByEdadGreaterThanEqual(persona.getEdad())).thenReturn(pArrayList);
        assertNotNull(personaServ.findbyEdad(persona.getEdad()));

    }

    @Test
    void testGetAllPersona() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findAll()).thenReturn(pArrayList);
        assertNotNull(personaServ.getAllPersona());
    }

    @Test
    void testGetbyId() throws Exception{
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        assertEquals(persona, personaServ.getbyId(persona.getId()));
        
    }

    @Test
    void testSavePersona() throws Exception {
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertNotNull(personaServ.savePersona(pDTO));
    }

    @Test
    void testUpdatePersona() throws Exception {
        Persona persona2 = persona;
        persona2.setNombre("NameTest2");
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        assertNotNull(personaServ.updatePersona(pDTO));
    }

    // ----------------------------------------------------------------
    // --------------------- Tests TEx Methods -------------------------
    // ----------------------------------------------------------------

    @Test
    void testDeletePersona() throws Exception {
        doNothing().when(personaRepo).deleteById(anyInt());
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        assertNotNull(personaServ.deletePersona(persona.getId()));
    }

    @Test
    void testFindByFullid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByTipoidAndNumid(persona.getTipoid(),persona.getNumid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByFullid(persona.getTipoid(),persona.getNumid()));

    }

    @Test
    void testFindByNumeroid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByNumid(persona.getNumid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByNumeroid(persona.getNumid()));

    }

    @Test
    void testFindByTipoid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByTipoid(persona.getTipoid())).thenReturn(pArrayList);
        assertNotNull(personaServ.findByTipoid(persona.getTipoid()));
    }

    @Test
    void testFindbyEdad() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByEdadGreaterThanEqual(persona.getEdad())).thenReturn(pArrayList);
        assertNotNull(personaServ.findbyEdad(persona.getEdad()));

    }

    @Test
    void testGetAllPersona() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findAll()).thenReturn(pArrayList);
        assertNotNull(personaServ.getAllPersona());
    }

    @Test
    void testGetbyId() throws Exception{
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        assertEquals(persona, personaServ.getbyId(persona.getId()));
        
    }

    @Test
    void testSavePersona() throws Exception {
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertNotNull(personaServ.savePersona(pDTO));
    }

    @Test
    void testUpdatePersona() throws Exception {
        Persona persona2 = persona;
        persona2.setNombre("NameTest2");
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        assertNotNull(personaServ.updatePersona(pDTO));
    }

}
