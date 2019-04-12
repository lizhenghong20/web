package cn.farwalker.ravv.service.flash.sale.biz.impl;


import java.util.*;

import javax.annotation.Resource;

import cn.farwalker.ravv.common.constants.FlashSaleCategoryStatusEnum;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleCategoryVo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleService;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuVo;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.dao.IFlashGoodsSkuDao;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

@Service
public class FlashSaleServiceImpl implements IFlashSaleService{

    @Autowired
    private IFlashSaleBiz iFlashSaleBiz;

    @Autowired
    private IFlashGoodsSkuDao iFlashGoodsSkuDao;

    @Override
    public List<FlashSaleCategoryVo> getCategory() {
        EntityWrapper<FlashSaleBo> flashSaleBoWrapper = new EntityWrapper<>();
        //modify:consumer_divide    created by cowboy 2018/12/1  判断状态改为判断时间
        Date currentTime = new Date();
        flashSaleBoWrapper.gt(FlashSaleBo.Key.endtime.toString(), currentTime);
        List<FlashSaleBo> queryList = iFlashSaleBiz.selectList(flashSaleBoWrapper);
        List<FlashSaleCategoryVo> resultList = new ArrayList<>();
        for (FlashSaleBo item : queryList) {
            FlashSaleCategoryVo newOne = new FlashSaleCategoryVo();
            Tools.bean.copyProperties(item, newOne);
            resultList.add(newOne);
        }
        FlashSaleCategoryVo nowVo = new FlashSaleCategoryVo();
        String todayStr = Tools.date.formatsmall(new Date());
        //今天的0点
        Date today = Tools.date.parseDate(todayStr).getTime();
        //明天的0点
        Date tomorrow = new Date(today.getTime() + 24 * 3600 * 1000);
        //后当天的0点
        Date afterTomorrow = new Date(tomorrow.getTime() + 24 * 3600 * 1000);

        //最接近当前的时间
         Date max = null;
        int  maxIndex = -1;
        for (int i = 0; i < resultList.size(); i++) {
            FlashSaleCategoryVo item = resultList.get(i);
            //如果当前时间落在开始后,即活动已开始
            if (currentTime.getTime() >= item.getStarttime().getTime()) {
                //最近的活动置为now状态
                if(max == null){
                    max = item.getStarttime();
                    maxIndex = i;
                }
                if ( item.getStarttime().getTime() > max.getTime()) {
                    max = item.getStarttime();
                    maxIndex = i;
                } else {//不是最近的置为进行中
                    item.setCategoryStatus(FlashSaleCategoryStatusEnum.STARTED);
                }

            } else {//活动未开始
                //如果活动开始时间在今天
                if(item.getStarttime().getTime() < tomorrow.getTime()){
                    item.setCategoryStatus(FlashSaleCategoryStatusEnum.UPCOMING);
                }else if(item.getStarttime().getTime() >=tomorrow.getTime()&&item.getStarttime().getTime() < afterTomorrow.getTime()){//如果在明天
                    item.setCategoryStatus(FlashSaleCategoryStatusEnum.TOMORROW);
                }else{//如果在后天或以后则不显示
                    item.setCategoryStatus(FlashSaleCategoryStatusEnum.AFTERTOMORROW);
                }
            }

        }

        resultList.get(maxIndex).setCategoryStatus(FlashSaleCategoryStatusEnum.NOW);
        resultList.sort(Comparator.comparing(FlashSaleCategoryVo::getStarttime));
        return resultList;
    }

    @Override
    public List<FlashGoodsSkuVo> getGoods(Long flashSaleId, Integer currentPage, Integer pageSize){
        Page<FlashGoodsSkuVo> page = new Page(currentPage,pageSize);
        List<FlashGoodsSkuVo> resultList =  iFlashGoodsSkuDao.selectGoodsSkuListByFlashSaleId(page,flashSaleId);
        if(resultList == null)
            return null;
        resultList.forEach(item->{
            item.setCurrentTime(new Date());
            item.setGoodsImageUrl(QiniuUtil.getFullPath(item.getGoodsImageUrl()));
            item.setGoodsImageUrlMajor(QiniuUtil.getFullPath(item.getGoodsImageUrlMajor()));
        });
        return resultList;
    }

    @Override
    public List<FlashGoodsSkuVo> getGoodsInHome(Integer currentPage, Integer pageSize) {
        //先查找出当前时间段内所有正在进行的限时购
        Date now = new Date();
        EntityWrapper<FlashSaleBo> flashSaleQuery = new EntityWrapper<>();
        //获取当前时间处于开始和结束时间之间的闪购信息，降序排序
        flashSaleQuery.gt(FlashSaleBo.Key.endtime.toString(),now)
                .lt(FlashSaleBo.Key.starttime.toString(),now)
                .orderBy(FlashSaleBo.Key.starttime.toString(),false);
        List<FlashSaleBo> flashSaleList = iFlashSaleBiz.selectList(flashSaleQuery);
        if(flashSaleList.size() == 0)
            return null;
        List<FlashGoodsSkuVo> resultList = null;
        //取出筛选出的第一条且有商品的那一个目录
        for(int i = 0; i < flashSaleList.size(); i++){
            FlashSaleBo currentFlash = flashSaleList.get(i);
             resultList = getGoods(currentFlash.getId(),currentPage,pageSize);
            if(resultList.size() != 0)
                break;
        }

        return resultList;
    }


	@Resource
	private IFlashGoodsSkuBiz flashGoodsSkuBiz;

	@Override
	public List<FlashGoodsSkuBo> flashGoodsSkuList(Long flashSaleId, Long goodsId) {
		EntityWrapper<FlashGoodsSkuBo> wrapperSku = new EntityWrapper<>();
		wrapperSku.eq(FlashGoodsSkuBo.Key.flashSaleId.toString(), flashSaleId);
		wrapperSku.eq(FlashGoodsSkuBo.Key.goodsId.toString(), goodsId);
		List<FlashGoodsSkuBo> flashGoodsSkuList = flashGoodsSkuBiz.selectList(wrapperSku);
		
		return flashGoodsSkuList;
	}

    public static void main(String[] args) {

       String today =  Tools.date.formatsmall(new Date());
        System.out.println(today);

        Calendar  c = Tools.date.parseDate(today);
        System.out.println(c.getTime());
        System.out.println(new Date(c.getTimeInMillis()+ 24*3600*1000));

    }


}
