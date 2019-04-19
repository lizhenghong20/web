package cn.farwalker.waka.components.wechatpay.mp.api;

import cn.farwalker.waka.components.serviceForRavv.WechatSandBoxResult;
import cn.farwalker.waka.components.wechatpay.common.api.WxConsts;
import cn.farwalker.waka.components.wechatpay.common.bean.WxAccessToken;
import cn.farwalker.waka.components.wechatpay.common.bean.WxJsapiSignature;
import cn.farwalker.waka.components.wechatpay.common.bean.WxMenu;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxError;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxMediaUploadImgResult;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxMediaUploadResult;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import cn.farwalker.waka.components.wechatpay.common.session.StandardSessionManager;
import cn.farwalker.waka.components.wechatpay.common.session.WxSessionManager;
import cn.farwalker.waka.components.wechatpay.common.util.RandomUtils;
import cn.farwalker.waka.components.wechatpay.common.util.StringUtils;
import cn.farwalker.waka.components.wechatpay.common.util.crypto.SHA1;
import cn.farwalker.waka.components.wechatpay.common.util.crypto.WxCryptUtil;
import cn.farwalker.waka.components.wechatpay.common.util.fs.FileUtils;
import cn.farwalker.waka.components.wechatpay.common.util.http.*;
import cn.farwalker.waka.components.wechatpay.common.util.json.GsonHelper;
import cn.farwalker.waka.components.wechatpay.common.util.json.WxGsonBuilder;
import cn.farwalker.waka.components.wechatpay.common.util.xml.XStreamInitializer;
import cn.farwalker.waka.components.wechatpay.mp.bean.*;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.*;
import cn.farwalker.waka.components.wechatpay.mp.util.http.*;
import cn.farwalker.waka.components.wechatpay.mp.util.json.WxMpGsonBuilder;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.thoughtworks.xstream.XStream;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@Service("wxMpService")
public class WxMpServiceImpl implements WxMpService {

	protected final Logger log = LoggerFactory.getLogger(WxMpServiceImpl.class);

	/**
	 * 全局的是否正在刷新access token的锁
	 */
	protected final Object globalAccessTokenRefreshLock = new Object();

	/**
	 * 全局的是否正在刷新jsapi_ticket的锁
	 */
	protected final Object globalJsapiTicketRefreshLock = new Object();

	protected WxMpConfigStorage wxMpConfigStorage;

	protected CloseableHttpClient httpClient;

	protected HttpHost httpProxy;

	private int retrySleepMillis = 1000;

	private int maxRetryTimes = 5;

	protected WxSessionManager sessionManager = new StandardSessionManager();

	public boolean checkSignature(String timestamp, String nonce,
			String signature) {
		try {
			return SHA1.gen(wxMpConfigStorage.getToken(), timestamp, nonce)
					.equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	public String getAccessToken() throws WxErrorException {
		return getAccessToken(false);
	}

	public String getAccessToken(boolean forceRefresh) throws WxErrorException {
		if (forceRefresh) {
			wxMpConfigStorage.expireAccessToken();
		}
		if (wxMpConfigStorage.isAccessTokenExpired()) {
			synchronized (globalAccessTokenRefreshLock) {
				if (wxMpConfigStorage.isAccessTokenExpired()) {
					String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
							+ "&appid="
							+ wxMpConfigStorage.getAppId()
							+ "&secret=" + wxMpConfigStorage.getSecret();
					try {
						HttpGet httpGet = new HttpGet(url);
						if (httpProxy != null) {
							RequestConfig config = RequestConfig.custom()
									.setProxy(httpProxy).build();
							httpGet.setConfig(config);
						}
						CloseableHttpResponse response = getHttpclient()
								.execute(httpGet);
						String resultContent = new BasicResponseHandler()
								.handleResponse(response);
						WxError error = WxError.fromJson(resultContent);
						if (error.getErrorCode() != 0) {
							throw new WxErrorException(error);
						}
						WxAccessToken accessToken = WxAccessToken
								.fromJson(resultContent);
						wxMpConfigStorage.updateAccessToken(
								accessToken.getAccessToken(),
								accessToken.getExpiresIn());
					} catch (ClientProtocolException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return wxMpConfigStorage.getAccessToken();
	}

	public String getJsapiTicket() throws WxErrorException {
		return getJsapiTicket(false);
	}

	public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
		if (forceRefresh) {
			wxMpConfigStorage.expireJsapiTicket();
		}
		if (wxMpConfigStorage.isJsapiTicketExpired()) {
			synchronized (globalJsapiTicketRefreshLock) {
				if (wxMpConfigStorage.isJsapiTicketExpired()) {
					String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
					String responseContent = execute(
							new SimpleGetRequestExecutor(), url, null);
					JsonElement tmpJsonElement = Streams.parse(new JsonReader(
							new StringReader(responseContent)));
					JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
					String jsapiTicket = tmpJsonObject.get("ticket")
							.getAsString();
					int expiresInSeconds = tmpJsonObject.get("expires_in")
							.getAsInt();
					wxMpConfigStorage.updateJsapiTicket(jsapiTicket,
							expiresInSeconds);
				}
			}
		}
		return wxMpConfigStorage.getJsapiTicket();
	}

	public JSONObject createJsapiSinature3(String url) throws WxErrorException{
		JSONObject resultJson = new JSONObject();
		long timestamp = System.currentTimeMillis() / 1000;
		String noncestr = RandomUtils.getRandomStr();
		String jsapiTicket = getJsapiTicket(false);
		try {
			String signature = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
					"noncestr=" + noncestr, "timestamp=" + timestamp, "url="
							+ url);
			WxJsapiSignature jsapiSignature = new WxJsapiSignature();
			//jsapiSignature.setAppid(appId);
			jsapiSignature.setAppid(wxMpConfigStorage.getAppId());

			jsapiSignature.setTimestamp(timestamp);
			jsapiSignature.setNoncestr(noncestr);
			jsapiSignature.setUrl(url);
			jsapiSignature.setSignature(signature);
			resultJson.put("jsapiSignature",jsapiSignature);
			resultJson.put("jsapiTicket",jsapiTicket);
			return resultJson;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public WxJsapiSignature createJsapiSignature(String url)
			throws WxErrorException {
		long timestamp = System.currentTimeMillis() / 1000;
		String noncestr = RandomUtils.getRandomStr();
		String jsapiTicket = getJsapiTicket(false);
		try {
			String signature = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
					"noncestr=" + noncestr, "timestamp=" + timestamp, "url="
							+ url);
			WxJsapiSignature jsapiSignature = new WxJsapiSignature();
			jsapiSignature.setAppid(wxMpConfigStorage.getAppId());
			jsapiSignature.setTimestamp(timestamp);
			jsapiSignature.setNoncestr(noncestr);
			jsapiSignature.setUrl(url);
			jsapiSignature.setSignature(signature);
			return jsapiSignature;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public WxJsapiSignature createJsapiSignature2(String url)
			throws WxErrorException {
		String nonce_str = create_nonce_str();
		Long timestamp = create_timestamp();
		String string1;
		String signature = "";
		String jsapi_ticket = getJsapiTicket(false);
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		System.out.println(string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
			WxJsapiSignature jsapiSignature = new WxJsapiSignature();
			jsapiSignature.setAppid(wxMpConfigStorage.getAppId());
			jsapiSignature.setTimestamp(timestamp);
			jsapiSignature.setNoncestr(nonce_str);
			jsapiSignature.setUrl(url);
			jsapiSignature.setSignature(signature);
			return jsapiSignature;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private Long create_timestamp() {
		return System.currentTimeMillis() / 1000;
	}

	public void customMessageSend(WxMpCustomMessage message)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
		execute(new SimplePostRequestExecutor(), url, message.toJson());
	}

	public void menuCreate(WxMenu menu) throws WxErrorException {
		if (menu.getMatchRule() != null) {
			String url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional";
			execute(new SimplePostRequestExecutor(), url, menu.toJson());
		} else {
			String url = "https://api.weixin.qq.com/cgi-bin/menu/create";
			execute(new SimplePostRequestExecutor(), url, menu.toJson());
		}
	}

	public void menuDelete() throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
		execute(new SimpleGetRequestExecutor(), url, null);
	}

	public void menuDelete(String menuid) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delconditional";
		execute(new SimpleGetRequestExecutor(), url, "menuid=" + menuid);
	}

	public WxMenu menuGet() throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get";
		try {
			String resultContent = execute(new SimpleGetRequestExecutor(), url,
					null);
			return WxMenu.fromJson(resultContent);
		} catch (WxErrorException e) {
			// 46003 不存在的菜单数据
			if (e.getError().getErrorCode() == 46003) {
				return null;
			}
			throw e;
		}
	}

	public WxMenu menuTryMatch(String userid) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/trymatch";
		try {
			String resultContent = execute(new SimpleGetRequestExecutor(), url,
					"user_id=" + userid);
			return WxMenu.fromJson(resultContent);
		} catch (WxErrorException e) {
			// 46003 不存在的菜单数据 46002 不存在的菜单版本
			if (e.getError().getErrorCode() == 46003
					|| e.getError().getErrorCode() == 46002) {
				return null;
			}
			throw e;
		}
	}

	public WxMediaUploadResult mediaUpload(String mediaType, String fileType,
										   InputStream inputStream) throws WxErrorException, IOException {
		return mediaUpload(mediaType, FileUtils.createTmpFile(inputStream, UUID
				.randomUUID().toString(), fileType));
	}

	public WxMediaUploadResult mediaUpload(String mediaType, File file)
			throws WxErrorException {
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?type="
				+ mediaType;
		return execute(new MediaUploadRequestExecutor(), url, file);
	}

	@Override
	public WxMediaUploadImgResult mediaUploadImage(File file)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";
		return execute(new MediaUploadImageRequestExecutor(), url, file);
	}

	public File mediaDownload(String media_id) throws WxErrorException {
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
		System.out.println(wxMpConfigStorage.getTmpDirFile().getAbsolutePath());
		return execute(
				new MediaDownloadRequestExecutor(
						wxMpConfigStorage.getTmpDirFile()), url, "media_id="
						+ media_id);
	}

	public WxMpMaterialUploadResult materialFileUpload(String mediaType,
													   WxMpMaterial material) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?type="
				+ mediaType;
		return execute(new MaterialUploadRequestExecutor(), url, material);
	}

	public WxMpMaterialUploadResult materialNewsUpload(WxMpMaterialNews news)
			throws WxErrorException {
		if (news == null || news.isEmpty()) {
			throw new IllegalArgumentException("news is empty!");
		}
		String url = "https://api.weixin.qq.com/cgi-bin/material/add_news";
		String responseContent = post(url, news.toJson());
		return WxMpMaterialUploadResult.fromJson(responseContent);
	}

	public InputStream materialImageOrVoiceDownload(String media_id)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_material";
		return execute(new MaterialVoiceAndImageDownloadRequestExecutor(
				wxMpConfigStorage.getTmpDirFile()), url, media_id);
	}

	public WxMpMaterialVideoInfoResult materialVideoInfo(String media_id)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_material";
		return execute(new MaterialVideoInfoRequestExecutor(), url, media_id);
	}

	public WxMpMaterialNews materialNewsInfo(String media_id)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_material";
		return execute(new MaterialNewsInfoRequestExecutor(), url, media_id);
	}

	public boolean materialNewsUpdate(
			WxMpMaterialArticleUpdate wxMpMaterialArticleUpdate)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/update_news";
		String responseText = post(url, wxMpMaterialArticleUpdate.toJson());
		WxError wxError = WxError.fromJson(responseText);
		if (wxError.getErrorCode() == 0) {
			return true;
		} else {
			throw new WxErrorException(wxError);
		}
	}

	public boolean materialDelete(String media_id) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/del_material";
		return execute(new MaterialDeleteRequestExecutor(), url, media_id);
	}

	public WxMpMaterialCountResult materialCount() throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount";
		String responseText = get(url, null);
		WxError wxError = WxError.fromJson(responseText);
		if (wxError.getErrorCode() == 0) {
			return WxMpGsonBuilder.create().fromJson(responseText,
					WxMpMaterialCountResult.class);
		} else {
			throw new WxErrorException(wxError);
		}
	}

	public WxMpMaterialNewsBatchGetResult materialNewsBatchGet(int offset,
															   int count) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";
		Map<String, Object> params = new HashMap<>();
		params.put("type", WxConsts.MATERIAL_NEWS);
		params.put("offset", offset);
		params.put("count", count);
		String responseText = post(url, WxGsonBuilder.create().toJson(params));
		WxError wxError = WxError.fromJson(responseText);
		if (wxError.getErrorCode() == 0) {
			return WxMpGsonBuilder.create().fromJson(responseText,
					WxMpMaterialNewsBatchGetResult.class);
		} else {
			throw new WxErrorException(wxError);
		}
	}

	public WxMpMaterialFileBatchGetResult materialFileBatchGet(String type,
															   int offset, int count) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";
		Map<String, Object> params = new HashMap<>();
		params.put("type", type);
		params.put("offset", offset);
		params.put("count", count);
		String responseText = post(url, WxGsonBuilder.create().toJson(params));
		WxError wxError = WxError.fromJson(responseText);
		if (wxError.getErrorCode() == 0) {
			return WxMpGsonBuilder.create().fromJson(responseText,
					WxMpMaterialFileBatchGetResult.class);
		} else {
			throw new WxErrorException(wxError);
		}
	}

	public WxMpMassUploadResult massNewsUpload(WxMpMassNews news)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				news.toJson());
		return WxMpMassUploadResult.fromJson(responseContent);
	}

	public WxMpMassUploadResult massVideoUpload(WxMpMassVideo video)
			throws WxErrorException {
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				video.toJson());
		return WxMpMassUploadResult.fromJson(responseContent);
	}

	public WxMpMassSendResult massGroupMessageSend(WxMpMassGroupMessage message)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				message.toJson());
		return WxMpMassSendResult.fromJson(responseContent);
	}

	public WxMpMassSendResult massOpenIdsMessageSend(
			WxMpMassOpenIdsMessage message) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				message.toJson());
		return WxMpMassSendResult.fromJson(responseContent);
	}

	public WxMpGroup groupCreate(String name) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/create";
		JsonObject json = new JsonObject();
		JsonObject groupJson = new JsonObject();
		json.add("group", groupJson);
		groupJson.addProperty("name", name);

		String responseContent = execute(new SimplePostRequestExecutor(), url,
				json.toString());
		return WxMpGroup.fromJson(responseContent);
	}

	public List<WxMpGroup> groupGet() throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/get";
		String responseContent = execute(new SimpleGetRequestExecutor(), url,
				null);
		/*
		 * 操蛋的微信API，创建时返回的是 { group : { id : ..., name : ...} } 查询时返回的是 { groups
		 * : [ { id : ..., name : ..., count : ... }, ... ] }
		 */
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		return WxMpGsonBuilder.INSTANCE.create().fromJson(
				tmpJsonElement.getAsJsonObject().get("groups"),
				new TypeToken<List<WxMpGroup>>() {
				}.getType());
	}

	public long userGetGroup(String openid) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/getid";
		JsonObject o = new JsonObject();
		o.addProperty("openid", openid);
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				o.toString());
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		return GsonHelper.getAsLong(tmpJsonElement.getAsJsonObject().get(
				"groupid"));
	}

	public void groupUpdate(WxMpGroup group) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/update";
		execute(new SimplePostRequestExecutor(), url, group.toJson());
	}

	public void userUpdateGroup(String openid, long to_groupid)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update";
		JsonObject json = new JsonObject();
		json.addProperty("openid", openid);
		json.addProperty("to_groupid", to_groupid);
		execute(new SimplePostRequestExecutor(), url, json.toString());
	}

	public void userUpdateRemark(String openid, String remark)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark";
		JsonObject json = new JsonObject();
		json.addProperty("openid", openid);
		json.addProperty("remark", remark);
		execute(new SimplePostRequestExecutor(), url, json.toString());
	}

	public WxMpUser userInfo(String openid, String lang)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info";
		lang = lang == null ? "zh_CN" : lang;
		String responseContent = execute(new SimpleGetRequestExecutor(), url,
				"openid=" + openid + "&lang=" + lang);
		return WxMpUser.fromJson(responseContent);
	}

	public WxMpUserBatchGetResult userInfoBatchGet(
			WxMpUserBatchGet wxMpUserBatchGet) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget";
		String responseContent = post(url, wxMpUserBatchGet.toJson());
		return WxMpUserBatchGetResult.fromJson(responseContent);
	}

	public WxMpUserList userList(String next_openid) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/user/get";
		String responseContent = execute(new SimpleGetRequestExecutor(), url,
				next_openid == null ? null : "next_openid=" + next_openid);
		return WxMpUserList.fromJson(responseContent);
	}

	public WxMpQrCodeTicket qrCodeCreateTmpTicket(int scene_id,
			Integer expire_seconds) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
		JsonObject json = new JsonObject();
		json.addProperty("action_name", "QR_SCENE");
		if (expire_seconds != null) {
			json.addProperty("expire_seconds", expire_seconds);
		}
		JsonObject actionInfo = new JsonObject();
		JsonObject scene = new JsonObject();
		scene.addProperty("scene_id", scene_id);
		actionInfo.add("scene", scene);
		json.add("action_info", actionInfo);
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				json.toString());
		return WxMpQrCodeTicket.fromJson(responseContent);
	}

	public WxMpQrCodeTicket qrCodeCreateLastTicket(int scene_id)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
		JsonObject json = new JsonObject();
		json.addProperty("action_name", "QR_LIMIT_SCENE");
		JsonObject actionInfo = new JsonObject();
		JsonObject scene = new JsonObject();
		scene.addProperty("scene_id", scene_id);
		actionInfo.add("scene", scene);
		json.add("action_info", actionInfo);
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				json.toString());
		return WxMpQrCodeTicket.fromJson(responseContent);
	}

	public WxMpQrCodeTicket qrCodeCreateLastTicket(String scene_str)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
		JsonObject json = new JsonObject();
		json.addProperty("action_name", "QR_LIMIT_STR_SCENE");
		JsonObject actionInfo = new JsonObject();
		JsonObject scene = new JsonObject();
		scene.addProperty("scene_str", scene_str);
		actionInfo.add("scene", scene);
		json.add("action_info", actionInfo);
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				json.toString());
		return WxMpQrCodeTicket.fromJson(responseContent);
	}

	public File qrCodePicture(WxMpQrCodeTicket ticket) throws WxErrorException {
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
		return execute(new QrCodeRequestExecutor(), url, ticket);
	}

	public String shortUrl(String long_url) throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
		JsonObject o = new JsonObject();
		o.addProperty("action", "long2short");
		o.addProperty("long_url", long_url);
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				o.toString());
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
	}

	public String templateSend(WxMpTemplateMessage templateMessage)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				templateMessage.toJson());
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		final JsonObject jsonObject = tmpJsonElement.getAsJsonObject();
		if (jsonObject.get("errcode").getAsInt() == 0)
			return jsonObject.get("msgid").getAsString();
		throw new WxErrorException(WxError.fromJson(responseContent));
	}

	public WxMpSemanticQueryResult semanticQuery(WxMpSemanticQuery semanticQuery)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/semantic/semproxy/search";
		String responseContent = execute(new SimplePostRequestExecutor(), url,
				semanticQuery.toJson());
		return WxMpSemanticQueryResult.fromJson(responseContent);
	}

	@Override
	public String oauth2buildAuthorizationUrl(String scope, String state) {
		return this.oauth2buildAuthorizationUrl(
				wxMpConfigStorage.getOauth2redirectUri(), scope, state);
	}

	@Override
	public String oauth2buildAuthorizationUrl(String redirectURI, String scope,
			String state) {
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
		url += "appid=" + wxMpConfigStorage.getAppId();
		url += "&redirect_uri=" + URIUtil.encodeURIComponent(redirectURI);
		url += "&response_type=code";
		url += "&scope=" + scope;
		if (state != null) {
			url += "&state=" + state;
		}
		url += "#wechat_redirect";
		return url;
	}

	@Override
	public WxMpOAuth2AccessToken oauth2getAccessToken(String code)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
		url += "appid=" + wxMpConfigStorage.getAppId();
		url += "&secret=" + wxMpConfigStorage.getSecret();
		url += "&code=" + code;
		url += "&grant_type=authorization_code";

		try {
			RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
			String responseText = executor.execute(getHttpclient(), httpProxy,
					url, null);
			return WxMpOAuth2AccessToken.fromJson(responseText);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
		url += "appid=" + wxMpConfigStorage.getAppId();
		url += "&grant_type=refresh_token";
		url += "&refresh_token=" + refreshToken;

		try {
			RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
			String responseText = executor.execute(getHttpclient(), httpProxy,
					url, null);
			return WxMpOAuth2AccessToken.fromJson(responseText);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken,
			String lang) throws WxErrorException {
		String url = "https://api.weixin.qq.com/sns/userinfo?";
		url += "access_token=" + oAuth2AccessToken.getAccessToken();
		url += "&openid=" + oAuth2AccessToken.getOpenId();
		if (lang == null) {
			url += "&lang=zh_CN";
		} else {
			url += "&lang=" + lang;
		}

		try {
			RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
			String responseText = executor.execute(getHttpclient(), httpProxy,
					url, null);
			return WxMpUser.fromJson(responseText);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean oauth2validateAccessToken(
			WxMpOAuth2AccessToken oAuth2AccessToken) {
		String url = "https://api.weixin.qq.com/sns/auth?";
		url += "access_token=" + oAuth2AccessToken.getAccessToken();
		url += "&openid=" + oAuth2AccessToken.getOpenId();

		try {
			RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
			executor.execute(getHttpclient(), httpProxy, url, null);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (WxErrorException e) {
			return false;
		}
		return true;
	}

	@Override
	public String[] getCallbackIP() throws WxErrorException {
		String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
		String responseContent = get(url, null);
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list")
				.getAsJsonArray();
		String[] ipArray = new String[ipList.size()];
		for (int i = 0; i < ipList.size(); i++) {
			ipArray[i] = ipList.get(i).getAsString();
		}
		return ipArray;
	}

	@Override
	public List<WxMpUserSummary> getUserSummary(Date beginDate, Date endDate)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/datacube/getusersummary";
		JsonObject param = new JsonObject();
		param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
		param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
		String responseContent = post(url, param.toString());
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		return WxMpGsonBuilder.INSTANCE.create().fromJson(
				tmpJsonElement.getAsJsonObject().get("list"),
				new TypeToken<List<WxMpUserSummary>>() {
				}.getType());
	}

	@Override
	public List<WxMpUserCumulate> getUserCumulate(Date beginDate, Date endDate)
			throws WxErrorException {
		String url = "https://api.weixin.qq.com/datacube/getusercumulate";
		JsonObject param = new JsonObject();
		param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
		param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
		String responseContent = post(url, param.toString());
		JsonElement tmpJsonElement = Streams.parse(new JsonReader(
				new StringReader(responseContent)));
		return WxMpGsonBuilder.INSTANCE.create().fromJson(
				tmpJsonElement.getAsJsonObject().get("list"),
				new TypeToken<List<WxMpUserCumulate>>() {
				}.getType());
	}

	public String get(String url, String queryParam) throws WxErrorException {
		return execute(new SimpleGetRequestExecutor(), url, queryParam);
	}

	public String post(String url, String postData) throws WxErrorException {
		return execute(new SimplePostRequestExecutor(), url, postData);
	}

	/**
	 * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
	 *
	 * @param executor
	 * @param uri
	 * @param data
	 * @return
	 * @throws WxErrorException
	 */
	public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data)
			throws WxErrorException {
		int retryTimes = 0;
		do {
			try {
				return executeInternal(executor, uri, data);
			} catch (WxErrorException e) {
				WxError error = e.getError();
				/**
				 * -1 系统繁忙, 1000ms后重试
				 */
				if (error.getErrorCode() == -1) {
					int sleepMillis = retrySleepMillis * (1 << retryTimes);
					try {
						log.debug("微信系统繁忙，{}ms 后重试(第{}次)", sleepMillis,
								retryTimes + 1);
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e1) {
						throw new RuntimeException(e1);
					}
				} else {
					throw e;
				}
			}
		} while (++retryTimes < maxRetryTimes);

		throw new RuntimeException("微信服务端异常，超出重试次数");
	}

	protected synchronized <T, E> T executeInternal(
			RequestExecutor<T, E> executor, String uri, E data)
			throws WxErrorException {
		if (uri.indexOf("access_token=") != -1) {
			throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
		}
		String accessToken = getAccessToken(false);

		String uriWithAccessToken = uri;
		uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token="
				+ accessToken : "&access_token=" + accessToken;

		try {
			return executor.execute(getHttpclient(), httpProxy,
					uriWithAccessToken, data);
		} catch (WxErrorException e) {
			WxError error = e.getError();
			/*
			 * 发生以下情况时尝试刷新access_token 40001
			 * 获取access_token时AppSecret错误，或者access_token无效 42001 access_token超时
			 */
			if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
				// 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access
				// token
				wxMpConfigStorage.expireAccessToken();
				return execute(executor, uri, data);
			}
			if (error.getErrorCode() != 0) {
				throw new WxErrorException(error);
			}
			return null;
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected CloseableHttpClient getHttpclient() {
		return httpClient;
	}

	public void setWxMpConfigStorage(WxMpConfigStorage wxConfigProvider) {
		this.wxMpConfigStorage = wxConfigProvider;

		String http_proxy_host = wxMpConfigStorage.getHttp_proxy_host();
		int http_proxy_port = wxMpConfigStorage.getHttp_proxy_port();
		String http_proxy_username = wxMpConfigStorage.getHttp_proxy_username();
		String http_proxy_password = wxMpConfigStorage.getHttp_proxy_password();

		final HttpClientBuilder builder = HttpClients.custom();
		if (StringUtils.isNotBlank(http_proxy_host)) {
			// 使用代理服务器
			if (StringUtils.isNotBlank(http_proxy_username)) {
				// 需要用户认证的代理服务器
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(http_proxy_host,
						http_proxy_port), new UsernamePasswordCredentials(
						http_proxy_username, http_proxy_password));
				builder.setDefaultCredentialsProvider(credsProvider);
			} else {
				// 无需用户认证的代理服务器
			}
			httpProxy = new HttpHost(http_proxy_host, http_proxy_port);
		}
		if (wxConfigProvider.getSSLContext() != null) {
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					wxConfigProvider.getSSLContext(),
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			builder.setSSLSocketFactory(sslsf);
		}
		httpClient = builder.build();
	}

	@Override
	public void setRetrySleepMillis(int retrySleepMillis) {
		this.retrySleepMillis = retrySleepMillis;
	}

	@Override
	public void setMaxRetryTimes(int maxRetryTimes) {
		this.maxRetryTimes = maxRetryTimes;
	}

	@Override
	public WxMpPrepayIdResult getPrepayId(String openId, String outTradeNo,
			double amt, String body, String tradeType, String ip,
			String callbackUrl) {
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		packageParams.put("body", body);
		packageParams.put("out_trade_no", outTradeNo);
		packageParams.put("total_fee", (int) (amt * 100) + "");
		packageParams.put("spbill_create_ip", ip);
		packageParams.put("notify_url", callbackUrl);
		packageParams.put("trade_type", tradeType);
		packageParams.put("openid", openId);

		return getPrepayId(packageParams);
	}

	public WxMpPrepayIdResult getPrepayId(final Map<String, String> parameters) {
		String nonce_str = System.currentTimeMillis() + "";

		final SortedMap<String, String> packageParams = new TreeMap<String, String>(
				parameters);
		packageParams.put("appid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		packageParams.put("nonce_str", nonce_str);
		checkParameters(packageParams);

		String sign = WxCryptUtil.createSign(packageParams,
				wxMpConfigStorage.getPartnerKey());
		packageParams.put("sign", sign);

		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : packageParams.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");

		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/pay/unifiedorder");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}

		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = getHttpclient().execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpPrepayIdResult.class);
			WxMpPrepayIdResult wxMpPrepayIdResult = (WxMpPrepayIdResult) xstream
					.fromXML(responseContent);
			return wxMpPrepayIdResult;
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to get prepay id due to IO exception.", e);
		}
	}

	@Override
	public WxMpPrepayIdResult getPrepayIdWithSandBox(final Map<String, String> parameters) {

		final SortedMap<String, String> packageParams = new TreeMap<String, String>(
				parameters);
		packageParams.put("appid", parameters.get("app_id"));
		packageParams.put("mch_id", parameters.get("partner_id"));
		packageParams.put("nonce_str", parameters.get("nonce_str"));
		String sign = WxCryptUtil.createSign(packageParams,
				wxMpConfigStorage.getPartnerKey());
		//先获取sandbox_signKey,再重新生成签名
		Map<String,String> sandSignParam = new HashMap<>();
		sandSignParam.put("mch_id", parameters.get("partner_id"));
		sandSignParam.put("nonce_str", parameters.get("nonce_str"));
		sandSignParam.put("sign", sign);
		//获取signKey
		String signKey = getSignKeyWithSandBox(sandSignParam);
		String finalSign = WxCryptUtil.createSign(packageParams,
				signKey);
		packageParams.put("sign",finalSign);

		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : packageParams.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");

		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}

		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = getHttpclient().execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpPrepayIdResult.class);
			WxMpPrepayIdResult wxMpPrepayIdResult = (WxMpPrepayIdResult) xstream
					.fromXML(responseContent);
			wxMpPrepayIdResult.setSign(finalSign);
			return wxMpPrepayIdResult;
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to get prepay id due to IO exception.", e);
		}
	}

	@Override
	public String getSignBySandBox(Map<String, String> parameters) {
		return null;
	}


	final String[] REQUIRED_ORDER_PARAMETERS = new String[] { "appid",
			"mch_id", "body", "out_trade_no", "total_fee", "spbill_create_ip",
			"notify_url", "trade_type", };

	private void checkParameters(Map<String, String> parameters) {
		for (String para : REQUIRED_ORDER_PARAMETERS) {
			if (!parameters.containsKey(para))
				throw new IllegalArgumentException("Reqiured argument '" + para
						+ "' is missing.");
		}
		if ("JSAPI".equals(parameters.get("trade_type"))
				&& !parameters.containsKey("openid"))
			throw new IllegalArgumentException(
					"Reqiured argument 'openid' is missing when trade_type is 'JSAPI'.");
		if ("NATIVE".equals(parameters.get("trade_type"))
				&& !parameters.containsKey("product_id"))
			throw new IllegalArgumentException(
					"Reqiured argument 'product_id' is missing when trade_type is 'NATIVE'.");
	}

	@Override
	public Map<String, String> getJSSDKPayInfo(String openId,
			String outTradeNo, double amt, String body, String tradeType,
			String ip, String callbackUrl) {
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		packageParams.put("body", body);
		packageParams.put("out_trade_no", outTradeNo);
		packageParams.put("total_fee", (int) (amt * 100) + "");
		packageParams.put("spbill_create_ip", ip);
		packageParams.put("notify_url", callbackUrl);
		packageParams.put("trade_type", tradeType);
		packageParams.put("openid", openId);

		return getJSSDKPayInfo(packageParams);
	}

	/**
	 * @description: 签名出错注意查看时间戳字母大小写
	 * @param: parameter
	 * @return map
	 * @author Mr.Simple
	 * @date 2018/11/28 15:52
	 */
	@Override
	public Map<String, String> getJSSDKPayInfo(Map<String, String> parameters) {
		WxMpPrepayIdResult wxMpPrepayIdResult = getPrepayId(parameters);
		String prepayId = wxMpPrepayIdResult.getPrepay_id();
		if (prepayId == null || prepayId.equals("")) {

			throw new WakaException(String.format(wxMpPrepayIdResult.getErr_code_des()));

//			throw new RuntimeException(String.format(
//					"Failed to get prepay id due to error code '%s'(%s).",
//					wxMpPrepayIdResult.getErr_code(),
//					wxMpPrepayIdResult.getErr_code_des()));
		}

		Map<String, String> payInfo = new HashMap<String, String>();
		payInfo.put("appid", wxMpConfigStorage.getAppId());
		payInfo.put("partnerid", wxMpConfigStorage.getPartnerId());
		payInfo.put("prepayid", prepayId);
		payInfo.put("package", "Sign=WXPay");
		payInfo.put("noncestr", System.currentTimeMillis() + "");
		// 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
		payInfo.put("timestamp",
				String.valueOf(System.currentTimeMillis() / 1000));
//		payInfo.put("signType", "MD5");

		String finalSign = WxCryptUtil.createSign(payInfo,
				wxMpConfigStorage.getPartnerKey());
		payInfo.put("sign", finalSign);
		return payInfo;
	}

	/**
	 * @description: 重写逻辑，沙箱环境下签名需要用请求到的签名
	 * @param: parameter
	 * @return map
	 * @author Mr.Simple
	 * @date 2018/11/28 11:08
	 */
	@Override
	public Map<String, String> getJSSDKPayInfoWithSandBox(Map<String, String> parameters) {
		//沙箱逻辑下，要先获取signKey（替换partnerId）再做操作
		String noncestr = System.currentTimeMillis() + "";
		parameters.put("app_id", wxMpConfigStorage.getAppId());
		parameters.put("partner_id", wxMpConfigStorage.getPartnerId());
		parameters.put("nonce_str", noncestr);
		WxMpPrepayIdResult wxMpPrepayIdResult = getPrepayIdWithSandBox(parameters);
		String prepayId = wxMpPrepayIdResult.getPrepay_id();
		if (prepayId == null || prepayId.equals("")) {

			throw new WakaException(String.format(wxMpPrepayIdResult.getErr_code_des()));

//			throw new RuntimeException(String.format(
//					"Failed to get prepay id due to error code '%s'(%s).",
//					wxMpPrepayIdResult.getErr_code(),
//					wxMpPrepayIdResult.getErr_code_des()));
		}

		Map<String, String> payInfo = new HashMap<String, String>();
		payInfo.put("appid", wxMpConfigStorage.getAppId());
		payInfo.put("partnerid", wxMpConfigStorage.getPartnerId());
		payInfo.put("prepayid", prepayId);
		payInfo.put("package", "Sign=WXPay");
		payInfo.put("noncestr", noncestr);
		// 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
		payInfo.put("timestamp",
				String.valueOf(System.currentTimeMillis() / 1000));
//		payInfo.put("signType", "MD5");

		String finalSign = wxMpPrepayIdResult.getSign();
//		String finalSign = WxCryptUtil.createSign(payInfo,
//				wxMpConfigStorage.getPartnerKey());
		payInfo.put("sign", finalSign);
		//判断是否含有二维码链接
		if(Tools.string.isNotEmpty(wxMpPrepayIdResult.getCode_url())){
			payInfo.put("code_url", wxMpPrepayIdResult.getCode_url());
		}

		return payInfo;
	}

	@Override
	public WxMpPayResult getJSSDKPayResult(String transactionId,
										   String outTradeNo) {
		String nonce_str = System.currentTimeMillis() + "";

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		if (transactionId != null && !"".equals(transactionId.trim()))
			packageParams.put("transaction_id", transactionId);
		else if (outTradeNo != null && !"".equals(outTradeNo.trim()))
			packageParams.put("out_trade_no", outTradeNo);
		else
			throw new IllegalArgumentException(
					"Either 'transactionId' or 'outTradeNo' must be given.");
		packageParams.put("nonce_str", nonce_str);
		packageParams.put(
				"sign",
				WxCryptUtil.createSign(packageParams,
						wxMpConfigStorage.getPartnerKey()));

		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : packageParams.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");

		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/pay/orderquery");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}

		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpPayResult.class);
			WxMpPayResult wxMpPayResult = (WxMpPayResult) xstream
					.fromXML(responseContent);
			return wxMpPayResult;
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to query order due to IO exception.", e);
		}
	}


	@Override
	public WxMpRefundResult getJSSDKRefundResult(String transactionId,
												 String outRefundNo,
												 BigDecimal totalFee,
												 BigDecimal refundFee,
												 String refundDesc) {
		String nonce_str = System.currentTimeMillis() + "";

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		packageParams.put("nonce_str", nonce_str);
		if (transactionId != null && !"".equals(transactionId.trim())) {
			packageParams.put("transaction_id", transactionId);
			packageParams.put("out_refund_no", outRefundNo);
		} else {
			throw new IllegalArgumentException(
					"Either 'transactionId' or 'outRefundNo' must be given.");
		}
		packageParams.put("total_fee", totalFee + "");
		packageParams.put("refund_fee", refundFee + "");
		packageParams.put("refund_desc", refundDesc);
		packageParams.put(
				"sign",
				WxCryptUtil.createSign(packageParams,
						wxMpConfigStorage.getPartnerKey()));

		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : packageParams.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");

		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/secapi/pay/refund");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}

		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpRefundResult.class);
			WxMpRefundResult wxMpRefundResult = (WxMpRefundResult) xstream
					.fromXML(responseContent);
			return wxMpRefundResult;
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to refund order due to IO exception.", e);
		}
	}

	@Override
	public Map<String, String> getJSSDKQueryPay(String transactionId, String outTradeNo) {
		Map<String, String> result = new TreeMap<String, String>();


		WxMpPayResult wxMpRedpackResult = getJSSDKPayResult(transactionId, outTradeNo);
		if (!wxMpRedpackResult.getReturn_code().equals("SUCCESS")) {
			WxError error = new WxError();
			log.error("签名失败");
			error.setErrorCode(-1);
			throw new WakaException("微信签名错误");
		}
		if (wxMpRedpackResult.getTrade_state().equals("SUCCESS")) {
			result.put("transaction_id", wxMpRedpackResult.getTransaction_id());
			result.put("out_trade_no", wxMpRedpackResult.getOut_trade_no());
			BigDecimal amount = new BigDecimal(wxMpRedpackResult.getTotal_fee()).divide(new BigDecimal(100));
			result.put("金额", amount + "");
		} else
			throw new WakaException("解析失败");

		return result;
	}

	@Override
	public WxMpPayCallback getJSSDKCallbackData(String xmlData) {
		try {
			XStream xstream = XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpPayCallback.class);
			WxMpPayCallback wxMpCallback = (WxMpPayCallback) xstream
					.fromXML(xmlData);
			return wxMpCallback;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new WxMpPayCallback();
	}

	@Override
	public boolean checkJSSDKCallbackDataSignature(Map<String, String> kvm,
			String signature) {
		return signature.equals(WxCryptUtil.createSign(kvm,
				wxMpConfigStorage.getPartnerKey()));
	}

	@Override
	public WxRedpackResult sendRedpack(Map<String, String> parameters)
			throws WxErrorException {
		String nonce_str = System.currentTimeMillis() + "";

		SortedMap<String, String> packageParams = new TreeMap<String, String>(
				parameters);
		packageParams.put("wxappid", wxMpConfigStorage.getAppId());
		packageParams.put("mch_id", wxMpConfigStorage.getPartnerId());
		packageParams.put("nonce_str", nonce_str);

		String sign = WxCryptUtil.createSign(packageParams,
				wxMpConfigStorage.getPartnerKey());
		packageParams.put("sign", sign);

		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : packageParams.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");

		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}

		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = getHttpclient().execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = XStreamInitializer.getInstance();
			xstream.processAnnotations(WxRedpackResult.class);
			WxRedpackResult wxMpRedpackResult = (WxRedpackResult) xstream
					.fromXML(responseContent);
			return wxMpRedpackResult;
		} catch (IOException e) {
			log.error(
					MessageFormatter
							.format("The exception was happened when sending redpack '{}'.",
									request.toString()).getMessage(), e);
			WxError error = new WxError();
			error.setErrorCode(-1);
			throw new WxErrorException(error);
		}
	}

	public WxMpConfigStorage getWxMpConfigStorage() {
		return wxMpConfigStorage;
	}

	public String getSignKeyWithSandBox(Map<String,String> signParam){
		StringBuilder request = new StringBuilder("<xml>");
		for (Entry<String, String> para : signParam.entrySet()) {
			request.append(String.format("<%s>%s</%s>", para.getKey(),
					para.getValue(), para.getKey()));
		}
		request.append("</xml>");
		HttpPost httpPost = new HttpPost(
				"https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey");
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}
		String signkey = null;
		StringEntity entity = new StringEntity(request.toString(), Consts.UTF_8);
		httpPost.setEntity(entity);
		try {
			CloseableHttpResponse response = getHttpclient().execute(httpPost);
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			XStream xstream = me.chanjar.weixin.common.util.xml.XStreamInitializer.getInstance();
			xstream.alias("xml", WxMpPrepayIdResult.class);
			WechatSandBoxResult sandBoxResult = (WechatSandBoxResult) xstream.fromXML(responseContent);
//			WxMpPrepayIdResult wxMpPrepayIdResult = (WxMpPrepayIdResult) xstream
//					.fromXML(responseContent);
			signkey = sandBoxResult.getSandbox_signkey();
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to get prepay id due to IO exception.", e);
		}
		return signkey;
	}
}
