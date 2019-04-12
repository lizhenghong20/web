package cn.farwalker.waka.components.wechatpay.mp.bean;

import cn.farwalker.waka.components.wechatpay.mp.util.json.WxMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpUserBatchGet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<WxMpUserBatchGetItem> userList = new ArrayList<WxMpUserBatchGetItem>();

	public List<WxMpUserBatchGetItem> getUserList() {
		return userList;
	}

	public void setUserList(List<WxMpUserBatchGetItem> userList) {
		this.userList = userList;
	}

	public void addUser(WxMpUserBatchGetItem item) {
		this.userList.add(item);
	}

	public String toJson() {
		return WxMpGsonBuilder.INSTANCE.create().toJson(this);
	}

	public static class WxMpUserBatchGetItem {

		private String openid;

		private String lang;

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public String getLang() {
			return lang;
		}

		public void setLang(String lang) {
			this.lang = lang;
		}

		@Override
		public String toString() {
			return "WxMpUserBatchGetItem [" + "openid=" + openid + ", lang="
					+ lang + "]";
		}
	}

	@Override
	public String toString() {
		return "WxMpUserBatchGet [" + "userList=" + userList + "]";
	}
}
