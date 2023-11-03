package com.crio.jukebox.commands;

import java.util.ArrayList;
import java.util.List;

import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.services.IPlaylistService;

public class ModifyPlaylistCommand implements ICommand {
    
    private final IPlaylistService playlistService;

    public ModifyPlaylistCommand(IPlaylistService playlistService) {
        this.playlistService = playlistService;
    }
    // Modify a Playlist to add/delete Songs from the playlist.
    // Sample Input Token List:- ["MODIFY-PLAYLIST","ADD-SONG", "1", "1", "6", "7"]
    // or ["MODIFY-PLAYLIST","DELETE-SONG", "1", "1", "6", "7"]
    // Also Handle Exceptions and print the error messsages if any.
    // Use Parameterized Exceptions in the Service class to match with the Unit Tests Output.
   
    @Override
    public void execute(List<String> tokens) {
        String commandName = tokens.get(1);
        String userId = tokens.get(2);
        String playlistId = tokens.get(3);

        List<String> songIds = new ArrayList<>();
        for(int i = 4; i < tokens.size(); i++){
            songIds.add(tokens.get(i));
        }
        PlaylistSummaryDto pDto = null;
        try {
            if(commandName.equals("ADD-SONG")){
                pDto = playlistService.addSongPlaylist(userId, playlistId, songIds);
                //System.out.println(pDto);
            }
            if(commandName.equals("DELETE-SONG")){
                pDto = playlistService.deleteSongPlaylist(userId, playlistId, songIds);
                //System.out.println(pDto);
            }
            System.out.println(pDto);

        } catch(UserNotFoundException u){
            System.out.println(u.getMessage());
        } catch(PlaylistNotFoundException p){
            System.out.println(p.getMessage());
        } catch(SongNotFoundException s){
            System.out.println(s.getMessage());
        } catch(RuntimeException r){
            r.printStackTrace();
        }

    }
}
