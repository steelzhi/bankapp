package ya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.mapper.UserMapper;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;
import ru.ya.service.UserService;
import ya.config.AccountsConfigurationTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {UserService.class, UserRepository.class})
@Import(AccountsConfigurationTest.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockitoBean
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockitoBean
    BankAccountRepository bankAccountRepository;

    @Test
    void getUserDto() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        //UserDto userDto = new UserDto(1, "1", "1", "1", "1", null, null, null);

        when(userRepository.findByLogin("1"))
                .thenReturn(user);

        UserDto userDtoFromDb = userService.getUserDto("1");
        assertEquals(user.getName(), userDtoFromDb.getName(), "Names don't match");

        verify(userRepository, times(1)).findByLogin("1");
    }

    @Test
    void getOtherUserDtos() {
        User user2 = new User("2", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        User user3 = new User("3", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());

        when(userRepository.findAllOtherUsersExceptUser("1"))
                .thenReturn(List.of(user2, user3));

        List<UserDto> userDtosFromDb = userService.getOtherUserDtos("1");
        assertTrue(userDtosFromDb.size() == 2, "List doesn't contain 2 users");

        verify(userRepository, times(1)).findAllOtherUsersExceptUser("1");
    }


    @Test
    void saveUser() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);

        when(userRepository.save(any()))
                .thenReturn(user);

        User savedUser = userService.addUser(userDto);
        assertEquals(user.getName(), savedUser.getName(), "Names don't match");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void changePasswordAndReturnIfChanged() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        UserDto userDtoWithEncodedPassword = userService.getUserDtoWithEncodedPassword(userDto);

        doNothing().when(userRepository).changePassword(userDtoWithEncodedPassword.getPassword(), userDtoWithEncodedPassword.getId());
        when(userRepository.findById(userDtoWithEncodedPassword.getId()))
                .thenReturn(Optional.of(user));
        userService.changePasswordAndReturnIfChanged(userDto);

        verify(userRepository, times(1)).changePassword(userDtoWithEncodedPassword.getPassword(), userDtoWithEncodedPassword.getId());
    }

    @Test
    void changeOtherDataAndReturnIfChanged() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        UserDto userDtoWithEncodedPassword = userService.getUserDtoWithEncodedPassword(userDto);

        doNothing().when(userRepository).changeOtherData(userDto.getName(), userDto.getSurname(), userDto.getBirthdate(), userDto.getId());
        when(userRepository.findById(userDtoWithEncodedPassword.getId()))
                .thenReturn(Optional.of(user));
        userService.changeOtherDataAndReturnIfChanged(userDto);

        verify(userRepository, times(1)).changeOtherData(userDto.getName(), userDto.getSurname(), userDto.getBirthdate(), userDto.getId());
    }

    @Test
    void deleteUser() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        when(userRepository.findByLogin(user.getLogin()))
                .thenReturn(user);

        doNothing().when(userRepository).deleteById(user.getId());
        userService.deleteUser(userDto);

        verify(userRepository, times(1)).findByLogin(user.getLogin());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

}
