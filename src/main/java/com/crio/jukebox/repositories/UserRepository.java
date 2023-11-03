package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.crio.jukebox.entities.User;

public class UserRepository implements IUserRepository {

    private final Map<String, User> userMap;
    private Integer autoIncrement = 0;

    public UserRepository(){
        userMap = new HashMap<String, User>();
    }

    public UserRepository(Map<String, User> userMap) {
        this.userMap = userMap;
        this.autoIncrement = userMap.size();
    }

    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            autoIncrement++;
            User u = new User(Integer.toString(autoIncrement), entity.getName());
            userMap.put(u.getId(), u);
            return u;
        }
        userMap.put(entity.getId(), entity);
        return entity;
    }
    
    @Override
    public List<User> findAll() {
        return userMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public boolean existsById(String id) {
        User temp = userMap.get(id);
        if(temp == null){
            return false;
        }
        return true;
    }

    @Override
    public void delete(User entity) {
        if(entity.getId() == null){
            return;
        }
        userMap.remove(entity.getId());
    }

    @Override
    public void deleteById(String id) {
        if(id == null){
            return;
        }
        userMap.remove(id);
    }

    @Override
    public long count() {
        return userMap.values().stream().count();
    }
}
