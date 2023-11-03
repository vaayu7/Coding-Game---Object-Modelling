package com.crio.jukebox.entities;

import java.util.List;

public class Song extends BaseEntity{

    private final String title;
    private final String genre;
    private final Album album;
    private final Artist owner;
    private final List<Artist> featureArtists;

    public Song(String title, String genre, Album album, Artist owner, List<Artist> featureArtists){
        this.title = title;
        this.genre = genre;
        this.album = album;
        this.owner = owner;
        this.featureArtists = featureArtists;
    }

    public Song(String id ,String title, String genre, Album album, Artist owner, List<Artist> featureArtists){
        this(title, genre, album, owner, featureArtists);
        this.id = id;
    }

    // Copy Constructor
    public Song(Song entity){
        this(entity.getId(), entity.getTitle(), entity.getGenre(), entity.getAlbum(), entity.getOwner(), entity.getFeaturArtists());
    }

    //Getters
    public String getTitle(){
        return title;
    }
    public String getGenre(){
        return genre;
    }
    public Album getAlbum(){
        return album;
    }
    public Artist getOwner(){
        return owner;
    }
    public List<Artist> getFeaturArtists(){
        return featureArtists;
    }

    // Some formalities
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
        Song other = (Song) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Song [id=" + id + ", title=" + title + ", genre=" + genre + ", album=" + album.getName() + ", owner="+ owner.getName() +", featureArtists"+ featureArtists + "]";
    }
}

