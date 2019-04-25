package cn.farwalker.ravv.admin.basearea;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AdminBaseareaServiceImpl implements AdminBaseareaService{



    @Resource
    private IBaseAreaBiz baseAreaBiz;
    protected IBaseAreaBiz getBiz(){
        return baseAreaBiz;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(BaseAreaBo vo) {
        Boolean isAtRoot = false;
        if(vo.getPid() == -1L){
            vo.setPid(0L);
            isAtRoot = true;
        }
//        vo.setLeaf(Boolean.TRUE);
        //TODO 等级是否存储？

        Boolean rs =getBiz().insert(vo);
        if(rs) {
            //区域全路径及名称
            String fullPath = vo.getId().toString();
            String fullPathName = vo.getName();

            if (!isAtRoot) {
                //将上级改为非子节点
                BaseAreaBo baseArea = getBiz().selectById(vo.getPid());
//                baseArea.setLeaf(Boolean.FALSE);
                getBiz().updateById(baseArea);

                fullPath = baseArea.getFullPath() + "/" + vo.getId();
//                fullPathName = baseArea.getFullName() + " " + vo.getName();
            }

            //存储区域全路径
            vo.setFullPath(fullPath);
//            vo.setFullName(fullPathName);
            getBiz().updateById(vo);
            return true;
        }
        else
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        //判断是否为末级区域，不是则不能删除
        BaseAreaBo baseArea = baseAreaBiz.selectById(id);
        if(null == baseArea) {
            throw new WakaException("没有该区域数据");
        }
        Boolean rs = getBiz().deleteById(id);
        if (rs) {
            if (null != baseArea.getPid() && !baseArea.getPid().equals(0L)) {
                Wrapper<BaseAreaBo> wrapper = new EntityWrapper<>();
                wrapper.eq(BaseAreaBo.Key.pid.toString(), baseArea.getPid());
                List<BaseAreaBo> areaList = getBiz().selectList(wrapper);
                if (areaList.size() == 0) {
                    BaseAreaBo BaseAreaBo = getBiz().selectById(baseArea.getPid());
                    if (null != BaseAreaBo) {
                        getBiz().updateById(BaseAreaBo);
                    }
                }
            }
            return rs;
        }else{
            throw new WakaException("删除区域失败");
        }

    }

    @Override
    public BaseAreaBo doGet(Long id) {
        BaseAreaBo rs = getBiz().selectById(id);
        return rs;
    }

    @Override
    public Page<BaseAreaBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<BaseAreaBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<BaseAreaBo> wrap =ControllerUtils.getWrapper(query);
        Page<BaseAreaBo> rs =getBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Object update(BaseAreaBo vo) {
        //判断是否有改区域
        BaseAreaBo baseArea = baseAreaBiz.selectById(vo.getId());
        if(null == baseArea) {
            throw new WakaException("没有该区域数据");
        }
        baseArea.setCountryCode(vo.getCountryCode());
        baseArea.setName(vo.getName());

        Object rs =getBiz().updateById(baseArea);
        return rs;
    }

    @Override
    public List<BaseAreaBo> getAreaTree(CountryCodeEnum countryCode, Long parentid) {
        Wrapper<BaseAreaBo> query = new EntityWrapper<>();
        final String PID = BaseAreaBo.Key.pid.toString();
        if(null == countryCode) {
            throw new WakaException("请选择国家");
        }
        query.eq(BaseAreaBo.Key.countryCode.toString(), countryCode);
        if(parentid == null || parentid.longValue()==0){
//			query.isNull(PID);
            query.eq(PID,Integer.valueOf(0));
        }
        else{
            query.eq(PID, parentid);
        }
        List<BaseAreaBo> areatree = getBiz().selectList(query);
        return areatree;
    }
}
