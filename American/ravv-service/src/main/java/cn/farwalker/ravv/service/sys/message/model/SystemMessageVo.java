package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

import java.util.List;

/**
 * @description: 包含判断信息是否为空字段
 * @param:
 * @return
 * @author Mr.Simple
 * @date 2019/1/12 16:34
 */
@Data
public class SystemMessageVo {
    List<SystemMessageBo> messageBoList;
    boolean listIsNull;
}
