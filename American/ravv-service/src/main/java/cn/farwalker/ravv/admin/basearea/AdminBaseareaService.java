package cn.farwalker.ravv.admin.basearea;

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminBaseareaService {

    public Boolean create(BaseAreaBo vo);

    public Boolean delete(Long id);

    BaseAreaBo doGet(Long id);

    public Page<BaseAreaBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield);

    public Object update(BaseAreaBo vo);

    public List<BaseAreaBo> getAreaTree(CountryCodeEnum countryCode, Long parentid);
}
