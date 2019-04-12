package cn.farwalker.ravv.service.web.webmodel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;

/**
 * 模块商品<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Repository
public interface IWebModelGoodsDao extends BaseMapper<WebModelGoodsBo> {
	List<GoodsListVo> selectHomePage(@Param("modelCode")String modelCode, @Param("showType")String showType);

	List<Long> selectGoodsIdListByMenuId(Page page,@Param("modelCode")String modelCode, @Param("menuIdList")List<Long> menuIdList);

	/**
	 * 条件联表搜索
	 * @param goodsName
	 * @param modelCode
	 * @param start
	 * @param size
	 * @return
	 */
    List<WebModelGoodsVo> searchModelGoods(@Param("goodsName")String goodsName, @Param("modelCode") String modelCode, @Param("showType")String showType, @Param("start")Integer start,  @Param("size")Integer size);
    
    /**
     * 联表计算总数
     * @param goodsName
     * @param modelCode
     * @return
     */
    Integer countModelGoods(@Param("goodsName")String goodsName, @Param("modelCode") String modelCode, @Param("showType")String showType);
}