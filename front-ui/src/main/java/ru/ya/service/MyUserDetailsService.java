package ru.ya.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ya.dto.UserDto;
import ru.ya.model.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

/*    @Autowired
    Logger logger;*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://localhost:8090/" + username, UserDto.class);
        UserDto userDto = response.getBody();
        UserPrincipal userPrincipal = new UserPrincipal(userDto);

        return userPrincipal;
    }
}
