package cn.farwalker.ravv.admin.basebrand;

import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminBaseBrandService {

    public Boolean create(BaseBrandBo vo);

    public Boolean delete(Long id);

    public BaseBrandBo getOne(Long id);

    public Page<BaseBrandBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield);

    public Boolean update(BaseBrandBo vo);
}
