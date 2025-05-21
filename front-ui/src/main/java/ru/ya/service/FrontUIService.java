package ru.ya.service;

import org.springframework.stereotype.Service;
import ru.ya.dto.UserDto;
import ru.ya.model.User;

@Service
public class FrontUIService {
    public boolean isUserPasswordCorrect(User user) {
        return (user.getPassword().equals(user.getConfirmedPassword()));
    }




    
}
