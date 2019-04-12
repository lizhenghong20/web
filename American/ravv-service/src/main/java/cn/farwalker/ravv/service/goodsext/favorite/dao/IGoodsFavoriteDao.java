package cn.farwalker.ravv.service.goodsext.favorite.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品收藏<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IGoodsFavoriteDao extends BaseMapper<GoodsFavoriteBo>{

    public int deleteFavoriteByBatch(@Param("memberId") Long memberId, @Param("goodsIdList") List<Long> goodsIdList);
}