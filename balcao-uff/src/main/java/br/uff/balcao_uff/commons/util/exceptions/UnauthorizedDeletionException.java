package br.uff.balcao_uff.commons.util.exceptions;

public class UnauthorizedDeletionException extends RuntimeException {

	private static final long serialVersionUID = -6164256929243703431L;

	public UnauthorizedDeletionException(String message) {
		super(message);
	}
}
