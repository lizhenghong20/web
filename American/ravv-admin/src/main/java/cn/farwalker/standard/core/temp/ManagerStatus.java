package cn.farwalker.standard.core.temp;

/**
 * Created by asus on 2019/3/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
public enum ManagerStatus {
    OK(1, "启用"),
    FREEZED(2, "冻结"),
    DELETED(3, "被删除");

    int code;
    String message;

    private ManagerStatus(int code, String message) {
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

    public static String valueOf(Integer value) {
        if(value == null) {
            return "";
        } else {
            ManagerStatus[] var4;
            int var3 = (var4 = values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                ManagerStatus ms = var4[var2];
                if(ms.getCode() == value.intValue()) {
                    return ms.getMessage();
                }
            }

            return "";
        }
    }
}

