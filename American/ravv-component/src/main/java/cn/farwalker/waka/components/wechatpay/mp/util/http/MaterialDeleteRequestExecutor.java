package cn.farwalker.waka.components.wechatpay.mp.util.http;

import cn.farwalker.waka.components.wechatpay.common.bean.result.WxError;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import cn.farwalker.waka.components.wechatpay.common.util.http.RequestExecutor;
import cn.farwalker.waka.components.wechatpay.common.util.http.Utf8ResponseHandler;
import cn.farwalker.waka.components.wechatpay.common.util.json.WxGsonBuilder;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MaterialDeleteRequestExecutor implements RequestExecutor<Boolean, String> {


  public MaterialDeleteRequestExecutor() {
    super();
  }

  public Boolean execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String materialId) throws WxErrorException, ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (httpProxy != null) {
      RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
      httpPost.setConfig(config);
    }

    Map<String, String> params = new HashMap<>();
    params.put("media_id", materialId);
    httpPost.setEntity(new StringEntity(WxGsonBuilder.create().toJson(params)));
    CloseableHttpResponse response = httpclient.execute(httpPost);
    String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
    WxError error = WxError.fromJson(responseContent);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    } else {
      return true;
    }
  }

}
