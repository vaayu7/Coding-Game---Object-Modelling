package com.crio.jukebox.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.entities.Album;
import com.crio.jukebox.entities.Artist;
import com.crio.jukebox.entities.Playlist;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.exceptions.EmptyPlaylistException;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.repositories.IPlaylistRepository;
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

@DisplayName("PlaylistServiceTest")
@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    private IPlaylistRepository playlistRepositoryMock;
    @Mock
    private IUserRepository userRepositoryMock;
    @Mock
    private ISongRepository songRepositoryMock;

    @InjectMocks
    private PlaylistService playlistService;

    private static List<Song> songs = new ArrayList<>();

    @BeforeAll //@BeforeEach but not every test cases uses it
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
    @DisplayName("create method should create Playlist")
    public void create_ShouldReturnPlaylist(){
        //Arrange
        User user = new User("1", "PK");
        // List<Artist> featureArtists = new ArrayList<Artist>(){
        //     {
        //         add(new Artist("Ed Sheeran"));
        //         add(new Artist("Cardi.B"));
        //     }
        // };
        // List<Song> songs =  new ArrayList<Song>(){
        //     {
        //     add(new Song("1" ,"South of the Border", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists));
        //     }
        // };
        Playlist expected  = new Playlist("1", "MY_PLAYLIST_1", songs);
        PlaylistSummaryDto expectedPDto = new PlaylistSummaryDto(expected.getId(), expected.getName(), expected.getSongList().stream().map(s -> s.getTitle()).collect(Collectors.toList()));
        
        when(userRepositoryMock.findById(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(songRepositoryMock.findById(any(String.class))).thenReturn(Optional.ofNullable(songs.get(0)));
        when(playlistRepositoryMock.save(any(Playlist.class))).thenReturn(expected);

        //Act
        PlaylistSummaryDto actualPDto = playlistService.create("1", "PLAYLIST_1", songs.stream().map(s -> s.getId()).collect(Collectors.toList()));
        Playlist actual = new Playlist(actualPDto.getPlaylistId(), actualPDto.getPlaylistName(), songs);

        //Assert
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedPDto.toString(), actualPDto.toString());
        verify(playlistRepositoryMock, times(1)).save(any(Playlist.class));
    }

    @Test
    @DisplayName("create method Should Throw UserNotFoundException If Given User Id Is Not Found")
    public void create_ShouldThrowUserNotFoundException(){
        //Arrange
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.empty());


        //Act and Assert
        Assertions.assertThrows(UserNotFoundException.class, ()-> playlistService.create("1", "PLAYLIST_1", songs.stream().map(s-> s.getId()).collect(Collectors.toList())));
        verify(userRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("create method Should Throw SongNotFoundException If Some Requested Song Not Available")
    public void create_ShouldThrowSongNotFoundException(){
        //Arrange
        User user = mock(User.class);
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        //Act and Assert
        Assertions.assertThrows(SongNotFoundException.class, ()-> playlistService.create("1", "PLAYLIST_1", songs.stream().map(s-> s.getId()).collect(Collectors.toList())));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("deletePlaylist method Should Delete A Playlist")
    public void deletePlaylist_ShouldDeleteAPlaylist(){
        //Arrange
        Playlist p1 = new Playlist("1", "PLAYLIST_1", songs);
        Playlist p2 = new Playlist("2", "Shrey", songs);
        User user = new User("1", "Pankaj", new ArrayList<Playlist>(){{add(p1); add(p2);}});//List.of(p1,p2)-> Immutable List

        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(p2));
        //Act and Assert
        playlistService.deletePlaylist("1", "2");
        Assertions.assertFalse(user.checkIfPlaylistExist(p2));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("deletePlaylist method Should Throw UserNotFoundException If Given User Id Is Not Found")
    public void deletePlaylist_ShouldThrowUserNotFoundException(){
        //Arrange
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        //Act and Assert
        Assertions.assertThrows(UserNotFoundException.class, () -> playlistService.deletePlaylist("1", "1"));
        verify(userRepositoryMock, times(1)).findById(anyString());
    }
    
    @Test
    @DisplayName("deletePlaylist method Should Throw PlaylistNotFoundException If Given Playlist IDs do not exist")
    public void deletePlaylist_ShouldThrowPlaylistNotFoundExceptionIfPlaylistIdNotExist(){
        //Arrange
        //User user = mock(User.class);
        // mocking should be done only of the dependency you are injecting or services and function you are using
        User user = new User("1", "Pankaj");
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        //Act and Assert
        Assertions.assertThrows(PlaylistNotFoundException.class, () -> playlistService.deletePlaylist("1", "1"));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
    }
    
    @Test
    @DisplayName("deletePlaylist method Should Throw PlaylistNotFoundException If Given Playlist is not Found in User")
    public void deletePlaylist_ShouldThrowPlaylistNotFoundExceptionIfPlaylistNotFound(){
        //Arrange
        Playlist p1 = new Playlist("1", "PLAYLIST_1", songs);
        Playlist p2 = new Playlist("2", "Shrey", songs);
        User user = new User("1", "Pankaj", List.of(p1));

        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(p2));
        //Act and Assert
        Assertions.assertThrows(PlaylistNotFoundException.class, () -> playlistService.deletePlaylist("1", "2"));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
        //NotAMockException verify(mock)
        //verify(user, times(1)).checkIfPlaylistExist(any(Playlist.class));
    }

    @Test
    @DisplayName("addSongPlaylist method Should Add Songs to Playlist of User")
    public void addSongPlaylist_ShouldAddSongsToPlaylist(){
        //Arrange
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);
        User user = new User("1", "Pankaj", new ArrayList<Playlist>(){{add(playlist);}});
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Eminem"));
                add(new Artist("50Cent"));
            }
        };
        Song newSong = new Song("3", "Remember The Name", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists);

        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.of(newSong));
        
        //Act
        playlistService.addSongPlaylist("1", "1", List.of(newSong.getId()));
        //Assert
        Assertions.assertTrue(playlist.checkIfSongExist(newSong));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
        //only mocked //NotAMockException verify(mock)
        //verify(playlist, times(1)).addSong(any(Song.class));
    }

    @Test
    @DisplayName("addSongPlaylist method Should Throw SongNotFoundException If Song Ids not exist")
    public void addSongPlaylist_ShouldThrowSongNotFoundException(){
        //Arrange
        // User user = mock(User.class);
        // Playlist playlist = mock(Playlist.class);
        // mocking should be done only of the dependency you are injecting or services and function you are using
        
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);
        User user = new User("1", "Pankaj", List.of(playlist));

        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));
        //when(user.checkIfPlaylistExist(playlist)).thenReturn(true);
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.empty());
        //Act and Assert
        Assertions.assertThrows(SongNotFoundException.class, () -> playlistService.addSongPlaylist("1", "1", List.of("31", "32")));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
        //verify(mock)
        //verify(user, atLeast(1)).checkIfPlaylistExist(any(Playlist.class));
    }

    @Test
    @DisplayName("deleteSongPlaylist method Should Delete Songs from Playlist of User")
    public void deleteSongPlaylist_ShouldDeleteSongsToPlaylist(){
        //Arrange
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Eminem"));
                add(new Artist("50Cent"));
            }
        };
        Song newSong = new Song("3", "Remember The Name", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists);
        songs.add(newSong);
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);
        User user = new User("1", "Pankaj", List.of(playlist));

        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.of(newSong));

        //Act
        playlistService.deleteSongPlaylist("1", "1", List.of(newSong.getId()));

        //Assert
        Assertions.assertFalse(playlist.checkIfSongExist(newSong));
        Assertions.assertEquals(2, songs.size());
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
        //not mock verify(mock)
        //verify(playlist, times(1)).deleteSong(any(Song.class));
    }

    @Test
    @DisplayName("deleteSongPlaylist method Should Throw SongNotFoundException If Songs to be Deletion are not present in the Playlist")
    public void deleteSongPlaylist_ShouldThrowSongNotFoundException(){
        //Arrange
        List<Artist> featureArtists = new ArrayList<Artist>(){
            {
                add(new Artist("Ed Sheeran"));
                add(new Artist("Eminem"));
                add(new Artist("50Cent"));
            }
        };
        Song newSong = new Song("3", "Remember The Name", "Pop", new Album("No.6 Collaborations Project"), featureArtists.get(0), featureArtists);
        //songs -> s1 , s2 Song
        songs.remove(newSong);
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);
        User user = new User("1", "Pankaj", List.of(playlist));
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));
        when(songRepositoryMock.findById(anyString())).thenReturn(Optional.of(newSong));

        //Act and Assert
        Assertions.assertThrows(SongNotFoundException.class, () -> playlistService.deleteSongPlaylist("1", "1", List.of(newSong.getId())));
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
        verify(songRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("playPlaylist method Should Play First Song of Playlist")
    public void playPlaylist_ShouldPlayPlaylist(){
        //Arrange
        Playlist playlist = new Playlist("1", "PLAYLIST_1", songs);
        User user = new User("1", "Pankaj", List.of(playlist));
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));

        Song playSong = songs.get(0);
        SongSummaryDto expectedSongSummaryDto = new SongSummaryDto(playSong.getTitle(), playSong.getAlbum().getName(), playSong.getFeaturArtists().stream().map(s -> s.getName()).collect(Collectors.toList()));
        //Act
        SongSummaryDto actualSongSummaryDto = playlistService.playPlaylist("1", "1");
        //Assert
        Assertions.assertEquals(expectedSongSummaryDto.toString(), actualSongSummaryDto.toString());
        Assertions.assertEquals(playSong, user.getUserPlaylistCurrentSong().getCurrentSong());
        Assertions.assertEquals(playlist, user.getUserPlaylistCurrentSong().getActivePlaylist());
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("playPlaylist method Should Throw EmptyPlaylistException If Given Playlist is Empty")
    public void playPlaylist_ShouldThrowEmptyPlaylistException(){
        //Arrange
        Playlist playlist = new Playlist("1", "PLAYLIST_1", List.of());//empty playlist
        User user = new User("1", "Pankaj", List.of(playlist));
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));
        when(playlistRepositoryMock.findById(anyString())).thenReturn(Optional.of(playlist));

        //Act and Assert
        Assertions.assertThrows(EmptyPlaylistException.class, () -> playlistService.playPlaylist("1", "1"));
        Assertions.assertTrue(playlist.getSongList().isEmpty());
        verify(userRepositoryMock, times(1)).findById(anyString());
        verify(playlistRepositoryMock, times(1)).findById(anyString());
    }
}
