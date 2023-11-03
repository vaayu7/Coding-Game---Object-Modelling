package com.crio.jukebox.commands;

import java.util.List;

import com.crio.jukebox.services.IUserService;
import com.crio.jukebox.dtos.UserInfoDto;

public class CreateUserCommand implements ICommand {

    private final IUserService userService;

    public CreateUserCommand(IUserService userService){
        this.userService = userService;
    }

    // Create a new user in the system.    
    // Sample Input Token List:- ["CREATE-USER","Kiran"]
    // Also Handle Exceptions and print the error messsages if any.

    @Override
    public void execute(List<String> tokens) {
        String name = tokens.get(1);
        UserInfoDto u = userService.create(name);
        System.out.println(u.getId() + " " + u.getName());
    }
}