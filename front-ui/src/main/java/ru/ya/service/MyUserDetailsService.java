package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.ya.dto.UserDto;
import ru.ya.model.UserPrincipal;
import ru.ya.util.ResponseFromModule;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Value("${module-accounts}")
    private String moduleAccountsHost;

    @Autowired
    private RestClient restClient;

    @Autowired
    ResponseFromModule responseFromModule;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = responseFromModule.getUserDtoResponseFromModuleAccounts(username);
        UserPrincipal userPrincipal = new UserPrincipal(userDto);

        return userPrincipal;
    }
}
