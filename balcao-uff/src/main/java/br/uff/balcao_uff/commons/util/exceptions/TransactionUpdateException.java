package br.uff.balcao_uff.commons.util.exceptions;

public class TransactionUpdateException extends RuntimeException {
	private static final long serialVersionUID = 7509158085282727099L;

	public TransactionUpdateException(String message) {
		super(message);
	}
}
