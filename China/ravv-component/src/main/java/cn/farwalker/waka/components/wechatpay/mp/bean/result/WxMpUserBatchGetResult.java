package cn.farwalker.waka.components.wechatpay.mp.bean.result;


import cn.farwalker.waka.components.wechatpay.mp.util.json.WxMpGsonBuilder;

import java.io.Serializable;
import java.util.List;

public class WxMpUserBatchGetResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<WxMpUser> userInfoList = null;

	public List<WxMpUser> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<WxMpUser> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public static WxMpUserBatchGetResult fromJson(String json) {
		return WxMpGsonBuilder.INSTANCE.create().fromJson(json,
				WxMpUserBatchGetResult.class);
	}
}
