package com.crio.jukebox.dtos;

import java.util.Arrays;
import java.util.List;

public class SongSummaryDto {
    
    private final String songName;
    private final String albumName;
    private final List<String> artists;

    public SongSummaryDto(String songName, String albumName, List<String> artists){
        this.songName = songName;
        this.albumName = albumName;
        this.artists = artists;
    }

    public String getSongName(){
        return songName;
    }

    public String getAlbumName(){
        return albumName;
    } 

    public List<String> getArtists(){
        return artists;
    }

    @Override
    public String toString() {
        return "Current Song Playing" +"\n" + "Song - "+ songName + "\n" + "Album - "+ albumName + "\n"+ "Artists - "+ Arrays.toString(artists.toArray()).replace("[", "").replace("]", "").replace(", ", ",");
    }
    
}
