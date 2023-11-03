package com.crio.jukebox.services;

import java.util.List;

import com.crio.jukebox.dtos.PlaylistSummaryDto;
import com.crio.jukebox.dtos.SongSummaryDto;
import com.crio.jukebox.exceptions.PlaylistNotFoundException;
import com.crio.jukebox.exceptions.SongNotFoundException;

public interface IPlaylistService {
    
    public PlaylistSummaryDto create(String userId, String name, List<String> songIds);

    public void deletePlaylist(String userId, String playlistId) throws PlaylistNotFoundException;
    
    public PlaylistSummaryDto addSongPlaylist(String userId, String playlistId, List<String> songIds) throws SongNotFoundException;

    public PlaylistSummaryDto deleteSongPlaylist(String userId, String playlistId, List<String> songIds) throws SongNotFoundException;

    public SongSummaryDto playPlaylist(String userId, String playlistId) throws PlaylistNotFoundException;
    
}
