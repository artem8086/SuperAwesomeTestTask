package art.soft.test.models;

public class MessageObject {
    private final String message;
    private final boolean isError;

    public MessageObject(String message) {
        this.message = message;
        isError = false;
    }

    public MessageObject(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }
}
