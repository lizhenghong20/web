package cn.farwalker.waka.util;

public class YMException extends RuntimeException {
    private static final long serialVersionUID = 6517393358388543635L;

    public YMException() {
        super();
    }

    public YMException(String message) {
        super(message);
    }

    public YMException(String message, Throwable cause) {
        super(message, cause);
    }

    public YMException(Throwable cause) {
        super(cause);
    }
}
