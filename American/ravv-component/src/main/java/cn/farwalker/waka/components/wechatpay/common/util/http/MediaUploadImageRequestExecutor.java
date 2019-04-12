package cn.farwalker.waka.components.wechatpay.common.util.http;

import cn.farwalker.waka.components.wechatpay.common.bean.result.WxError;
import cn.farwalker.waka.components.wechatpay.common.bean.result.WxMediaUploadImgResult;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;

/**
 * 上传媒体文件请求执行器，请求的参数是File, 返回的结果是String
 * 
 * @author Jack Cui
 *
 */
public class MediaUploadImageRequestExecutor implements
		RequestExecutor<WxMediaUploadImgResult, File> {

	@Override
	public WxMediaUploadImgResult execute(CloseableHttpClient httpclient,
			HttpHost httpProxy, String uri, File file) throws WxErrorException,
			ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		if (httpProxy != null) {
			RequestConfig config = RequestConfig.custom().setProxy(httpProxy)
					.build();
			httpPost.setConfig(config);
		}
		if (file != null) {
			HttpEntity entity = MultipartEntityBuilder.create()
					.addBinaryBody("media", file)
					.setMode(HttpMultipartMode.RFC6532).build();
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-Type",
					ContentType.MULTIPART_FORM_DATA.toString());
		}
		try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
			String responseContent = Utf8ResponseHandler.INSTANCE
					.handleResponse(response);
			WxError error = WxError.fromJson(responseContent);
			if (error.getErrorCode() != 0) {
				throw new WxErrorException(error);
			}
			return WxMediaUploadImgResult.fromJson(responseContent);
		}
	}

}
