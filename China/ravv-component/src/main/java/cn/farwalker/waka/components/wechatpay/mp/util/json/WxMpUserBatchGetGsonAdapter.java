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

import java.lang.reflect.Type;

public class WxMpUserBatchGetGsonAdapter implements
        JsonSerializer<WxMpUserBatchGet>, JsonDeserializer<WxMpUserBatchGet> {

	public JsonElement serialize(WxMpUserBatchGet message, Type typeOfSrc,
                                 JsonSerializationContext context) {
		JsonObject newsJson = new JsonObject();

		JsonArray articleJsonArray = new JsonArray();
		for (WxMpUserBatchGet.WxMpUserBatchGetItem item : message.getUserList()) {
			JsonObject articleJson = WxMpGsonBuilder
					.create()
					.toJsonTree(item,
							WxMpUserBatchGet.WxMpUserBatchGetItem.class)
					.getAsJsonObject();
			articleJsonArray.add(articleJson);
		}
		newsJson.add("user_list", articleJsonArray);

		return newsJson;
	}

	public WxMpUserBatchGet deserialize(JsonElement jsonElement, Type type,
                                        JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		WxMpUserBatchGet wxMpMassNews = new WxMpUserBatchGet();
		JsonObject json = jsonElement.getAsJsonObject();
		if (json.get("user_list") != null && !json.get("user_list").isJsonNull()) {
			JsonArray articles = json.getAsJsonArray("user_list");
			for (JsonElement article1 : articles) {
				JsonObject articleInfo = article1.getAsJsonObject();
				WxMpUserBatchGet.WxMpUserBatchGetItem article = WxMpGsonBuilder
						.create().fromJson(articleInfo,
								WxMpUserBatchGet.WxMpUserBatchGetItem.class);
				wxMpMassNews.addUser(article);
			}
		}
		return wxMpMassNews;
	}
}
