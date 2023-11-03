package com.crio.jukebox.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.crio.jukebox.entities.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {
    
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        final Map<String,User> userMap = new HashMap<String,User>(){
            {
                put("1",new User("1", "user1"));
                put("2",new User("2", "user2"));
                put("3",new User("3", "user3"));
            }
        };
        userRepository = new UserRepository(userMap);
    }

    @Test
    @DisplayName("save method should create and return new User")
    public void saveUser(){
        //Arrange
        final User user4 = new User("user4");
        User expectedUser = new User("4", "user4");
        //Act
        User actualUser = userRepository.save(user4);
        //Assert
        Assertions.assertEquals(expectedUser,actualUser);
    }

    @Test
    @DisplayName("findAll method should return All User")
    public void findAllUser(){
        //Arrange
        int expectedCount = 3;
        //Act
        List<User> actualUsers = userRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount,actualUsers.size());
        Assertions.assertEquals(expectedCount, userRepository.count());
    }

    @Test
    @DisplayName("findAll method should return Empty List if no Users Found")
    public void findAllUser_ShouldReturnEmptyList(){
        //Arrange
        int expectedCount = 0;
        UserRepository emptyUserRepository = new UserRepository();
        //Act
        List<User> actualUsers = emptyUserRepository.findAll();
        //Assert
        Assertions.assertEquals(expectedCount,actualUsers.size());
    }

    @Test
    @DisplayName("findById method should return User Given Id")
    public void findById_ShouldReturnUser_GivenUserId(){
        //Arrange
        String expectedUserId = "3";
        //Act
        Optional<User> actualUser = userRepository.findById(expectedUserId);
        //Assert
        Assertions.assertEquals(expectedUserId,actualUser.get().getId());
    }

    @Test
    @DisplayName("findById method should return empty if User Not Found")
    public void findById_ShouldReturnEmptyIfUserNotFound(){
        //Arrange
        Optional<User> expected = Optional.empty();
        //Act
        Optional<User> actual = userRepository.findById("5");
        //Assert
        Assertions.assertEquals(expected,actual);
    }
}
