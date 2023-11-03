package com.crio.jukebox.exceptions;

public class NoSuchDataException extends RuntimeException{
    
    public NoSuchDataException(){
        super();
    }
    
    public NoSuchDataException(String mssg){
        super(mssg);
    }
}
