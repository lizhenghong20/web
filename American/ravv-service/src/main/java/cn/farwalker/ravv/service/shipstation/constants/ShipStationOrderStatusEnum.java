package cn.farwalker.ravv.service.shipstation.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum ShipStationOrderStatusEnum implements EnumManager.IEnumJsons  {
    AWAITINGPAYMENT("awaiting_payment","等待支付"),
    AWAITINGSHIPMENT("awaiting_shipment","等待发货"),
    SHIPED("shipped","发货"),
    ONHOLD("on_hold","等待收货"),
    CANCELLED("cancelled","取消")
    ;
    private String key;
    private String desc;

    ShipStationOrderStatusEnum(String key, String desc){
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

    public boolean compare(int key) {
        return this.key.equals(key);
    }
}
