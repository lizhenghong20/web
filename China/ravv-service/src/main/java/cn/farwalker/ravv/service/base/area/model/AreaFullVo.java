package cn.farwalker.ravv.service.base.area.model;

import java.util.Arrays;
import java.util.List;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;

/**
 * 省、市、区，统一处理
 * @author Administrator
 *
 */
public class AreaFullVo {
	private BaseAreaBo provinceBo;
	private BaseAreaBo cityBo;
	private BaseAreaBo areaBo;
	
	public static AreaFullVo getAreaFullVo(Long areaId,IBaseAreaBiz biz){
		if(areaId == null){
			throw new WakaException("地址的地区id为空");
		}
		BaseAreaBo bo = biz.selectById(areaId);
		String paths = bo.getFullPath();
		if(Tools.string.isEmpty(paths)){
			return new AreaFullVo(bo,null,null);
		}
		String[] ps = paths.split("/");
		List<BaseAreaBo> rds = biz.selectBatchIds(Arrays.asList(ps));
		BaseAreaBo[] areas = {null,null,null};
		for(int i = 0;i < ps.length && i < areas.length;i++){
			Long id = new Long(ps[i]);
			if(id.equals(areaId)){
				areas[i]=bo;	
			}
			else{
				areas[i] = Tools.collection.getBo(rds, id);
			}
			//BaseAreaBo b =(areaId.toString().equals(ps) ? bo: biz.selectById(ps[i]));
		}
		if(ps.length == 2){
			return new AreaFullVo(areas[0],areas[0],areas[1]);
		} else
			return new AreaFullVo(areas[0],areas[1],areas[2]);
	}
	

	
	/**
	 * 
	 * @param areaIds 上下级按"/"拼接的字符串
	 * @param biz
	 * @return
	 */
	public static AreaFullVo getAreaFullVo(String areaIds,IBaseAreaBiz biz){
		if(Tools.string.isEmpty(areaIds)){
			throw new WakaException("地址的地区id为空");
		}
		
		String[] ps = areaIds.split("/");
		List<BaseAreaBo> rds = biz.selectBatchIds(Arrays.asList(ps));
		BaseAreaBo[] areas = {null,null,null};
		for(int i = 0;i < ps.length && i < areas.length;i++){
			Long id = new Long(ps[i]);
			areas[i] = Tools.collection.getBo(rds, id);
		}
		if(ps.length == 2){
			return new AreaFullVo(areas[0],areas[0],areas[1]);
		} else
			return new AreaFullVo(areas[0],areas[1],areas[2]);
	}
	public AreaFullVo(){
		
	}
	public AreaFullVo(BaseAreaBo province,BaseAreaBo city,BaseAreaBo area){
		this.provinceBo = province;
		this.cityBo = city;
		this.areaBo = area;
	}
	
	public BaseAreaBo getProvinceBo() {
		return provinceBo;
	}

	public void setProvinceBo(BaseAreaBo provinceBo) {
		this.provinceBo = provinceBo;
	}

	public BaseAreaBo getCityBo() {
		return cityBo;
	}

	public void setCityBo(BaseAreaBo cityBo) {
		this.cityBo = cityBo;
	}

	public BaseAreaBo getAreaBo() {
		return areaBo;
	}

	public void setAreaBo(BaseAreaBo areaBo) {
		this.areaBo = areaBo;
	}
	
	public String getFullAddress(){
		StringBuilder sb = new StringBuilder();
		if(provinceBo!=null){
			sb.append(provinceBo.getName());
		}
		if(cityBo!=null){
			sb.append('-').append(cityBo.getName());
		}
		if(areaBo!=null){
			sb.append('-').append(areaBo.getName());
		}
		return sb.toString();
	}
}
