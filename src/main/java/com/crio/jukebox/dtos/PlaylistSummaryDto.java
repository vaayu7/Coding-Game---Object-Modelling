package com.crio.jukebox.dtos;

import java.util.Arrays;
import java.util.List;

public class PlaylistSummaryDto {
    
    private final String playlistId;
    private final String playlistName;
    private final List<String> songIds;

    public PlaylistSummaryDto(String playlistId, String playlistName, List<String> songIds){
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.songIds = songIds;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    @Override
    public String toString() {
        return "Playlist ID - " + playlistId + "\n" + "Playlist Name - " + playlistName + "\n" +"Song IDs - "
                + Arrays.toString(songIds.toArray()).replace("[", "").replace("]", "").replace(", ", " ");
    }

    
}
