package co.pragma.mono.dto;

import org.mapstruct.Mapper;
import co.pragma.mono.model.Persona;

@Mapper
public interface PersonaMapper {

    PersonaDTO PersonaToPersonaDTO(Persona persona);
    Persona PersonaDTOToPersona(PersonaDTO personaDTO);

}
