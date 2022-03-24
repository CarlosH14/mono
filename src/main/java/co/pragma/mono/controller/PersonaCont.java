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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.pragma.mono.dto.PersonaDTO;
import co.pragma.mono.model.Persona;
import co.pragma.mono.services.ImagenServ;
import co.pragma.mono.services.PersonaServ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/persona")
public class PersonaCont {

    // Services
    @Autowired
    PersonaServ serv;
    @Autowired
    ImagenServ iserv;

    // -------------------List all-------------------------------------
    @Operation(summary = "List all Personas in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK", content = @Content),
        @ApiResponse(responseCode = "403", description = "Table Persona is empty", content = @Content),
    })
    @GetMapping
    public ResponseEntity<ArrayList<Persona>> getAllPersona() throws Exception {
        return new ResponseEntity<ArrayList<Persona>>(serv.getAllPersona(), (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // -------------------Save new-------------------------------------
    @Operation(summary = "Save a new Persona in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED", content = @Content),
        @ApiResponse(responseCode = "400", description = "Information no provided", content = @Content),
    })
    @PostMapping
    public ResponseEntity<Persona> savePersona(@RequestBody PersonaDTO pDTO) throws Exception{
        
        Persona p = serv.savePersona(pDTO);
        return new ResponseEntity<Persona>(p, (HttpStatus.CREATED));
    }
    // ----------------------------------------------------------------
    // -------------------Update---------------------------------------
    @Operation(summary = "Update a Persona")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "ACCEPTED", content = @Content),
        @ApiResponse(responseCode = "400", description = "Information no provided", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona not found", content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable("id") int id, @RequestBody PersonaDTO pDTO) throws java.lang.Exception {
        
        pDTO.setId(id);
        Persona p =  serv.updatePersona(pDTO);
        return new ResponseEntity<Persona>(p, (HttpStatus.ACCEPTED));
    }
    // ----------------------------------------------------------------
    // -------------------Get by id {table}----------------------------
    @Operation(summary = "Get a Persona by his Table ID")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Persona> getbyId(@PathVariable("id") int id) throws java.lang.Exception {
        Persona pAux = serv.getbyId(id);
        return new ResponseEntity<Persona>(pAux, (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // -------------Get by age {/edad?edad=#}--------------------------
    @Operation(summary = "Get Personas by age")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona with age greater or equal not found", content = @Content),
    })
    @GetMapping("/edad")
    public ResponseEntity<ArrayList<Persona>> findbyEdad(@RequestParam int edad) throws java.lang.Exception {
        ArrayList<Persona> pAux = serv.findbyEdad(edad);
        return new ResponseEntity<ArrayList<Persona>>(pAux, (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // -------------------Delete by id {table}-------------------------
    @Operation(summary = "Delete Personas by his Table ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Failed to delete", content = @Content),
    })
    @DeleteMapping(path="/{id}")
    public String deletePersona(@PathVariable("id") int id) throws Exception{
        boolean aux = serv.deletePersona(id);
        if(aux){
            return "Successfully deleted: " + id;
        }else{
            return "Failed to delete: " + id;   
        }
    }
    // ----------------------------------------------------------------
    // ----------Get by id {persona} {/id?id=#####}--------------------
    @Operation(summary = "Get Personas by his Number ID")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona with ID number not found", content = @Content),
    })
    @GetMapping("/id")
    public ResponseEntity<ArrayList<Persona>> findByNumeroid(@RequestParam int id) throws java.lang.Exception {
        ArrayList<Persona> pAux = serv.findByNumeroid(id);
        return new ResponseEntity<ArrayList<Persona>>(pAux, (HttpStatus.FOUND));
    }
    // --------------------------------------------------------------
    // ---------Get by id type {persona} {/type?id=xxxxx}------------
    @Operation(summary = "Get Personas by his ID Type")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona with that ID Type not found", content = @Content),
    })
    @GetMapping("/type")
    public ResponseEntity<ArrayList<Persona>> findByTipoid(@RequestParam String id) throws java.lang.Exception {
        ArrayList<Persona> pAux = serv.findByTipoid(id);
        return new ResponseEntity<ArrayList<Persona>>(pAux, (HttpStatus.FOUND));
    }
    // ----------------------------------------------------------------
    // ------Get by type & number id {persona} {/XX&####}--------------
    @Operation(summary = "Get Personas by his Number ID and ID Type")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "FOUND", content = @Content),
        @ApiResponse(responseCode = "403", description = "Persona with that information is not found", content = @Content),
    })
    @GetMapping("/p/{requests}")
    public ResponseEntity<ArrayList<Persona>> findByFullid(@PathVariable("requests") String requests) throws java.lang.Exception{
        String[] req = requests.split("&");
        Integer numid = Integer.parseInt(req[1]);
        String tipoid = req[0];
        ArrayList<Persona> pAux = serv.findByFullid(tipoid, numid);
        return new ResponseEntity<ArrayList<Persona>>(pAux, (HttpStatus.FOUND));
    }

}