package com.pjatk.MPR.Exceptions;

public class DogAlreadyExistsException extends RuntimeException {
    public DogAlreadyExistsException() {
        super("Dog already exists");
    }
}
