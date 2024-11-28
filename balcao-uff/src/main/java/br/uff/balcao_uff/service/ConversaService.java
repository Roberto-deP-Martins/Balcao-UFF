package br.uff.balcao_uff.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.uff.balcao_uff.entity.AnuncioEntity;
import br.uff.balcao_uff.entity.ConversaEntity;
import br.uff.balcao_uff.entity.MessageEntity;
import br.uff.balcao_uff.entity.UserEntity;
import br.uff.balcao_uff.repository.AnuncioRepository;
import br.uff.balcao_uff.repository.ConversaRepository;
import br.uff.balcao_uff.repository.MessageRepository;
import br.uff.balcao_uff.repository.UserRepository;

@Service
public class ConversaService {

	@Autowired
	private ConversaRepository conversaRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private AnuncioRepository anuncioRepository;
	
	@Autowired
	private UserRepository userRepository;

	public ConversaEntity iniciarConversa(Long anuncioId, Long interessadoId) {
	    // Verificar se a conversa já existe
	    verificarConversaExistente(anuncioId, interessadoId);

	    // Buscar o anúncio pelo ID
	    AnuncioEntity anuncio = anuncioRepository.findById(anuncioId)
	            .orElseThrow(() -> new IllegalArgumentException("Anúncio não encontrado com ID: " + anuncioId));

	    // Buscar o interessado pelo ID
	    UserEntity interessado = userRepository.findById(interessadoId)
	            .orElseThrow(() -> new IllegalArgumentException("Interessado não encontrado com ID: " + interessadoId));

	    // Criar nova conversa
	    ConversaEntity novaConversa = ConversaEntity.builder()
	            .anuncio(anuncio)
	            .interessado(interessado)
	            .anunciante(anuncio.getUser()) // Pega o anunciante diretamente do anúncio
	            .build();

	    return conversaRepository.save(novaConversa);
	}


	public List<MessageEntity> buscarMensagens(Long conversaId) {
		return messageRepository.findByConversaId(conversaId);
	}

	public void verificarConversaExistente(Long anuncioId, Long interessadoId) {
		if (conversaRepository.existsByAnuncioIdAndInteressadoId(anuncioId, interessadoId)) {
			throw new IllegalStateException(
					"Já existe uma conversa entre o anunciante e este interessado para este anúncio.");
		}
	}
}
