package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

@Data
public class OrderInfoMessage extends SystemMessageBo {
    boolean hasOrder;
    String returnsType;
}
