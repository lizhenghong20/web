package cn.farwalker.waka.oss;

public class OssException extends RuntimeException {
    private static final long serialVersionUID = 6517393358388543635L;

    public OssException() {
        super();
    }

    public OssException(String message) {
        super(message);
    }

    public OssException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssException(Throwable cause) {
        super(cause);
    }
}
