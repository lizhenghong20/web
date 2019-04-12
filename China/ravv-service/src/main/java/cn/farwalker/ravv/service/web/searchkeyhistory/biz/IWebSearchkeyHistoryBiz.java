package cn.farwalker.ravv.service.web.searchkeyhistory.biz;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.web.searchkeyhistory.constants.SearchTypeEnum;
import cn.farwalker.ravv.service.web.searchkeyhistory.model.WebSearchkeyHistoryBo;

import java.util.List;

/**
 * 搜索关键字历史<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IWebSearchkeyHistoryBiz extends IService<WebSearchkeyHistoryBo>{
	
	/**
	 * 存储用户关键字搜索记录
	 * @param memberId 会员id
	 * @param keyWords 关键字
	 * @return
	 */
	Boolean addKeyHistory(Long memberId, String keyWords, SearchTypeEnum searchType);
	
	/**
	 * 清楚会员所有历史记录
	 * @param memberId 会员id
	 * @return
	 */
	Boolean deleteAllKeyHistory(Long memberId);

	List<String> getSearchKey(Long memberId);
}