package co.pragma.mono.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.pragma.mono.model.Imagen;
import co.pragma.mono.services.ImagenServ;
import co.pragma.mono.services.PersonaServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/imagen")
public class ImagenCont {

    // Services
    @Autowired
    ImagenServ imagenServ;
    @Autowired
    PersonaServ personaServ;
    
    // -------------------List all-------------------------------------
    @Operation(summary = "List all Imagenes in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Table Imagen is empty", content = @Content),
    })
    @GetMapping
    public ResponseEntity<ArrayList<Imagen>> getAllImagen() throws Exception {
        return new ResponseEntity<ArrayList<Imagen>>(imagenServ.getAllImagen(), (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // -------------------List all Mongo-------------------------------------
    @Operation(summary = "List all Imagenes in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Table Imagen is empty", content = @Content),
    })
    @GetMapping("/mongo")
    public ResponseEntity<ArrayList<Imagen>> getAllImagenMongo() throws Exception {
        return new ResponseEntity<ArrayList<Imagen>>(imagenServ.getAllImagenMongo(), (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    
    // -------------------Save new-------------------------------------
    @Operation(summary = "Save a new Imagen in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", content = @Content),
        @ApiResponse(responseCode = "400", description = "Information no provided", content = @Content),
    })
    @PostMapping
    public ResponseEntity<Imagen> saveImagen(@RequestParam("id") int id, @RequestParam("photo") MultipartFile file) throws Exception {
        Imagen i = imagenServ.saveImagen(id, file);
        return new ResponseEntity<Imagen>(i, (HttpStatus.CREATED));
    }
    // ----------------------------------------------------------------
    // -------------------Update---------------------------------------
    @Operation(summary = "Update a Imagen")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "ACCEPTED", content = @Content),
        @ApiResponse(responseCode = "403", description = "Imagen not found", content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Imagen> updateImagen(@PathVariable("id") int id, @RequestParam("personid") int personid, @RequestParam("photo") MultipartFile file) throws Exception {
        Imagen i = imagenServ.updateImagen(id, personid, file);
        return new ResponseEntity<Imagen>(i, (HttpStatus.CREATED));
    }
    // ----------------------------------------------------------------
    // -------------------Get by id {table}----------------------------
    @Operation(summary = "Get a Imagen by his Table ID")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Imagen not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Imagen> getbyId(@PathVariable("id") int id) throws Exception {
        Imagen iAux = imagenServ.getbyId(id).get();
        return new ResponseEntity<Imagen>(iAux, (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // -------------------Get by Mongo id {table}----------------------------
    @Operation(summary = "Get a Imagen by his Mongo Document ID")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Imagen not found", content = @Content),
    })
    @GetMapping("/mongo/{id}")
    public ResponseEntity<Imagen> getbyIdMongo(@PathVariable("id") String id) throws Exception {
        Imagen iAux = imagenServ.getbyIdMongo(id);
        return new ResponseEntity<Imagen>(iAux, (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    
    // -------------------Delete by id {table}-------------------------
    @Operation(summary = "Delete Imagen by his Table ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Failed to delete", content = @Content),
    })
    @DeleteMapping(path="/{id}")
    public String deleteImagen(@PathVariable("id") int id) throws Exception{
        
        boolean aux = imagenServ.deleteImagen(id);
        if(aux){
            return "Successfully deleted: " + id;
        }else{
            return "Failed to delete: " + id;   
        }
        
    }
    // ----------------------------------------------------------------
    
}
