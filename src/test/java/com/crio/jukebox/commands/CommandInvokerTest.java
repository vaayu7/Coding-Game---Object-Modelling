package com.crio.jukebox.commands;

import static org.mockito.ArgumentMatchers.anyList;

import java.util.ArrayList;

import com.crio.jukebox.exceptions.NoSuchCommandException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CommandInvokerTest")
@ExtendWith(MockitoExtension.class)
public class CommandInvokerTest {
    
    private CommandInvoker commandInvoker;

    @Mock
    CreatePlaylistCommand createPlaylistCommand;

    @Mock
    CreateUserCommand createUserCommand;

    @Mock
    DeletePlaylistCommand deletePlaylistCommand;

    @Mock
    ModifyPlaylistCommand modifyPlaylistCommand;

    @Mock
    PlayPlaylistCommand playPlaylistCommand;

    @Mock
    PlaySongCommand playSongCommand;

    @BeforeEach
    public void setup(){
        commandInvoker = new CommandInvoker();
        commandInvoker.register("CREATE-USER", createUserCommand);
        commandInvoker.register("CREATE-PLAYLIST", createPlaylistCommand);
        commandInvoker.register("DELETE-PLAYLIST", deletePlaylistCommand);
        commandInvoker.register("MODIFY-PLAYLIST", modifyPlaylistCommand);
        commandInvoker.register("PLAY-PLAYLIST", playPlaylistCommand);
        commandInvoker.register("PLAY-SONG", playSongCommand);
    }

    @Test
    @DisplayName("executeCommand method Should Execute Command Given CommandName and List of tokens")
    public void executeCommand_GivenNameAndTokens_ShouldExecuteCommand() {
        commandInvoker.executeCommand("CREATE-USER", anyList());
        commandInvoker.executeCommand("CREATE-PLAYLIST", anyList());
        commandInvoker.executeCommand("DELETE-PLAYLIST", anyList());
        commandInvoker.executeCommand("MODIFY-PLAYLIST", anyList());
        commandInvoker.executeCommand("PLAY-PLAYLIST", anyList());
        commandInvoker.executeCommand("PLAY-SONG", anyList());
    }

    @Test
    @DisplayName("executeCommand method Should Throw Exception Given Incorrect CommandName and List of tokens")
    public void executeCommand_GivenIncorrectNameAndTokens_ShouldThrowException() {
        //Act and Assert
        Assertions.assertThrows(NoSuchCommandException.class,() -> commandInvoker.executeCommand("RANDOM-COMMAND",new ArrayList<String>()));

    }
}
