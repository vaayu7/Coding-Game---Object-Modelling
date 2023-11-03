package com.crio.jukebox.appConfig;

import com.crio.jukebox.commands.*;
import com.crio.jukebox.repositories.*;
import com.crio.jukebox.repositories.data.DataLoader;
import com.crio.jukebox.repositories.data.SongData;
import com.crio.jukebox.services.*;


public class ApplicationConfig {

    private final IUserRepository iUserRepository= new UserRepository();

    private final IPlaylistRepository iPlaylistRepository = new PlaylistRepository();

    private final ISongRepository iSongRepository = new SongRepository();


    private final IUserService iUserService = new UserService(iUserRepository);
    
    private final IPlaylistService iPlaylistService = new PlaylistService(iPlaylistRepository, iUserRepository, iSongRepository);

    private final ISongService iSongService = new SongService(iSongRepository, iUserRepository);
   
    
    private final ICommand createUserCommand = new CreateUserCommand(iUserService);

    private final ICommand createPlaylistCommand = new CreatePlaylistCommand(iPlaylistService);
    private final ICommand deletePlaylistCommand = new DeletePlaylistCommand(iPlaylistService);
    private final ICommand modifyPlaylistCommand = new ModifyPlaylistCommand(iPlaylistService);
    private final ICommand playPlaylistCommand = new PlayPlaylistCommand(iPlaylistService);

    private final ICommand playSongCommand = new PlaySongCommand(iSongService);


    private final CommandInvoker commandInvoker = new CommandInvoker();
    
    private final DataLoader dataLoader = new DataLoader();
    
    public CommandInvoker getCommandInvoker(){
        commandInvoker.register("CREATE-USER", createUserCommand);
        commandInvoker.register("CREATE-PLAYLIST", createPlaylistCommand);
        commandInvoker.register("MODIFY-PLAYLIST", modifyPlaylistCommand);
        commandInvoker.register("DELETE-PLAYLIST", deletePlaylistCommand);
        commandInvoker.register("PLAY-PLAYLIST", playPlaylistCommand);
        commandInvoker.register("PLAY-SONG", playSongCommand);
        return commandInvoker;
    }

    public DataLoader getDataLoader(){
        dataLoader.register("LOAD-DATA", new SongData(iSongRepository));
        return dataLoader;
    }

}