package br.uff.balcao_uff.commons.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import br.uff.balcao_uff.api.dto.request.AnuncioRequestDTO;
import br.uff.balcao_uff.api.dto.response.AnuncioResponseDTO;
import br.uff.balcao_uff.entity.AnuncioEntity;

/**
 * Classe utilitária para converter DTOs para entidades e vice-versa.
 */
public class AnuncioMapperUtil {
    private static final ModelMapper modelMapperSave = new ModelMapper();
    private static final ModelMapper modelMapperUpdate = new ModelMapper();

    static {
        // Configurando ModelMapper com estratégias de correspondência estrita
        modelMapperSave.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapperUpdate.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * Converte uma entidade AnuncioEntity para AnuncioResponseDTO.
     *
     * @param anuncio a entidade AnuncioEntity
     * @return o DTO AnuncioResponseDTO
     */
    public static AnuncioResponseDTO toResponseDTO(AnuncioEntity anuncio) {
        return modelMapperSave.map(anuncio, AnuncioResponseDTO.class);
    }

    /**
     * Converte um DTO AnuncioRequestDTO para AnuncioEntity (Salvar).
     * Ignora o ID durante o mapeamento.
     *
     * @param requestDTO o DTO AnuncioRequestDTO
     * @return a entidade AnuncioEntity
     */
    public static AnuncioEntity toEntity(AnuncioRequestDTO requestDTO) {
        AnuncioEntity entity = modelMapperSave.map(requestDTO, AnuncioEntity.class);
        entity.setId(null);
        return entity;
    }

    /**
     * Converte um DTO AnuncioRequestDTO para AnuncioEntity (Atualizar).
     * Considera o ID durante o mapeamento.
     *
     * @param requestDTO o DTO AnuncioRequestDTO
     * @param existingEntity a entidade existente
     * @return a entidade AnuncioEntity atualizada
     */
    public static AnuncioEntity toUpdateEntity(AnuncioRequestDTO requestDTO, AnuncioEntity existingEntity) {
        if (requestDTO == null || existingEntity == null) {
            return null;
        }

        // Mapeia o DTO para a entidade existente, preservando o ID
        modelMapperUpdate.map(requestDTO, existingEntity);

        return existingEntity;
    }
}
