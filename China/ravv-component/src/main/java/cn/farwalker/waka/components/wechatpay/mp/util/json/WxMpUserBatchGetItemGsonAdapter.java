/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package cn.farwalker.waka.components.wechatpay.mp.util.json;

import cn.farwalker.waka.components.wechatpay.mp.bean.WxMpUserBatchGet;
import com.google.gson.*;
import cn.farwalker.waka.components.wechatpay.common.util.json.GsonHelper;

import java.lang.reflect.Type;

public class WxMpUserBatchGetItemGsonAdapter implements
        JsonSerializer<WxMpUserBatchGet.WxMpUserBatchGetItem>,
        JsonDeserializer<WxMpUserBatchGet.WxMpUserBatchGetItem> {

	public JsonElement serialize(
			WxMpUserBatchGet.WxMpUserBatchGetItem userItem, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject articleJson = new JsonObject();

		articleJson.addProperty("openid", userItem.getOpenid());
		articleJson.addProperty("lang", userItem.getLang());
		return articleJson;
	}

	public WxMpUserBatchGet.WxMpUserBatchGetItem deserialize(
            JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		JsonObject userInfo = jsonElement.getAsJsonObject();
		WxMpUserBatchGet.WxMpUserBatchGetItem user = new WxMpUserBatchGet.WxMpUserBatchGetItem();

		JsonElement openid = userInfo.get("openid");
		if (openid != null && !openid.isJsonNull()) {
			user.setOpenid(GsonHelper.getAsString(openid));
		}
		JsonElement lang = userInfo.get("lang");
		if (lang != null && !lang.isJsonNull()) {
			user.setLang(GsonHelper.getAsString(lang));
		}
		return user;
	}
}
