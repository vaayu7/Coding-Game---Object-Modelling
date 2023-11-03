package com.crio.jukebox.services;

import com.crio.jukebox.repositories.IUserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.entities.Playlist;
import com.crio.jukebox.entities.Song;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.entities.UserPlaylistCurrentSong;
import com.crio.jukebox.exceptions.SongNotFoundException;
import com.crio.jukebox.exceptions.UserNotFoundException;
import com.crio.jukebox.repositories.ISongRepository;

public class SongService implements ISongService {
    
    private final ISongRepository songRepository;
    private final IUserRepository userRepository;

    public SongService(ISongRepository songRepository, IUserRepository userRepository){
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    // Switch song on the active playlist currently playing any song to a particular songID if it exist in active playlist.
    @Override
    public SongSummaryDto playSong(String userId, String songId) throws UserNotFoundException, SongNotFoundException{
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        //fetch active playlist
        final Playlist p = user.getUserPlaylistCurrentSong().getActivePlaylist();
        //fetch song from songRepo
        final Song s = songRepository.findById(songId).get();
        if(! p.checkIfSongExist(s)){ //check song exist or not
            throw new SongNotFoundException("Given song id is not a part of the active playlist");
        }

        UserPlaylistCurrentSong userPlaylistCurrentSong = new UserPlaylistCurrentSong(p, s);
        user.setUserPlaylistCurrentSong(userPlaylistCurrentSong);

        userRepository.save(user);
        SongSummaryDto songSummaryDto = new SongSummaryDto(s.getTitle(), s.getAlbum().getName(), s.getFeaturArtists().stream().map(x-> x.getName()).collect(Collectors.toList()));
        return songSummaryDto;
    }

    // Switch song on the active playlist currently playing any song to next song in active playlist.
    @Override
    public SongSummaryDto nextSong(String userId) throws UserNotFoundException{
        
        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        //fetch active playlist
        final Playlist p = user.getUserPlaylistCurrentSong().getActivePlaylist();
        //fetch current playing song
        final Song s = user.getUserPlaylistCurrentSong().getCurrentSong();
        //fetch all songs from active playlist
        final List<Song> songs = p.getSongList();
        //get index of current song
        int index = IntStream.range(0, songs.size()).filter(songIndex-> songs.get(songIndex).equals(s)).findFirst().getAsInt();
        //logic for next song
        int size = songs.size();
        int currIdx = (index + 1) % size;
        //fetch next song of active playlist
        final Song currentSong = songs.get(currIdx);

        UserPlaylistCurrentSong userPlaylistCurrentSong = new UserPlaylistCurrentSong(p, currentSong);
        user.setUserPlaylistCurrentSong(userPlaylistCurrentSong);

        userRepository.save(user);

        SongSummaryDto songSummaryDto = new SongSummaryDto(currentSong.getTitle(), currentSong.getAlbum().getName(), currentSong.getFeaturArtists().stream().map(x-> x.getName()).collect(Collectors.toList()));
        return songSummaryDto;
    }

    // Switch song on the active playlist currently playing any song to back song in active playlist.
    @Override
    public SongSummaryDto backSong(String userId) throws UserNotFoundException{

        final User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User for given id: " + userId + " not found!"));
        //fetch active playlist
        final Playlist p = user.getUserPlaylistCurrentSong().getActivePlaylist();
        //fetch current playing song
        final Song s = user.getUserPlaylistCurrentSong().getCurrentSong();
        //fetch all songs from active playlist
        final List<Song> songs = p.getSongList();
        //get index of current song
        int index = IntStream.range(0, songs.size()).filter(songInd-> songs.get(songInd).equals(s)).findFirst().getAsInt();
        //logic for previous song
        int size = songs.size();
        int currIdx = (index + size - 1) % size;
        //fetch previous song of active playlist
        final Song currentSong = songs.get(currIdx);
        
        UserPlaylistCurrentSong userPlaylistCurrentSong = new UserPlaylistCurrentSong(p, currentSong);
        user.setUserPlaylistCurrentSong(userPlaylistCurrentSong);

        userRepository.save(user);

        SongSummaryDto songSummaryDto = new SongSummaryDto(currentSong.getTitle(), currentSong.getAlbum().getName(), currentSong.getFeaturArtists().stream().map(x-> x.getName()).collect(Collectors.toList()));
        return songSummaryDto;
    }
    
}
