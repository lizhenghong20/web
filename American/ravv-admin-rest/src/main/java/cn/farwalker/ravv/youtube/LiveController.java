package cn.farwalker.ravv.youtube;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.ravv.youtube.liveanchor.LiveAnchorController;

/**
 * YouTube 直播<br/>
 * @author chensl
 */
@RestController
@RequestMapping("/youtube/live")
public class LiveController{
	private final static  Logger log =LoggerFactory.getLogger(LiveAnchorController.class);

//	/**
//	 * 查看主播某月直播时长
//	 * @param anchorMemberId 主播id
//	 * @param month 查看的月份
//	 * @return
//	 */
//    @RequestMapping("/month_live_duration")
//    public JsonResult<?> getMonthLiveDuration(Long anchorMemberId, Date month){
//    	
//    	return JsonResult.newSuccess(null);
//    }
	
    
}
