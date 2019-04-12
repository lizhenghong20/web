package cn.farwalker.ravv.admin.youtube;

import cn.farwalker.ravv.admin.youtube.dto.LiveAnchorVO;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.Date;
import java.util.List;

public interface AdminYoutubeService {

    Boolean createLiveAnchor(YoutubeLiveAnchorBo vo);

    Boolean deleteLiveAnchor(Long id);

    YoutubeLiveAnchorBo getOneLiveAnchor(Long id);

    Page<LiveAnchorVO> getListLiveAnchor(List<QueryFilter> query, Integer start, Integer size,
                                      String sortfield);

    Boolean updateLiveAnchor(YoutubeLiveAnchorBo vo);

    Integer getMonthLiveDuration(Long anchorMemberId, Date month);

    Boolean freezeAnchor(YoutubeLiveAnchorBo vo);

    Boolean unfreezeAnchor(Long anchorId, Long sysUserId, String unfreezeReason);



    Boolean createLiveVideo(YoutubeLiveVideoBo vo);

    Boolean deleteLiveVideo(Long id);

    YoutubeLiveVideoBo getOneLiveVideo(Long id);

    Page<YoutubeLiveVideoBo> getListLiveVideo(List<QueryFilter> query, Integer start, Integer size,
                                         String sortfield, Long anchorMemberId);

    Boolean updateLiveVideo(YoutubeLiveVideoBo vo);
}
