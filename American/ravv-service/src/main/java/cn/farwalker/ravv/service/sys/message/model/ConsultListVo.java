package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

import java.util.List;

@Data
public class ConsultListVo {
    List<MessageConsultVo> consultList;
    boolean listIsNull;
}
