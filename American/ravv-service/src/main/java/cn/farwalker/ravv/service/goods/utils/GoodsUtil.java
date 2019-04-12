package cn.farwalker.ravv.service.goods.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.common.constants.ImagePositionEnum;
import cn.farwalker.ravv.service.goods.base.model.PropsVo;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * 关于商品处理的一些方法
 * @author Administrator
 *
 */
public class GoodsUtil {
	/**
	 * 取得商品描述路径图片的全路径
	 * @param url 多个以逗号分隔
	 * @return 拼成多个以逗号分隔
	 */
	public static String getCdnFullPaths(String url){
		return getCdnFullPaths(url, ",");
	}
	
	/**
	 * 取CDN全路径
	 * @param url
	 * @param splitChar
	 * @return
	 */
	public static String getCdnFullPaths(String url,String splitChar){
		if(Tools.string.isEmpty(url)){
			return "";
		}
		else if(url.indexOf(splitChar)==-1){
			return QiniuUtil.getFullPath(url);
		}
		else{
			String[] rs = getCdnFullPath(url.split(splitChar));
			StringBuilder sb = Tools.string.joinSplit(splitChar,(Object[])rs);
			return sb.toString();
		}
	}
	/**
	 * 删除cdn域名,取得相对路径
	 * @param urls
	 * @return
	 */
	public static String getCdnRelativePath(String urls){
		return getCdnRelativePath(urls, ",");
	}
	/**
	 * 删除cdn域名,取得相对路径
	 * @param urls
	 * @return
	 */
	public static String getCdnRelativePath(String urls,String splitChar){
		if(Tools.string.isEmpty(urls)){
			return "";
		}
		else if(urls.indexOf(splitChar)==-1){
			return QiniuUtil.getRelativePath(urls);
		}
		else{
			String[] paths = urls.split(splitChar);
			String[] rs = getCdnRelativePath(paths);
			StringBuilder sb = Tools.string.joinSplit(splitChar, (Object[])rs);
			return sb.toString();
		}
	}
	
	/**
	 * 删除cdn域名,取得相对路径
	 * @param urls
	 * @return
	 */
	public static String[] getCdnRelativePath(String... urls){
		String[] rs = new String[urls.length];
		for(int i =0 ;i < urls.length;i++){
			String f = QiniuUtil.getRelativePath(urls[i]);
			rs[i] = f;
		}
		return rs;
	}
	
	/**
	 * 取CDN的全路径
	 * @param urls
	 * @return
	 */
	public static String[] getCdnFullPath(String... urls){
		String[] rs = new String[urls.length];
		for(int i =0 ;i < urls.length;i++){
			String f = QiniuUtil.getFullPath(urls[i]);
			rs[i] = f;
		}
		return rs;
		
	}
	/**
	 * 取得商品的图片
	 * @param images
	 * @param type
	 * @return
	 */
	public static GoodsImageBo getGoodsImage(List<GoodsImageBo> images ,ImagePositionEnum type){
		List<GoodsImageBo> rs = getGoodsImages(images, type);
		if(rs.isEmpty()){
			return null;
		}
		else{
			return rs.get(0);
		}
	}
	
	/**
	 * 取得商品的图片
	 * @param images
	 * @param type
	 * @return 不会为null
	 */
	public static List<GoodsImageBo> getGoodsImages(List<GoodsImageBo> images ,ImagePositionEnum type){
		List<GoodsImageBo> rs = new ArrayList<>(images.size());
		for(GoodsImageBo bo : images){
			if(bo.getImgPosition() == type){
				rs.add(bo);
			}
		}
		return rs;
	}
	
	/** 根据扩展名判断是否视频
	 * <input type="file" accept="video/avi,video/mp4,video/flv,video/3gp,video/swf" capture="camcorder" @change="onFileChange" style="display: none;">
	 * https://blog.csdn.net/qq_27717857/article/details/79917995
	 */
	public static boolean isVideo(String url){
		int idx = ( url == null ? 0:url.lastIndexOf('.'));
		if(idx <=0 || idx+1==url.length()){
			return false;
		}
		else{
			String ext = url.substring(idx+1);
			return "avi".equalsIgnoreCase(ext) || "mp4".equalsIgnoreCase(ext) 
					|| "3gp".equalsIgnoreCase(ext) || "swf".equalsIgnoreCase(ext);  
		}
	}
	/**
	 * 把Goods的Props属性转为对象
	 * @param goodsProps
	 * @return
	 */
	public static List<PropsVo> parsePropsVo(String goodsProps){
		if(Tools.string.isEmpty(goodsProps) ||"[]".equals(goodsProps)){
			return Collections.emptyList();
		}
		//List<PropsVo> vo = Tools.json.toList(goodsProps, PropsVo.class);
		List arys = Tools.json.toObject(goodsProps,List.class);
		List<PropsVo> result = new ArrayList<>(arys.size());
		for(Object m : arys){
			PropsVo e = new PropsVo();
		    Tools.bean.mapToObject((Map<?, ?>)m, e);
			result.add(e);
		}
		return result;
	}
	/**
	 * 把Goods的Props属性对象转为json
	 * @param goodsProps
	 * @return
	 */
	public static String parsePropsVo(List<PropsVo>  goodsProps){
		if(Tools.collection.isEmpty(goodsProps)){
			return "";
		}
		StringBuilder rs = new StringBuilder();
		//rs.append();
		for(PropsVo v :goodsProps){
			rs.append(',').append(v.toJson());
		}
		rs.setCharAt(0, '[');
		rs.append(']');
		return rs.toString();
	}
}
