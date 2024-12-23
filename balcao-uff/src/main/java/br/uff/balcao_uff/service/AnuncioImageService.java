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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            log.info("Caminho de Imagem Configurado: " + caminhoImagem);

            if (!arquivo.isEmpty()) {
                byte[] bytes = arquivo.getBytes();
                String uniqueID = UUID.randomUUID().toString();
                Path caminho = Paths.get(caminhoImagem, uniqueID + "_" + arquivo.getOriginalFilename());
                Path parentDir = caminho.getParent();

                log.info("Caminho para salvar a imagem: " + caminho.toString());
                log.info("Verificando se o diretório existe: " + parentDir.toString());

                if (!Files.exists(parentDir)) {
                    log.info("Diretório não existe. Criando diretório: " + parentDir.toString());
                    Files.createDirectories(parentDir);
                }

                log.info("Escrevendo bytes no caminho: " + caminho.toString());
                Files.write(caminho, bytes);

                entity.setImagePath(caminho.toString());
                log.info("Imagem salva com sucesso no caminho: " + caminho.toString());
            }
        } catch (Exception e) {
            log.error("Erro ao salvar a imagem", e);
            throw new RuntimeException("Erro ao salvar imagem: " + caminhoImagem + "/" + arquivo.getOriginalFilename(), e);
        }

        entity.setAnuncio(anuncioRepository.findById(anuncioId).orElseThrow(() -> new RuntimeException("Anúncio não encontrado")));

        AnuncioImageEntity entitySave = repository.save(entity);
        log.info("AnuncioImageEntity salvo com sucesso com ID: " + entitySave.getId());

        return entitySave;
    }
}

