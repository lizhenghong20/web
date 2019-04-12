package cn.farwalker.ravv.service.goodscart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by asus on 2018/12/13.
 */
@Data
public class UpdateCartVo {
    GoodsCartVo updatedCartItem;
    BigDecimal totalAmount;
    List<Long> selectedCartIds;
}
