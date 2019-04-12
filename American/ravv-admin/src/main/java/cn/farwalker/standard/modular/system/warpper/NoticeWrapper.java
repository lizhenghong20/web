package cn.farwalker.standard.modular.system.warpper;

import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import java.math.BigInteger;
import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author Jason Chen
 * @date 2017年4月25日 18:10:31
 */
public class NoticeWrapper extends BaseControllerWarpper {

    public NoticeWrapper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	BigInteger i1 = (BigInteger) map.get("creater");
    	Long creater = i1.longValue();
        map.put("createrName", ConstantFactory.me().getUserNameById(creater));
    }

}
