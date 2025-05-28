package ru.ya.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.ErrorOperation;
import ru.ya.enums.SuccessfullOperation;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Operation {
    SuccessfullOperation successfullOperation;
    ErrorOperation errorOperation;
    String userLogin;
    String bankAccountNumber;

    public Operation(SuccessfullOperation successfullOperation, String userLogin) {
        this.successfullOperation = successfullOperation;
        this.userLogin = userLogin;
    }

    public Operation(ErrorOperation errorOperation, String userLogin) {
        this.errorOperation = errorOperation;
        this.userLogin = userLogin;
    }

    public Operation(ErrorOperation errorOperation) {
        this.errorOperation = errorOperation;
    }

    public Operation(SuccessfullOperation successfullOperation) {
        this.successfullOperation = successfullOperation;
    }
}
