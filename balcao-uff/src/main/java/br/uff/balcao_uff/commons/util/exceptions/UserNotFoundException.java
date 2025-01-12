package br.uff.balcao_uff.commons.util.exceptions;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4598071930548703703L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
