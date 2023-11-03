package com.crio.jukebox.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User extends BaseEntity {

    private final String name;
    private List<Playlist> playlists;
    //Current Song of Active Playlist Store
    private UserPlaylistCurrentSong userPlaylistCurrentSong;
    

    public User(String id, String name, List<Playlist> playlists){
        this(id, name);
        this.playlists = playlists;
    }
    
    public User(String id, String name){
        this(name);
        this.id = id;
        this.userPlaylistCurrentSong = new UserPlaylistCurrentSong();
    }

    public User(String name){
        this.name = name;
        this.playlists = new ArrayList<Playlist>();
    }

    public User(String id, String name, List<Playlist> playlists, UserPlaylistCurrentSong userPlaylistCurrentSong){
        this(id, name, playlists);
        this.userPlaylistCurrentSong = userPlaylistCurrentSong;
    }

    //Copy Constructor
    public User(User entity){
        this(entity.getId(), entity.getName(), entity.getPlaylists());
        this.userPlaylistCurrentSong = entity.userPlaylistCurrentSong;
    }

    //Getters
    public String getName() {
        return name;
    }
    public List<Playlist> getPlaylists() {
        return playlists.stream().collect(Collectors.toList());
    }
    public UserPlaylistCurrentSong getUserPlaylistCurrentSong() {
        return userPlaylistCurrentSong;
    }

    //Setters
    public void setUserPlaylistCurrentSong(UserPlaylistCurrentSong userPlaylistCurrentSong) {
        this.userPlaylistCurrentSong = userPlaylistCurrentSong;
    }

    //Behaviours
    public void addPlaylist(Playlist playlist){
        playlists.add(playlist);
    }
    public void deletePlaylist(Playlist playlist){
        playlists.removeIf(p-> p.getId() == playlist.getId());
    }
    public boolean checkIfPlaylistExist(Playlist playlist){
        if(this.getPlaylists().isEmpty()){
            return false;
        }
        return this.getPlaylists().stream().anyMatch(p-> p.equals(playlist));
    }

    //Formalities
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", playlists=" + playlists + "]";
    }

}