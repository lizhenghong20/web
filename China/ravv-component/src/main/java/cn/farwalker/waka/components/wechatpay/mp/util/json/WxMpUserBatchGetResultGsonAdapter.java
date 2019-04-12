/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package cn.farwalker.waka.components.wechatpay.mp.util.json;

import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpUser;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpUserBatchGetResult;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WxMpUserBatchGetResultGsonAdapter implements
        JsonDeserializer<WxMpUserBatchGetResult> {

	public WxMpUserBatchGetResult deserialize(JsonElement jsonElement,
                                              Type type, JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		WxMpUserBatchGetResult wxMpUserBatchGetResult = new WxMpUserBatchGetResult();
		JsonObject json = jsonElement.getAsJsonObject();
		if (json.get("user_info_list") != null
				&& !json.get("user_info_list").isJsonNull()) {
			JsonArray item = json.getAsJsonArray("user_info_list");
			List<WxMpUser> items = new ArrayList<>();
			for (JsonElement anItem : item) {
				JsonObject articleInfo = anItem.getAsJsonObject();
				items.add(WxMpGsonBuilder.create().fromJson(articleInfo,
						WxMpUser.class));
			}
			wxMpUserBatchGetResult.setUserInfoList(items);
		}
		return wxMpUserBatchGetResult;
	}
}
