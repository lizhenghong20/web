/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package cn.farwalker.waka.components.wechatpay.mp.util.json;

import cn.farwalker.waka.components.wechatpay.common.api.WxConsts;
import cn.farwalker.waka.components.wechatpay.mp.bean.WxMpMassOpenIdsMessage;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WxMpMassOpenIdsMessageGsonAdapter implements JsonSerializer<WxMpMassOpenIdsMessage> {

  public JsonElement serialize(WxMpMassOpenIdsMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();
    
    JsonArray toUsers = new JsonArray();
    for (String openId : message.getToUsers()) {
      toUsers.add(new JsonPrimitive(openId));
    }
    messageJson.add("touser", toUsers);
    
    if (WxConsts.MASS_MSG_NEWS.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WxConsts.MASS_MSG_NEWS, sub);
    }
    if (WxConsts.MASS_MSG_TEXT.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("content", message.getContent());
      messageJson.add(WxConsts.MASS_MSG_TEXT, sub);
    }
    if (WxConsts.MASS_MSG_VOICE.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WxConsts.MASS_MSG_VOICE, sub);
    }
    if (WxConsts.MASS_MSG_IMAGE.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WxConsts.MASS_MSG_IMAGE, sub);
    }
    if (WxConsts.MASS_MSG_VIDEO.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WxConsts.MASS_MSG_VIDEO, sub);
    }
    messageJson.addProperty("msgtype", message.getMsgType());
    return messageJson;
  }

}
