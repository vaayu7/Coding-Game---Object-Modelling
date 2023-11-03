package com.crio.jukebox.services;

import com.crio.jukebox.dtos.SongSummaryDto;

public interface ISongService {
    
    public SongSummaryDto playSong(String userId, String songId);

    public SongSummaryDto nextSong(String userId);

    public SongSummaryDto backSong(String userId);

}