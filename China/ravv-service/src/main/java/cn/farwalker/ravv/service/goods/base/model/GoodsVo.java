package cn.farwalker.ravv.service.goods.base.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.farwalker.ravv.common.constants.ImagePositionEnum;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

public class GoodsVo extends GoodsBo {
    /**
     * SKU列表
     */
    private List<GoodsSkuVo> skuList;
    /**商品描述路径*/
    private String imageDetails;
    /**标题图片路径*/
    private String imageTitles;
    /**主图图片路径*/
    private String imageMajor;
    
    /**标题视频路径*/
    private String videoTitle;
    /**详情视频路径*/
    private String videoDetail;
    
    /**仓库名称*/
    private String storehouseName;
    
    /**
     * 商品的所有图片
     */
    private List<GoodsImageBo> images;
    

	public List<GoodsSkuVo> getSkuList() {
		return skuList;
	}

	@LoadJoinValue(by="id",table=GoodsSkuDefBo.TABLE_NAME,joinfield="goodsId")
	public void setSkuList(List<GoodsSkuDefBo> psku) {
		this.skuList = new ArrayList<>();	
		if(Tools.collection.isNotEmpty(psku)){
			for (GoodsSkuDefBo sku : psku) {
				GoodsSkuVo e = Tools.bean.cloneBean(sku, new GoodsSkuVo());
				skuList.add(e);
			}
		}
	}


	private List<PropsVo> propsVo;
	public List<PropsVo> getPropsVo() {
		if(propsVo == null ){
			propsVo = GoodsUtil.parsePropsVo(getProps());
		}
		return propsVo;
	}
	
	@Override
	public void setProps(String v) {
		super.setProps(v);
		propsVo = null;
	}

	@LoadJoinValue(by="id",table=GoodsImageBo.TABLE_NAME,joinfield="goodsId")
	public void setImages(List<GoodsImageBo> images) {
		if(Tools.collection.isNotEmpty(images)){
			Collections.sort(images,new Comparator<GoodsImageBo>(){
				@Override
				public int compare(GoodsImageBo o1, GoodsImageBo o2) {
					int s1 = Tools.number.nullIf(o1.getSequence(), 999);
					int s2 = Tools.number.nullIf(o2.getSequence(), 999);
					return s1-s2;
				}
			});
		}
		this.images = images;
	}
	/**
     * 商品的所有图片
     */
	public List<GoodsImageBo> getImages() {
		return images;
	}
	
	/**主图*/
	public String getImageMajor() {
		if(Tools.string.isEmpty(imageMajor)){
			StringBuilder sm =  getFullURL(ImagePositionEnum.MAJOR,false);
			imageMajor = sm.toString();
		}
		return imageMajor;
	}
	
    /** 商品描述路径(全路径,多个以逗号分隔)*/
    public String getImageDetails(){
    	if(Tools.string.isEmpty(imageDetails)){
    		StringBuilder paths = getFullURL(ImagePositionEnum.DETAIL,false);
    		imageDetails = paths.toString();
    	}
        return imageDetails;
    }
    

	/**标题图*/
	public String getImageTitles() {
    	if(Tools.string.isEmpty(imageTitles)){
    		StringBuilder major =  getFullURL( ImagePositionEnum.MAJOR,false);
    		StringBuilder title =  getFullURL(ImagePositionEnum.TITLE,false);
    		if(title.length()>0){
    			major.append(',').append(title);
    		}
    		if(major.length()>0 && major.charAt(0) ==','){
    			major.deleteCharAt(0);
    		}
    		imageTitles = major.toString();
    	}
    	return imageTitles;
	}

    private StringBuilder getFullURL(ImagePositionEnum type,boolean isVideo){
    	if(Tools.collection.isEmpty(images)){
    		return new StringBuilder();
    	}

    	List<GoodsImageBo> rds = GoodsUtil.getGoodsImages(images, type);
		StringBuilder paths = new StringBuilder();
		for(GoodsImageBo bo : rds){
			boolean video = Boolean.TRUE.equals( bo.getIsVideo());
			if(isVideo == video){
				String fu = GoodsUtil.getCdnFullPaths(bo.getImgUrl());
				if(Tools.string.isNotEmpty(fu)){
					paths.append(fu).append(',');
				}	
			}
		}
		if(paths.length()>0){
			paths.deleteCharAt(paths.length()-1);
		}
		return paths;
    }

    /** 商品描述路径(全路径,多个以逗号分隔)*/
	public void setImageDetails(String imageDetails) {
		this.imageDetails = imageDetails;
	}

	public void setImageTitles(String imageTitles) {
		this.imageTitles = imageTitles;
	}
	/**主图*/
	public void setImageMajor(String imageMajor) {
		this.imageMajor = imageMajor;
	}
	 /**仓库名称*/
	public String getStorehouseName() {
		return storehouseName;
	}
	 /**仓库名称*/
	@LoadJoinValue(by="storehouseId",table=StorehouseBo.TABLE_NAME,joinfield="id",returnfield="storename")
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}
	 /**标题视频路径*/
	public String getVideoTitle() {
		if(Tools.string.isEmpty(videoTitle)){
    		StringBuilder sb= getFullURL(ImagePositionEnum.TITLE, true);
    		videoTitle = sb.toString();
    	}
		return videoTitle;
	}
	 /**标题视频路径*/
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	/**详情视频路径*/
	public String getVideoDetail() {
    	if(Tools.string.isEmpty(videoDetail)){
    		StringBuilder sb= getFullURL(ImagePositionEnum.DETAIL, true);
    		videoDetail = sb.toString();
    	}
		return videoDetail;
	}
	/**详情视频路径*/
	public void setVideoDetail(String videoDetail) {
		this.videoDetail = videoDetail;
	}
}
