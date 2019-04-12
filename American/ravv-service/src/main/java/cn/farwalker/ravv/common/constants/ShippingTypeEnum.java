package cn.farwalker.ravv.common.constants;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * @deprecated 有重复，看看是否可以合并？ {@link cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum}
 * @author Administrator
 *
 */
public enum ShippingTypeEnum implements IEnumJsons {

    FREESHIP("Free Shipping","Free Shipping")

    ;


    private String key;
    private String desc;

    ShippingTypeEnum(String key,String desc){
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String getLabel() {
        return desc;
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean compare(String key) {
        return this.key.equals(key);
    }

    public static ShippingTypeEnum getEnumByKey(String key) {
        ShippingTypeEnum[] all = ShippingTypeEnum.values();
        for(ShippingTypeEnum a : all) {
            if(a.getKey().equals(key)) {
                return a;
            }
        }
        return null;
    }
}
