package cn.farwalker.ravv.service.goods.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum GoodsStatusEnum implements IEnumJsons{
	 //NEW("new", "新建"),
	 ONLINE("online", "上架"), 
	 OFFLINE("offline", "下架"), 
	 OPERATOR_OFFLINE("operator_offline", "运营下架"), 
	 SYS_OFFLINE("sys_offline", "系统下架"), 
	 DELETE("delete", "删除"), 
	 OPERATOR_DELETE("operator_delete", "运营删除");
	
	private String key;
	private String desc;
	
	GoodsStatusEnum(String key, String desc){
		this.key = key;
		this.desc = desc;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return desc;
	}
	@Override
	public String toString() {
		return super.toString();// EnumManager.toString(this);
	}
	public static boolean validatGoodsStatus(String status) {
        GoodsStatusEnum[] arr = GoodsStatusEnum.values();
        for (GoodsStatusEnum tmp : arr) {
            if (tmp.getKey().equals(status)) {
                return true;
            }
        }
        return false;
    }
	
	/**
     * 该状态是否是下架状态
     * @param key
     * @return
     */
    public static boolean isUnderLineStatus(String key) {
        if (OFFLINE.getKey().equals(key) || OPERATOR_OFFLINE.getKey().equals(key)
                || SYS_OFFLINE.getKey().equals(key)) {
            return true;
        }
        return false;
    }
    
    /**
     * 该状态是否是删除状态
     * @param key
     * @return
     */
    public static boolean isDelStatus(String key) {
        if (OPERATOR_DELETE.getKey().equals(key) || DELETE.getKey().equals(key)) {
            return true;
        }
        return false;
    }
}
