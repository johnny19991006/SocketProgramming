package client.utils;

public enum OutputMessage {

    STRING_WELCOME_MESSAGE("채팅방에 오신것을 환영합니다\n");

    private final String message;

    OutputMessage(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
