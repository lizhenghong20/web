package cn.farwalker.ravv.service.web.searchkeyhistory.biz.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.mapper.Condition;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.web.searchkeyhistory.model.WebSearchkeyHistoryBo;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.ravv.service.web.searchkeyhistory.dao.IWebSearchkeyHistoryDao;
import cn.farwalker.ravv.service.web.searchkeyhistory.biz.IWebSearchkeyHistoryBiz;
import cn.farwalker.ravv.service.web.searchkeyhistory.constants.SearchTypeEnum;
import org.springframework.transaction.annotation.Transactional;

/**
 * 搜索关键字历史<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class WebSearchkeyHistoryBizImpl extends ServiceImpl<IWebSearchkeyHistoryDao,WebSearchkeyHistoryBo> implements IWebSearchkeyHistoryBiz{

	@Resource
	private IWebSearchkeyHistoryBiz webSearchkeyHistoryBiz;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean addKeyHistory(Long memberId, String keyWords, SearchTypeEnum searchType) {
		//检查是否有相同的关键字
		Wrapper<WebSearchkeyHistoryBo> wrapper = new EntityWrapper<>();
		wrapper.eq(WebSearchkeyHistoryBo.Key.memberId.toString(), memberId);
		wrapper.eq(WebSearchkeyHistoryBo.Key.word.toString(), keyWords);
		if(null != searchType) {
			wrapper.eq(WebSearchkeyHistoryBo.Key.searchType.toString(), searchType);
		}
		
		WebSearchkeyHistoryBo sameHistory = webSearchkeyHistoryBiz.selectOne(wrapper);
		
		Boolean rs = true;
		if(null != sameHistory) {
			sameHistory.setGmtModified(new Date());
			sameHistory.setSearchCount(sameHistory.getSearchCount() + 1);
			rs = webSearchkeyHistoryBiz.updateById(sameHistory);
		}else {
			WebSearchkeyHistoryBo historyBo = new WebSearchkeyHistoryBo();
			historyBo.setMemberId(memberId);
			historyBo.setSearchType(searchType);
			historyBo.setWord(keyWords);
			historyBo.setSearchCount(0);
			
			rs = webSearchkeyHistoryBiz.insert(historyBo);
		}

		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean deleteAllKeyHistory(Long memberId) {
		//获取会员所有的搜索记录
		Wrapper<WebSearchkeyHistoryBo> wrapper = new EntityWrapper<>();
		wrapper.eq(WebSearchkeyHistoryBo.Key.memberId.toString(), memberId);
		
		List<WebSearchkeyHistoryBo> keyHistoryList = webSearchkeyHistoryBiz.selectList(wrapper);
		
		Boolean rs = true;
		if(Tools.collection.isEmpty(keyHistoryList)) {
			rs = false;
		}
		
		List<Long> keyHistoryIdList = new ArrayList<>();
		for(WebSearchkeyHistoryBo keyHistory : keyHistoryList) {
			keyHistoryIdList.add(keyHistory.getId());
		}
		
		rs = webSearchkeyHistoryBiz.deleteBatchIds(keyHistoryIdList);
		
		return rs;
	}

	@Override
	public List<String> getSearchKey(Long memberId) {
		List<String> key = new ArrayList<>();
		List<WebSearchkeyHistoryBo> searchKeyList = webSearchkeyHistoryBiz.selectList(Condition.create()
								.eq(WebSearchkeyHistoryBo.Key.memberId.toString(), memberId)
								.isNotNull(WebSearchkeyHistoryBo.Key.word.toString())
								.orderBy(WebSearchkeyHistoryBo.Key.gmtModified.toString(), false));
		if(searchKeyList.size() != 0){
			//处理word为空的字段
			searchKeyList.forEach(item->{
				if(Tools.string.isNotEmpty(item.getWord()))
					key.add(item.getWord());
			});
		}
		return key;
	}
}