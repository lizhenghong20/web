package cn.farwalker.standard.core.util;

import java.util.ArrayList;
import java.util.List;

import cn.farwalker.standard.core.temp.Const;
import cn.farwalker.waka.config.properties.WakaProperties;
import cn.farwalker.waka.core.MenuNode;
import cn.farwalker.waka.util.Tools;


/**
 * api接口文档显示过滤
 *
 * @author Jason Chen
 * @date 2017-08-17 16:55
 */
public class ApiMenuFilter extends MenuNode {


    public static List<MenuNode> build(List<MenuNode> nodes) {

        //如果关闭了接口文档,则不显示接口文档菜单
        WakaProperties wakaProperties = Tools.springContext.getBean(WakaProperties.class);
        if (!wakaProperties.getSwaggerOpen()) {
            List<MenuNode> menuNodesCopy = new ArrayList<>();
            for (MenuNode menuNode : nodes) {
                if (Const.API_MENU_NAME.equals(menuNode.getName())) {
                    continue;
                } else {
                    menuNodesCopy.add(menuNode);
                }
            }
            nodes = menuNodesCopy;
        }

        return nodes;
    }
}
