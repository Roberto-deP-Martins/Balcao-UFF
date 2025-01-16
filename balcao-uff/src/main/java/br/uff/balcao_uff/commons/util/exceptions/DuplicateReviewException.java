package br.uff.balcao_uff.commons.util.exceptions;

public class DuplicateReviewException extends RuntimeException {
    
	private static final long serialVersionUID = 7446119602233966901L;

	public DuplicateReviewException(String message) {
        super(message);
    }
}
