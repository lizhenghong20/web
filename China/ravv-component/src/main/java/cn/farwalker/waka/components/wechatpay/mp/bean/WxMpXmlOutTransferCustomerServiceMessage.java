package cn.farwalker.waka.components.wechatpay.mp.bean;

import cn.farwalker.waka.components.wechatpay.common.api.WxConsts;
import cn.farwalker.waka.components.wechatpay.common.util.xml.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutTransferCustomerServiceMessage extends WxMpXmlOutMessage {
  @XStreamAlias("TransInfo")
  protected final TransInfo transInfo = new TransInfo();

  public WxMpXmlOutTransferCustomerServiceMessage() {
    this.msgType = WxConsts.CUSTOM_MSG_TRANSFER_CUSTOMER_SERVICE;
  }

  public String getKfAccount() {
    return transInfo.getKfAccount();
  }

  public void setKfAccount(String kfAccount) {
    transInfo.setKfAccount(kfAccount);
  }

  @XStreamAlias("TransInfo")
  public static class TransInfo {

    @XStreamAlias("KfAccount")
    @XStreamConverter(value= XStreamCDataConverter.class)
    private String kfAccount;

    public String getKfAccount() {
      return kfAccount;
    }

    public void setKfAccount(String kfAccount) {
      this.kfAccount = kfAccount;
    }
  }
}
