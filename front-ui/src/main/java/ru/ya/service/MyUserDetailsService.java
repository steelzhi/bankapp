package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ya.dto.UserDto;
import ru.ya.model.UserPrincipal;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://localhost:8090/" + username, UserDto.class);
        UserDto userDto = response.getBody();
        UserPrincipal userPrincipal = new UserPrincipal(userDto);
/*        User user = new User(userDto.getUsername(), userDto.getPassword(), List.of(new SimpleGrantedAuthority(userDto.getRole().toString())));
        return user;*/
        return userPrincipal;
    }
}
