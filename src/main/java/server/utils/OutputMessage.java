package server.utils;

public enum OutputMessage {

    STRING_LOGIN_MESSAGE("님이 들어오셨습니다."),
    STRING_LOGOUT_MESSAGE("님이 나가셨습니다."),
    STRING_LOGIN_ERROR_MESSAGE("님이 나가셨습니다.");

    private final String message;

    OutputMessage(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
