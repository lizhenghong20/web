package cn.farwalker.ravv.service.sys.message.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.sys.message.model.SystemMessageBo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息推送<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface ISystemMessageDao extends BaseMapper<SystemMessageBo>{
    public int insertBatch(@Param("promotionList") List<SystemMessageBo> promotionList);
}