package cn.farwalker.ravv.service.goodsext.viewlog.biz.impl;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.price.dao.IGoodsPriceDao;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogBiz;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogService;
import cn.farwalker.ravv.service.goodsext.viewlog.dao.IGoodsViewLogDao;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogBo;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GoodsViewLogServiceImpl implements IGoodsViewLogService {

    @Autowired
    private IGoodsViewLogBiz iGoodsViewLogBiz;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    private IGoodsImageBiz iGoodsImageBiz;

    @Autowired
    private IGoodsViewLogDao iGoodsViewLogDao;

    @Autowired
    private IGoodsPriceDao iGoodsPriceDao;

    public static void main(String[] args){
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = s1.format(new Date());
        log.info("date:{}", date);
        Date now = null;
        try {
            now = s1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("before now:{}",now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, 12);
        now = cal.getTime();
        log.info("after now:{}",now);


//        String time = s2.format(now);
//        log.info("time:{}", time);
//        Date d = null;
//        try {
//            d = s2.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        log.info("d:{}", d);
    }

    /**
     * @description: 添加浏览足迹
     * @param: memberId, goodsId
     * @return boolean
     * @author Mr.Simple
     * @date 2018/12/24 17:47
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addViewLog(Long memberId, Long goodsId) {

        Date otherTime = new Date();
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = s1.format(new Date());
        log.info("date:{}", date);
        Date now = null;
        try {
            now = s1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("before now:{}",now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, 12);
        now = cal.getTime();
        log.info("after now:{}",now);
        //判断该商品是否有记录
        EntityWrapper<GoodsViewLogBo> queryGoods = new EntityWrapper<>();
        queryGoods.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
        queryGoods.eq(GoodsViewLogBo.Key.goodsId.toString(), goodsId);
        int goodCount = 0;
        goodCount = iGoodsViewLogBiz.selectCount(queryGoods);
        if(goodCount == 1){
            //替换该商品浏览时间
            GoodsViewLogBo goodsViewLogBo = new GoodsViewLogBo();
            goodsViewLogBo.setViewTime(now);
            goodsViewLogBo.setGmtModified(otherTime);
            return iGoodsViewLogBiz.update(goodsViewLogBo, queryGoods);
        }
        //查询该用户足迹是否超过100条
        EntityWrapper<GoodsViewLogBo> queryViewLog = new EntityWrapper<>();
        queryViewLog.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
        int count = iGoodsViewLogBiz.selectCount(queryViewLog);
        if(count == 100){
            List<String> orderList = new ArrayList<>();
            orderList.add(GoodsViewLogBo.Key.viewTime.toString());
            //删除时间最久的一条记录
            queryViewLog.orderAsc(orderList);
            List<GoodsViewLogBo> viewLogBoList = iGoodsViewLogBiz.selectList(queryViewLog);
            GoodsViewLogBo firstView = viewLogBoList.get(0);
            if(!iGoodsViewLogBiz.deleteById(firstView))
                throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        //插入该条记录
        GoodsViewLogBo viewLogBo = new GoodsViewLogBo();
        viewLogBo.setMemberId(memberId);
        viewLogBo.setGoodsId(goodsId);
        viewLogBo.setViewTime(now);
        viewLogBo.setGmtCreate(otherTime);
        viewLogBo.setGmtModified(otherTime);
        return iGoodsViewLogBiz.insert(viewLogBo);
    }

    /**
     * @description: 获取所有浏览记录
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/27 10:02
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public List<GoodsViewLogVo> updateAndGetAllViewLog(Long memberId, int currentPage, int pageSize) {
        log.info("==================currentPage:",currentPage);
        log.info("==================pageSize:",pageSize);
        List<GoodsViewLogVo> goodsViewLogVoList = new ArrayList<>();
        Page<GoodsViewLogBo> page = new Page<>(currentPage, pageSize);
        page.setAsc(false);
        page.setOrderByField(GoodsViewLogBo.Key.gmtModified.toString());
        EntityWrapper<GoodsViewLogBo> queryViewLog = new EntityWrapper<>();
        queryViewLog.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
//        queryViewLog.orderBy(GoodsViewLogBo.Key.gmtModified.toString(), false);
        Page<GoodsViewLogBo> goodsViewLogBoPage = iGoodsViewLogBiz.selectPage(page, queryViewLog);
        List<GoodsViewLogBo> viewLogBoList = goodsViewLogBoPage.getRecords();
        List<Long> goodsIdList = new ArrayList<>();
        if(viewLogBoList.size() != 0){
            viewLogBoList.forEach(item->{
                GoodsViewLogVo viewLogVo = new GoodsViewLogVo();
                BeanUtils.copyProperties(item, viewLogVo);
                GoodsBo goodsBo = new GoodsBo();
                goodsBo = iGoodsBiz.selectById(item.getGoodsId());
                if(goodsBo != null){
                    //将goodsBo设置到viewLogVo里
                    viewLogVo.setGoodsBo(goodsBo);
                    //查询商品是否被删除
                    //查询图片
                    EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
                    queryImage.eq(GoodsImageBo.Key.goodsId.toString(), item.getGoodsId());
                    queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
                    GoodsImageBo goodsImageBo = new GoodsImageBo();
                    goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
                    //将图片设置到vo里
                    if(goodsImageBo != null){
                        viewLogVo.setImgUrl(QiniuUtil.getFullPath(goodsImageBo.getImgUrl()));
                    }
                }
                else {
                    goodsIdList.add(item.getGoodsId());
                }

                goodsViewLogVoList.add(viewLogVo);
            });
        }
        List<GoodsViewLogVo> reQueryList = new ArrayList<>();
        //判断被删除商品idlist是否为空
        if(goodsIdList.size() != 0){
            //删除该这些商品的浏览记录
            for (Long goodsId : goodsIdList) {
                EntityWrapper<GoodsViewLogBo> queryDelete = new EntityWrapper<>();
                queryDelete.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
                queryDelete.eq(GoodsViewLogBo.Key.goodsId.toString(), goodsId);
                if(!iGoodsViewLogBiz.delete(queryDelete))
                    throw new WakaException(RavvExceptionEnum.DELETE_ERROR + "get all view");
                //重新查询
                reQueryList = updateAndGetAllViewLog(memberId, currentPage, pageSize);
            }

            return reQueryList;
        }
        else {
            //查出商品最低价
            goodsViewLogVoList.forEach(item->{
                //查出商品最低价
                BigDecimal minPrice = iGoodsPriceDao.getMinPriceMyGoodsId(item.getGoodsId());
                item.setMinPrice(minPrice);
            });
            return goodsViewLogVoList;
        }

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteViewLog(Long memberId, Long goodsId) {
        EntityWrapper<GoodsViewLogBo> queryViewLog = new EntityWrapper<>();
        queryViewLog.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
        queryViewLog.eq(GoodsViewLogBo.Key.goodsId.toString(), goodsId);
        if(!iGoodsViewLogBiz.delete(queryViewLog))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        return "delete viewlog successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteByTime(Long memberId, Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String deleteTime = sdf.format(time);
        Date delete = null;
        try {
            delete = sdf.parse(deleteTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalTime = sdf.format(delete);

        log.info("delete:{}",delete);
        log.info("finalTime:{}",finalTime);
        log.info("deleteTime:{}",deleteTime);
        log.info("time:{}", time);
        EntityWrapper<GoodsViewLogBo> queryViewLog = new EntityWrapper<>();
        queryViewLog.eq(GoodsViewLogBo.Key.memberId.toString(), memberId);
        queryViewLog.eq(GoodsViewLogBo.Key.viewTime.toString(), deleteTime);
        if(!iGoodsViewLogBiz.delete(queryViewLog))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        return "delete viewlog successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteBatchViewLog(Long memberId, String goodsIds) {
        List<Long> goodsIdList = Tools.string.convertStringToLong(goodsIds);
        int deleteCount = 0;
        deleteCount = iGoodsViewLogDao.deleteViewLogByBatch(memberId,goodsIdList);
        if(deleteCount <= 0)
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        return "delete viewlog successful";
    }


}
