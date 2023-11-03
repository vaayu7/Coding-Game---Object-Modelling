package com.crio.jukebox.entities;

public class Album { //extends BaseEntity otherwise we have to make repository of this to initialize its id without id object of Album contains null at id place
    
    private final String name;

    public Album(String name){
        this.name = name;
    }

    // public Album(String id, String name){
    //     this(name);
    //     this.id = id;
    // }

    //Getters
    public String getName(){
        return name;
    }

    //Formalities
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Album other = (Album) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Album [name=" + name + "]"; //"Album [id = "+ id +", name=" + name + "]"
    }
    
}
