package org.benaya.ai.winnersystem.exception;

import static org.benaya.ai.winnersystem.constant.ControllerConstants.BETS_ARE_BLOCKED_EXCEPTION_MESSAGE;

public class BetsAreBlockedException extends RuntimeException {
    public BetsAreBlockedException() {
        super(BETS_ARE_BLOCKED_EXCEPTION_MESSAGE);
    }
}
