package com.crio.jukebox.commands;

import java.util.List;

import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.services.ISongService;

public class PlaySongCommand implements ICommand {

    private final ISongService songService;

    public PlaySongCommand(ISongService songService) {
        this.songService = songService;
    }
    // Switch songs on the active playlist currently playing any song.
    // Sample Input Token List:- ["PLAY-SONG","1", "5"]
    // or ["PLAY-SONG","1", "NEXT"]
    // or ["PLAY-SONG","1", "BACK"]
    // Also Handle Exceptions and print the error messsages if any.
    // Use Parameterized Exceptions in the Service class to match with the Unit Tests Output.
   

    @Override
    public void execute(List<String> tokens) {
        
        String userId = tokens.get(1);
        String param = tokens.get(2);

        SongSummaryDto sDto;
        try{
            if(param.equals("NEXT")){
                sDto = songService.nextSong(userId);   
            } else if(param.equals("BACK")){
                sDto = songService.backSong(userId);
            } else {
                sDto = songService.playSong(userId, param);
            }
            System.out.println(sDto);

        } catch(UserNotFoundException u){
            System.out.println(u.getMessage());
        } catch(SongNotFoundException s){
            System.out.println(s.getMessage());
        } catch(RuntimeException r){
            r.printStackTrace();
        }
    }
    
}