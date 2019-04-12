package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/26.
 */
public abstract class Tip {
    protected int code;
    protected String message;

    public Tip() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
