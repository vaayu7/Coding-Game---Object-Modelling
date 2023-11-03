package com.crio.jukebox.repositories;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.entities.Playlist;

public class PlaylistRepository implements IPlaylistRepository {

    private final Map<String, Playlist> playlistMap;
    private Integer autoIncrement = 0;

    public PlaylistRepository(){
        playlistMap = new HashMap<String, Playlist>();
    }

    public PlaylistRepository(Map<String, Playlist> playlistMap) {
        this.playlistMap = playlistMap;
        this.autoIncrement = playlistMap.size();
    }

    @Override
    public Playlist save(Playlist entity) {
        if (entity.getId() == null) {
            autoIncrement++;
            Playlist p = new Playlist(Integer.toString(autoIncrement), entity.getName(), entity.getSongList());
            playlistMap.put(p.getId(), p);
            return p;
        }
        playlistMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Playlist> findAll() {
        return playlistMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Playlist> findById(String id) {
        return Optional.ofNullable(playlistMap.get(id));
    }

    @Override
    public boolean existsById(String id) {
        Playlist temp = playlistMap.get(id);
        if(temp == null){
            return false;
        }
        return true;
    }

    @Override
    public void delete(Playlist entity) {
        if(entity.getId() == null){
            return;
        }
        playlistMap.remove(entity.getId());
    }

    @Override
    public void deleteById(String id) {
        if(id == null){
            return;
        }
        playlistMap.remove(id);
    }

    @Override
    public long count() {
        return playlistMap.values().stream().count();
    }
}
