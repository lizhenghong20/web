package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/22.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
public class WakaException extends RuntimeException {
    private static final long serialVersionUID = -2505645154350127849L;
    private Integer code;
    private String message;

    public WakaException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.message = serviceExceptionEnum.getMessage();
    }

    public WakaException(String message) {
        this.code = 0;
        this.message = message;
    }

    public WakaException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = 0;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
