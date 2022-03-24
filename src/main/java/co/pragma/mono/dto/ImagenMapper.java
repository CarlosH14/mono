package co.pragma.mono.dto;

import org.mapstruct.Mapper;

import co.pragma.mono.model.Imagen;

@Mapper
public interface ImagenMapper {

    ImagenDTO ImagenToImagenDTO(Imagen imagen);
    Imagen ImagenDTOToImagen (ImagenDTO imagenDTO);
    
}
