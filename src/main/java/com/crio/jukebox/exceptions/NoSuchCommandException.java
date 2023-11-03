package com.crio.jukebox.exceptions;

public class NoSuchCommandException extends RuntimeException{

    public NoSuchCommandException(){
        super();
    }
    
    public NoSuchCommandException(String mssg){
        super(mssg);
    }
}