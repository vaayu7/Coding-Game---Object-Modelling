package com.crio.jukebox.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crio.jukebox.dtos.UserInfoDto;
import com.crio.jukebox.entities.User;
import com.crio.jukebox.repositories.IUserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UserServiceTest")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("create method should create UserInfoDto")
    public void create_ShouldReturnUser(){
        //Arrange
        User expectedUser = new User("1", "Pankaj");
        UserInfoDto expectedUserInfoDto = new UserInfoDto(expectedUser.getId(), expectedUser.getName());
        when(userRepositoryMock.save(any(User.class))).thenReturn(expectedUser);
        //Act
        UserInfoDto actualUserInfoDto = userService.create("Pankaj");
        User actualUser = new User(actualUserInfoDto.getId(), actualUserInfoDto.getName());
        //User actualUser = new User(uDto.getId(), uDto.getName());
        //Assert
        Assertions.assertEquals(expectedUserInfoDto.toString(), actualUserInfoDto.toString());
        Assertions.assertEquals(expectedUser, actualUser);
        verify(userRepositoryMock, times(1)).save(any(User.class));

    }
}
