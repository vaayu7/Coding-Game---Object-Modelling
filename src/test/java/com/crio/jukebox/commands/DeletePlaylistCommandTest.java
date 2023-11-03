package com.crio.jukebox.commands;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.crio.codingame.exceptions.UserNotFoundException;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
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

@DisplayName("DeletePlaylistCommandTest")
@ExtendWith(MockitoExtension.class)
public class DeletePlaylistCommandTest {

    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Mock
    IPlaylistService playlistServicemock;

    @InjectMocks
    DeletePlaylistCommand deletePlaylistCommand;

    @BeforeEach
    public void setup(){
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("execute method of DeletePlaylistCommand Should Print Error Message To Console If User Not Found")
    public void execute_ShouldPrintErrorMessage_GivenUserNotFound(){
        //Arrange
        String expectedOutput = "User for given id: 1 not found!";
        doThrow(new UserNotFoundException(expectedOutput)).when(playlistServicemock).deletePlaylist(anyString(), anyString());//Mocking Void Methods with Mockito
        //Act
        deletePlaylistCommand.execute(List.of("DELETE-PLAYLIST","1", "1"));
        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(playlistServicemock, times(1)).deletePlaylist(anyString(), anyString());
    }

    @Test
    @DisplayName("execute method of DeletePlaylistCommand Should Print Delete Successful To Console")
    public void execute_ShouldPrintSuccessful(){
        //Arrange
        String expectedOutput = "Delete Successful";
        doNothing().when(playlistServicemock).deletePlaylist("1", "1");
        //Act
        deletePlaylistCommand.execute(List.of("DELETE-PLAYLIST", "1", "1"));
        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(playlistServicemock, times(1)).deletePlaylist("1", "1");
    }

    @Test
    @DisplayName("execute method of DeletePlaylistCommand Should Print Error Message To Console If Playlist Not Found")
    public void execute_ShouldPrintErrorMessage_GivenPlaylistNotFound(){
        //Arrange
        String expectedOutput = "Playlist Not Found";
        doThrow(new PlaylistNotFoundException(expectedOutput)).when(playlistServicemock).deletePlaylist(anyString(), anyString());
        //Act
        deletePlaylistCommand.execute(List.of("DELETE-PLAYLIST", "1", "1"));
        //Assert
        Assertions.assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
        verify(playlistServicemock, times(1)).deletePlaylist(anyString(), anyString());
    }

    @AfterEach
    public void tearDown(){
        System.setOut(standardOut);
    }
}