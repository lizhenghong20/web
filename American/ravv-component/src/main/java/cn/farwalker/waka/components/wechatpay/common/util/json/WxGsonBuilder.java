package cn.farwalker.waka.components.wechatpay.common.util.json;

import cn.farwalker.waka.components.wechatpay.common.bean.WxAccessToken;
import cn.farwalker.waka.components.wechatpay.common.bean.WxMenu;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxError;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxMediaUploadResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WxGsonBuilder {

  public static final GsonBuilder INSTANCE = new GsonBuilder();

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WxAccessToken.class, new WxAccessTokenAdapter());
    INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
    INSTANCE.registerTypeAdapter(WxMenu.class, new WxMenuGsonAdapter());
    INSTANCE.registerTypeAdapter(WxMediaUploadResult.class, new WxMediaUploadResultAdapter());
  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
