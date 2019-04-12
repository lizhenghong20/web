package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/26.
 */
public class SuccessTip extends Tip {
    public SuccessTip() {
        super.code = 200;
        super.message = "操作成功";
    }
}
