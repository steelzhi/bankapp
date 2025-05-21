package ru.ya.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ya.dto.UserDto;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {
    private UserDto userDto;

    public UserPrincipal(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userDto.getRole().toString()));
    }

    @Override
    public String getPassword() {
        System.out.println("pass: " + userDto.getPassword());
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getLogin();
    }


}