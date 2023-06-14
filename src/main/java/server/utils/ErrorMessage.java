package server.utils;

public enum ErrorMessage {
    STRING_INVALID_PASSWORD("올바르지 않은 형식의 문자열입니다.");

    private final String message;

    ErrorMessage(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
