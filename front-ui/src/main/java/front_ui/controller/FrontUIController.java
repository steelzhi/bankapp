package front_ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontUIController {

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
        return "account";
    }
}
