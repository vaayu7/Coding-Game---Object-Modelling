package com.crio.jukebox.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crio.jukebox.entities.Album;
import com.crio.jukebox.entities.Artist;
import com.crio.jukebox.entities.Playlist;
import com.crio.jukebox.entities.Song;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlaylistRepositoryTest {
    
    private PlaylistRepository playlistRepository;

    @BeforeEach
    public void setup(){
        final Artist a1 = new Artist("Ed Sheeran");
        final Artist a2 = new Artist("Cardi.B");
        final Artist a3 = new Artist("Camilla Cabello");
        final Artist a4 = new Artist("Justin Bieber");
        final List<Artist> featureArtists_1 = new ArrayList<Artist>(){
            {
                add(a1);
                add(a2);
                add(a3);
            }
        };
        final List<Artist> featureArtists_2 = new ArrayList<Artist>(){
            {
                add(a1);
                add(a4);
            }
        };
        final Album album_1 = new Album("No.6 Collaborations Project");
        final Song s1 = new Song("1" ,"South of the Border", "Pop", album_1, a1, featureArtists_1);
        final Song s2 = new Song("2" ,"I Don't Care", "Pop", album_1, a1, featureArtists_2);
        
        final List<Song> list1 = new ArrayList<Song>(){
            {
                add(s1);
                add(s2);
            }
        };
        final List<Song> list2 = new ArrayList<Song>(){
            {
                add(s1);
            }
        };
        final List<Song> list3 = new ArrayList<Song>(){
            {
                add(s2);
            }
        };

        final Map<String, Playlist> playlistMap = new HashMap<String, Playlist>(){
            {
                put("1", new Playlist("1", "PLAYLIST_1", list1));
                put("2", new Playlist("2", "PLAYLIST_2", list2));
                put("3", new Playlist("3", "PLAYLIST_3", list3));
            }
        };

        playlistRepository = new PlaylistRepository(playlistMap);
    }

    @Test
    @DisplayName("save method should create and return new Playlist")
    public void saveSong(){
        //Arrange
        final Artist a1 = new Artist("Ed Sheeran");
        final Artist a2 = new Artist("Skrillex");
        final Song s3 = new Song("3", "Pop", new Album("No.6 Collaborations Project"), a1, List.of(a1, a2));
        final Playlist p4 = new Playlist("PLAYLIST_4", List.of(s3));

        Playlist expectedPlaylist= new Playlist("4", "PLAYLIST_4", List.of(s3));
        //Act
        Playlist actualPlaylist= playlistRepository.save(p4);
        //Assert
        Assertions.assertEquals(expectedPlaylist, actualPlaylist);
    }

    @Test
    @DisplayName("findAll method should return All Playlist")
    public void findAllUser(){
        //Arrange
        int expectedCount = 3;
        //Act
        List<Playlist> actualPlaylists = playlistRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount, actualPlaylists.size());
        Assertions.assertEquals(expectedCount, playlistRepository.count());
    }

    @Test
    @DisplayName("findAll method should return Empty List if no Playlist Found")
    public void findAllUser_ShouldReturnEmptyList(){
        //Arrange
        int expectedCount = 0;
        PlaylistRepository emptySongRepository = new PlaylistRepository();
        //Act
        List<Playlist> actualPlaylists = emptySongRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount, actualPlaylists.size());
    }

    @Test
    @DisplayName("findById method should return Playlist Given Id")
    public void findById_ShouldReturnUser_GivenUserId(){
        //Arrange
        String expectedPlaylistId = "3";
        //Act
        Optional<Playlist> actualPlaylist = playlistRepository.findById(expectedPlaylistId);
        //Assert
        Assertions.assertEquals(expectedPlaylistId, actualPlaylist.get().getId());
    }

    @Test
    @DisplayName("findById method should return empty if Playlist Not Found")
    public void findById_ShouldReturnEmptyIfUserNotFound(){
        //Arrange
        Optional<Playlist> expected = Optional.empty();
        //Act
        Optional<Playlist> actual = playlistRepository.findById("5");
        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleteById method should delete Playlist Given Id")
    public void deleteById_ShouldDeletePlaylist_GivenPlaylistId(){
        //Arrange
        String playlistId = "3";
        int expectedCount = 2;
        //Act
        playlistRepository.deleteById(playlistId);
        //Assert
        Assertions.assertEquals(expectedCount, playlistRepository.count());

    }

}
