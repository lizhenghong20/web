package cn.farwalker.ravv.service.goodsext.viewlog.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogBo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品查看历史<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IGoodsViewLogDao extends BaseMapper<GoodsViewLogBo>{

    public int deleteViewLogByBatch(@Param("memberId") Long memberId, @Param("goodsIdList") List<Long> goodsIdList);
}