package ru.ya.service;

import org.springframework.stereotype.Service;
import ru.ya.dto.UserDto;
import ru.ya.model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class FrontUIService {
    public boolean isUserPasswordCorrect(User user) {
        return (user.getPassword().equals(user.getConfirmedPassword()));
    }


    public boolean isUserAnAdult(User user) {
        long numberOfYears = ChronoUnit.YEARS.between(user.getBirthdate(), LocalDate.now());
        return (numberOfYears >= 18);
    }
}
