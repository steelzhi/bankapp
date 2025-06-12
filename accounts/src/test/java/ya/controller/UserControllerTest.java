package ya.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ya.controller.UserController;
import ru.ya.dto.UserDto;
import ru.ya.enums.ErrorOperation;
import ru.ya.model.Operation;
import ru.ya.service.UserService;
import ru.ya.util.ResponseFromModule;
import ya.config.AccountsConfigurationTest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Import(AccountsConfigurationTest.class)
@SpringBootTest(classes = UserController.class)
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ResponseFromModule responseFromModule;

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "1")
    void getUser() throws Exception {
        UserDto userDto = new UserDto(1, "1", "1", "1", "1", null, null, null);

        when(userService.getUserDto("1"))
                .thenReturn(userDto);

        mockMvc.perform(get("/1"))
                .andExpect(status().isNotAcceptable());

        verify(userService, times(1)).getUserDto("1");
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "1")
    void getOtherUsers() throws Exception {
        when(userService.getOtherUserDtos("1"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users-except/1"))
                .andExpect(status().isNotAcceptable());

        verify(userService, times(1)).getOtherUserDtos("1");
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "1")
    void registerUserIsEmpty() throws Exception {
        UserDto userDto = new UserDto(1, "1", "1", "1", "1", null, null, null);
        String requestBody = "{\"login\": \"1\"}";

        when(userService.doesUserAlreadyExists(null))
                .thenReturn(true);

        when(responseFromModule.getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.USER_ALREADY_EXISTS, userDto.getLogin())))
                .thenReturn("main-page");

        mockMvc.perform(post("/register-user")
                        .content(requestBody)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().is4xxClientError());
    }

}
