package co.pragma.mono.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.pragma.mono.dto.ImagenDTO;
import co.pragma.mono.dto.ImagenMapper;
import co.pragma.mono.model.Imagen;
import co.pragma.mono.model.ImagenMongo;
import co.pragma.mono.model.Persona;
import co.pragma.mono.repository.ImagenRepo;
import co.pragma.mono.repository.PersonaRepo;
import co.pragma.mono.repository.toMongo;

@Service
public class ImagenServ {

    // Repositories
    @Autowired
    ImagenRepo imagenRepo;
    @Autowired
    PersonaRepo personaRepo;
    @Autowired
    toMongo mongoRepo;

    // Mapper
    private ImagenMapper imagenMapper= Mappers.getMapper(ImagenMapper.class);;
   
    // -------------------List all-------------------------------------
    public ArrayList<Imagen> getAllImagen() throws Exception{
        List<Imagen> pAux = imagenRepo.findAll();
        if (pAux.isEmpty()) {
            throw new Exception("Table Imagen is empty");
        }
        return (ArrayList<Imagen>) imagenRepo.findAll();
    }
    // ----------------------------------------------------------------
    // -------------------Save new-------------------------------------
    public Imagen saveImagen(ImagenDTO pDTO, MultipartFile file) throws Exception{
        Imagen p = imagenMapper.ImagenDTOToImagen(pDTO);
        Optional<Persona> person = personaRepo.findById(p.getPersonid());
        p.setImg(person.get().getNombre() + " " + person.get().getApellido());
        if(p.getImg() == null){
            throw new Exception("Imagen information must be provided");
        }
        
        if(person.isEmpty()){
            throw new Exception("Persona does not exist");
        }
        if(file.isEmpty()){
            throw new Exception("Photo must be provided");
        }
        person.get().setImg(p);
        //PersonaDTO personDTO = personaMapper.PersonaToPersonaDTO(person.get());
        Persona pAux = personaRepo.save(person.get());
        
        ImagenMongo imgToMongo = new ImagenMongo(Integer.toString((pAux.getImg()).getId()), pAux.getImg());
        imgToMongo = addPhoto(pAux.getImg(), file);
        mongoRepo.save(imgToMongo);
        //p = imagenRepo.save(p);
        return person.get().getImg();
    }
    // ----------------------------------------------------------------
    // -------------------Save in Mongo-------------------------------------
    public ImagenMongo addPhoto(Imagen p, MultipartFile file) throws IOException { 
        ImagenMongo photo = new ImagenMongo(Integer.toString(p.getId()), p); 
        photo.setPhoto(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
          return photo; 
    }
    // ----------------------------------------------------------------
    // -------------------Update---------------------------------------
    public Imagen updateImagen(int id, ImagenDTO pDTO, MultipartFile file) throws Exception{
        Imagen p = imagenMapper.ImagenDTOToImagen(pDTO);
        Optional<Persona> person = personaRepo.findById(p.getPersonid());
        if (!person.isPresent()) {
            throw new Exception("Persona with id " + p.getPersonid() + " not found");
        }
        if(person.get().getImg() == null){
            throw new Exception("Persona with id " + p.getPersonid() + " does not have an Imagen");
        }
        p.setImg(person.get().getNombre() + " " + person.get().getApellido());

        if(!imagenRepo.findById(id).isPresent()){
            throw new Exception("Imagen with id " + id + "not found");
        }
        if(p.getImg() == null){
            throw new Exception("Imagen information must be provided");
        }
        
        if(person.isEmpty()){
            throw new Exception("Persona does not exist");
        }
        person.get().setImg(p);
        //PersonaDTO personDTO = personaMapper.PersonaToPersonaDTO(person.get());
        Persona pAux = personaRepo.save(person.get());
        if(file.isEmpty()){
            throw new Exception("Photo must be provided");
        }
        ImagenMongo imgToMongo = new ImagenMongo(Integer.toString((pAux.getImg()).getId()), pAux.getImg());
        imgToMongo = addPhoto(pAux.getImg(), file);
        mongoRepo.save(imgToMongo);
        //p = imagenRepo.save(p);
        return person.get().getImg();
    }
    // ----------------------------------------------------------------
    // -------------------Get by id {table}----------------------------
    public Optional<Imagen> getbyId(int id) throws Exception{
        Optional<Imagen> pAux = imagenRepo.findById(id);
        if (!pAux.isPresent()) {
            throw new Exception("Imagen with id " + id + " not found");
        }
        ImagenMongo imongo = mongoRepo.findById(Integer.toString(imagenRepo.findById(id).get().getId())).get();
        System.out.println(imongo);
        return imagenRepo.findById(id);
    }
    // ----------------------------------------------------------------
    // -------------------Delete by id {table}-------------------------
    public boolean deleteImagen(int id) throws Exception{
        Imagen imagen = imagenRepo.findById(id).get();
        Persona person = personaRepo.findById(imagen.getPersonid()).get();
        person.setImg(null);
        //PersonaDTO personDTO = personaMapper.PersonaToPersonaDTO(person);
        person = personaRepo.save(person);
        ImagenMongo imgToMongo = mongoRepo.findById(Integer.toString(imagen.getId())).get();
        mongoRepo.delete(imgToMongo);
        try {
            imagenRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
