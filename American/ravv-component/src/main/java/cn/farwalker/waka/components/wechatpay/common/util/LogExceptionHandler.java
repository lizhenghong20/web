package cn.farwalker.waka.components.wechatpay.common.util;

import cn.farwalker.waka.components.wechatpay.common.api.WxErrorExceptionHandler;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogExceptionHandler implements WxErrorExceptionHandler {

  private Logger log = LoggerFactory.getLogger(WxErrorExceptionHandler.class);

  @Override
  public void handle(WxErrorException e) {

    log.error("Error happens", e);

  }

}
