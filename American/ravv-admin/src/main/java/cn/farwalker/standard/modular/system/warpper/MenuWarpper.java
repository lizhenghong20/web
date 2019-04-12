package cn.farwalker.standard.modular.system.warpper;

import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 *
 * @author Jason Chen
 * @date 2017年2月19日15:07:29
 */
public class MenuWarpper extends BaseControllerWarpper {

    public MenuWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getMenuStatusName((Integer) map.get("status")));
        Boolean isMenu = (Boolean) map.get("ismenu");
        String name = "";
        if(isMenu) {
        	name = "YES";
        }else {
        	name = "NO";
        }
        map.put("isMenuName", name);
    }

}
