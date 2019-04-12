package cn.farwalker.ravv.service.sys.notice.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通知dao
 *搬”cn.farwalker.standard.modular.system.dao.NoticeDao"
 * @author Jason Chen
 * @date 2017-11-09 23:03
 */
public interface ISysNoticeMgrDao {

    List<Map<String, Object>> list(@Param("condition") String condition);
}
