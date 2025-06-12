package ru.ya.mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ya.dto.UserDto;
import ru.ya.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class UserMapperTest {
    @Test
    void testCorrectMapping() throws IOException {
        User user = new User("1", "1", "1", "1", "1", LocalDate.now());
        UserDto userDto = UserMapper.mapToUserDto(user);
        assertEquals(userDto.getName(), user.getName(), "Names don't match");
    }
}
