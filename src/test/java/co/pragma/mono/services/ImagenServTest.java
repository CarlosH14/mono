package co.pragma.mono.services;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import co.pragma.mono.dto.ImagenDTO;
import co.pragma.mono.dto.ImagenMapper;
import co.pragma.mono.model.Imagen;
import co.pragma.mono.model.ImagenMongo;
import co.pragma.mono.model.Persona;
import co.pragma.mono.repository.ImagenRepo;
import co.pragma.mono.repository.PersonaRepo;
import co.pragma.mono.repository.toMongo;

public class ImagenServTest {
    @Mock
    private PersonaRepo personaRepo;
    @Mock
    private ImagenRepo imagenRepo;
    @Mock
    private toMongo mongoRepo;

    @InjectMocks
    private ImagenServ imagenServ;
    
    private Persona persona;
    private Imagen imagen;
    private ImagenMongo imagenMongo;
    private MultipartFile mFile;
    private ImagenMapper imagenMapper = Mappers.getMapper(ImagenMapper.class);
    

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        imagen = new Imagen(777,"NameTest LastNameTest");
        imagen.setId(999);
        persona = new Persona(777,"NameTest","LastNameTest","CityTest",77,"TypeTest",77777L,imagen);
        
        ArrayList<Persona> pArray = new ArrayList<Persona>();
        pArray.add(persona);
        File file = new File("C:/Users/carlos.hincapie/Downloads/DF11/11133.png");
        mFile = new MockMultipartFile("11133.png", Files.readAllBytes(Paths.get(file.getPath())));
        imagenMongo = new ImagenMongo("999",imagen);
        imagenMongo.setPhoto(new Binary(BsonBinarySubType.BINARY, mFile.getBytes()));
    }
    
    // ----------------------------------------------------------------
    // --------------------- Tests GE Methods -------------------------
    // ----------------------------------------------------------------
    
    @Test
    void testAddPhoto() throws IOException {

        assertEquals(imagenMongo, imagenServ.addPhoto(imagen, mFile)); 

    }

    @Test
    void testDeleteImagen() throws Exception {
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(imagenRepo.findById(imagen.getId())).thenReturn(Optional.of(imagen));
        when(mongoRepo.findById(Integer.toString(imagen.getId()))).thenReturn(Optional.of(imagenMongo));	
        doNothing().when(imagenRepo).deleteById(persona.getImg().getId());
        doNothing().when(mongoRepo).deleteById(Integer.toString(persona.getImg().getId()));
        assertEquals(Optional.of(imagen), imagenRepo.findById(imagen.getId()));
        assertTrue(imagenServ.deleteImagen(imagen.getId()));
    
    }

    @Test
    void testGetAllImagen() throws Exception {
        ArrayList<Imagen> iArrayList = new ArrayList<>(Arrays.asList(imagen)); 
        when(imagenRepo.findAll()).thenReturn(iArrayList);
        assertNotNull(imagenServ.getAllImagen());
    }

    @Test
    void testGetbyId() throws Exception {
        when(imagenRepo.findById(imagen.getId())).thenReturn(Optional.of(imagen));
        when(mongoRepo.findById(Integer.toString(imagen.getId()))).thenReturn(Optional.of(imagenMongo));
        assertEquals(Optional.of(imagen), imagenServ.getbyId(imagen.getId()));
        
    }

    @Test
    void testSaveImagen() throws Exception {
        when(imagenRepo.save(any(Imagen.class))).thenReturn(imagen);
        when(personaRepo.findById(imagen.getPersonid())).thenReturn(Optional.of(persona));
        when(personaRepo.save(any(Persona.class))).thenReturn(persona);
        ImagenDTO iDTO = imagenMapper.ImagenToImagenDTO(imagen);
        assertNotNull(imagenServ.saveImagen(iDTO, mFile));
    }

    @Test
    void testUpdateImagen() throws Exception {

        ImagenDTO iDTO = imagenMapper.ImagenToImagenDTO(imagen);
        Imagen imagen2 = new Imagen(777,"NameTest LastNameTest");
        ImagenDTO iDTO2 = imagenMapper.ImagenToImagenDTO(imagen2);
        File file2 = new File("C:/Users/carlos.hincapie/Downloads/DF11/10090.png");
        MultipartFile mFile2 = new MockMultipartFile("10090.png", Files.readAllBytes(Paths.get(file2.getPath())));
        ImagenMongo imagenMongo2 = new ImagenMongo("999",imagen2);
        imagenMongo2.setPhoto(new Binary(BsonBinarySubType.BINARY, mFile2.getBytes()));
        
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(imagenRepo.findById(imagen.getId())).thenReturn(Optional.of(imagen));
        when(mongoRepo.save(imagenMongo2)).thenReturn(imagenMongo2);
        when(personaRepo.save(persona)).thenReturn(persona);

        assertNotEquals(iDTO,iDTO2);
        assertEquals(imagen2,imagenServ.updateImagen(imagen.getId(),iDTO2, mFile2));
   
    }

    // ----------------------------------------------------------------
    // --------------------- Tests TEx Methods -------------------------
    // ----------------------------------------------------------------

    @Test
    void testGetAllImagenFail() throws Exception {
        ArrayList<Imagen> iArrayList = new ArrayList<>(Arrays.asList(imagen)); 
        iArrayList.clear();
        when(imagenRepo.findAll()).thenReturn(iArrayList);
        assertThrows(Exception.class, () -> imagenServ.getAllImagen());
    }

    @Test
    void testGetbyIdFail() throws Exception {
        Optional<Imagen> iAux = Optional.of(imagen);
        when(imagenRepo.findById(iAux.get().getId())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> imagenServ.getbyId(anyInt()));
    
    }

    @Test
    void testSaveImagenFail() throws Exception {

        Persona pAux = new Persona();
        ImagenDTO iDTO = imagenMapper.ImagenToImagenDTO(imagen);
        iDTO.setPersonid(pAux.getId());
        
        //when(imagenRepo.save(any(Imagen.class))).thenReturn(imagen);
        when(personaRepo.findById(imagen.getPersonid())).thenReturn(Optional.of(pAux));
        
        assertThrows(Exception.class, () -> imagenServ.saveImagen(iDTO, mFile));
   
    }

    @Test
    void testSaveImagenFail2() throws Exception {

        ImagenDTO pDTO = imagenMapper.ImagenToImagenDTO(imagen);
        File ifile = new File("C:/Users/carlos.hincapie/Downloads/DF11/vacio.png");
        ifile.createNewFile();
        MultipartFile imfile = new MockMultipartFile("11133.png", Files.readAllBytes(Paths.get(ifile.getPath())));
        persona.setImg(null);
        Optional<Persona> pPersona = Optional.of(persona);
        when(imagenRepo.save(any(Imagen.class))).thenReturn(imagen);
        when(personaRepo.findById(imagen.getPersonid())).thenReturn(pPersona);
        assertThrows(Exception.class, () -> imagenServ.saveImagen(pDTO, imfile));
   
    }

    @Test
    void testUpdateImagenFail() throws Exception {

        ImagenDTO iDTO = imagenMapper.ImagenToImagenDTO(imagen);
        Imagen imagen2 = new Imagen(0,"NameTest LastNameTest");
        ImagenDTO iDTO2 = imagenMapper.ImagenToImagenDTO(imagen2);
        File file2 = new File("C:/Users/carlos.hincapie/Downloads/DF11/10090.png");
        MultipartFile mFile2 = new MockMultipartFile("10090.png", Files.readAllBytes(Paths.get(file2.getPath())));
        ImagenMongo imagenMongo2 = new ImagenMongo("999",imagen2);
        imagenMongo2.setPhoto(new Binary(BsonBinarySubType.BINARY, mFile2.getBytes()));
        
        when(personaRepo.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(imagenRepo.findById(imagen.getId())).thenReturn(Optional.of(imagen));
        when(mongoRepo.save(imagenMongo2)).thenReturn(imagenMongo2);
        when(personaRepo.save(persona)).thenReturn(persona);

        assertNotEquals(iDTO,iDTO2);
        assertThrows(Exception.class, () -> imagenServ.updateImagen(imagen.getId(),iDTO2, mFile2));
   
    }
    
}
