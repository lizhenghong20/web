package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/25.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum Order {
    ASC("asc"),
    DESC("desc");

    private String des;

    private Order(String des) {
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
