package com.crio.jukebox.exceptions;

public class PlaylistNotFoundException extends RuntimeException{
    
    public PlaylistNotFoundException(){
        super();
    }

    public PlaylistNotFoundException(String mssg){
        super(mssg);
    }
}