package br.uff.balcao_uff.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.uff.balcao_uff.entity.AnuncioImageEntity;
import br.uff.balcao_uff.repository.AnuncioImageRepository;
import br.uff.balcao_uff.repository.AnuncioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnuncioImageService {

	@Autowired
	private AnuncioImageRepository repository;

	@Autowired
	private AnuncioRepository anuncioRepository;

	@Value("${app.caminhoImagem}") 
	private String caminhoImagem;

    @Transactional
    public AnuncioImageEntity save(Long anuncioId, AnuncioImageEntity entity, MultipartFile arquivo) {

        try {
            if (!arquivo.isEmpty()) {
                byte[] bytes = arquivo.getBytes();
                String uniqueID = UUID.randomUUID().toString();
                Path caminho = Paths.get(caminhoImagem + uniqueID + "_" + arquivo.getOriginalFilename());
                Path parentDir = caminho.getParent();
                if (!Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }
                Files.write(caminho, bytes);

                entity.setImagePath(caminhoImagem + uniqueID + "_" + arquivo.getOriginalFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        entity.setAnuncio(anuncioRepository.findById(anuncioId).orElseThrow(() -> new RuntimeException("Anúncio não encontrado")));

        AnuncioImageEntity entitySave = repository.save(entity);
        return entitySave;
    }
}
