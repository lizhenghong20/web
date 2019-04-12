package cn.farwalker.waka.components.wechatpay.mp.bean;

import cn.farwalker.waka.components.wechatpay.common.api.WxConsts;
import cn.farwalker.waka.components.wechatpay.common.util.xml.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutTextMessage extends WxMpXmlOutMessage {
  
  @XStreamAlias("Content")
  @XStreamConverter(value= XStreamCDataConverter.class)
  private String content;

  public WxMpXmlOutTextMessage() {
    this.msgType = WxConsts.XML_MSG_TEXT;
  }
  
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  
}
