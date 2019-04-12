package cn.farwalker.ravv.service.sys.operationlog.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.sys.operationlog.model.SysOperationLogBo;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * 日志记录dao
 * 搬”cn.farwalker.standard.modular.system.dao.LogDao"
 * @author Jason Chen
 * @Date 2017/4/16 23:44
 */
public interface ISysOperationLogMgrDao {

    /**
     * 获取操作日志
     *
     * @author Jason Chen
     * @Date 2017/4/16 23:48
     */
    List<Map<String, Object>> getOperationLogs(@Param("page") Page<SysOperationLogBo> page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName, @Param("logType") String logType, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 获取登录日志
     *
     * @author Jason Chen
     * @Date 2017/4/16 23:48
     */
    List<Map<String, Object>> getLoginLogs(@Param("page") Page<SysOperationLogBo> page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("logName") String logName, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);
}
