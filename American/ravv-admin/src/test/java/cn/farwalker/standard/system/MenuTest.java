package cn.farwalker.standard.system;

import java.util.List;
import java.util.Stack;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuDao;
import cn.farwalker.ravv.service.sys.menu.model.SysMenuBo;
import cn.farwalker.standard.base.BaseJunit;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 菜单测试
 *
 * @author Jason Chen
 * @date 2017-06-13 21:23
 */
public class MenuTest extends BaseJunit {

    @Autowired
    ISysMenuDao menuMapper;

    /**
     * 初始化pcodes
     *
     * @author Jason Chen
     * @Date 2017/6/13 21:24
     */
    @Test
    public void generatePcodes() {
        List<SysMenuBo> menus = menuMapper.selectList(null);
        for (SysMenuBo menu : menus) {
            if ("0".equals(menu.getPcode()) || null == menu.getPcode()) {
                menu.setPcodes("[0],");
            } else {
                StringBuffer sb = new StringBuffer();
                SysMenuBo parentMenu = getParentMenu(menu.getCode());
                sb.append("[0],");
                Stack<String> pcodes = new Stack<>();
                while (null != parentMenu.getPcode()) {
                    pcodes.push(parentMenu.getCode());
                    parentMenu = getParentMenu(parentMenu.getPcode());
                }
                int pcodeSize = pcodes.size();
                for (int i = 0; i < pcodeSize; i++) {
                    String code = pcodes.pop();
                    if (code.equals(menu.getCode())) {
                        continue;
                    }
                    sb.append("[" + code + "],");
                }

                menu.setPcodes(sb.toString());
            }
            menu.updateById();
        }
    }

    private SysMenuBo getParentMenu(String code) {
        Wrapper<SysMenuBo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.eq("code", code);
        List<SysMenuBo> menus = menuMapper.selectList(wrapper);
        if (menus == null || menus.size() == 0) {
            return new SysMenuBo();
        } else {
            return menus.get(0);
        }
    }
}
