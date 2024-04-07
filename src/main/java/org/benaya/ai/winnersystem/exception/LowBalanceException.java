package org.benaya.ai.winnersystem.exception;

import static org.benaya.ai.winnersystem.constant.ControllerConstants.LOW_BALANCE_EXCEPTION_MESSAGE;

public class LowBalanceException extends IllegalArgumentException{
    public LowBalanceException() {
        super(LOW_BALANCE_EXCEPTION_MESSAGE);
    }
}
