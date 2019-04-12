package cn.farwalker.ravv.service.sys.dict.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;

/**
 * 字典的dao
 *搬”cn.farwalker.standard.modular.system.dao.DictDao"
 *
 * @author Jason Chen
 * @date 2017年2月13日 下午11:10:24
 */
public interface ISysDictMgrDao {

    /**
     * 根据编码获取词典列表
     *
     * @param code
     * @return
     * @date 2017年2月13日 下午11:11:28
     */
    List<SysDictBo> selectByCode(@Param("code") String code);

    /**
     * 查询字典列表
     * 
     * @author Jason Chen
     * @Date 2017/4/26 13:04
     */
    List<Map<String,Object>> list(@Param("condition") String conditiion);
}
