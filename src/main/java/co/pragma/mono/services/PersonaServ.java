package co.pragma.mono.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;

import co.pragma.mono.dto.PersonaDTO;
import co.pragma.mono.dto.PersonaMapper;
import co.pragma.mono.model.Imagen;
import co.pragma.mono.model.ImagenMongo;
import co.pragma.mono.model.Persona;
import co.pragma.mono.repository.ImagenRepo;
import co.pragma.mono.repository.PersonaRepo;
import co.pragma.mono.repository.toMongo;

@Service
public class PersonaServ {

    // Repositories
    @Autowired
    PersonaRepo personaRepo;
    @Autowired
    ImagenRepo imagenRepo;
    @Autowired
    toMongo mongoRepo;
    // Mapper
    private PersonaMapper personaMapper = Mappers.getMapper(PersonaMapper.class);
    
    // -------------------List all-------------------------------------
    public ArrayList<Persona> getAllPersona() throws Exception{
        List<Persona> pAux = personaRepo.findAll();
        if (pAux.isEmpty()) {
            throw new Exception("Table Persona is empty");
        }
        return (ArrayList<Persona>) pAux;
    }
    // ----------------------------------------------------------------
    // -------------------Save new-------------------------------------
    public Persona savePersona(PersonaDTO pDTO) throws Exception{
        Persona p = personaMapper.PersonaDTOToPersona(pDTO);
        if(p.getNombre() == null || p.getNombre().equals("")){
            throw new MissingServletRequestParameterException("Nombre", "non empty name");
        }
        if(p.getApellido() == null || p.getApellido().equals("")){
            throw new MissingServletRequestParameterException("Apellido", "non empty last name");
        }
        if(p.getCiudad() == null || p.getCiudad().equals("")){
            throw new MissingServletRequestParameterException("Ciudad", "non empty city");
        }
        if(p.getEdad() <= 17 || p.getEdad() >= 89){
            throw new MissingServletRequestParameterException("Edad", "number between 17 and 89");
        }
        if(p.getTipoid()== null || p.getTipoid().equals("")){
            throw new MissingServletRequestParameterException("Tipo ID", "non empty ID type");
        }
        if(String.valueOf(p.getNumid()).length() < 5 || String.valueOf(p.getNumid()).length() > 11 ){
            throw new MissingServletRequestParameterException("N??mero ID", "ID with at least 5 numbers");
        }
        return personaRepo.save(p);
    }
    // ----------------------------------------------------------------
    // -------------------Update---------------------------------------
    public Persona updatePersona(int id, PersonaDTO pDTO) throws Exception{
        pDTO.setId(id);
        
        Optional<Persona> pAux = personaRepo.findById(id);

        if (!pAux.isPresent()) {
            throw new Exception("Persona with id " + id + " not found");
        }

        if(pDTO.getNombre() == null || pDTO.getNombre().equals("")){
            pDTO.setNombre(pAux.get().getNombre());
        }
        if(pDTO.getApellido() == null || pDTO.getApellido().equals("")){
            pDTO.setApellido(pAux.get().getApellido());
        }
        if(pDTO.getCiudad() == null || pDTO.getCiudad().equals("")){
            pDTO.setCiudad(pAux.get().getCiudad());
        }
        if(pDTO.getEdad() <= 17){
            pDTO.setEdad(pAux.get().getEdad());
        }
        if(pDTO.getEdad() >= 89){
            pDTO.setEdad(pAux.get().getEdad());
        }
        if(pDTO.getTipoid()== null || pDTO.getTipoid().equals("")){
            pDTO.setTipoid(pAux.get().getTipoid());
        }
        pDTO.setImg(pAux.get().getImg());
        if(pDTO.getImg() != null){
            pDTO.getImg().setImg(pDTO.getNombre()+" "+pDTO.getApellido());
            ImagenMongo mongoAux = mongoRepo.findById(Integer.toString(pDTO.getImg().getId())).get();
            mongoAux.setImagen(pDTO.getImg());
            mongoRepo.save(mongoAux);
        }
        
        if(String.valueOf(pDTO.getNumid()).length() < 5 || String.valueOf(pDTO.getNumid()).length() > 11 ){
            pDTO.setNumid(pAux.get().getNumid());
        }

        Persona p = personaMapper.PersonaDTOToPersona(pDTO);
        if(id <= 0){
            throw new Exception("ID must be greater than 0");
        }else{
            p.setId(id);
        }
        if(p.getNombre() == null || p.getNombre().equals("")){
            throw new Exception("Name must be provided");
        }
        if(p.getApellido() == null || p.getApellido().equals("")){
            throw new Exception( "Last name must be provided");
        }
        if(p.getCiudad() == null || p.getCiudad().equals("")){
            throw new Exception("City must be provided");
        }
        if(p.getEdad() <= 17){
            throw new Exception("Age value must be greater than 17");
        }
        if(p.getEdad() >= 89){
            throw new Exception("Age value must be lower than 89");
        }
        if(p.getTipoid()== null || p.getTipoid().equals("")){
            throw new Exception( "ID Type must be provided");
        }
        if(String.valueOf(p.getNumid()).length() < 5 || String.valueOf(p.getNumid()).length() > 11 ){
            throw new Exception("ID must contain at least 5 numbers");
        }
        
        return personaRepo.save(p);
    }
    // ----------------------------------------------------------------
    // -------------------Get by id {table}----------------------------
    public Persona getbyId(int id) throws Exception{
        Optional<Persona> pAux = personaRepo.findById(id);
        if (!pAux.isPresent() ) {
            throw new Exception("Persona with id " + id + " not found");
        }
        return pAux.get();
    }
    // ----------------------------------------------------------------
    // -------------Get by age {/edad?edad=#}--------------------------
    public ArrayList<Persona> findbyEdad(int edad) throws Exception{
        ArrayList<Persona> pAux = personaRepo.findByEdadGreaterThanEqual(edad);
        if (pAux.isEmpty()) {
            throw new Exception("Personas with age " + edad + " not found");
        }
        return pAux;
    }
    // --------------------------------------------------------------
    // ---------Get by id type {persona} {/type?id=xxxxx}------------
    public ArrayList<Persona> findByTipoid(String tipoid) throws Exception{
        ArrayList<Persona> pAux = personaRepo.findByTipoid(tipoid);
        if (pAux.isEmpty()) {
            throw new Exception("Personas with ID Type " + tipoid + " not found");
        }
        return personaRepo.findByTipoid(tipoid);
    }
    // ----------------------------------------------------------------
    // ----------Get by id {persona} {/id?id=#####}--------------------
    public ArrayList<Persona> findByNumeroid(Long numeroid) throws Exception{
        ArrayList<Persona> pAux = personaRepo.findByNumid(numeroid);
        if (pAux.isEmpty()) {
            throw new Exception("Persona with ID Number: " + numeroid + " not found");
        }
        return personaRepo.findByNumid(numeroid);
    }
    // ----------------------------------------------------------------
    // ------Get by type & number id {persona} {/XX&####}--------------
    public ArrayList<Persona> findByFullid(String requests) throws Exception{
        String[] req = requests.split("&");
        Long numeroid = Long.parseLong(req[1]);
        String tipoid = req[0];
        
        ArrayList<Persona> pAux = personaRepo.findByTipoidAndNumid(tipoid, numeroid);
        if (pAux.isEmpty()) {
            throw new Exception("Persona with ID " + numeroid + " and ID Type " + tipoid + " is not found");
        }
        return personaRepo.findByTipoidAndNumid(tipoid, numeroid);
    }
    // ----------------------------------------------------------------
    // -------------------Delete by id {table}-------------------------
    public boolean deletePersona(int id) throws Exception{
        Optional<Persona> p = personaRepo.findById(id);
        Optional<Imagen> i = imagenRepo.findByPersonid(id);
        if(!p.isPresent()){
            throw new Exception("Persona with ID " + id + " not found");
        }
        if (i.isPresent()){
            boolean deleted = true;
            try {
                imagenRepo.deleteById(p.get().getImg().getId());
                deleted = true;
            } catch (Exception e) {
                System.out.println(e);
                deleted = false;
            }
            mongoRepo.deleteById(Integer.toString(p.get().getImg().getId()));
            if(deleted){
                System.out.println("Successfully deleted image");
            }else{
                System.out.println("Failed to delete image");
                throw new Exception("Image with ID " + Integer.toString(p.get().getImg().getId()) + " not found to be deleted");
            }
        }
        try{
            personaRepo.deleteById(id);
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }    
    }

}