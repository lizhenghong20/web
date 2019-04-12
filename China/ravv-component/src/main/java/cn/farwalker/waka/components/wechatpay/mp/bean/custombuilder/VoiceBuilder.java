package cn.farwalker.waka.components.wechatpay.mp.bean.custombuilder;


import cn.farwalker.waka.components.wechatpay.common.api.WxConsts;
import cn.farwalker.waka.components.wechatpay.mp.bean.WxMpCustomMessage;

/**
 * 语音消息builder
 * <pre>
 * 用法: WxMpCustomMessage m = WxMpCustomMessage.VOICE().mediaId(...).toUser(...).build();
 * </pre>
 * @author chanjarster
 *
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder> {
  private String mediaId;

  public VoiceBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_VOICE;
  }

  public VoiceBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public WxMpCustomMessage build() {
    WxMpCustomMessage m = super.build();
    m.setMediaId(this.mediaId);
    return m;
  }
}
