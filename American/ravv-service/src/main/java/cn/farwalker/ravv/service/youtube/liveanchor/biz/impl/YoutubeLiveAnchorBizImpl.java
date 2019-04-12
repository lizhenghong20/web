package cn.farwalker.ravv.service.youtube.liveanchor.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.dao.IYoutubeLiveAnchorDao;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;

/**
 * YouTube 主播表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class YoutubeLiveAnchorBizImpl extends ServiceImpl<IYoutubeLiveAnchorDao, YoutubeLiveAnchorBo>
		implements IYoutubeLiveAnchorBiz {

	@Resource
	private IYoutubeLiveAnchorBiz youtubeLiveAnchorBiz;

	@Override
	public Boolean checkRecommend(Long anchorId, Long sequence) {
		// 检查主播推荐排序是否重复
		Wrapper<YoutubeLiveAnchorBo> repeatWrap = new EntityWrapper<>();
		repeatWrap.eq(YoutubeLiveAnchorBo.Key.sequence.toString(), sequence);

		YoutubeLiveAnchorBo anchor = youtubeLiveAnchorBiz.selectOne(repeatWrap);
		if (null != anchor) {
			if (!anchor.getId().equals(anchorId)) {
				throw new WakaException("输入的主播推荐排序已存在，不能重复");
			}
		} else {
			Boolean isRecommend = true;
			if(null != anchorId) {
				// 判断是否为推荐主播,不是则进行推荐数量校验
				Wrapper<YoutubeLiveAnchorBo> recommendWrap = new EntityWrapper<>();
				recommendWrap.eq(YoutubeLiveAnchorBo.Key.id.toString(), anchorId);

				YoutubeLiveAnchorBo recommendAnchor = youtubeLiveAnchorBiz.selectOne(recommendWrap);
				if (null != recommendAnchor && null != recommendAnchor.getSequence()) {
					isRecommend = false;
				}
			}

			if(isRecommend) {
				// 检查主播推荐数量是否超过5个
				Wrapper<YoutubeLiveAnchorBo> overWrap = new EntityWrapper<>();
				overWrap.isNotNull(YoutubeLiveAnchorBo.Key.sequence.toString());

				List<YoutubeLiveAnchorBo> recommendAnchorList = youtubeLiveAnchorBiz.selectList(overWrap);
				if (recommendAnchorList.size() >= 5) {
					throw new WakaException("主播推荐最多为五名，不能超出");
				}
			}
		}

		return true;
	}
}