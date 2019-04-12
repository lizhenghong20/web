package cn.farwalker.ravv.admin.goods.dto;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

/**
 * 商品与仓库
 * @author Administrator
 */
public class GoodsStoreVo extends GoodsBo{
	private static final long serialVersionUID = -623036881874000759L;
	/**仓库名称*/
	private String storeName;

	/**仓库名称*/
	public String getStoreName() {
		return storeName;
	}

	/**仓库名称*/
	@LoadJoinValue(by="storehouseId",table=StorehouseBo.TABLE_NAME,joinfield="id",returnfield="storename")
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}
