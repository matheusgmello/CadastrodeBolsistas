package dev.matheus.CadastroDeBolsistas.Exceptions;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}
