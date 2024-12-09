package br.uff.balcao_uff.api.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import br.uff.balcao_uff.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDTO {

    private Long id;
    private String content;
    private Long senderId;
    private Long conversaId;

    public MessageResponseDTO(MessageEntity messageEntity) {
        this.id = messageEntity.getId();
        this.content = messageEntity.getContent();
        this.senderId = messageEntity.getSender().getId();
        this.conversaId = messageEntity.getConversa().getId();
    }

    public static List<MessageResponseDTO> fromEntityList(List<MessageEntity> messages) {
        return messages.stream()
                .map(MessageResponseDTO::new)
                .collect(Collectors.toList());
    }
}
