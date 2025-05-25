package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ya.model.Operation;

@RestController
public class NotificationController {

    @Autowired
    Logger logger;

    @PostMapping("/notification")
    public String addInfoAndGetNotification(Model model, @RequestBody Operation operation) {
        model.addAttribute("operation", operation);
        switch (operation.getOperation()) {
            case USER_CREATING -> {
                return "user-registered-successfully.html";
            }
            case USER_DELETING -> {
            }
            case PASSWORD_EDITING -> {
            }
            case OTHER_DATA_EDITING -> {
            }
            case BANK_ACCOUNT_CREATING -> {
            }
            case BANK_ACCOUNT_DELETING -> {
            }
        }

        return "";
    }
}
