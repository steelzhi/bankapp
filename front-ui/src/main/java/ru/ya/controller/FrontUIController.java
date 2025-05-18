package ru.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import ru.ya.dto.UserDto;
import ru.ya.service.FrontUIService;

@Controller
public class FrontUIController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FrontUIService frontUIService;

    @GetMapping("/")
    public String getMainPage() {
        return "entry";
    }

    @GetMapping("/signup")
    public String register() {
        return "signup";
    }

    @GetMapping("/account")
    public String enterToCab() {

        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://localhost:8090/", UserDto.class);
        if (response != null) {
            return "account";
        } else {
            return "entry";
        }

                //.exchange("http://localhost:8090/", HttpMethod.GET, )

        //return "account";
    }
}
