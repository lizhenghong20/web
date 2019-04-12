package cn.farwalker.standard.modular.system.warpper;

import java.math.BigInteger;
import java.util.Map;

import cn.farwalker.standard.common.constant.factory.ConstantFactory;


/**
 * 部门列表的包装
 *
 * @author Jason Chen
 * @date 2017年4月25日 18:10:31
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	BigInteger i1 = (BigInteger) map.get("pid");
    	Long pid = i1.longValue();

        if (pid != null || pid.equals(0L)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

}
