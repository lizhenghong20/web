package cn.farwalker.waka.components.wechatpay.common.bean.result;

import me.chanjar.weixin.common.util.json.WxGsonBuilder;

import java.io.Serializable;

public class WxMediaUploadImgResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static WxMediaUploadImgResult fromJson(String json) {
		return WxGsonBuilder.create().fromJson(json,
				WxMediaUploadImgResult.class);
	}

	@Override
	public String toString() {
		return "WxMediaUploadImgResult [url=" + url + "]";
	}
}
