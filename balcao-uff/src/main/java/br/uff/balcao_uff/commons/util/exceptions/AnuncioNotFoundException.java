package br.uff.balcao_uff.commons.util.exceptions;

public class AnuncioNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -8019815710134263725L;
	
	public AnuncioNotFoundException(String message) {
        super(message);
    }

}
