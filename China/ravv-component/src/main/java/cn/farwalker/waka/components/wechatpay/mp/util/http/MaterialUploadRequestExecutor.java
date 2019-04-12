package cn.farwalker.waka.components.wechatpay.mp.util.http;

import cn.farwalker.waka.components.wechatpay.common.bean.result.WxError;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import cn.farwalker.waka.components.wechatpay.common.util.http.RequestExecutor;
import cn.farwalker.waka.components.wechatpay.common.util.http.Utf8ResponseHandler;
import cn.farwalker.waka.components.wechatpay.common.util.json.WxGsonBuilder;
import cn.farwalker.waka.components.wechatpay.mp.bean.WxMpMaterial;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpMaterialUploadResult;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class MaterialUploadRequestExecutor implements RequestExecutor<WxMpMaterialUploadResult, WxMpMaterial> {

  public WxMpMaterialUploadResult execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, WxMpMaterial material) throws WxErrorException, ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (httpProxy != null) {
      RequestConfig response = RequestConfig.custom().setProxy(httpProxy).build();
      httpPost.setConfig(response);
    }

    if (material != null) {
      File file = material.getFile();
      if (file == null || !file.exists()) {
        throw new FileNotFoundException();
      }
      // cuijk commented
      // BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder
      .addBinaryBody("media", file)
      	// cuijk commented
         // .addPart("media", new InputStreamBody(bufferedInputStream, material.getName()))
          .setMode(HttpMultipartMode.RFC6532);
      Map<String, String> form = material.getForm();
      if (material.getForm() != null) {
        multipartEntityBuilder.addTextBody("description", WxGsonBuilder.create().toJson(form));
      }
      httpPost.setEntity(multipartEntityBuilder.build());
      httpPost.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.toString());
    }

    CloseableHttpResponse response = httpclient.execute(httpPost);
    String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
    WxError error = WxError.fromJson(responseContent);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    } else {
      return WxMpMaterialUploadResult.fromJson(responseContent);
    }
  }

}
