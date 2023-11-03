package com.crio.jukebox.services;

import java.util.List;
import java.util.stream.Collectors;

import com.crio.jukebox.repositories.IUserRepository;
import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.entities.Playlist;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.UserPlaylistCurrentSong;
import com.crio.jukebox.exceptions.EmptyPlaylistException;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.repositories.IPlaylistRepository;
import com.crio.jukebox.repositories.ISongRepository;

public class PlaylistService implements IPlaylistService {

    private final IPlaylistRepository playlistRepository;
    private final IUserRepository userRepository;
    private final ISongRepository songRepository;

    public PlaylistService(IPlaylistRepository playlistRepository, IUserRepository userRepository, ISongRepository songRepository){
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    // Create a new Playlist from a pool of songs. A song could exist in multiple playlists.
    // Note:- Order in which the song is added in the playlist must be maintained.
    @Override
    public PlaylistSummaryDto create(String userId, String name, List<String> songIds) throws UserNotFoundException, SongNotFoundException{
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        
        final List<Song> songs = songIds.stream().map(s -> songRepository.findById(s).orElseThrow(() -> new SongNotFoundException("Some Requested Songs Not Available. Please try again."))).collect(Collectors.toList());
        // if(songs.size() != songIds.size()){
        //     throw new SongNotFoundException("Some Requested Songs Not Available. Please try again.");
        // }

        Playlist p = playlistRepository.save(new Playlist(name, songs));
        PlaylistSummaryDto pDto = new PlaylistSummaryDto(p.getId(), p.getName(), p.getSongList().stream().map(s-> s.getTitle()).collect(Collectors.toList()));
        user.addPlaylist(p);
        userRepository.save(user);
        return pDto;
    }

    // Delete a Playlist given Playlist ID if it exists in the system.
    @Override
    public void deletePlaylist(String userId, String playlistId) throws UserNotFoundException ,PlaylistNotFoundException {
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        final Playlist p = playlistRepository.findById(playlistId).orElseThrow(() -> new PlaylistNotFoundException("Playlist IDs do not exist"));
        if(! user.checkIfPlaylistExist(p)){
            throw new PlaylistNotFoundException("Playlist Not Found");
        }

        user.deletePlaylist(p);
        playlistRepository.deleteById(playlistId);
        //playlistRepository.delete(p);
        userRepository.save(user);

        // final List<Playlist> userPlaylists = user.getPlaylists();
        // Playlist playlist = userPlaylists.stream().filter(up -> up.getId().equals(playlistId)).findAny().orElse(null);
        // if(playlist == null){
        //     throw new PlaylistNotFoundException("Playlist Not Found");
        // }
    }

    // Modify a Playlist to add Songs to the playlist if they exist in system.
    // Note:- Do not add the song again if the Song ID already exists in the playlist.
    @Override
    public PlaylistSummaryDto addSongPlaylist(String userId, String playlistId, List<String> songIds)
            throws UserNotFoundException, PlaylistNotFoundException, SongNotFoundException {
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        final Playlist p = playlistRepository.findById(playlistId).orElseThrow(() -> new PlaylistNotFoundException("Playlist IDs do not exist"));
        if(! user.checkIfPlaylistExist(p)){
            throw new PlaylistNotFoundException("Playlist Not Found");
        }
        
        final List<Song> songs = songIds.stream().map(s-> songRepository.findById(s).orElseThrow(() -> new SongNotFoundException("Some Requested Songs Not Available. Please try again."))).collect(Collectors.toList());
        // if(songs.size() != songIds.size()){
        //     throw new SongNotFoundException("Some Requested Songs Not Available. Please try again.");
        // }

        for(Song s : songs){ 
            if(! p.checkIfSongExist(s)){
                p.addSong(s);
            }
        }
        playlistRepository.save(p);
        PlaylistSummaryDto pDto = new PlaylistSummaryDto(p.getId(), p.getName(), p.getSongList().stream().map(s-> s.getId()).collect(Collectors.toList()));
        userRepository.save(user);
        return pDto;
    }

    // Modify a Playlist to delete Songs from the playlist if they exist in playlist.
    @Override
    public PlaylistSummaryDto deleteSongPlaylist(String userId, String playlistId, List<String> songIds)
            throws UserNotFoundException, PlaylistNotFoundException, SongNotFoundException {
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        final Playlist p = playlistRepository.findById(playlistId).orElseThrow(() -> new PlaylistNotFoundException("Playlist IDs do not exist"));
        if(! user.checkIfPlaylistExist(p)){
            throw new PlaylistNotFoundException("Playlist Not Found");
        }

        final List<Song> songs = songIds.stream().map(s-> songRepository.findById(s).orElseThrow(() -> new SongNotFoundException("Some Requested Songs Not Available. Please try again."))).collect(Collectors.toList());

        for(Song s : songs){
            if(p.checkIfSongExist(s)){
                p.deleteSong(s);
            } else {
                throw new SongNotFoundException("Some Requested Songs for Deletion are not present in the playlist. Please try again.");
            }
        }
        playlistRepository.save(p);
        PlaylistSummaryDto pDto = new PlaylistSummaryDto(p.getId(), p.getName(), p.getSongList().stream().map(s-> s.getId()).collect(Collectors.toList()));
        userRepository.save(user);
        return pDto;
    }

    // Start playing the playlist. The output will be the first song of the playlist.
    @Override
    public SongSummaryDto playPlaylist(String userId, String playlistId) throws PlaylistNotFoundException, EmptyPlaylistException, UserNotFoundException{
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        final Playlist p = playlistRepository.findById(playlistId).orElseThrow(() -> new PlaylistNotFoundException("Playlist IDs do not exist"));
        if(! user.checkIfPlaylistExist(p)){
            throw new PlaylistNotFoundException("Playlist Not Found");
        }

        List<Song> songs = p.getSongList();
        if(songs.isEmpty()){
            throw new EmptyPlaylistException("Playlist is empty.");
        }

        Song currentSong = songs.get(0);//fetch first song of the playlist
        SongSummaryDto sDto = new SongSummaryDto(currentSong.getTitle(), currentSong.getAlbum().getName(), currentSong.getFeaturArtists().stream().map(x-> x.getName()).collect(Collectors.toList()));
        //create UserPlaylistCurrentSong obj san set it to User
        UserPlaylistCurrentSong userPlaylistCurrentSong = new UserPlaylistCurrentSong(p, currentSong);
        user.setUserPlaylistCurrentSong(userPlaylistCurrentSong);

        userRepository.save(user);
        return sDto;
    }
    
}
