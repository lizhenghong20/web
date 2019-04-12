package cn.farwalker.standard.core.temp;

/**
 * Created by asus on 2019/3/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
public enum MenuStatus {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    int code;
    String message;

    private MenuStatus(int code, String message) {
        this.code = code;
        this.message = message;
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

    public static String valueOf(Integer status) {
        if(status == null) {
            return "";
        } else {
            MenuStatus[] var4;
            int var3 = (var4 = values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                MenuStatus s = var4[var2];
                if(s.getCode() == status.intValue()) {
                    return s.getMessage();
                }
            }

            return "";
        }
    }
}

