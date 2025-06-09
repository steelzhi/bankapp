package service;

import config.BlockerConfigurationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.ya.service.BlockerService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {BlockerService.class})
@Import(BlockerConfigurationTest.class)
public class BlockerServiceTest {

    @Autowired
    BlockerService blockerService;

    @Test
    void isOperationSuspicious() {
        boolean ans = blockerService.isOperationSuspicious(BlockerService.CASH_MODULE);
        assertNotNull(ans, "Answer cannot be null");

        Boolean ans2 = blockerService.isOperationSuspicious("aaa");
        assertNull(ans2, "Answer must be null");
    }
}
