package cn.farwalker.ravv.service.goodsext.comment.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品评论<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IGoodsCommentDao extends BaseMapper<GoodsCommentBo>{
    public List<GoodsCommentBo> selectByCondition(Page page, @Param("goodsId") Long goodsId,
                                                  @Param("goodsPoint") int goodsPoint,
                                                  @Param("picture") boolean picture,
                                                  @Param("addtion") boolean addtion);

    public List<GoodsCommentBo> selectByGoodsIdOrderByPoint(Long goodsId);

}