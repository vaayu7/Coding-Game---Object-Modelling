package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.entities.Song;

public class SongRepository implements ISongRepository {

    private final Map<String, Song> songMap;
    private Integer autoIncrement = 0;

    public SongRepository(){
        songMap = new HashMap<String, Song>();
    }

    public SongRepository(Map<String, Song> songMap) {
        this.songMap = songMap;
        this.autoIncrement = songMap.size();
    }

    @Override
    public Song save(Song entity) {
        if (entity.getId() == null) {
            autoIncrement++;
            Song s = new Song(Integer.toString(autoIncrement), entity.getTitle(), entity.getGenre(), entity.getAlbum(),
                    entity.getOwner(), entity.getFeaturArtists());
            songMap.put(s.getId(), s);
            return s;
        }
        songMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<Song> findAll() {
        return songMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Song> findById(String id) {
        return Optional.ofNullable(songMap.get(id));
    }

    @Override
    public boolean existsById(String id) {
        Song temp = songMap.get(id);
        if(temp == null){
            return false;
        }
        return true;
    }

    @Override
    public void delete(Song entity) {
        if(entity.getId() == null){
            return;
        }
        songMap.remove(entity.getId());
    }

    @Override
    public void deleteById(String id) {
        if(id == null){
            return;
        }
        songMap.remove(id);
    }

    @Override
    public long count() {
        return songMap.values().stream().count();
    }
}
