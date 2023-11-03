package com.crio.jukebox.services;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.entities.*;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.repositories.ISongRepository;
import com.crio.jukebox.repositories.IUserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SongServiceTest")
@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private IUserRepository userRepositoryMock;
    @Mock
    private ISongRepository songRepositoryMock;

    @InjectMocks
    private SongService songService;

    private static List<Song> songs = new ArrayList<>();

    @BeforeAll
    public static void setUp(){
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
        
        songs = new ArrayList<Song>(){
            {
                add(s1);
                add(s2);
            }
        };
    }
    
    @Test
    @DisplayName("playSong method Should Switch to Particular Song in the Active Playlist of User")
    public void playSong_ShouldSwitchToParticularSong(){
        //Arrange
        //songs -> s1: id 1, s2: id 2 Song
        Playlist playlist1 = new Playlist("1", "PLAYLIST_1", songs);
        Playlist playlist2 = new Playlist("2", "PLAYLIST_2", songs);
        UserPlaylistCurrentSong currentSong = new UserPlaylistCurrentSong(playlist1, songs.get(0));//Playing First Song of 1stPlaylist
        User user = new User("1", "Pankaj",List.of(playlist1, playlist2), currentSong);

        Song playSong = songs.get(1); //2nd Song Switch
        when(userRepositoryMock.findById("1")).thenReturn(Optional.of(user));
        when(songRepositoryMock.findById("2")).thenReturn(Optional.of(playSong));
        SongSummaryDto expectedSongSummaryDto = new SongSummaryDto(playSong.getTitle(), playSong.getAlbum().getName(), playSong.getFeaturArtists().stream().map(s -> s.getName()).collect(Collectors.toList()));
        //Act
        //2nd Song Switch
        SongSummaryDto actualSongSummaryDto = songService.playSong("1", "2");
        //Assert
        Assertions.assertEquals(playlist1, user.getUserPlaylistCurrentSong().getActivePlaylist());
        Assertions.assertEquals(playSong, user.getUserPlaylistCurrentSong().getCurrentSong());
        Assertions.assertEquals(expectedSongSummaryDto.toString(), actualSongSummaryDto.toString());
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
    }
    
    @Test
    @DisplayName("playSong method Should Throw SongNotFoundException If Given SongId is not a part of the Active Playlist")
    public void playSong_ShouldThrowSongNotFoundException(){
        //Arrange
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Eminem"));
                add(new Artist("50Cent"));
            }
        };
        Song newSong = new Song("3", "Remember The Name", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists);
        
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);//songs -> s1: id 1, s2: id 2 Song
        UserPlaylistCurrentSong currentSong = new UserPlaylistCurrentSong(playlist, songs.get(0));
        User user = new User("1", "Pankaj", List.of(playlist), currentSong);
        
        when(userRepositoryMock.findById("1")).thenReturn(Optional.of(user));
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.of(newSong));

        //Act and Assert
        Assertions.assertThrows(SongNotFoundException.class, () -> songService.playSong("1", "3"));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("nextSong method Should Switch to Next Song in the Active Playlist")
    public void nextSong_ShouldSwitchToNextSong(){
        //Arrange
        Playlist playlist1 = new Playlist("1", "PLAYLIST_1", songs);//songs -> s1: id 1, s2: id 2 Song
        Playlist playlist2 = new Playlist("2", "PLAYLIST_2", songs);
        UserPlaylistCurrentSong currentSong = new UserPlaylistCurrentSong(playlist2, songs.get(1));//Playing Last(2nd) Song of 2ndPlaylist
        User user = new User("1", "Pankaj",List.of(playlist1, playlist2), currentSong);

        Song nextSong = songs.get(0);//1st Song of 2nd Playlist
        SongSummaryDto expectedSongSummaryDto = new SongSummaryDto(nextSong.getTitle(), nextSong.getAlbum().getName(), nextSong.getFeaturArtists().stream().map(s-> s.getName()).collect(Collectors.toList()));
        
        when(userRepositoryMock.findById("1")).thenReturn(Optional.of(user));
        //Act
        SongSummaryDto actualSongSummaryDto = songService.nextSong("1");
        //Assert
        Assertions.assertEquals(playlist2, user.getUserPlaylistCurrentSong().getActivePlaylist());
        Assertions.assertEquals(nextSong, user.getUserPlaylistCurrentSong().getCurrentSong());
        Assertions.assertEquals(expectedSongSummaryDto.toString(), actualSongSummaryDto.toString());
        verify(userRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("backSong method Should Switch to Previous Song in the Active Playlist")
    public void backSong_ShouldSwitchToBackSong(){
         //Arrange
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);//songs -> s1: id 1, s2: id 2 Song
        
        UserPlaylistCurrentSong currentSong = new UserPlaylistCurrentSong(playlist, songs.get(0));//Playing First(2nd) Song of Playlist
        User user = new User("1", "Pankaj",List.of(playlist), currentSong);
        
        Song backSong = songs.get(1);//Last(2nd) Song of Playlist
        SongSummaryDto expectedSongSummaryDto = new SongSummaryDto(backSong.getTitle(), backSong.getAlbum().getName(), backSong.getFeaturArtists().stream().map(s-> s.getName()).collect(Collectors.toList()));
        
        when(userRepositoryMock.findById("1")).thenReturn(Optional.of(user));
        //Act
        SongSummaryDto actualSongSummaryDto = songService.nextSong("1");
        //Assert
        Assertions.assertEquals(playlist, user.getUserPlaylistCurrentSong().getActivePlaylist());
        Assertions.assertEquals(backSong, user.getUserPlaylistCurrentSong().getCurrentSong());
        Assertions.assertEquals(expectedSongSummaryDto.toString(), actualSongSummaryDto.toString());
        verify(userRepositoryMock, times(1)).findById(anyString());
    }
}
