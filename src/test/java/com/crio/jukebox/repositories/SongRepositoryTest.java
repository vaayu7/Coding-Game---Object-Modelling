package com.crio.jukebox.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crio.jukebox.entities.Album;
import com.crio.jukebox.entities.Artist;
import com.crio.jukebox.entities.Song;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SongRepositoryTest {
    
    private SongRepository songRepository;

    @BeforeEach
    public void setup(){

        final Artist a1 = new Artist("Ed Sheeran");
        final Artist a2 = new Artist("Cardi.B");
        final Artist a3 = new Artist("Camilla Cabello");
        final Artist a4 = new Artist("Justin Bieber");
        final Artist a5 = new Artist("Eminem");
        final Artist a6 = new Artist("50Cent");
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
        final List<Artist> featureArtists_3 = new ArrayList<Artist>(){
            {
                add(a1);
                add(a5);
                add(a6);
            }
        };
        
        final Album album_1 = new Album("No.6 Collaborations Project");
        final Song s1 = new Song("1" ,"South of the Border", "Pop", album_1, a1, featureArtists_1);
        final Song s2 = new Song("2" ,"I Don't Care", "Pop", album_1, a1, featureArtists_2);
        final Song s3 = new Song("3", "Remember The Name", "Pop", album_1, a1, featureArtists_3);
        

        final Map<String, Song> songMap = new HashMap<String, Song>(){
            {
                put("1", s1);
                put("2", s2);
                put("3", s3);

            }
        };
        songRepository = new SongRepository(songMap);
    }

    @Test
    @DisplayName("save method should create and return new Song")
    public void saveSong(){
        //Arrange
        final Artist a1 = new Artist("Ed Sheeran");
        final Artist a2 = new Artist("Skrillex");
        final Song s4 = new Song("Way To Break My Heart", "Pop", new Album("No.6 Collaborations Project"), a1, List.of(a1, a2));

        Song expectedSong = new Song("4", "Way To Break My Heart", "Pop", new Album("No.6 Collaborations Project"), a1, List.of(a1, a2));
        //Act
        Song actualSong = songRepository.save(s4);
        //Assert
        Assertions.assertEquals(expectedSong, actualSong);
    }

    @Test
    @DisplayName("findAll method should return All Song")
    public void findAllUser(){
        //Arrange
        int expectedCount = 3;
        //Act
        List<Song> actualSongs = songRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount, actualSongs.size());
        Assertions.assertEquals(expectedCount, songRepository.count());
    }

    @Test
    @DisplayName("findAll method should return Empty List if no Songs Found")
    public void findAllUser_ShouldReturnEmptyList(){
        //Arrange
        int expectedCount = 0;
        SongRepository emptySongRepository = new SongRepository();
        //Act
        List<Song> actualSongs = emptySongRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount, actualSongs.size());
    }

    @Test
    @DisplayName("findById method should return Song Given Id")
    public void findById_ShouldReturnUser_GivenUserId(){
        //Arrange
        String expectedSongId = "3";
        //Act
        Optional<Song> actualSong = songRepository.findById(expectedSongId);
        //Assert
        Assertions.assertEquals(expectedSongId, actualSong.get().getId());
    }

    @Test
    @DisplayName("findById method should return empty if Song Not Found")
    public void findById_ShouldReturnEmptyIfUserNotFound(){
        //Arrange
        Optional<Song> expected = Optional.empty();
        //Act
        Optional<Song> actual = songRepository.findById("5");
        //Assert
        Assertions.assertEquals(expected, actual);
    }

}
