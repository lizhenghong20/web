package cn.farwalker.ravv.admin.basebrand;

import cn.farwalker.ravv.service.base.brand.biz.IBaseBrandBiz;
import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AdminBaseBrandServiceImpl implements AdminBaseBrandService {
    @Resource
    private IBaseBrandBiz baseBrandBiz;
    protected IBaseBrandBiz getBiz(){
        return baseBrandBiz;
    }

    @Resource
    private IGoodsService goodsService;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(BaseBrandBo vo) {
        String url = GoodsUtil.getCdnRelativePath(vo.getLogoUrl());
        vo.setLogoUrl(url);
        Boolean rs =getBiz().insert(vo);
        if(!rs)
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        //判断仓库是否已关联商品
        List<GoodsBo> goodsList =  goodsService.getGoodsBybrandId(id);
        if(CollectionUtils.isNotEmpty(goodsList)) {
            throw new WakaException("该品牌已有关联商品，不能删除。");
        }
        Boolean rs =getBiz().deleteById(id);
        if(!rs)
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);

        return rs;
    }

    @Override
    public BaseBrandBo getOne(Long id) {
        BaseBrandBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        String url = GoodsUtil.getCdnFullPaths(rs.getLogoUrl());
        rs.setLogoUrl(url);
        return rs;
    }

    @Override
    public Page<BaseBrandBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        //createMethodSinge创建方法
        Page<BaseBrandBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<BaseBrandBo> wrap =ControllerUtils.getWrapper(query);
        Page<BaseBrandBo> rs =getBiz().selectPage(page,wrap);
        if(CollectionUtils.isNotEmpty(rs.getRecords())) {
            for(BaseBrandBo baseBrandBo : rs.getRecords()) {
                if(StringUtils.isNotEmpty(baseBrandBo.getLogoUrl())) {
                    baseBrandBo.setLogoUrl(QiniuUtil.getFullPath(baseBrandBo.getLogoUrl()));
                }
            }
        }else {
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean update(BaseBrandBo vo) {
        String url = GoodsUtil.getCdnRelativePath(vo.getLogoUrl());
        vo.setLogoUrl(url);
        Boolean rs =getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }
}
