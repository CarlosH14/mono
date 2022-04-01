package co.pragma.mono.services;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import co.pragma.mono.model.Imagen;
import co.pragma.mono.model.ImagenMongo;
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
    private Imagen imagen;
    private ImagenMongo imagenMongo;
    private PersonaMapper personaMapper = Mappers.getMapper(PersonaMapper.class);
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        
        imagen = new Imagen(777,"NameTest LastNameTest");
        imagen.setId(999);
        persona = new Persona(777,"NameTest","LastNameTest","CityTest",77,"TypeTest",77777L,imagen);
        imagenMongo = new ImagenMongo("999",imagen,null);
        ArrayList<Persona> pArray = new ArrayList<Persona>();
        pArray.add(persona);
    }
    
    // ----------------------------------------------------------------
    // --------------------- Tests GE Methods -------------------------
    // ----------------------------------------------------------------
    @Test
    void testDeletePersona() throws Exception {   
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(imagenRepo.findById(persona.getImg().getId())).thenReturn(Optional.of(imagen));
        when(mongoRepo.findById(Integer.toString(persona.getImg().getId()))).thenReturn(Optional.of(imagenMongo));	
        doNothing().when(personaRepo).deleteById(persona.getId());
        doNothing().when(imagenRepo).deleteById(persona.getImg().getId());
        doNothing().when(mongoRepo).deleteById(Integer.toString(persona.getImg().getId()));
        assertEquals(Optional.of(imagen), imagenRepo.findById(imagen.getId()));
        assertTrue(personaServ.deletePersona(persona.getId()));
    }

    @Test
    void testFindByFullid() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        when(personaRepo.findByTipoidAndNumid(persona.getTipoid(),persona.getNumid())).thenReturn(pArrayList);
        String requests = persona.getTipoid()+"&"+persona.getNumid();
        assertNotNull(personaServ.findByFullid(requests));

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
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaNombreDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setNombre("");
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaNombreNullDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setNombre(null);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaApellidoDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setApellido("");
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaApellidoNullDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setApellido(null);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaCiudadDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setCiudad("");
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaCiudadNullDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setCiudad(null);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaEdadDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setEdad(0);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaEdad2DTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setEdad(150);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaTipoidDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setTipoid("");
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaTipoidNullDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setTipoid(null);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaNumidDTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setNumid(0L);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaNumid2DTO() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        pDTO2.setNumid(100000000000L);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertEquals(persona2,personaServ.updatePersona(persona2.getId(),pDTO2));
    }

    // ----------------------------------------------------------------
    // --------------------- Tests TEx Methods -------------------------
    // ----------------------------------------------------------------

    @Test
    void testDeletePersonaFail() throws Exception {
        //Optional<Persona> pAux = Optional.of(persona);
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.empty());
        doNothing().when(personaRepo).deleteById(anyInt());
        assertThrows(Exception.class, () -> personaServ.deletePersona(anyInt()));
    }

    @Test
    void testDeletePersonaFail2() throws Exception {
        //Optional<Imagen> iAux = Optional.of(persona.getImg());
        when(imagenRepo.findById(persona.getImg().getId())).thenReturn(Optional.empty());
        doNothing().when(personaRepo).deleteById(anyInt());
        assertThrows(Exception.class, () -> personaServ.deletePersona(anyInt()));
    }

    @Test
    void testFindByFullidFail() throws Exception {
        ArrayList<Persona> pAux = new ArrayList<>(Arrays.asList(persona));
        pAux.clear();
        when(personaRepo.findByTipoidAndNumid(persona.getTipoid(),persona.getNumid())).thenReturn(pAux);
        String requests = persona.getTipoid()+"&"+persona.getNumid();
        assertThrows(Exception.class, () -> personaServ.findByFullid(requests));
    }

    @Test
    void testFindByNumeroidFail() throws Exception {
        ArrayList<Persona> pAux = new ArrayList<>(Arrays.asList(persona));
        pAux.clear();
        when(personaRepo.findByNumid(persona.getNumid())).thenReturn(pAux);
        assertThrows(Exception.class, () -> personaServ.findByNumeroid(anyLong()));
    }

    @Test
    void testFindByTipoidFail() throws Exception {
        ArrayList<Persona> pAux = new ArrayList<>(Arrays.asList(persona));
        pAux.clear();
        when(personaRepo.findByTipoid(persona.getTipoid())).thenReturn(pAux);
        assertThrows(Exception.class, () -> personaServ.findByTipoid(anyString()));
    }

    @Test
    void testFindbyEdadFail() throws Exception {
        ArrayList<Persona> pAux = new ArrayList<>(Arrays.asList(persona));
        pAux.clear();
        when(personaRepo.findByEdadGreaterThanEqual(persona.getEdad())).thenReturn(pAux);
        assertThrows(Exception.class, () -> personaServ.findbyEdad(anyInt()));

    }

    @Test
    void testGetAllPersonaFail() throws Exception {
        ArrayList<Persona> pArrayList = new ArrayList<>(Arrays.asList(persona)); 
        pArrayList.clear();
        when(personaRepo.findAll()).thenReturn(pArrayList);
        assertThrows(Exception.class, () -> personaServ.getAllPersona());
    }

    @Test
    void testGetbyIdFail() throws Exception{
        Optional<Persona> pAux = Optional.of(persona);
        when(personaRepo.findById(pAux.get().getId())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> personaServ.getbyId(anyInt()));
    }

    @Test
    void testSavePersonaFailNombre() throws Exception {
        persona.setNombre(null);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
        
    }

    @Test
    void testSavePersonaFailNombre2() throws Exception {
        persona.setNombre("");
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
        
    }

    @Test
    void testSavePersonaFailApellido() throws Exception {
        persona.setApellido(null);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailApellido2() throws Exception {
        persona.setApellido("");
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailEdad() throws Exception {
        persona.setEdad(0);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailEdad2() throws Exception {
        persona.setEdad(150);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailCiudad() throws Exception {
        persona.setCiudad(null);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailCiudad2() throws Exception {
        persona.setCiudad("");
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailTipoid() throws Exception {
        persona.setTipoid(null);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailTipoid2() throws Exception {
        persona.setTipoid("");
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailNumid() throws Exception {
        persona.setNumid(100000000000L);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testSavePersonaFailNumid2() throws Exception {
        persona.setNumid(0L);
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        assertThrows(Exception.class, () -> personaServ.savePersona(pDTO));
    }

    @Test
    void testUpdatePersonaFail() throws Exception {
        Persona persona2 = new Persona(0,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailNombre() throws Exception {
        Persona persona2 = new Persona(777,"","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailNombre2() throws Exception {
        Persona persona2 = new Persona(777,null,"LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailApellido() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailApellido2() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2",null,"CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailCiudad() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2",null,88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailCiudad2() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailEdad() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",0,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailEdad2() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",150,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailTipoid() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"",88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailTipoid2() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,null,88888L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailNumid() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",0L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailNumid2() throws Exception {
        Persona persona2 = new Persona(777,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",100000000000L,imagen);
        PersonaDTO pDTO = personaMapper.PersonaToPersonaDTO(persona);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        when(personaRepo.findById(persona2.getId())).thenReturn(Optional.of(persona2));
        when(imagenRepo.save(imagen)).thenReturn(imagen);
        when(mongoRepo.findById(Integer.toString(pDTO2.getImg().getId()))).thenReturn(Optional.of(imagenMongo));
        when(mongoRepo.save(imagenMongo)).thenReturn(imagenMongo);
        when(personaRepo.save(persona2)).thenReturn(persona2);
        assertFalse(pDTO.equals(pDTO2));
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

    @Test
    void testUpdatePersonaFailPresent() throws Exception {
        Persona persona2 = new Persona(888,"NameTest2","LastNameTest2","CityTest2",88,"TypeTest2",88888L,imagen);
        PersonaDTO pDTO2 = personaMapper.PersonaToPersonaDTO(persona2);
        //Optional<Persona> pAux = Optional.of(persona2);
        
        when(personaRepo.findById(pDTO2.getId())).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> personaServ.updatePersona(persona.getId(),pDTO2));
    }

}
