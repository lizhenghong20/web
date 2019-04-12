package cn.farwalker.ravv.service.goodsext.viewlog.biz;

import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogBo;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogVo;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface IGoodsViewLogService {

    /**
     * @description: 添加足迹，控制数量
     * @param: goodsId,memberId
     * @return boolean
     * @author Mr.Simple
     * @date 2018/12/24 17:11
     */
    public boolean addViewLog(Long memberId, Long goodsId);

    public List<GoodsViewLogVo> updateAndGetAllViewLog(Long memberId, int currentPage, int pageSize);

    public String deleteViewLog(Long memberId, Long goodsId);

    public String deleteByTime(Long memberId, Date time);

    public String deleteBatchViewLog(Long memberId, String goodsIds);
}
