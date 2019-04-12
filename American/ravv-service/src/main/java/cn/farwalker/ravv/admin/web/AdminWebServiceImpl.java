package cn.farwalker.ravv.admin.web;

import cn.farwalker.ravv.service.web.menu.biz.IWebMenuBiz;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsVo;
import cn.farwalker.ravv.service.web.slider.biz.IWebSliderBiz;
import cn.farwalker.ravv.service.web.slider.model.WebSliderBo;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsService;
import cn.farwalker.ravv.service.web.webmodel.constants.ModelShowTypeEnum;
import cn.farwalker.ravv.service.web.webmodel.constants.ShowTypeEnum;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminWebServiceImpl implements  AdminWebService {

    @Resource
    private IWebMenuBiz webMenuBiz;

    protected IWebMenuBiz getBiz() {
        return webMenuBiz;
    }

    @Resource
    private IWebMenuGoodsBiz webMenuGoodsBiz;

    protected IWebMenuGoodsBiz getWebMenuGoodsBiz() {
        return webMenuGoodsBiz;
    }

    @Resource
    private IWebModelBiz webModelBiz;

    protected IWebModelBiz getWebModelBiz(){
        return webModelBiz;
    }

    @Resource
    private IWebModelGoodsService webModelGoodsService;

    @Resource
    private IWebModelGoodsBiz webModelGoodsBiz;

    protected IWebModelGoodsBiz getWebModelGoodsBiz() {
        return webModelGoodsBiz;
    }

    @Resource
    private IWebSliderBiz webSliderBiz;

    protected IWebSliderBiz getWebSliderBiz() {
        return webSliderBiz;
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createWebMenu(WebMenuBo vo) {
        vo.setPicture1(QiniuUtil.getRelativePath(vo.getPicture1()));
        vo.setPicture2(QiniuUtil.getRelativePath(vo.getPicture2()));
        vo.setLogo(QiniuUtil.getRelativePath(vo.getLogo()));
        Boolean rs = getBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteWebMenu(Long id) {
        //TODO 有子节点不给删除
        WebMenuBo query = new WebMenuBo();
        query.setParentid(id);
        List<WebMenuBo> webMenuList = this.getBiz().selectList(new EntityWrapper<WebMenuBo>(query));
        if(Tools.collection.isNotEmpty(webMenuList)) {
            throw new WakaException("该标签存在子标签！");
        }
        Boolean rs = getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        //删除标签对应的商品
        WebMenuGoodsBo wmgQuery = new WebMenuGoodsBo();
        wmgQuery.setMenuId(id);
        webMenuGoodsBiz.delete(new EntityWrapper<WebMenuGoodsBo>(wmgQuery));
        return rs;
    }

    @Override
    public WebMenuBo getOneWebMenu(Long id) {
        WebMenuBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        rs.setPicture1(QiniuUtil.getFullPath(rs.getPicture1()));
        rs.setPicture2(QiniuUtil.getFullPath(rs.getPicture2()));
        rs.setLogo(QiniuUtil.getFullPath(rs.getLogo()));
        return rs;
    }

    @Override
    public Page<WebMenuBo> getListWebMenu(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        // createMethodSinge创建方法
        Page<WebMenuBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<WebMenuBo> wrap = ControllerUtils.getWrapper(query);
        Page<WebMenuBo> rs = getBiz().selectPage(page, wrap);
        List<WebMenuBo> merchantList = rs.getRecords();
        Integer length = merchantList.size();
        for (Integer i = 0; i < length; i++) {
            WebMenuBo vo = merchantList.get(i);
            vo.setPicture1(QiniuUtil.getFullPath(vo.getPicture1()));
            vo.setPicture2(QiniuUtil.getFullPath(vo.getPicture2()));
            vo.setLogo(QiniuUtil.getFullPath(vo.getLogo()));
        }
        return rs;
    }

    @Override
    public List<WebMenuBo> getAllListWebMenu() {
        // createMethodSinge创建方法
        List<WebMenuBo> rs = getBiz().selectList(null);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateWebMenu(WebMenuBo vo) {
        vo.setPicture1(QiniuUtil.getRelativePath(vo.getPicture1()));
        vo.setPicture2(QiniuUtil.getRelativePath(vo.getPicture2()));
        vo.setLogo(QiniuUtil.getRelativePath(vo.getLogo()));
        Boolean rs = getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createWebMenuGoods(WebMenuGoodsBo vo) {
        vo.setPicture(QiniuUtil.getRelativePath(vo.getPicture()));
        Boolean rs = getWebMenuGoodsBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteWebMenuGoods(Long id) {
        Boolean rs = getWebMenuGoodsBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public WebMenuGoodsVo getOneWebMenuGoods(Long id) {
        WebMenuGoodsBo menuGoodsBo = getWebMenuGoodsBiz().selectById(id);
        if(menuGoodsBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        menuGoodsBo.setPicture(QiniuUtil.getFullPath(menuGoodsBo.getPicture()));
        WebMenuGoodsVo rs = Tools.bean.cloneBean(menuGoodsBo, new WebMenuGoodsVo());
        // LoadJoinValueImpl.load(webMenuGoodsBiz, rs);
        LoadJoinValueImpl.load(getWebMenuGoodsBiz(), rs);
        return rs;
    }

    @Override
    public Page<WebMenuGoodsVo> getListWebMenuGoods(List<QueryFilter> query, Integer start, Integer size,
                                                    String sortfield) {
        Page<WebMenuGoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<WebMenuGoodsBo> wrap = ControllerUtils.getWrapper(query);
        Page<WebMenuGoodsBo> webMenuGoodsPage = getWebMenuGoodsBiz().selectPage(page, wrap);
        Page<WebMenuGoodsVo> pageVo = ControllerUtils.convertPageRecord(webMenuGoodsPage, WebMenuGoodsVo.class);

        List<WebMenuGoodsVo> webMenuGoodsList = pageVo.getRecords();
        LoadJoinValueImpl.load(getWebMenuGoodsBiz(), webMenuGoodsList);
        Integer length = webMenuGoodsList.size();
        for (Integer i = 0; i < length; i++) {
            WebMenuGoodsVo vo = webMenuGoodsList.get(i);
            vo.setPicture(QiniuUtil.getFullPath(vo.getPicture()));
        }

        return pageVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateWebMenuGoods(WebMenuGoodsBo vo) {
        vo.setPicture(QiniuUtil.getRelativePath(vo.getPicture()));
        Boolean rs = getWebMenuGoodsBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean addWebMenuGoods(List<Long> goodsIdList, Long menuId) {
        List<WebMenuGoodsBo> webMenuGoodsList = new ArrayList<WebMenuGoodsBo>();
        for (Long goodsId : goodsIdList) {
            WebMenuGoodsBo bo = new WebMenuGoodsBo();
            bo.setMenuId(menuId);
            bo.setGoodsId(goodsId);
            webMenuGoodsList.add(bo);
        }
        Boolean rs = false;
        if (Tools.collection.isNotEmpty(webMenuGoodsList)) {
            rs = getWebMenuGoodsBiz().insertBatch(webMenuGoodsList);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createWebModel(WebModelBo vo) {
        vo.setShowType(ModelShowTypeEnum.NONE);
        Boolean rs = getWebModelBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteWebModel(Long id) {
        Boolean rs = getWebModelBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public WebModelBo getOneWebModel(Long id) {
        WebModelBo rs = getWebModelBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<WebModelBo> getListWebModel(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<WebModelBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<WebModelBo> wrap = ControllerUtils.getWrapper(query);
        Page<WebModelBo> rs = getWebModelBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateWebModel(WebModelBo vo) {
        if(vo.getShowType() == null) {
            vo.setShowType(ModelShowTypeEnum.NONE);
        }
        Boolean rs = getWebModelBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public List<WebModelBo> getAllListWebModel() {
        List<WebModelBo> rs = getWebModelBiz().selectList(null);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createWebModelGoods(WebModelGoodsBo vo) {
        Boolean rs = getWebModelGoodsBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteWebModelGoods(Long id) {
        Boolean rs = getWebModelGoodsBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public WebModelGoodsVo getOneWebModelGoods(Long id) {
        WebModelGoodsBo modelGoodsBo = getWebModelGoodsBiz().selectById(id);
        WebModelGoodsVo rs = Tools.bean.cloneBean(modelGoodsBo, new WebModelGoodsVo());
        // LoadJoinValueImpl.load(webMenuGoodsBiz, rs);
        LoadJoinValueImpl.load(getWebModelGoodsBiz(), rs);
        return rs;
    }

    @Override
    public Page<WebModelGoodsVo> getListWebModelGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        // 判断查询方式，若条件中 goodsName不为空则需要联表查询
        Boolean isLinkTable = false;
        String goodsName = null;
        String modelCode = null;
        String showType = null;
        if (Tools.collection.isNotEmpty(query)) {
            for (QueryFilter q : query) {
                if (q.getField().equals("goodsName") && Tools.string.isNotEmpty(q.getStartValue())) {
                    goodsName = q.getStartValue();
                    isLinkTable = true;
                } else if (q.getField().equals("modelCode") && Tools.string.isNotEmpty(q.getStartValue())) {
                    modelCode = q.getStartValue();
                } else if (q.getField().equals("showType") && Tools.string.isNotEmpty(q.getStartValue())) {
                    showType = q.getStartValue();
                }
            }
        }
        // createMethodSinge创建方法
        Page<WebModelGoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Page<WebModelGoodsVo> rs = null;
        if (isLinkTable) {
            rs = ControllerUtils.getPage(start, size, sortfield);
            webModelGoodsService.searchModelGoods(rs, goodsName, modelCode, showType, start, size);
        } else {
            Wrapper<WebModelGoodsBo> wrap = ControllerUtils.getWrapper(query);
            Page<WebModelGoodsBo> webModelGoodsPage = getWebModelGoodsBiz().selectPage(page, wrap);
            rs = ControllerUtils.convertPageRecord(webModelGoodsPage, WebModelGoodsVo.class);
        }
        List<WebModelGoodsVo> webMenuGoodsList = rs.getRecords();
        LoadJoinValueImpl.load(getWebModelGoodsBiz(), webMenuGoodsList);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateWebModelGoods(WebModelGoodsBo vo) {
        Boolean rs = getWebModelGoodsBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean addWebModelGoods(List<Long> goodsIdList, String modelCode) {
        List<WebModelGoodsBo> webMenuGoodsList = new ArrayList<WebModelGoodsBo>();
        for (Long goodsId : goodsIdList) {
            // 判断对应商品是否有对应的前端的分类标签，没有则不添加
            WebMenuGoodsBo query = new WebMenuGoodsBo();
            query.setGoodsId(goodsId);
            WebMenuGoodsBo webMenuGoodsBo = webMenuGoodsBiz.selectOne(new EntityWrapper<WebMenuGoodsBo>(query));
            if (webMenuGoodsBo == null || webMenuGoodsBo.getMenuId() == null) {
                // 如果没有对应的标签商品则不能添加到模块中
                continue;
            }
            WebModelGoodsBo bo = new WebModelGoodsBo();
            // bo.setShowType("true");
            bo.setMenuId(webMenuGoodsBo.getMenuId());
            bo.setGoodsId(goodsId);
            bo.setModelCode(modelCode);
            // 默认为二级页面显示
            bo.setShowType(ShowTypeEnum.STAGE);
            webMenuGoodsList.add(bo);
        }
        Boolean rs = true;

        if (Tools.collection.isNotEmpty(webMenuGoodsList)) {
            rs = getWebModelGoodsBiz().insertBatch(webMenuGoodsList);
        }
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createWebSlider(WebSliderBo vo) {
        vo.setPicture(QiniuUtil.getFullPath(vo.getPicture()));
        // 判断当前页面类型对应的生效图片张数 （最多只能显示5条轮播）
        WebSliderBo query = new WebSliderBo();
        query.setPageName(vo.getPageName());
        query.setStatus(true);
        List<WebSliderBo> sliderList = getWebSliderBiz().selectList(new EntityWrapper<WebSliderBo>(query));
        if(Tools.collection.isNotEmpty(sliderList)) {
            if(sliderList.size() >= 5 && vo.getStatus()) {
                throw new WakaException(vo.getPageName().getLabel() + "最多只能显示5张图片");
            }
        }
        Boolean rs = getWebSliderBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteWebSlider(Long id) {
        Boolean rs = getWebSliderBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public WebSliderBo getOneWebSlider(Long id) {
        WebSliderBo rs = getWebSliderBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        rs.setPicture(QiniuUtil.getFullPath(rs.getPicture()));
        return rs;
    }

    @Override
    public Page<WebSliderBo> getListWebSlider(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<WebSliderBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<WebSliderBo> wrap = ControllerUtils.getWrapper(query);
        wrap.orderBy(WebSliderBo.Key.pageName.toString()).orderBy(WebSliderBo.Key.sequence.toString(), true);
        Page<WebSliderBo> rs = getWebSliderBiz().selectPage(page, wrap);
        // 获取图片全路径
        List<WebSliderBo> webSliderList = rs.getRecords();
        Integer length = webSliderList.size();
        for (Integer i = 0; i < length; i++) {
            WebSliderBo vo = webSliderList.get(i);
            vo.setPicture(QiniuUtil.getFullPath(vo.getPicture()));
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateWebSlider(WebSliderBo vo) {
        vo.setPicture(QiniuUtil.getFullPath(vo.getPicture()));
        // 判断当前页面类型对应的生效图片张数 （最多只能显示5条轮播）
        WebSliderBo query = new WebSliderBo();
        query.setPageName(vo.getPageName());
        query.setStatus(true);
        List<WebSliderBo> sliderList = getWebSliderBiz().selectList(new EntityWrapper<WebSliderBo>(query));
        if(Tools.collection.isNotEmpty(sliderList)) {
            if(sliderList.size() >= 5 && vo.getStatus()) {
                WebSliderBo oldImg = getWebSliderBiz().selectById(vo.getId());
                //只改装填的情况
                if(oldImg.getPageName() == vo.getPageName() && oldImg.getStatus() != vo.getStatus() && vo.getStatus()) {
                    throw new WakaException(vo.getPageName().getLabel() + "最多只能显示5张图片");
                }else if(oldImg.getPageName() != vo.getPageName() && vo.getStatus()) {
                    throw new WakaException(vo.getPageName().getLabel() + "最多只能显示5张图片");
                }
            }
        }
        Boolean rs = getWebSliderBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }
}
