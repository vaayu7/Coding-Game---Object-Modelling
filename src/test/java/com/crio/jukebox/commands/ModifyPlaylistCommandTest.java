package com.crio.jukebox.commands;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.services.IPlaylistService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ModifyPlaylistCommandTest")
@ExtendWith(MockitoExtension.class)
public class ModifyPlaylistCommandTest {
    
    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    IPlaylistService playlistServicemock;

    @InjectMocks
    ModifyPlaylistCommand modifyPlaylistCommand;

    @BeforeEach
    public void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("execute method of ModifyPlaylistCommand Should Print Modified Playlist Summary After Adding Songs")
    public void execute_ShouldPrintPlaylistSummaryDto_1(){
        //Arrange
        String expectedOutput = "Playlist ID - 1" + "\n" + "Playlist Name - PLAYLIST_1" + "\n" +"Song IDs - 1 2 3 4 5";
        PlaylistSummaryDto pDto = new PlaylistSummaryDto("1", "PLAYLIST_1", List.of("1", "2", "3", "4", "5"));
        when(playlistServicemock.addSongPlaylist("1", "1", List.of("4", "5"))).thenReturn(pDto);
        //Act
        modifyPlaylistCommand.execute(List.of("MODIFY-PLAYLIST", "ADD-SONG", "1", "1", "4", "5"));
        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(playlistServicemock, times(1)).addSongPlaylist("1", "1", List.of("4", "5"));
    }

    @Test
    @DisplayName("execute method of ModifyPlaylistCommand Should Print Modified Playlist Summary After Deleting Songs")
    public void execute_ShouldPrintPlaylistSummaryDto_2(){
        //Arrange
        String expectedOutput = "Playlist ID - 1" + "\n" + "Playlist Name - PLAYLIST_1" + "\n" +"Song IDs - 1 2 3";
        PlaylistSummaryDto pDto = new PlaylistSummaryDto("1", "PLAYLIST_1", List.of("1", "2", "3"));
        when(playlistServicemock.deleteSongPlaylist("1", "1", List.of("4", "5"))).thenReturn(pDto);
        //Act
        modifyPlaylistCommand.execute(List.of("MODIFY-PLAYLIST", "DELETE-SONG", "1", "1", "4", "5"));
        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(playlistServicemock, times(1)).deleteSongPlaylist("1", "1", List.of("4", "5"));
    
    }

    @AfterEach
    public void tearDown(){
        System.setOut(standardOut);
    }
}