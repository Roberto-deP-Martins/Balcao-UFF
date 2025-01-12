package br.uff.balcao_uff.commons.util.exceptions;

import java.io.Serial;

public class NegocioNaoAceitoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1670950929347973286L;

    public NegocioNaoAceitoException(String message) {
        super(message);
    }
}
