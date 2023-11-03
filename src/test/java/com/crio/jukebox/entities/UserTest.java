package com.crio.jukebox.entities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Test")
public class UserTest {
    
    @Test
    @DisplayName("checkIfPlaylistExists should Return true If Playlist is Found")
    public void checkIfPlaylistExists_ShouldReturnTrue_GivenPlaylist(){
        //Arrange
        String id = "1";
        String name = "MY_PLAYLIST_1";
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Cardi.B"));
            }
        };
        List<Song> songs =  new ArrayList<Song>(){
            {
            add(new Song("1" ,"South of the Border", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists));
            }
        };
        Playlist playlist = new Playlist(id, name, songs);
        //Act
        User user =  new User(id, name, new ArrayList<Playlist>(){{ add(playlist); }});
        //Assert
        Assertions.assertTrue(user.checkIfPlaylistExist(playlist));

    }

   @Test
   @DisplayName("checkIfPlaylistExists should Return False If No Playlist is Found")
   public void checkIfContestExists_ShouldReturnFalse_GivenContest(){
        //Arrange
        String id = "1";
        String name = "MY_PLAYLIST_1";
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Cardi.B"));
            }
        };
        List<Song> songs =  new ArrayList<Song>(){
            {
            add(new Song("1" ,"South of the Border", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists));
            }
        };
        Playlist playlist = new Playlist(id, name, songs);
        //Act
        User user =  new User(id, name);
        //Assert
        Assertions.assertFalse(user.checkIfPlaylistExist(playlist));

   }
}
