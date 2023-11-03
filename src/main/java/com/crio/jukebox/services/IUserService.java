package com.crio.jukebox.services;

import com.crio.jukebox.dtos.UserInfoDto;

public interface IUserService {
    
    public UserInfoDto create(String name);
    
}
