package cn.farwalker.waka.components.wechatpay.common.api;


import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;

/**
 * WxErrorException处理器
 */
public interface WxErrorExceptionHandler {

  public void handle(WxErrorException e);

}
