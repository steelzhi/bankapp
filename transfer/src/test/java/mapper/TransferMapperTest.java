package mapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.TransferDataDto;
import ru.ya.mapper.CoupleOfValuesMapper;
import ru.ya.model.CoupleOfValues;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class TransferMapperTest {
    @Test
    void testCorrectMapping() throws IOException {
        CoupleOfValues coupleOfValues = new CoupleOfValues("RUB", 200, "USD", 2.3);
        TransferDataDto transferDataDto = new TransferDataDto(1, 2, "11", "RUB", "22", "USD", 2.3);
        CoupleOfValuesDto coupleOfValuesDto = CoupleOfValuesMapper.mapToCoupleOfValuesDto(coupleOfValues, transferDataDto);

        assertEquals(coupleOfValuesDto.getValueFrom(), coupleOfValues.getValueFrom(), "Values don't match");
        assertEquals(coupleOfValuesDto.getReceiverId(), transferDataDto.getReceiverId(), "Receivers don't match");
    }
}
