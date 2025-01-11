package br.uff.balcao_uff.commons.util.exceptions;

public class SameUserException extends RuntimeException {
	private static final long serialVersionUID = 599765995855742494L;

	public SameUserException(String message) {
		super(message);
	}
}
