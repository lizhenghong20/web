package cn.farwalker.ravv.service.sys.dept.dao;

import cn.farwalker.waka.core.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门dao
 *搬”cn.farwalker.standard.modular.system.dao.DeptDao"
 * @author Jason Chen
 * @date 2017年2月17日20:28:58
 */
public interface ISysDeptMgrDao {

    /**
     * 获取ztree的节点列表
     *
     * @return
     * @date 2017年2月17日 下午8:28:43
     */
    List<ZTreeNode> tree();

    List<Map<String, Object>> list(@Param("condition") String condition);
}
