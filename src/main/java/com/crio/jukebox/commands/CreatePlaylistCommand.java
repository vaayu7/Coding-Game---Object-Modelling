package com.crio.jukebox.commands;

import java.util.ArrayList;
import java.util.List;

import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.services.IPlaylistService;

public class CreatePlaylistCommand implements ICommand {

    private final IPlaylistService playlistService;

    public CreatePlaylistCommand(IPlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // Create a new Playlist from a pool of songs. A song could exist in multiple playlists.
    // Sample Input Token List:- ["CREATE_PLAYLIST","1","MY_PLAYLIST_1", "1", "4", "5","6"]
    // Also Handle Exceptions and print the error messsages if any.
    // Use Parameterized Exceptions in the Service class to match with the Unit Tests Output.
    @Override
    public void execute(List<String> tokens) {
        String userId = tokens.get(1);
        String playlistName = tokens.get(2);
        List<String> songIds = new ArrayList<>();
        for(int i = 3; i < tokens.size(); i++){
            songIds.add(tokens.get(i));
        }

        try{
            PlaylistSummaryDto pDto = playlistService.create(userId, playlistName, songIds);
            System.out.println("Playlist ID - " + pDto.getPlaylistId());
        } catch(UserNotFoundException u){
            System.out.println(u.getMessage());
        } catch(SongNotFoundException s){
            System.out.println(s.getMessage());
        } catch(RuntimeException r){
            r.printStackTrace();
        }
    }
}
