package cn.farwalker.ravv.service.web.menu.model;

import lombok.Data;

import java.util.List;

@Data
public class AppMenuFrontVo{
    WebMenuBo firstLevel;
    List<WebMenuFrontVo> otherLevelList;
}
