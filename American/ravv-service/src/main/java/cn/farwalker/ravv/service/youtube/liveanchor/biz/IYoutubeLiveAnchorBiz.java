package cn.farwalker.ravv.service.youtube.liveanchor.biz;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;

/**
 * YouTube 主播表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IYoutubeLiveAnchorBiz extends IService<YoutubeLiveAnchorBo>{
	
	/**
	 * 校验主播推荐和排序是否超出或重复（目前推荐主播数为5个）
	 * @param anchorId
	 * @param sequence
	 * @return
	 */
	Boolean checkRecommend(Long anchorId, Long sequence);
}