package cn.farwalker.ravv.service.youtube.livevideo.model;

import com.google.api.client.util.DateTime;
import lombok.Data;

/**
 * Created by asus on 2018/12/28.
 */
@Data
public class YouTubeSearchForm {
    String part = "snippet";
    String eventType;//completed,live,upcoming
    String queryItem = "ravv";
    String type = "video";
    String channelId = "UCoQQDRxvK0yqW_Ap5Tc-cVA";
    String videoEmbeddable="any";//any,true
    DateTime publishedAfter;
    DateTime publishedBefore;
    long   maxResults = 50;
}
