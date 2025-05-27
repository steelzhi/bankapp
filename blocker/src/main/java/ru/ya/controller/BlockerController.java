package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ya.service.BlockerService;

@RestController
public class BlockerController {
    @Autowired
    BlockerService blockerService;

    @Autowired
    Logger logger;

    @PostMapping("/check-operation")
    public Boolean isOperationSuspicious(@RequestBody String requesterModuleName) {
        logger.atInfo().log("Checking operation for requester = " + requesterModuleName);
        return blockerService.isOperationSuspicious(requesterModuleName);
    }
}
