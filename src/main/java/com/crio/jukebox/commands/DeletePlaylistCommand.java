package com.crio.jukebox.commands;

import java.util.List;

import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
import com.crio.jukebox.services.IPlaylistService;

public class DeletePlaylistCommand implements ICommand {
    
    private final IPlaylistService playlistService;

    public DeletePlaylistCommand(IPlaylistService playlistService) {
        this.playlistService = playlistService;
    }
    // Delete a Playlist given Playlist ID if it exists in the system.
    // Sample Input Token List:- ["DELETE-PLAYLIST","1", "2"]
    // Also Handle Exceptions and print the error messsages if any.
    // Use Parameterized Exceptions in the Service class to match with the Unit Tests Output.
    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String playlistId = tokens.get(2);

        try{
            playlistService.deletePlaylist(userId, playlistId);
            System.out.println("Delete Successful");
        } catch(UserNotFoundException u){
            System.out.println(u.getMessage());
        } catch(PlaylistNotFoundException p){
            System.out.println(p.getMessage());
        } catch(RuntimeException r){
            r.printStackTrace();;
        }
        
    }
}
